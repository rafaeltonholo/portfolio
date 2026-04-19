package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val BREADCRUMB_FONT_SIZE_REM = 0.75 // 12px
private const val BREADCRUMB_SEPARATOR_GAP_REM = 0.75 // 12px around each separator
private const val BREADCRUMB_MARGIN_BOTTOM_REM = 2.0 // 32px
private const val BACK_ICON_NAME = "arrow-left"
private const val BACK_ICON_SIZE_REM = 0.875
private const val BACK_ICON_GAP_REM = 0.25

/**
 * Top-of-page breadcrumb for a project detail route. Ports the handoff
 * `meta-row` placed above the project hero in `pages2.jsx`:
 * `← work / {project-name lowercased} / case-study.md`.
 *
 * The leading segment is a real `<a href>` accent anchor (via Kobweb's
 * [Link]) with a Phosphor `arrow-left` glyph; the trailing segments are
 * muted inline spans separated by `/`.
 *
 * @param projectName the project's display name; lowercased for the middle
 *   segment to match the handoff's `.toLowerCase()` treatment.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun ProjectBreadcrumb(projectName: String, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.projectDetail

    val finalModifier = ProjectBreadcrumbStyle
        .toModifier()
        .color(tokens.muted.muted)
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        Link(
            path = Routes.PROJECTS,
            modifier = Modifier
                .color(tokens.accent.accent)
                .cursor(Cursor.Pointer)
                .display(DisplayStyle.LegacyInlineFlex)
                .alignItems(AlignItems.Center)
                .gap(BACK_ICON_GAP_REM.cssRem)
                .textDecorationLine(TextDecorationLine.None),
            variant = UndecoratedLinkVariant then UncoloredLinkVariant,
        ) {
            PhosphorIcon(name = BACK_ICON_NAME, size = BACK_ICON_SIZE_REM.cssRem)
            ComposeText(strings.breadcrumbRoot)
        }
        Span(
            attrs = Modifier
                .margin(leftRight = BREADCRUMB_SEPARATOR_GAP_REM.cssRem)
                .toAttrs(),
        ) {
            ComposeText("/")
        }
        Span(attrs = Modifier.toAttrs()) {
            ComposeText(projectName.lowercase())
        }
        Span(
            attrs = Modifier
                .margin(leftRight = BREADCRUMB_SEPARATOR_GAP_REM.cssRem)
                .toAttrs(),
        ) {
            ComposeText("/")
        }
        Span(attrs = Modifier.toAttrs()) {
            ComposeText(strings.breadcrumbFile)
        }
    }
}

/**
 * Base `CssStyle` for [ProjectBreadcrumb]. Mono family at 12px, inline flex
 * with a 32px bottom margin so the breadcrumb hugs the top of the
 * project-detail shell and breathes before the hero.
 */
val ProjectBreadcrumbStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(BREADCRUMB_FONT_SIZE_REM.cssRem)
            .margin(bottom = BREADCRUMB_MARGIN_BOTTOM_REM.cssRem)
    }
}
