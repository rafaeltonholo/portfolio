package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

/**
 * Syntax-coloring palette for terminal / code snippet surfaces. The IDE voice
 * is part of the site's brand identity (precise, opinionated, handmade), so
 * the keyword / string / number hues live in the token system alongside
 * [InkScale], [PaperScale], and [AccentSwatch] rather than as hardcoded
 * constants inside [dev.tonholo.portfolio.components.organisms.home.TerminalSnippet].
 *
 * The naming convention tracks the active page `ColorMode`, not the terminal
 * card background. In light mode the terminal card inverts to an `ink`
 * surface, so the syntax hues lean LIGHTER to stay readable on that dark
 * card. In dark mode the terminal card inverts to a near-paper surface, so
 * the syntax hues go DARKER to read on the lighter card. That inversion
 * matches `.term-kw` / `.term-str` / `.term-num` in the handoff CSS.
 *
 * @property keyword Kotlin reserved words (`fun`, `val`, `by`, ...). Purple.
 * @property string String literals. Mint-adjacent green, kept distinct from
 *   the site accent mint so code snippets do not read as "accent everywhere".
 * @property number Numeric literals. Warm amber, shared across color modes.
 */
data class SyntaxScale(val keyword: Color, val string: Color, val number: Color)

// Light-mode page → dark terminal card → lighter syntax colors read cleanly on
// the ink surface. Keyword is a soft lavender, string is a pale mint, number
// is a warm amber.
private const val LIGHT_KEYWORD = 0xB99CF7
private const val LIGHT_STRING = 0xA3D9A5
private const val LIGHT_NUMBER = 0xF2C078

// Dark-mode page → paper-2 terminal card → darker syntax colors read cleanly
// on the lighter surface. Keyword is a saturated violet, string is a deeper
// forest green, number keeps the amber (same hue reads on both card shades).
private const val DARK_KEYWORD = 0x8B5CF6
private const val DARK_STRING = 0x3D8A4D
private const val DARK_NUMBER = 0xF2C078

object Syntax {
    val light = SyntaxScale(
        keyword = Color.rgb(LIGHT_KEYWORD),
        string = Color.rgb(LIGHT_STRING),
        number = Color.rgb(LIGHT_NUMBER),
    )
    val dark = SyntaxScale(
        keyword = Color.rgb(DARK_KEYWORD),
        string = Color.rgb(DARK_STRING),
        number = Color.rgb(DARK_NUMBER),
    )
}
