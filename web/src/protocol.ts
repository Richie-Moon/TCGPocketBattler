// Mirrors server/src/main/java/com/tcgpocket/server: BoardView, the messages GameSocket, Game, RemotePlayer and
// SandboxGame send, and SandboxCommand. Change both sides in the same commit.

/** `id` is this copy's instance id; `card` is the printed card, such as "A1-094". */
export interface CardView {
  id: number
  card: string
  name: string
}

/** A printed attack or Ability; `text` is its effect, empty when it has none. */
export interface TextView {
  name: string
  text: string
}

/** `id` stays the same from hand to play and through evolution. `attacks` and `ability` are the printed text. */
export interface PokemonView {
  id: number
  card: string
  name: string
  hp: number
  maxHp: number
  energy: Record<string, number>
  statuses: string[]
  tool: CardView | null
  attacks: TextView[]
  ability: TextView | null
}

/** Object-storage names from the side's deck ("Coin_Pokéball.png"), under COIN_BASE, SLEEVE_BASE and PLAYMAT_BASE. */
export interface Cosmetics {
  coin: string
  sleeve: string
  playmat: string
}

export interface SideView {
  name: string
  points: number
  /** For the opponent, only the cards revealed (Hand Scope, Mew); handSize is the real count. */
  hand: CardView[]
  handSize: number
  deckSize: number
  /** Your own deck's top cards, top first, once you've looked at them (Porygon); always empty for the opponent. */
  topCards: CardView[]
  discard: CardView[]
  active: PokemonView | null
  /** By slot, three of them, null where a slot is empty. */
  bench: (PokemonView | null)[]
  energy: string | null
  nextEnergy: string | null
  cosmetics: Cosmetics
  /** Null except in the sandbox, where nothing is hidden. */
  sandbox: { deck: CardView[]; energyTypes: string[] } | null
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
    | 'answer'
  /** The card played, evolved with or used; for an attack, the attacking Pokemon; for setup, the Active. */
  card: number | null
  /** The Pokemon it lands on or brings up. */
  target: number | null
  /** For setup, the card from hand for each Bench slot, null for an empty one; empty otherwise. */
  bench: (number | null)[]
  /** For a play, the printed id of an Item, Supporter or Stadium (a card that does not stay on a Pokemon), flashed before its effect. */
  shown: string | null
  /** For a Basic played to the Bench, the slot it goes into. */
  slot: number | null
}

export interface DecisionMessage {
  type: 'decision'
  id: number
  prompt: string
  options: OptionView[]
  /** Sandbox only: the question is for the opponent's side. */
  opponent?: boolean
  /** Sandbox only: whether board edits are open, which is only at the "Your move" question. */
  editable?: boolean
  /** Sandbox only: how the next coins will land, in order, true for heads. */
  coins?: boolean[]
}

export type ServerMessage =
  | { type: 'waiting' }
  /** `flips`: the coins flipped since the last board, in order, true for heads. */
  | { type: 'state'; board: BoardView; flips: boolean[] }
  | DecisionMessage
  | { type: 'over'; board: BoardView; flips: boolean[]; result: string }
  | { type: 'error'; message: string }
  | LogMessage

/**
 * What the opponent chose, worded for you; `turn` and `yourTurn` place it in the log. `kind`, `card` and `target`
 * are its OptionView's, so the board can show the move; `card` is null for a chosen card, which stays unnamed.
 */
export interface LogMessage {
  type: 'log'
  turn: number
  yourTurn: boolean
  text: string
  kind: OptionView['kind']
  card: number | null
  target: number | null
  /** A played card that does not stay on a Pokemon (Item, Supporter, Stadium), as "P-A-005": flashed before its effect. */
  shown: string | null
}

/** The only thing the browser ever sends to /play. */
export interface Answer {
  decision: number
  option: number
}

export type SandboxZone = 'HAND' | 'DECK' | 'DISCARD' | 'ACTIVE' | 'BENCH'

/**
 * An edit /sandbox also takes. `opponent` picks the side; a string `card` is a printed id and a numeric `card` or
 * `pokemon` an instance id. `position` is a deck index (0 the top, null the bottom) or a Bench slot (null the first empty).
 */
export type SandboxCommand =
  | { command: 'addCard'; opponent: boolean; card: string; zone: SandboxZone; position: number | null }
  | { command: 'move'; card: number; zone: SandboxZone; position: number | null }
  | { command: 'remove'; card: number }
  | { command: 'shuffle'; opponent: boolean }
  | { command: 'setDamage'; pokemon: number; damage: number }
  | { command: 'setEnergy'; pokemon: number; type: string; count: number }
  | { command: 'addStatus'; pokemon: number; status: string }
  | { command: 'removeStatus'; pokemon: number; status: string }
  | { command: 'attachTool'; pokemon: number; card: string }
  | { command: 'removeTool'; pokemon: number }
  | { command: 'evolve'; pokemon: number; card: string }
  | { command: 'setPlayedThisTurn'; pokemon: number; played: boolean }
  | { command: 'setPoints'; opponent: boolean; points: number }
  | { command: 'setEnergyZone'; opponent: boolean; current: string | null; next: string | null; types: string[] }
  | { command: 'resetTurn'; opponent: boolean }
  | { command: 'setTurn'; turn: number }
  | { command: 'setStadium'; card: string | null }
  | { command: 'queueCoins'; coins: boolean[] }
  | { command: 'clearCoins' }
