package dev.tonholo.portfolio.theme.typography

/**
 * Font-family stacks used across the Phase 2 redesign, mirroring
 * `:root` custom properties in the handoff `styles.css`.
 *
 * - [MONO] is the IDE-inspired display voice (headings, meta labels, code).
 * - [SANS] is the body voice used across prose paragraphs.
 * - [SERIF] is the editorial accent for lead paragraphs and article bodies.
 *
 * [DISPLAY] and [BODY] are aliases matching the `--font-display` and
 * `--font-body` custom properties; they can be retargeted in one spot if a
 * future variant flips the pairing.
 */
object FontFamilies {
    const val MONO: String = "'JetBrains Mono', ui-monospace, SFMono-Regular, Menlo, monospace"
    const val SANS: String = "'Geist', -apple-system, sans-serif"
    const val SERIF: String = "'Fraunces', Georgia, serif"
    const val DISPLAY: String = MONO
    const val BODY: String = SANS
}
