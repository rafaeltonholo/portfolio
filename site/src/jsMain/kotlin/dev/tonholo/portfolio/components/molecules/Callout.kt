package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.descendants
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.components.atoms.PhosphorWeight
import dev.tonholo.portfolio.state.LocalThemeState
import dev.tonholo.portfolio.state.ThemeState
import dev.tonholo.portfolio.theme.color.AccentAlternates
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.applyInlineCodeStyle
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ch
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val LIGHT_NOTE_COLOR_HEX = 0x0969DA
private const val DARK_NOTE_COLOR_HEX = 0x4493F8

private const val TINT_ALPHA_LIGHT = 0.06f
private const val TINT_ALPHA_DARK = 0.12f

private val CALLOUT_BORDER_WIDTH = 4.dp
private val CALLOUT_BORDER_RADIUS_END = 6.dp
private val CALLOUT_BORDER_RADIUS_START = 0.dp
private const val CALLOUT_PADDING_Y_REM = 0.875
private const val CALLOUT_PADDING_X_REM = 1.125
private const val CALLOUT_MARGIN_Y_REM = 1.25
private const val CALLOUT_TITLE_GAP_REM = 0.5
private const val CALLOUT_TITLE_MARGIN_BOTTOM_REM = 0.5
private val CALLOUT_TITLE_FONT_SIZE = 14.px
private val CALLOUT_BODY_FONT_SIZE = 15.px
private const val CALLOUT_BODY_LINE_HEIGHT = 1.6
private const val CALLOUT_MAX_WIDTH_CH = 68
private const val CALLOUT_TITLE_LETTER_SPACING_EM = -0.01
private const val CALLOUT_ICON_SIZE_REM = 1.125
private const val PARAGRAPH_BOTTOM_GAP_EM = 0.5

/**
 * All supported alert and quote types for markdown callouts.
 *
 * Covers every standard GitHub Flavored Markdown alert keyword (`NOTE`, `TIP`,
 * `IMPORTANT`, `WARNING`, `CAUTION`) alongside standard blockquotes (`QUOTE`).
 *
 * @param iconName canonical Phosphor icon name for this callout type.
 * @param defaultTitle default uppercase title label rendered in the header.
 * @param lightColor accent color used when rendering in light color mode.
 * @param darkColor accent color used when rendering in dark color mode.
 */
enum class CalloutType(
    val iconName: String,
    val defaultTitle: String,
    val lightColor: Color,
    val darkColor: Color,
) {
    NOTE(
        iconName = "info",
        defaultTitle = "Note",
        lightColor = Color.rgb(LIGHT_NOTE_COLOR_HEX),
        darkColor = Color.rgb(DARK_NOTE_COLOR_HEX),
    ),
    TIP(
        iconName = "lightbulb",
        defaultTitle = "Tip",
        lightColor = AccentMint.light.accent,
        darkColor = AccentMint.dark.accent,
    ),
    IMPORTANT(
        iconName = "warning-circle",
        defaultTitle = "Important",
        lightColor = AccentAlternates.purple.light.accent,
        darkColor = AccentAlternates.purple.dark.accent,
    ),
    WARNING(
        iconName = "warning",
        defaultTitle = "Warning",
        lightColor = AccentAlternates.amber.light.accent,
        darkColor = AccentAlternates.amber.dark.accent,
    ),
    CAUTION(
        iconName = "warning-octagon",
        defaultTitle = "Caution",
        lightColor = AccentAlternates.crimson.light.accent,
        darkColor = AccentAlternates.crimson.dark.accent,
    ),
    QUOTE(
        iconName = "quotes",
        defaultTitle = "",
        lightColor = AccentMint.light.accent,
        darkColor = AccentMint.dark.accent,
    ),
    ;

    /**
     * Resolves the prominent accent color for the callout border and title header.
     *
     * @param isDark whether dark mode is currently active.
     * @return [Color] for border and title.
     */
    fun resolveAccentColor(isDark: Boolean): Color = if (isDark) darkColor else lightColor

    /**
     * Resolves the background tint color for the callout surface.
     *
     * @param isDark whether dark mode is currently active.
     * @return [Color] with alpha tint for the surface.
     */
    fun resolveBackgroundColor(isDark: Boolean): Color {
        val accent = resolveAccentColor(isDark = isDark).toRgb()
        val alpha = if (isDark) TINT_ALPHA_DARK else TINT_ALPHA_LIGHT
        return Color.rgba(r = accent.red, g = accent.green, b = accent.blue, a = alpha)
    }
}

/**
 * Renders a GitHub-style alert or quotation callout with a colored indicator border,
 * subtle surface tint, icon header, and formatted body content.
 *
 * @param type [CalloutType] indicating the severity/category.
 * @param modifier optional [Modifier] chained onto the container.
 * @param title optional custom title overriding the type's default label.
 * @param content composable body content (paragraphs, code, lists).
 */
@Composable
fun Callout(
    type: CalloutType,
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable () -> Unit,
) {
    val themeState: ThemeState = LocalThemeState.current
    val isDark: Boolean = themeState.current.isDark
    val accentColor: Color = type.resolveAccentColor(isDark = isDark)
    val backgroundColor: Color = type.resolveBackgroundColor(isDark = isDark)
    val resolvedTitle: String = title ?: type.defaultTitle

    Div(
        attrs = CalloutContainerStyle
            .toModifier()
            .borderLeft(width = CALLOUT_BORDER_WIDTH, style = LineStyle.Solid, color = accentColor)
            .backgroundColor(backgroundColor)
            .then(modifier)
            .toAttrs(),
    ) {
        if (resolvedTitle.isNotBlank()) {
            Div(
                attrs = CalloutHeaderStyle
                    .toModifier()
                    .color(accentColor)
                    .toAttrs(),
            ) {
                PhosphorIcon(
                    name = type.iconName,
                    weight = PhosphorWeight.Fill,
                    size = CALLOUT_ICON_SIZE_REM.cssRem,
                    modifier = Modifier.margin(right = CALLOUT_TITLE_GAP_REM.cssRem),
                )
                Span(
                    attrs = Modifier
                        .fontFamily(*FontFamilies.DISPLAY)
                        .fontWeight(FontWeight.SemiBold)
                        .fontSize(CALLOUT_TITLE_FONT_SIZE)
                        .letterSpacing(CALLOUT_TITLE_LETTER_SPACING_EM.em)
                        .toAttrs(),
                ) {
                    ComposeText(resolvedTitle)
                }
            }
        }
        Div(attrs = CalloutBodyStyle.toModifier().toAttrs()) {
            content()
        }
    }
}

/**
 * Base `CssStyle` for the callout container box.
 */
val CalloutContainerStyle = CssStyle {
    base {
        Modifier
            .borderRadius(
                topLeft = CALLOUT_BORDER_RADIUS_START,
                bottomLeft = CALLOUT_BORDER_RADIUS_START,
                topRight = CALLOUT_BORDER_RADIUS_END,
                bottomRight = CALLOUT_BORDER_RADIUS_END,
            )
            .padding(
                topBottom = CALLOUT_PADDING_Y_REM.cssRem,
                leftRight = CALLOUT_PADDING_X_REM.cssRem,
            )
            .margin(top = CALLOUT_MARGIN_Y_REM.cssRem, bottom = CALLOUT_MARGIN_Y_REM.cssRem)
            .maxWidth(CALLOUT_MAX_WIDTH_CH.ch)
    }
}

/**
 * Base `CssStyle` for the callout icon + title header row.
 */
val CalloutHeaderStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .margin(bottom = CALLOUT_TITLE_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the callout body content, normalizing inner paragraph margins.
 */
val CalloutBodyStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.BODY)
            .fontSize(CALLOUT_BODY_FONT_SIZE)
            .lineHeight(CALLOUT_BODY_LINE_HEIGHT)
    }
    descendants("p") {
        Modifier.margin(top = 0.em, bottom = PARAGRAPH_BOTTOM_GAP_EM.em)
    }
    cssRule(" > p:last-child") {
        Modifier.margin(bottom = 0.em)
    }
    cssRule(" :not(pre) > code") {
        Modifier.applyInlineCodeStyle(isDark = colorMode == ColorMode.DARK)
    }
}
