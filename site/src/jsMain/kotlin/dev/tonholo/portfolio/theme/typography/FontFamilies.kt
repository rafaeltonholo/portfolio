package dev.tonholo.portfolio.theme.typography

/**
 * Font-family stacks used across the Phase 2 redesign, mirroring
 * `:root` custom properties in the handoff `styles.css`.
 *
 * Kobweb's [com.varabyte.kobweb.compose.ui.modifiers.fontFamily] takes varargs
 * of individual family names (no embedded commas), so each stack is exposed as
 * an `Array<String>`. Call sites spread with `*FontFamilies.MONO`.
 *
 * - [MONO] is the IDE-inspired display voice (headings, meta labels, code).
 * - [SANS] is the body voice used across prose paragraphs.
 *
 * [DISPLAY] and [BODY] are aliases matching the `--font-display` and
 * `--font-body` custom properties; they can be retargeted in one spot if a
 * future variant flips the pairing.
 */
object FontFamilies {
    val MONO: Array<String> = arrayOf(
        "JetBrains Mono",
        "ui-monospace",
        "SFMono-Regular",
        "Menlo",
        "monospace",
    )

    val SANS: Array<String> = arrayOf(
        "Fira Sans",
        "-apple-system",
        "sans-serif",
    )

    val DISPLAY: Array<String> = MONO
    val BODY: Array<String> = SANS
}
