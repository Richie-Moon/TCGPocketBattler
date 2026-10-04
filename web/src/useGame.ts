import { useEffect, useRef, useState } from 'react'
import type { Answer, BoardView, DecisionMessage, LogMessage, ServerMessage } from './protocol'

/** One line of the battle log. `turn` lines are the dividers; `point` lines are knockouts. */
export interface LogEntry {
  kind: 'turn' | 'you' | 'them' | 'point' | 'info'
  text: string
}

/**
 * One connection to /play (or /play?bot, which starts at once against a bot): the latest board, the open
 * question if any, a way to answer it, and a log of what this page saw happen.
 */
export function useGame(bot = false) {
  const socket = useRef<WebSocket | null>(null)
  const [connection, setConnection] = useState<'connecting' | 'waiting' | 'playing' | 'closed'>('connecting')
  const [board, setBoard] = useState<BoardView | null>(null)
  const [decision, setDecision] = useState<DecisionMessage | null>(null)
  const [result, setResult] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [log, setLog] = useState<LogEntry[]>([])
  // The latest board's coin flips; `id` tells two boards with the same flips apart.
  const [flips, setFlips] = useState<{ id: number; heads: boolean[] } | null>(null)
  // The opponent's move, shown on the board it was made from for MOVE_MS before the board it led to arrives.
  const [move, setMove] = useState<LogMessage | null>(null)
  // A played Item, Supporter or Stadium, either side's, shown large for FLASH_MS before its effect; `id` tells two
  // plays of the same card apart, and `card` is the instance played from your hand (null for the opponent's).
  const [flash, setFlash] = useState<{ id: number; shown: string; card: number | null } | null>(null)
  const flashes = useRef(0)
  // Messages are handled in order; one bringing coin flips holds itself, and everything after it, until they land.
  // Your own flash joins the queue too, so the board your play led to waits for it.
  const queue = useRef(Promise.resolve())

  useEffect(() => {
    // Deferred so StrictMode's mount-unmount-mount in dev cancels the timer instead of opening a throwaway
    // socket; the server would pair that socket with a real one, then abandon the game when it closed.
    let ws: WebSocket | undefined
    let last: BoardView | null = null
    let shown = 0 // the last turn given a divider
    let flipped = 0
    const timer = setTimeout(connect, 0)
    return () => {
      clearTimeout(timer)
      ws?.close()
    }

    // Dividers up to `turn`. A whole turn can pass between two boards, so skipped turns are filled in; turns alternate.
    function divide(turn: number, yourTurn: boolean) {
      const entries: LogEntry[] = []
      for (let t = shown ? shown + 1 : turn; t <= turn; t++) {
        const yours = yourTurn === ((turn - t) % 2 === 0)
        entries.push({ kind: 'turn', text: `Turn ${t} · ${yours ? 'You' : 'Opponent'}` })
      }
      shown = Math.max(shown, turn)
      return entries
    }

    // ponytail: the log is only moves, points and the result; log engine events (damage, flips) server-side to show more.
    function track(next: BoardView) {
      const entries = next.you.active ? divide(next.turn, next.yourTurn) : []
      if (last && next.you.points > last.you.points) entries.push({ kind: 'point', text: 'You took a point.' })
      if (last && next.opponent.points > last.opponent.points)
        entries.push({ kind: 'point', text: 'Opponent took a point.' })
      if (entries.length) setLog((log) => [...log, ...entries])
      last = next
    }

    /**
     * Shows a board's coin flips, settling 0.5s after the last has landed, as the coins start to fade (Battle's Coins:
     * 0.3s apart, 0.6s spins).
     */
    function land(message: ServerMessage) {
      if ((message.type !== 'state' && message.type !== 'over') || !message.flips.length) return
      setFlips({ id: ++flipped, heads: message.flips })
      return new Promise<void>((resolve) => setTimeout(resolve, message.flips.length * 300 + 800))
    }

    function connect() {
      ws = new WebSocket(`${location.protocol === 'https:' ? 'wss' : 'ws'}://${location.host}/play${bot ? '?bot' : ''}`)
      socket.current = ws

      ws.onmessage = (event) => {
        const message = JSON.parse(event.data) as ServerMessage
        queue.current = queue.current.then(() => land(message)).then(() => handle(message))
      }
      ws.onclose = () => {
        queue.current = queue.current.then(() => setConnection('closed'))
      }
    }

    function handle(message: ServerMessage): Promise<void> | void {
      switch (message.type) {
        case 'waiting':
          setConnection('waiting')
          break
        case 'state':
          setConnection('playing')
          setBoard(message.board)
          setDecision(null)
          track(message.board)
          break
        case 'decision':
          setDecision(message)
          setError(null)
          break
        case 'over':
          setBoard(message.board)
          setDecision(null)
          setResult(message.result)
          track(message.board)
          setLog((log) => [...log, { kind: 'info', text: `Game over: ${message.result}.` }])
          break
        case 'error':
          setError(message.message)
          break
        case 'log': {
          const entries = [...divide(message.turn, message.yourTurn), { kind: 'them', text: `Opponent: ${message.text}` } as const]
          setLog((log) => [...log, ...entries])
          setMove(message)
          return (message.shown ? show(message.shown, null) : wait(MOVE_MS)).then(() => setMove(null))
        }
      }
    }
  }, [bot])

  function choose(option: number) {
    if (!decision) return
    const answer: Answer = { decision: decision.id, option }
    const { shown, card } = decision.options[option]
    if (shown) queue.current = queue.current.then(() => show(shown, card))
    socket.current?.send(JSON.stringify(answer))
    setLog((log) => [...log, { kind: 'you', text: decision.options[option].label }])
    setDecision(null)
  }

  function show(shown: string, card: number | null) {
    setFlash({ id: ++flashes.current, shown, card })
    return wait(FLASH_MS).then(() => setFlash(null))
  }

  return { connection, board, flips, move, flash, decision, result, error, log, choose }
}

const wait = (ms: number) => new Promise<void>((resolve) => setTimeout(resolve, ms))

/** How long each of the opponent's moves holds the board, so a bot's turn plays out one move at a time. */
const MOVE_MS = 1300
/** How long a played Item, Supporter or Stadium is flashed on the board before its effect. */
const FLASH_MS = 750
