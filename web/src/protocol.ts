// Mirrors server/src/main/java/com/tcgpocket/server: BoardView, and the messages GameSocket, Game and
// RemotePlayer send. Change both sides in the same commit.

/** `id` is this copy's instance id; `card` is the printed card, such as "A1-094". */
export interface CardView {
  id: number
  card: string
  name: string
}

/** `id` stays the same from hand to play and through evolution. */
export interface PokemonView {
  id: number
  card: string
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
  /** For the opponent, only the cards revealed (Hand Scope, Mew); handSize is the real count. */
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

/**
 * One option. `card` and `target` are instance ids from the board, so a drag of card 31 onto Pokemon 12 is
 * `options.findIndex(o => o.card === 31 && o.target === 12)`. Answer with that index, never the ids.
 */
export interface OptionView {
  label: string
  kind:
    | 'attack'
    | 'endTurn'
    | 'attach'
    | 'retreat'
    | 'evolve'
    | 'play'
    | 'ability'
    | 'pokemon'
    | 'card'
    | 'setup'
    | 'none'
    | 'other'
  /** The card played, evolved with or used; for an attack, the attacking Pokemon; for setup, the Active. */
  card: number | null
  /** The Pokemon it lands on or brings up. */
  target: number | null
  /** For setup, the cards from hand to Bench; empty otherwise. */
  bench: number[]
}

export interface DecisionMessage {
  type: 'decision'
  id: number
  prompt: string
  options: OptionView[]
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
