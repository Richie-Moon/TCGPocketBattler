// Mirrors server/src/main/java/com/tcgpocket/server: BoardView, and the messages GameSocket, Game and
// RemotePlayer send. Change both sides in the same commit.

export interface CardView {
  id: string
  name: string
}

export interface PokemonView {
  id: string
  name: string
  hp: number
  maxHp: number
  energy: Record<string, number>
  statuses: string[]
  tool: CardView | null
}

export interface SideView {
  name: string
  points: number
  /** Always empty for the opponent; handSize is the real count. */
  hand: CardView[]
  handSize: number
  deckSize: number
  discard: CardView[]
  active: PokemonView | null
  bench: PokemonView[]
  energy: string | null
  nextEnergy: string | null
}

export interface BoardView {
  turn: number
  yourTurn: boolean
  stadium: CardView | null
  you: SideView
  opponent: SideView
}

export interface DecisionMessage {
  type: 'decision'
  id: number
  prompt: string
  options: string[]
}

export type ServerMessage =
  | { type: 'waiting' }
  | { type: 'state'; board: BoardView }
  | DecisionMessage
  | { type: 'over'; board: BoardView; result: string }
  | { type: 'error'; message: string }

/** The only thing the browser ever sends. */
export interface Answer {
  decision: number
  option: number
}
