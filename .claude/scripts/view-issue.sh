#!/usr/bin/env bash
#
# get-issue.sh - Fetch a GitHub issue (with all comments) and print it as JSON.
#
# Usage:   get-issue.sh <issue-number>
# Example: get-issue.sh 42        (a leading '#' is also accepted: '#42')
#
# Designed for non-interactive / headless use (e.g. called from an AI skill):
#   - Never prompts. Success: one JSON object on stdout, exit 0.
#   - Failure: {"error": "...", "code": "..."} on stderr, non-zero exit:
#       2 = bad usage / invalid issue number
#       3 = issue not found (or token has no access to it)
#       4 = the number is a pull request, not an issue
#       5 = missing dependency (curl / jq), or token missing / rejected
#       1 = any other GitHub/API error (incl. rate limiting)
#
# Requirements: bash, curl, jq.
#
# Authentication: a token read from the environment variable named in TOKEN_VAR
# (default GITHUB_TOKEN). In GitHub Actions, use the built-in token with
# `permissions: issues: read`:
#
#   env:
#     GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
#
# Elsewhere, any token with read access to issues on REPO works
# (fine-grained PAT: "Issues: Read-only"; classic PAT: "repo" for private repos).

set -euo pipefail

# ---------------------------------------------------------------------------
# Configuration - the only repository this script will read from.
# In GitHub Actions, GITHUB_REPOSITORY and GITHUB_API_URL are set automatically,
# so the script reads from the repo the workflow runs in, on the right host.
# Outside Actions, the hardcoded fallbacks below are used.
# ---------------------------------------------------------------------------
REPO="${GITHUB_REPOSITORY:-OWNER/REPO}"
# REST API base URL fallback:
#   github.com                    -> https://api.github.com
#   GitHub Enterprise Cloud (ghe.com, data residency) -> https://api.<subdomain>.ghe.com
#   GitHub Enterprise Server      -> https://<hostname>/api/v3
API_URL="${GITHUB_API_URL:-https://api.github.com}"
# Name of the Repository variable that holds the token.
TOKEN_VAR="CUSTOM_GITHUB_PAT"

# Emit a JSON error on stderr and exit. Uses printf so it works even without jq.
fail() {
  local code="$1" exit_code="$2" msg="$3"
  msg=${msg//\\/\\\\}; msg=${msg//\"/\\\"}
  msg=${msg//$'\n'/ }; msg=${msg//$'\r'/}; msg=${msg//$'\t'/ }
  printf '{"error":"%s","code":"%s"}\n' "$msg" "$code" >&2
  exit "$exit_code"
}

# ---------------------------------------------------------------------------
# Validate input and environment
# ---------------------------------------------------------------------------
[[ $# -eq 1 ]] || fail usage 2 "Expected exactly one argument: the issue number. Usage: $(basename "$0") <issue-number>"

ISSUE="${1#\#}"
[[ "$ISSUE" =~ ^[1-9][0-9]{0,9}$ ]] || fail invalid_issue_number 2 "Invalid issue number: '$1'. Must be a positive integer."

command -v curl >/dev/null 2>&1 || fail missing_dependency 5 "'curl' is not installed or not on PATH."
command -v jq   >/dev/null 2>&1 || fail missing_dependency 5 "'jq' is not installed or not on PATH."

TOKEN="${!TOKEN_VAR:-}"
[[ -n "$TOKEN" ]] || fail missing_token 5 "Environment variable $TOKEN_VAR is not set or empty."

WORK_DIR="$(mktemp -d)"
trap 'rm -rf "$WORK_DIR"' EXIT

# Headers go in a private temp file so the token never appears on a command line
# (and therefore not in `ps` output or shell traces).
HEADERS="$WORK_DIR/headers"
( umask 077; printf 'Authorization: Bearer %s\nAccept: application/vnd.github+json\nX-GitHub-Api-Version: 2022-11-28\nUser-Agent: get-issue-script\n' "$TOKEN" >"$HEADERS" )

# GET a REST path; prints the response body. Maps HTTP errors to exit codes.
api_get() {
  local path="$1" body="$WORK_DIR/body" status
  if ! status="$(curl -sS --max-time 30 --retry 2 \
                   -H @"$HEADERS" -o "$body" -w '%{http_code}' \
                   "$API_URL/$path" 2>"$WORK_DIR/curl_err")"; then
    fail network_error 1 "Request to $API_URL failed: $(cat "$WORK_DIR/curl_err")"
  fi
  case "$status" in
    200) cat "$body" ;;
    401) fail unauthorized 5 "Token in $TOKEN_VAR was rejected (HTTP 401)." ;;
    404|410) fail not_found 3 "Issue #$ISSUE not found in $REPO (or token has no access)." ;;
    *)
      local msg; msg="$(jq -r '.message // empty' "$body" 2>/dev/null || true)"
      fail api_error 1 "GitHub API returned HTTP $status for $path${msg:+: $msg}" ;;
  esac
}

# ---------------------------------------------------------------------------
# Fetch issue and all comments (paginated, 100 per page)
# ---------------------------------------------------------------------------
ISSUE_JSON="$(api_get "repos/$REPO/issues/$ISSUE")"

if [[ "$(jq -r 'has("pull_request")' <<<"$ISSUE_JSON")" == "true" ]]; then
  fail is_pull_request 4 "#$ISSUE in $REPO is a pull request, not an issue."
fi

COMMENTS_JSON="[]"
page=1
while :; do
  PAGE_JSON="$(api_get "repos/$REPO/issues/$ISSUE/comments?per_page=100&page=$page")"
  COMMENTS_JSON="$(jq -s 'add' <<<"$COMMENTS_JSON$PAGE_JSON")"
  [[ "$(jq 'length' <<<"$PAGE_JSON")" -lt 100 ]] && break
  page=$((page + 1))
done

# ---------------------------------------------------------------------------
# Shape the output
# ---------------------------------------------------------------------------
jq -n \
  --arg repo "$REPO" \
  --argjson issue "$ISSUE_JSON" \
  --argjson comments "$COMMENTS_JSON" '
  def login: (.user.login // "ghost");
  ($comments | map({
      id,
      author: login,
      created_at,
      updated_at,
      url: .html_url,
      body: (.body // "")
  })) as $c
  | {
      repo: $repo,
      number: $issue.number,
      title: $issue.title,
      state: $issue.state,
      state_reason: $issue.state_reason,
      url: $issue.html_url,
      author: ($issue | login),
      created_at: $issue.created_at,
      updated_at: $issue.updated_at,
      closed_at: $issue.closed_at,
      body: ($issue.body // ""),
      participants: ([($issue | login)] + ($c | map(.author)) | unique),
      comment_count: ($c | length),
      comments: $c
    }'
