package dev.tonholo.portfolio.components.organisms.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.unit.sp
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Button as WebButton
import org.jetbrains.compose.web.dom.Text as ComposeText

private val TABS_BAR_BORDER_WIDTH_PX = 1.dp
private val TAB_FONT_SIZE = 12.sp

// ---------------------------------------------------------------------------
// `.prose` body. Serif 19px, 1.65 line-height, max-width 66ch, color ink-2.
// `.prose p + p { margin-top: 1.2em; }`  -- Kotlin-side: the first paragraph
// renders with no top margin, subsequent paragraphs get 1.2em top margin.
// `.prose h3`: display font, 22px, weight 500, letter-spacing -0.015em, color
// ink, margin 2em 0 0.6em.
// ---------------------------------------------------------------------------
private val PROSE_FONT_SIZE = 19.sp
private const val PROSE_LINE_HEIGHT = 1.65
private const val PROSE_MAX_WIDTH_CH = 66.0
private const val PROSE_P_GAP_EM = 1.2
private val PROSE_H3_FONT_SIZE = 22.sp
private const val PROSE_H3_LETTER_SPACING_EM = -0.015
private const val PROSE_H3_LINE_HEIGHT = 1.2
private const val PROSE_H3_MARGIN_TOP_EM = 2.0
private const val PROSE_H3_MARGIN_BOTTOM_EM = 0.6

// ---------------------------------------------------------------------------
// Now-panel `// updated` timestamp. Handoff inline: mono 13px, muted color,
// 20px bottom margin.
// ---------------------------------------------------------------------------
private val NOW_TIMESTAMP_FONT_SIZE = 13.dp

// ---------------------------------------------------------------------------
// Hobbies panel. Handoff inline: 2-column grid at max-width 900px, 48px gap.
// Each cell: accent meta label (`/cooking`) + serif 17px body copy.
// ---------------------------------------------------------------------------
private const val HOBBIES_META_LETTER_SPACING_EM = 0.08
private val HOBBIES_META_FONT_SIZE = 11.sp
private const val HOBBIES_META_LINE_HEIGHT = 1.4
private val HOBBIES_BODY_FONT_SIZE = 17.sp
private const val HOBBIES_BODY_LINE_HEIGHT = 1.55

/**
 * Tabs on the About page. Ports `.tabs` + `.tab-panel` from the handoff: four
 * tabs (Story / Now / Hobbies / Beyond code) render a mono numbered prefix
 * (`01`, `02`, ...) before the label. The active tab gets an accent bottom
 * border; the others stay muted. Only the active panel renders; the rest stay
 * unmounted (matching the handoff `.tab-panel { display: none }` rule).
 */
enum class AboutTab(val number: String) {
    Story(number = "01"),
    Now(number = "02"),
    Hobbies(number = "03"),
    Beyond(number = "04"),
}

/**
 * Resolves the localized label for a given [AboutTab]. Kept inline so callers
 * do not have to thread [Strings.About] through every helper.
 */
private fun AboutTab.labelFor(strings: Strings.About): String = when (this) {
    AboutTab.Story -> strings.tabStory
    AboutTab.Now -> strings.tabNow
    AboutTab.Hobbies -> strings.tabHobbies
    AboutTab.Beyond -> strings.tabBeyond
}

/**
 * About page tabs organism. Maps to the `.tabs` + `.tab-panel` blocks in the
 * handoff CSS and the matching `AboutPage` JSX (lines 19-85). Internal state
 * lives in the organism; the parent only owns layout.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun AboutTabs(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(AboutTab.Story) }
    val strings = LocalStrings.current.about

    Div(attrs = modifier.toAttrs()) {
        Div(
            attrs = TabsBarStyle
                .toModifier()
                .role("tablist")
                .toAttrs(),
        ) {
            AboutTab.entries.forEach { tab ->
                TabButton(
                    tab = tab,
                    label = tab.labelFor(strings = strings),
                    isActive = tab == selected,
                    onClick = { selected = tab },
                )
            }
        }
        Div(
            attrs = Modifier.toAttrs {
                attr("role", "tabpanel")
                attr("id", "tabpanel-${selected.name.lowercase()}")
                attr("aria-labelledby", "tab-${selected.name.lowercase()}")
            },
        ) {
            when (selected) {
                AboutTab.Story -> StoryPanel(strings = strings)
                AboutTab.Now -> NowPanel(strings = strings)
                AboutTab.Hobbies -> HobbiesPanel(strings = strings)
                AboutTab.Beyond -> BeyondPanel(strings = strings)
            }
        }
    }
}

/**
 * One button in the tab bar. Active state renders the ink color and an accent
 * bottom border; inactive state stays muted with a transparent border.
 */
@Composable
private fun TabButton(
    tab: AboutTab,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    val tokens = LocalPortfolioTokens.current
    val textColor = if (isActive) tokens.ink.ink else tokens.muted.muted
    val borderColor = if (isActive) tokens.accent.accent else Transparent

    val finalModifier = TabButtonStyle
        .toModifier()
        .role("tab")
        .color(textColor)
        .borderBottom(
            width = 2.dp,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .onClick { onClick() }

    WebButton(
        attrs = finalModifier.toAttrs {
            val tabId = "tab-${tab.name.lowercase()}"
            val panelId = "tabpanel-${tab.name.lowercase()}"
            attr("type", "button")
            attr("id", tabId)
            attr("aria-controls", panelId)
            attr("aria-selected", isActive.toString())
        },
    ) {
        Span(
            attrs = Modifier
                .color(tokens.muted.muted2)
                .margin(right = SiteDimensions.size.Sm)
                .toAttrs(),
        ) {
            ComposeText(tab.number)
        }
        ComposeText(label)
    }
}

private val Transparent: Color = Color.rgba(r = 0, g = 0, b = 0, a = 0f)

// ---------------------------------------------------------------------------
// Panels
// ---------------------------------------------------------------------------

@Composable
private fun StoryPanel(strings: Strings.About) {
    val tokens = LocalPortfolioTokens.current
    ProseContainer {
        ProseParagraph(text = strings.storyIntro1, isFirst = true, tokens = tokens)
        ProseParagraph(text = strings.storyIntro2, isFirst = false, tokens = tokens)
        ProseHeading(text = strings.storyHeading2010, tokens = tokens)
        ProseParagraph(text = strings.storyBody2010, isFirst = true, tokens = tokens)
        ProseHeading(text = strings.storyHeading2015, tokens = tokens)
        ProseParagraph(text = strings.storyBody2015, isFirst = true, tokens = tokens)
        ProseHeading(text = strings.storyHeading2021, tokens = tokens)
        ProseParagraph(text = strings.storyBody2021, isFirst = true, tokens = tokens)
        ProseHeading(text = strings.storyHeadingToday, tokens = tokens)
        ProseParagraph(text = strings.storyBodyToday, isFirst = true, tokens = tokens)
    }
}

@Composable
private fun NowPanel(strings: Strings.About) {
    val tokens = LocalPortfolioTokens.current
    ProseContainer {
        P(
            attrs = NowTimestampStyle
                .toModifier()
                .color(tokens.muted.muted)
                .toAttrs(),
        ) {
            ComposeText(strings.nowUpdatedPrefix)
        }
        ProseHeading(text = strings.nowHeadingWorking, tokens = tokens, collapseTop = true)
        ProseParagraph(text = strings.nowBodyWorking, isFirst = true, tokens = tokens)
        ProseHeading(text = strings.nowHeadingLearning, tokens = tokens)
        ProseParagraph(text = strings.nowBodyLearning, isFirst = true, tokens = tokens)
        ProseHeading(text = strings.nowHeadingWriting, tokens = tokens)
        ProseParagraph(text = strings.nowBodyWriting, isFirst = true, tokens = tokens)
        ProseHeading(text = strings.nowHeadingThinking, tokens = tokens)
        ProseParagraph(text = strings.nowBodyThinking, isFirst = true, tokens = tokens)
    }
}

@Composable
private fun HobbiesPanel(strings: Strings.About) {
    val tokens = LocalPortfolioTokens.current
    Div(attrs = HobbiesGridStyle.toModifier().toAttrs()) {
        strings.hobbies.forEach { hobby ->
            HobbyCell(hobby = hobby, tokens = tokens)
        }
    }
}

@Composable
private fun HobbyCell(hobby: Strings.About.Hobby, tokens: PortfolioTokens) {
    Div(attrs = Modifier.toAttrs()) {
        Div(
            attrs = HobbiesMetaStyle
                .toModifier()
                .color(tokens.accent.accent)
                .toAttrs(),
        ) {
            ComposeText("/" + hobby.label.lowercase())
        }
        P(
            attrs = HobbiesBodyStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(hobby.body)
        }
    }
}

@Composable
private fun BeyondPanel(strings: Strings.About) {
    val tokens = LocalPortfolioTokens.current
    ProseContainer {
        ProseParagraph(text = strings.beyondBody1, isFirst = true, tokens = tokens)
        ProseParagraph(text = strings.beyondBody2, isFirst = false, tokens = tokens)
        ProseParagraph(text = strings.beyondBody3, isFirst = false, tokens = tokens)
    }
}

// ---------------------------------------------------------------------------
// Prose primitives
// ---------------------------------------------------------------------------

@Composable
private fun ProseContainer(content: @Composable () -> Unit) {
    val tokens = LocalPortfolioTokens.current
    Div(
        attrs = ProseStyle
            .toModifier()
            .color(tokens.ink.ink2)
            .toAttrs(),
    ) {
        content()
    }
}

@Composable
private fun ProseParagraph(text: String, isFirst: Boolean, tokens: PortfolioTokens) {
    val margin = if (isFirst) 0.em else PROSE_P_GAP_EM.em
    P(
        attrs = Modifier
            .margin(top = margin, bottom = 0.em)
            .color(tokens.ink.ink2)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

@Composable
private fun ProseHeading(text: String, tokens: PortfolioTokens, collapseTop: Boolean = false) {
    val topMargin = if (collapseTop) 0.em else PROSE_H3_MARGIN_TOP_EM.em
    H2(
        attrs = AboutProseHeadingStyle
            .toModifier()
            .margin(top = topMargin, bottom = PROSE_H3_MARGIN_BOTTOM_EM.em)
            .color(tokens.ink.ink)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

// ---------------------------------------------------------------------------
// Styles
// ---------------------------------------------------------------------------

/**
 * Base `CssStyle` for the `.tabs` container. Flex row with a 1px bottom rule
 * in `paper-3` and 40px top / 32px bottom margins. The rule color is read
 * from the active theme's paper palette.
 */
val TabsBarStyle = CssStyle {
    base {
        val isDark = colorMode == ColorMode.DARK
        val ruleColor = if (isDark) Paper.dark.paper2 else Paper.light.paper3
        Modifier
            .display(DisplayStyle.Flex)
            // Tabs scroll horizontally if the viewport is narrower than their
            // combined width, but vertical overflow is explicitly hidden to
            // avoid a stray scrollbar caused by the 1px active tab overlap.
            .minWidth(0.px)
            .overflow {
                x(Overflow.Auto)
                y(Overflow.Hidden)
            }
            .styleModifier {
                property("scrollbar-width", "none")
            }
            .borderBottom(width = TABS_BAR_BORDER_WIDTH_PX, style = LineStyle.Solid, color = ruleColor)
            .margin(
                top = SiteDimensions.size.Xxl,
                bottom = SiteDimensions.size.Xl,
            )
    }
    cssRule("::-webkit-scrollbar") {
        Modifier.display(DisplayStyle.None)
    }
}

/**
 * Base `CssStyle` for a single tab button. Mono typography at 13px, tight
 * 14px x 22px padding, no top/side border, -1px bottom margin so the active
 * accent line overlaps the shared rule below. Color and the bottom border
 * color are applied inline by [TabButton] from the active theme.
 */
val TabButtonStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(TAB_FONT_SIZE)
            .fontWeight(FontWeight.Medium)
            .padding(
                topBottom = SiteDimensions.size.Md,
                leftRight = SiteDimensions.size.Lg,
            )
            .backgroundColor(Transparent)
            .border(style = LineStyle.None)
            .margin(bottom = (-1).dp)
            .cursor(Cursor.Pointer)
            // Keep each label on a single line so the horizontal scroll inside
            // the tabs bar works predictably on narrow viewports.
            .whiteSpace(WhiteSpace.NoWrap)
            .transition(
                Transition.group(
                    properties = listOf("color", "border-color"),
                    duration = 0.18.s,
                ),
            )
    }
}

/**
 * Base `CssStyle` for `.prose`. Serif 19px body copy, 1.65 line-height, capped
 * at 66ch. Color is applied inline by [ProseContainer] from the active theme.
 */
val ProseStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.SERIF)
            .fontSize(PROSE_FONT_SIZE)
            .lineHeight(PROSE_LINE_HEIGHT)
            .maxWidth(PROSE_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
    }
}

/**
 * Base `CssStyle` for `.prose h3`: display font, 22px, weight 500, tight
 * letter-spacing. Top / bottom margins are applied inline by [ProseHeading]
 * so the `Now` panel's first heading can collapse the top gap.
 */
val AboutProseHeadingStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.DISPLAY)
            .fontSize(PROSE_H3_FONT_SIZE)
            .fontWeight(FontWeight.Medium)
            .lineHeight(PROSE_H3_LINE_HEIGHT)
            .letterSpacing(PROSE_H3_LETTER_SPACING_EM.em)
    }
}

/**
 * Base `CssStyle` for the Now panel's `// updated` timestamp. Mono 13px, 20px
 * bottom margin; color is applied inline from the `muted` token.
 */
val NowTimestampStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(NOW_TIMESTAMP_FONT_SIZE)
            .margin(top = 0.cssRem, bottom = SiteDimensions.size.Xl)
    }
}

/**
 * Base `CssStyle` for the hobbies 2-column grid. Single column at the base
 * width; switches to two equal columns at [Breakpoint.LG]. 48px gap, capped
 * at 900px wide per the handoff's inline `maxWidth:900`.
 */
val HobbiesGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { size(value = 1.fr) }
            .gap(SiteDimensions.size.Xxl)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            size(value = 1.fr)
            size(value = 1.fr)
        }
    }
}

/**
 * Base `CssStyle` for the meta label inside a hobbies cell (`.meta.meta-accent`).
 * Mono 11px uppercase with 0.08em tracking and an 8px bottom margin. Color is
 * applied inline so it follows the active accent swatch.
 */
val HobbiesMetaStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(HOBBIES_META_FONT_SIZE)
            .fontWeight(FontWeight.Medium)
            .lineHeight(HOBBIES_META_LINE_HEIGHT)
            .letterSpacing(HOBBIES_META_LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .margin(bottom = SiteDimensions.size.Sm)
    }
}

/**
 * Base `CssStyle` for the body copy inside a hobbies cell. Serif 17px with a
 * 1.55 line-height; color is applied inline from the `ink-2` token.
 */
val HobbiesBodyStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.SERIF)
            .fontSize(HOBBIES_BODY_FONT_SIZE)
            .lineHeight(HOBBIES_BODY_LINE_HEIGHT)
            .margin(topBottom = 0.cssRem)
    }
}
