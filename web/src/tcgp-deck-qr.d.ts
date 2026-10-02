/** The slice of github:KevinGutowski/tcgp-deck-qr that sharing a deck uses; the package ships no types. */
declare module 'tcgp-deck-qr' {
  export class CardDatabase {}
  /** Fetches the community card database, which maps a set and collector number to Pocket's own card id. */
  export function loadCardDatabase(): Promise<CardDatabase>
  type Card = { set: string; number: number }
  type Resolved = { trainerIds: number[]; pokemonIds: number[]; energyTypes: number[] }
  /** Throws unless the deck has exactly 20 cards, 1 to 3 energy types and only cards the database knows. */
  export function resolveDeck(deck: { pokemon: Card[]; trainers: Card[]; energy: string[] }, database: CardDatabase): Resolved
  export function encodePayload(deck: Resolved): string
  export function createQrSvg(payload: string): string
}
