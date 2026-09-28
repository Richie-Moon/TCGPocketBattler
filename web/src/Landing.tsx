import type { CSSProperties } from 'react'

/**
 * Background card outlines, placed as in the 1920x1080 and 390x844 Figma frames but anchored to the
 * nearest viewport edges. fontSize is the scale: 10px draws a 600px-wide card.
 */
const cards: { style: CSSProperties; mobile?: boolean }[] = [
  { style: { left: -350, top: -380, rotate: '14deg', fontSize: 10 } },
  { style: { left: 6, bottom: -454, rotate: '-10deg', fontSize: 9.667 } },
  { style: { right: -241, top: -372, rotate: '-12deg', fontSize: 10 } },
  { style: { right: 59, bottom: -567, rotate: '8deg', fontSize: 9.667 } },
  { style: { left: 'calc(50% - 174px)', top: -660, rotate: '-4deg', fontSize: 9 } },
  { style: { right: -186, bottom: -256, rotate: '-10deg', fontSize: 6.667 }, mobile: true },
]

/** Shown to signed-out visitors. "Log in" starts the server's Google OAuth flow. */
export function Landing() {
  return (
    <div className="landing">
      <div className="card-outlines" aria-hidden>
        {cards.map(({ style, mobile }, index) => (
          <div key={index} className={mobile ? 'card-outline mobile' : 'card-outline'} style={style}>
            <div className="name" />
            <div className="art" />
            <div className="attack" />
            <div className="attack short" />
          </div>
        ))}
      </div>
      <h1>TCG Pocket Battler</h1>
      <p>TCG Pocket Battle Simulator</p>
      <a className="login" href="/oauth2/authorization/google">Log in</a>
    </div>
  )
}
