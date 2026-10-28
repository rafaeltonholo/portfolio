package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.ariaLabel
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
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
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.unit.sp
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Nav as WebNav
import org.jetbrains.compose.web.dom.Text as ComposeText

private val BREADCRUMB_FONT_SIZE = 12.sp
private const val BACK_ICON_NAME = "arrow-left"

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
        .ariaLabel("Breadcrumb")
        .color(tokens.muted.muted)
        .then(modifier)

    WebNav(attrs = finalModifier.toAttrs()) {
        Link(
            path = Routes.PROJECTS,
            modifier = Modifier
                .color(tokens.accent.accent)
                .cursor(Cursor.Pointer)
                .display(DisplayStyle.LegacyInlineFlex)
                .alignItems(AlignItems.Center)
                .gap(SiteDimensions.size.Xsm)
                .textDecorationLine(TextDecorationLine.None),
            variant = UndecoratedLinkVariant then UncoloredLinkVariant,
        ) {
            PhosphorIcon(name = BACK_ICON_NAME, size = SiteDimensions.layout.button.icon.Medium)
            ComposeText(strings.breadcrumbRoot)
        }
        Span(
            attrs = Modifier
                .margin(leftRight = SiteDimensions.size.Md)
                .toAttrs(),
        ) {
            ComposeText("/")
        }
        Span(attrs = Modifier.toAttrs()) {
            ComposeText(projectName.lowercase())
        }
        Span(
            attrs = Modifier
                .margin(leftRight = SiteDimensions.size.Md)
                .toAttrs(),
        ) {
            ComposeText("/")
        }
        Span(attrs = Modifier.toAttrs { attr("aria-current", "page") }) {
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
        // `Flex` + `flex-wrap: wrap` lets the `work / project / case-study.md`
        // trail break across rows on a phone instead of overflowing the shell
        // padding and pushing the slash separators into their own columns.
        Modifier
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
            .alignItems(AlignItems.Center)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(BREADCRUMB_FONT_SIZE)
            .margin(bottom = SiteDimensions.size.Xxl)
    }
}
