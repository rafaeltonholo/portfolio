package dev.tonholo.portfolio.theme.typography

import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextTransform
import dev.tonholo.portfolio.theme.typography.unit.sp
import org.jetbrains.compose.web.css.em

// Size scale in `sp`; 16sp == 1rem at the site's default root font-size.
// Values mirror the handoff px scale, with clamp maxima noted inline.
private val HERO_SIZE = 86.sp // clamp max
private val H1_SIZE = 68.sp // clamp max
private val H2_SIZE = 28.sp
private val H3_DISPLAY_SIZE = 26.sp
private val BODY_LARGE_SIZE = 17.sp
private val BODY_SIZE = 16.sp
private val BODY_SMALL_SIZE = 14.sp
private val SERIF_LEAD_SIZE = 20.sp
private val SERIF_BODY_SIZE = 17.sp
private val META_LABEL_SIZE = 12.sp
private val META_SMALL_SIZE = 12.sp
private val META_PLAIN_SIZE = 12.sp
private val CHIP_SIZE = 11.sp
private val CODE_SIZE = 13.sp

// Line-heights are unitless per handoff CSS.
private const val HERO_LINE_HEIGHT = 0.95
private const val H1_LINE_HEIGHT = 1.05
private const val H2_LINE_HEIGHT = 1.15
private const val H3_DISPLAY_LINE_HEIGHT = 1.2
private const val BODY_LINE_HEIGHT = 1.55
private const val SERIF_LEAD_LINE_HEIGHT = 1.5
private const val SERIF_BODY_LINE_HEIGHT = 1.6
private const val META_LINE_HEIGHT = 1.4
private const val CODE_LINE_HEIGHT = 1.6

// Letter-spacing in em (negative for display tightening, positive for meta tracking).
private const val HERO_LETTER_SPACING_EM = -0.035
private const val H1_LETTER_SPACING_EM = -0.025
private const val H2_LETTER_SPACING_EM = -0.02
private const val H3_DISPLAY_LETTER_SPACING_EM = -0.02
private const val META_LABEL_LETTER_SPACING_EM = 0.02
private const val META_SMALL_LETTER_SPACING_EM = 0.06

/**
 * Curated type scale, mapped 1:1 from the handoff `styles.css` selectors.
 *
 * | Slot         | CSS reference                          | Size       | Weight | Line-height | Letter-spacing |
 * |--------------|----------------------------------------|------------|--------|-------------|----------------|
 * | [hero]       | `.hero-a__title`                       | 5.5rem     | 500    | 0.95        | -0.03em        |
 * | [h1]         | `h1`, `.about-hdr h1`, `.resume-head h1` | 4.25rem  | 500    | 1.05        | -0.025em       |
 * | [h2]         | `.section-head h2`, project section h2 | 1.75rem    | 500    | 1.15        | -0.02em        |
 * | [h3Display]  | `.prose h3` subheadings                | 1.625rem   | 500    | 1.2         | -0.02em        |
 * | [bodyLarge]  | `.hero-a__subtitle`, `.prose p`        | 1.0625rem  | 400    | 1.55        | -              |
 * | [body]       | default body copy                      | 1rem       | 400    | 1.55        | -              |
 * | [bodySmall]  | secondary body                         | 0.875rem   | 400    | 1.55        | -              |
 * | [serifLead]  | `.article-detail .lede`, hero-b desc   | 1.25rem    | 400    | 1.5         | -              |
 * | [serifBody]  | `.prose p` serif variant               | 1.0625rem  | 400    | 1.6         | -              |
 * | [metaLabel]  | `.meta`, `.num`                        | 0.75rem    | 500    | 1.4         | 0.02em + uppercase |
 * | [metaSmall]  | `.meta` small variants                 | 0.6875rem  | 500    | 1.4         | 0.06em + uppercase |
 * | [metaPlain]  | long mono eyebrows (>20ch)             | 0.75rem    | 500    | 1.4         | - (natural case)  |
 * | [chip]       | `.chip`                                | 0.6875rem  | 500    | 1.4         | -              |
 * | [code]       | `.term`, inline code                   | 0.8125rem  | 400    | 1.6         | -              |
 */
object Typography {
    val hero: TextStyle = TextStyle(
        family = FontFamilies.DISPLAY,
        size = HERO_SIZE,
        weight = FontWeight.Medium,
        lineHeight = HERO_LINE_HEIGHT,
        letterSpacing = HERO_LETTER_SPACING_EM.em,
    )

    val h1: TextStyle = TextStyle(
        family = FontFamilies.DISPLAY,
        size = H1_SIZE,
        weight = FontWeight.Medium,
        lineHeight = H1_LINE_HEIGHT,
        letterSpacing = H1_LETTER_SPACING_EM.em,
    )

    val h2: TextStyle = TextStyle(
        family = FontFamilies.DISPLAY,
        size = H2_SIZE,
        weight = FontWeight.Medium,
        lineHeight = H2_LINE_HEIGHT,
        letterSpacing = H2_LETTER_SPACING_EM.em,
    )

    val h3Display: TextStyle = TextStyle(
        family = FontFamilies.DISPLAY,
        size = H3_DISPLAY_SIZE,
        weight = FontWeight.Medium,
        lineHeight = H3_DISPLAY_LINE_HEIGHT,
        letterSpacing = H3_DISPLAY_LETTER_SPACING_EM.em,
    )

    val bodyLarge: TextStyle = TextStyle(
        family = FontFamilies.BODY,
        size = BODY_LARGE_SIZE,
        weight = FontWeight.Normal,
        lineHeight = BODY_LINE_HEIGHT,
    )

    val body: TextStyle = TextStyle(
        family = FontFamilies.BODY,
        size = BODY_SIZE,
        weight = FontWeight.Normal,
        lineHeight = BODY_LINE_HEIGHT,
    )

    val bodySmall: TextStyle = TextStyle(
        family = FontFamilies.BODY,
        size = BODY_SMALL_SIZE,
        weight = FontWeight.Normal,
        lineHeight = BODY_LINE_HEIGHT,
    )

    val serifLead: TextStyle = TextStyle(
        family = FontFamilies.SERIF,
        size = SERIF_LEAD_SIZE,
        weight = FontWeight.Normal,
        lineHeight = SERIF_LEAD_LINE_HEIGHT,
    )

    val serifBody: TextStyle = TextStyle(
        family = FontFamilies.SERIF,
        size = SERIF_BODY_SIZE,
        weight = FontWeight.Normal,
        lineHeight = SERIF_BODY_LINE_HEIGHT,
    )

    val metaLabel: TextStyle = TextStyle(
        family = FontFamilies.MONO,
        size = META_LABEL_SIZE,
        weight = FontWeight.Medium,
        lineHeight = META_LINE_HEIGHT,
        letterSpacing = META_LABEL_LETTER_SPACING_EM.em,
        transform = TextTransform.Uppercase,
    )

    val metaSmall: TextStyle = TextStyle(
        family = FontFamilies.MONO,
        size = META_SMALL_SIZE,
        weight = FontWeight.Medium,
        lineHeight = META_LINE_HEIGHT,
        letterSpacing = META_SMALL_LETTER_SPACING_EM.em,
    )

    /**
     * Long-form mono eyebrow. Same family / size / weight / line-height as
     * [metaLabel] but with the uppercase transform and positive tracking
     * dropped so phrases past ~20 characters stay readable (ALL-CAPS on
     * 30+ char strings defeats word-shape recognition). Reach for this
     * when the eyebrow is a sentence fragment (`// commercial project` +
     * separator + `2021 - 2022`), not a single short label (`ROLE`, `STATUS`).
     */
    val metaPlain: TextStyle = TextStyle(
        family = FontFamilies.MONO,
        size = META_PLAIN_SIZE,
        weight = FontWeight.Medium,
        lineHeight = META_LINE_HEIGHT,
    )

    val chip: TextStyle = TextStyle(
        family = FontFamilies.MONO,
        size = CHIP_SIZE,
        weight = FontWeight.Medium,
        lineHeight = META_LINE_HEIGHT,
    )

    val code: TextStyle = TextStyle(
        family = FontFamilies.MONO,
        size = CODE_SIZE,
        weight = FontWeight.Normal,
        lineHeight = CODE_LINE_HEIGHT,
    )
}
