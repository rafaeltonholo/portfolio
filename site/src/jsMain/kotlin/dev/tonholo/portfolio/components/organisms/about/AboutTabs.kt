package dev.tonholo.portfolio.components.organisms.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.css.Transition
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
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.transition
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
import dev.tonholo.portfolio.theme.typography.FontFamilies
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
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Button as WebButton
import org.jetbrains.compose.web.dom.Text as ComposeText

// ---------------------------------------------------------------------------
// Tab bar. Handoff `.tabs` (styles.css): flex row, no gap, border-bottom 1px
// `paper-3`, 40px top + 32px bottom margin.
// ---------------------------------------------------------------------------
private const val TABS_BAR_MARGIN_TOP_REM = 2.5 // 40px
private const val TABS_BAR_MARGIN_BOTTOM_REM = 2.0 // 32px
private const val TABS_BAR_BORDER_WIDTH_PX = 1.0 // 1px

// ---------------------------------------------------------------------------
// Single tab button. Handoff `.tab`: mono 13px, 14px x 22px padding, 2px
// bottom border, -1px bottom margin so the active accent line sits flush on
// the shared rule, 0.18s transition on color + border-color. Numeric prefix
// (`.num`) uses muted-2 color and 8px right margin.
// ---------------------------------------------------------------------------
private const val TAB_FONT_SIZE_REM = 0.8125 // 13px
private const val TAB_PADDING_Y_REM = 0.875 // 14px
private const val TAB_PADDING_X_REM = 1.375 // 22px
private const val TAB_BORDER_BOTTOM_WIDTH_PX = 2.0 // 2px
private const val TAB_MARGIN_BOTTOM_PX = -1.0 // -1px so the accent line overlaps
private const val TAB_NUM_MARGIN_RIGHT_REM = 0.5 // 8px
private const val TAB_TRANSITION_DURATION_S = 0.18

// ---------------------------------------------------------------------------
// `.prose` body. Serif 19px, 1.65 line-height, max-width 66ch, color ink-2.
// `.prose p + p { margin-top: 1.2em; }`  -- Kotlin-side: the first paragraph
// renders with no top margin, subsequent paragraphs get 1.2em top margin.
// `.prose h3`: display font, 22px, weight 500, letter-spacing -0.015em, color
// ink, margin 2em 0 0.6em.
// ---------------------------------------------------------------------------
private const val PROSE_FONT_SIZE_REM = 1.1875 // 19px
private const val PROSE_LINE_HEIGHT = 1.65
private const val PROSE_MAX_WIDTH_CH = 66.0
private const val PROSE_P_GAP_EM = 1.2
private const val PROSE_H3_FONT_SIZE_REM = 1.375 // 22px
private const val PROSE_H3_LETTER_SPACING_EM = -0.015
private const val PROSE_H3_LINE_HEIGHT = 1.2
private const val PROSE_H3_MARGIN_TOP_EM = 2.0
private const val PROSE_H3_MARGIN_BOTTOM_EM = 0.6

// ---------------------------------------------------------------------------
// Now-panel `// updated` timestamp. Handoff inline: mono 13px, muted color,
// 20px bottom margin.
// ---------------------------------------------------------------------------
private const val NOW_TIMESTAMP_FONT_SIZE_REM = 0.8125 // 13px
private const val NOW_TIMESTAMP_MARGIN_BOTTOM_REM = 1.25 // 20px

// ---------------------------------------------------------------------------
// Hobbies panel. Handoff inline: 2-column grid at max-width 900px, 48px gap.
// Each cell: accent meta label (`/cooking`) + serif 17px body copy.
// ---------------------------------------------------------------------------
private const val HOBBIES_GRID_GAP_REM = 3.0 // 48px
private const val HOBBIES_GRID_MAX_WIDTH_REM = 56.25 // 900px
private const val HOBBIES_GRID_COL_FR = 1.0
private const val HOBBIES_META_MARGIN_BOTTOM_REM = 0.5 // 8px
private const val HOBBIES_META_LETTER_SPACING_EM = 0.08
private const val HOBBIES_META_FONT_SIZE_REM = 0.6875 // 11px
private const val HOBBIES_META_LINE_HEIGHT = 1.4
private const val HOBBIES_BODY_FONT_SIZE_REM = 1.0625 // 17px
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
        Div(attrs = TabsBarStyle.toModifier().toAttrs()) {
            AboutTab.entries.forEach { tab ->
                TabButton(
                    tab = tab,
                    label = tab.labelFor(strings = strings),
                    isActive = tab == selected,
                    onClick = { selected = tab },
                )
            }
        }
        when (selected) {
            AboutTab.Story -> StoryPanel(strings = strings)
            AboutTab.Now -> NowPanel(strings = strings)
            AboutTab.Hobbies -> HobbiesPanel()
            AboutTab.Beyond -> BeyondPanel()
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
        .color(textColor)
        .borderBottom(
            width = TAB_BORDER_BOTTOM_WIDTH_PX.px,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .onClick { onClick() }

    WebButton(attrs = finalModifier.toAttrs { attr("type", "button") }) {
        Span(
            attrs = Modifier
                .color(tokens.muted.muted2)
                .margin(right = TAB_NUM_MARGIN_RIGHT_REM.cssRem)
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
        ProseParagraph(
            text = "Ever since I was little, I've been trying to break things open " +
                "to see how they worked - usually my parents' computer, occasionally " +
                "a VHS player I regret.",
            isFirst = true,
            tokens = tokens,
        )
        ProseParagraph(
            text = "My first real contact with programming was at twelve, when I decided " +
                "I wanted a private MU server for me and my cousins. I didn't know what I " +
                "was doing. The server was unstable. The cousins were thrilled.",
            isFirst = false,
            tokens = tokens,
        )
        ProseHeading(text = strings.storyHeading2010, tokens = tokens)
        ProseParagraph(
            text = "High school technical program, Java class, and the Android SDK was " +
                "brand new. I built a barely functional TODO app. It crashed on orientation " +
                "change. I was hooked.",
            isFirst = true,
            tokens = tokens,
        )
        ProseHeading(text = strings.storyHeading2015, tokens = tokens)
        ProseParagraph(
            text = "After a few years of full-stack work, I realized I only wanted to work " +
                "on what I could hold. Android became the focus. Kotlin landed the next year " +
                "and never left.",
            isFirst = true,
            tokens = tokens,
        )
        ProseHeading(text = strings.storyHeading2021, tokens = tokens)
        ProseParagraph(
            text = "First time working on a codebase with millions of users and millions of " +
                "lines. Everything I thought I knew about architecture got rearranged. Compose " +
                "migration, dynamic features, and a 30-minute build time that became a personal " +
                "vendetta.",
            isFirst = true,
            tokens = tokens,
        )
        ProseHeading(text = strings.storyHeadingToday, tokens = tokens)
        ProseParagraph(
            text = "Bleacher Report, Ottawa, more Compose, more Kotlin. Writing a bit more. " +
                "Cooking a lot more.",
            isFirst = true,
            tokens = tokens,
        )
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
        ProseParagraph(
            text = "A feature-delivery system at Bleacher Report that lets us ship entire " +
                "sections on-demand without the usual APK bloat. Think Play Feature Delivery, " +
                "but with sensible developer ergonomics.",
            isFirst = true,
            tokens = tokens,
        )
        ProseHeading(text = strings.nowHeadingLearning, tokens = tokens)
        ProseParagraph(
            text = "Compose Multiplatform on desktop. It's rough in spots but the feeling of " +
                "one codebase across 4 targets is genuinely exciting again.",
            isFirst = true,
            tokens = tokens,
        )
        ProseHeading(text = strings.nowHeadingWriting, tokens = tokens)
        ProseParagraph(
            text = "Two drafts in the queue - one on Compose stability (the good, the painful, " +
                "the \"actually fine\") and one on why I stopped fearing Gradle.",
            isFirst = true,
            tokens = tokens,
        )
        ProseHeading(text = strings.nowHeadingThinking, tokens = tokens)
        ProseParagraph(
            text = "How to share knowledge without falling into the trap of writing beginner " +
                "content I don't enjoy writing. Also, whether to start a YouTube channel " +
                "(probably not).",
            isFirst = true,
            tokens = tokens,
        )
    }
}

/** Hardcoded hobby entries for the hobbies panel. */
private val HOBBIES = listOf(
    "Cooking" to
        "Mostly Italian, slow, on Sundays. My pasta game is solid, my bread game is a work in progress.",
    "Coffee" to
        "V60 every morning, usually Ethiopian. I own more filters than I should admit.",
    "Running" to
        "The Ottawa river path in every season. 10K most weeks, a half-marathon twice a year.",
    "Games" to
        "Metroidvanias. Currently replaying Hollow Knight (again). Silksong, any day now.",
    "Reading" to
        "Non-fiction tech + one novel always going in parallel. Current pair: DDIA and Station Eleven.",
    "Travel" to
        "Short trips inside Canada. Long trips back home to Brazil every year or two.",
)

@Composable
private fun HobbiesPanel() {
    val tokens = LocalPortfolioTokens.current
    Div(attrs = HobbiesGridStyle.toModifier().toAttrs()) {
        HOBBIES.forEach { (key, body) ->
            HobbyCell(key = key, body = body, tokens = tokens)
        }
    }
}

@Composable
private fun HobbyCell(key: String, body: String, tokens: PortfolioTokens) {
    Div(attrs = Modifier.toAttrs()) {
        Div(
            attrs = HobbiesMetaStyle
                .toModifier()
                .color(tokens.accent.accent)
                .toAttrs(),
        ) {
            ComposeText("/" + key.lowercase())
        }
        P(
            attrs = HobbiesBodyStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(body)
        }
    }
}

@Composable
private fun BeyondPanel() {
    val tokens = LocalPortfolioTokens.current
    ProseContainer {
        ProseParagraph(
            text = "I moved from Brazil to Canada in 2022 - a small decision that became a " +
                "huge one. Adjusting to the cold, the snow, the French, the strange Tim " +
                "Hortons loyalty that comes with living here.",
            isFirst = true,
            tokens = tokens,
        )
        ProseParagraph(
            text = "My wife is a product designer (and my first reader for anything I write). " +
                "We met at a university lab in Belo Horizonte where she was building, of all " +
                "things, a physical board-game prototype with Arduino. We've been making things " +
                "together ever since.",
            isFirst = false,
            tokens = tokens,
        )
        ProseParagraph(
            text = "I try to give something back to the communities I learned from - mostly " +
                "through open source and occasional talks. If you're an early-career engineer " +
                "and want to chat, my inbox is open.",
            isFirst = false,
            tokens = tokens,
        )
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
    H3(
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
            .borderBottom(width = TABS_BAR_BORDER_WIDTH_PX.px, style = LineStyle.Solid, color = ruleColor)
            .margin(
                top = TABS_BAR_MARGIN_TOP_REM.cssRem,
                bottom = TABS_BAR_MARGIN_BOTTOM_REM.cssRem,
            )
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
            .fontSize(TAB_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.Medium)
            .padding(
                topBottom = TAB_PADDING_Y_REM.cssRem,
                leftRight = TAB_PADDING_X_REM.cssRem,
            )
            .backgroundColor(Transparent)
            .border(style = LineStyle.None)
            .margin(bottom = TAB_MARGIN_BOTTOM_PX.px)
            .cursor(Cursor.Pointer)
            .transition(
                Transition.group(
                    properties = listOf("color", "border-color"),
                    duration = TAB_TRANSITION_DURATION_S.s,
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
            .fontSize(PROSE_FONT_SIZE_REM.cssRem)
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
            .fontSize(PROSE_H3_FONT_SIZE_REM.cssRem)
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
            .fontSize(NOW_TIMESTAMP_FONT_SIZE_REM.cssRem)
            .margin(top = 0.cssRem, bottom = NOW_TIMESTAMP_MARGIN_BOTTOM_REM.cssRem)
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
            .gridTemplateColumns { size(HOBBIES_GRID_COL_FR.fr) }
            .gap(HOBBIES_GRID_GAP_REM.cssRem)
            .maxWidth(HOBBIES_GRID_MAX_WIDTH_REM.cssRem)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            size(HOBBIES_GRID_COL_FR.fr)
            size(HOBBIES_GRID_COL_FR.fr)
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
            .fontSize(HOBBIES_META_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.Medium)
            .lineHeight(HOBBIES_META_LINE_HEIGHT)
            .letterSpacing(HOBBIES_META_LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .margin(bottom = HOBBIES_META_MARGIN_BOTTOM_REM.cssRem)
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
            .fontSize(HOBBIES_BODY_FONT_SIZE_REM.cssRem)
            .lineHeight(HOBBIES_BODY_LINE_HEIGHT)
            .margin(topBottom = 0.cssRem)
    }
}
