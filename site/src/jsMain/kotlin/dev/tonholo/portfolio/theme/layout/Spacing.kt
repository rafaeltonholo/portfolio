package dev.tonholo.portfolio.theme.layout

import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.cssRem

/**
 * Semantic spacing scale that powers rhythm between sibling elements. The
 * values follow a 4pt base (4 / 8 / 16 / 32 / 64), so consumers can reach
 * for `Spacing.md` without having to decide between `0.5rem` and `0.75rem`
 * at call sites.
 *
 * Prefer [Spacing] over raw `cssRem` literals for new code. Mixing sizes
 * across hierarchy levels (e.g. `Spacing.xs` between tight siblings and
 * `Spacing.xl` between sections) produces a visible cadence that reads
 * better than the old 8px-everywhere pattern.
 *
 * Keep this surface small on purpose - five stops is enough to cover
 * almost every layout gap the site needs; exotic values still belong at
 * the call site with a comment explaining why they diverge.
 */
object Spacing {
    /** 4px. Tight pairing inside a single control (icon + label). */
    val xs: CSSNumeric = 0.25.cssRem

    /** 8px. Default between closely-related siblings (chip rows, bullets). */
    val sm: CSSNumeric = 0.5.cssRem

    /** 16px. Between related content blocks inside a component. */
    val md: CSSNumeric = 1.0.cssRem

    /** 32px. Between sibling sections sharing a parent container. */
    val lg: CSSNumeric = 2.0.cssRem

    /** 64px. Between unrelated page sections. Use sparingly. */
    val xl: CSSNumeric = 4.0.cssRem
}
