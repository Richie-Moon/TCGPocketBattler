/** Shown to signed-out visitors. "Log in" starts the server's Google OAuth flow. */
export function Landing() {
  return (
    <div className="landing">
      <h1>TCG Pocket Battler</h1>
      <p>TCG Pocket Battle Simulator</p>
      <a className="login" href="/oauth2/authorization/google">Log in</a>
    </div>
  )
}
