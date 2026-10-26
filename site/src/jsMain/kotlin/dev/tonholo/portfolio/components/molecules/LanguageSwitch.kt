package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.ariaLabel
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Button as WebButton
import org.jetbrains.compose.web.dom.Text as ComposeText

// `.lang-switch` geometry (from handoff styles.css).
private val LANG_SWITCH_FONT_SIZE = 12.dp
private val LANG_SWITCH_GAP = 4.dp

// `.lang-opt` geometry.
private val LANG_OPT_PADDING_Y = 6.dp
private val LANG_OPT_PADDING_X = 4.dp
private val LANG_OPT_RADIUS = 2.dp
private const val LANG_OPT_LETTER_SPACING_EM = 0.02
private const val LANG_OPT_TRANSITION_DURATION_S = 0.15

// WCAG 2.5.5 touch-target floor on `pointer: coarse` devices.
private val LANG_OPT_TOUCH_MIN_SIZE = 44.dp

// `.lang-opt .lang-locale` dim styling, tuned to 0.75 so contrast passes WCAG AA (4.5:1).
private const val LANG_LOCALE_OPACITY = 0.75

// `.lang-sep` dim styling.
private const val LANG_SEP_OPACITY = 0.3

// Locale display parts: base code + dimmer suffix. `en-CA` -> ("en", "-CA").
private const val LOCALE_CODE_SEPARATOR = "-"

// Titles shown on hover for each option.
private const val TITLE_EN_CA = "English (Canada)"
private const val TITLE_PT_BR = "Português (Brasil)"

// Screen-reader label for the outer `role="group"` container.
private const val GROUP_ARIA_LABEL = "language"

private val TransparentColor: Color = Color.rgba(r = 0, g = 0, b = 0, a = 0f)

/**
 * Language selector pill pair. Maps to `.lang-switch` in the handoff CSS:
 * two mono buttons rendering the BCP-47 code (`en-CA` / `pt-BR`) separated
 * by a muted `/` glyph. The active option uses the accent color and a
 * heavier weight; the `-CA` / `-BR` suffix is always dimmer than the
 * leading language code.
 *
 * @param current the [Locale] whose option is rendered `.active`; also
 *   drives each button's `aria-pressed`.
 * @param onSwitch invoked with the clicked [Locale] when either option is
 *   pressed.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun LanguageSwitch(current: Locale, onSwitch: (Locale) -> Unit, modifier: Modifier = Modifier) {
    val finalModifier = LanguageSwitchStyle
        .toModifier()
        .role("group")
        .ariaLabel(GROUP_ARIA_LABEL)
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        LanguageOption(
            locale = Locale.EnCa,
            current = current,
            title = TITLE_EN_CA,
            onSwitch = onSwitch,
        )
        LanguageSeparator()
        LanguageOption(
            locale = Locale.PtBr,
            current = current,
            title = TITLE_PT_BR,
            onSwitch = onSwitch,
        )
    }
}

@Composable
private fun LanguageOption(
    locale: Locale,
    current: Locale,
    title: String,
    onSwitch: (Locale) -> Unit,
) {
    val tokens = LocalPortfolioTokens.current
    val isActive = current == locale

    val (code, suffix) = splitLocaleTag(locale.tag)

    val activeColor = tokens.accent.accent
    val baseColor = tokens.ink.ink3

    val optionModifier = LanguageOptionStyle
        .toModifier()
        .color(if (isActive) activeColor else baseColor)
        .fontWeight(if (isActive) FontWeight.SemiBold else FontWeight.Normal)
        .onClick { onSwitch(locale) }

    WebButton(
        attrs = optionModifier.toAttrs {
            attr("type", "button")
            attr("aria-pressed", isActive.toString())
            attr("aria-label", title)
            attr("title", title)
        },
    ) {
        ComposeText(code)
        Span(
            attrs = LanguageLocaleSuffixStyle
                .toModifier()
                .toAttrs(),
        ) {
            ComposeText(suffix)
        }
    }
}

@Composable
private fun LanguageSeparator() {
    Span(
        attrs = LanguageSeparatorStyle
            .toModifier()
            .toAttrs { attr("aria-hidden", "true") },
    ) {
        ComposeText(value = "/")
    }
}

private fun splitLocaleTag(tag: String): Pair<String, String> {
    val dashIndex = tag.indexOf(LOCALE_CODE_SEPARATOR)
    return if (dashIndex < 0) {
        tag to ""
    } else {
        tag.substring(startIndex = 0, endIndex = dashIndex) to tag.substring(startIndex = dashIndex)
    }
}

/**
 * Base `CssStyle` for [LanguageSwitch]. Ports `.lang-switch`: an inline flex
 * row, 4px gap, mono font at 12px, `ink-3` resting color.
 */
val LanguageSwitchStyle = CssStyle {
    val isDark = colorMode == ColorMode.DARK
    val ink = if (isDark) Ink.dark else Ink.light
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(LANG_SWITCH_GAP)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(LANG_SWITCH_FONT_SIZE)
            .color(ink.ink3)
    }
}

/**
 * Base `CssStyle` for each `.lang-opt` pill. Transparent background, no
 * border, 6px/4px padding, 2px corner radius, 0.02em tracking, with a
 * color/background transition for the hover state. Hover brightens color
 * to `ink`; the active color and font weight are applied inline by the
 * composable so they stay token-aware.
 */
val LanguageOptionStyle = CssStyle {
    val isDark = colorMode == ColorMode.DARK
    val ink = if (isDark) Ink.dark else Ink.light
    base {
        Modifier
            .backgroundColor(TransparentColor)
            .border(style = LineStyle.None)
            .padding(
                topBottom = LANG_OPT_PADDING_Y,
                leftRight = LANG_OPT_PADDING_X,
            )
            .cursor(Cursor.Pointer)
            .color(ink.ink3)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(LANG_SWITCH_FONT_SIZE)
            .letterSpacing(LANG_OPT_LETTER_SPACING_EM.em)
            .borderRadius(LANG_OPT_RADIUS)
            .transition(
                Transition.of(
                    property = "color",
                    duration = LANG_OPT_TRANSITION_DURATION_S.s,
                ),
                Transition.of(
                    property = "background",
                    duration = LANG_OPT_TRANSITION_DURATION_S.s,
                ),
            )
    }
    hover {
        Modifier.color(ink.ink)
    }
    cssRule(
        mediaQuery = CSSMediaQuery.MediaFeature(
            name = "pointer",
            value = StylePropertyValue(value = "coarse"),
        ),
    ) {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .minWidth(LANG_OPT_TOUCH_MIN_SIZE)
            .minHeight(LANG_OPT_TOUCH_MIN_SIZE)
    }
}

/**
 * Base `CssStyle` for the `-CA` / `-BR` suffix inside each option. Keeps the
 * suffix visually lighter than the leading language code per
 * `.lang-opt .lang-locale { opacity: 0.5; font-weight: 400; }`.
 */
val LanguageLocaleSuffixStyle = CssStyle {
    base {
        Modifier
            .opacity(value = LANG_LOCALE_OPACITY)
            .fontWeight(FontWeight.Normal)
    }
}

/**
 * Base `CssStyle` for the muted `/` separator between options. Maps to
 * `.lang-sep { opacity: 0.3; user-select: none; }` in the handoff.
 */
val LanguageSeparatorStyle = CssStyle {
    base {
        Modifier
            .opacity(value = LANG_SEP_OPACITY)
            .userSelect(UserSelect.None)
    }
}
