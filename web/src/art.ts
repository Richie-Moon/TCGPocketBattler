/** Card art, named by printed id, in a public bucket; VITE_CARD_BASE=/cards uses local files. */
export const CARD_BASE = import.meta.env.VITE_CARD_BASE ??
  'https://objectstorage.ap-sydney-1.oraclecloud.com/n/sd3dz8oxtchf/b/assets-bucket/o/cards'

/** Profile icons, "Icon_<name>.png", in the same public bucket as the card art. */
export const ICON_BASE = 'https://objectstorage.ap-sydney-1.oraclecloud.com/n/sd3dz8oxtchf/b/assets-bucket/o/icons'

/** Emblems, "Emblem_<name>.png", in the same public bucket. */
export const EMBLEM_BASE = 'https://objectstorage.ap-sydney-1.oraclecloud.com/n/sd3dz8oxtchf/b/assets-bucket/o/emblems'

/** Energy icons, "<type>.png" with the type in lower case, in the same public bucket. */
export const ENERGY_BASE = 'https://objectstorage.ap-sydney-1.oraclecloud.com/n/sd3dz8oxtchf/b/assets-bucket/o/energy'

/** Deck coins, "Coin_<name>.png", in the same public bucket. */
export const COIN_BASE = 'https://objectstorage.ap-sydney-1.oraclecloud.com/n/sd3dz8oxtchf/b/assets-bucket/o/coins'
