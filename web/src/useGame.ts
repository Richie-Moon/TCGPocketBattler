import { useEffect, useRef, useState } from 'react'
import type { Answer, BoardView, DecisionMessage, ServerMessage } from './protocol'

/** One connection to /play: the latest board, the open question if any, and a way to answer it. */
export function useGame() {
  const socket = useRef<WebSocket | null>(null)
  const [connection, setConnection] = useState<'connecting' | 'waiting' | 'playing' | 'closed'>('connecting')
  const [board, setBoard] = useState<BoardView | null>(null)
  const [decision, setDecision] = useState<DecisionMessage | null>(null)
  const [result, setResult] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    // Deferred so StrictMode's mount-unmount-mount in dev cancels the timer instead of opening a throwaway
    // socket; the server would pair that socket with a real one, then abandon the game when it closed.
    let ws: WebSocket | undefined
    const timer = setTimeout(connect, 0)
    return () => {
      clearTimeout(timer)
      ws?.close()
    }

    function connect() {
      ws = new WebSocket(`${location.protocol === 'https:' ? 'wss' : 'ws'}://${location.host}/play`)
      socket.current = ws

      ws.onmessage = (event) => {
        const message = JSON.parse(event.data) as ServerMessage
        switch (message.type) {
          case 'waiting':
            setConnection('waiting')
            break
          case 'state':
            setConnection('playing')
            setBoard(message.board)
            setDecision(null)
            break
          case 'decision':
            setDecision(message)
            setError(null)
            break
          case 'over':
            setBoard(message.board)
            setDecision(null)
            setResult(message.result)
            break
          case 'error':
            setError(message.message)
            break
        }
      }
      ws.onclose = () => setConnection('closed')
    }
  }, [])

  function choose(option: number) {
    if (!decision) return
    const answer: Answer = { decision: decision.id, option }
    socket.current?.send(JSON.stringify(answer))
    setDecision(null)
  }

  return { connection, board, decision, result, error, choose }
}
