package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Section

/**
 * Outer `<section>` chrome for hero-shaped sections (page headers, article and
 * project shells, the home hero, the 404 body). Mobile-first: `base` IS the
 * mobile tier with [SiteDimensions.padding.heroTop] on top (extra breathing
 * room above the fold), [SiteDimensions.padding.section] on bottom, and
 * [SiteDimensions.size.Lg] horizontal gutter. From [Breakpoint.MD] the gutter
 * ramps up to [SiteDimensions.size.Xxl]; vertical padding scales through the
 * underlying [dev.tonholo.portfolio.theme.layout.SiteSizeVar.Xxl] CSS variable
 * so no per-organism overrides are needed.
 */
val HeroSectionStyle = CssStyle {
    base {
        Modifier.padding(
            top = SiteDimensions.padding.heroTop,
            bottom = SiteDimensions.padding.section,
            leftRight = SiteDimensions.size.Lg,
        )
    }
    Breakpoint.MD {
        Modifier.padding(
            top = SiteDimensions.padding.heroTop,
            bottom = SiteDimensions.padding.section,
            leftRight = SiteDimensions.size.Xxl,
        )
    }
}

/**
 * Hero-shaped section layout primitive. Wraps [content] in a semantic
 * `<section id="...">` with [HeroSectionStyle] padding (heroTop top, section
 * bottom, Lg→Xxl gutter) and an inner [SectionContentStyle] cap so content
 * stays under [SiteDimensions.layout.contentMaxWidth].
 *
 * Use this for page headers (About, Articles, Resume, Projects), the home
 * hero, the 404 body, and the article / project detail shells. Inner content
 * is rendered as a flexible `<div>` so consumers can compose their own grid,
 * column, or single-element layout without an opinionated row/column wrapper.
 *
 * For default-rhythm body sections (selected work grid, articles list, about
 * tabs, resume body), use [SectionContainer] instead — it shares the same
 * gutter and content cap but uses [SiteDimensions.padding.section] on top so
 * adjacent body sections stay in rhythm with each other.
 *
 * @param id stable HTML id used for in-page anchors.
 * @param modifier applied to the outer `<section>` after the base style.
 * @param contentModifier applied to the inner content `<div>` after the base
 *   style. Useful for adding a max-width override or a layout grid class.
 * @param content composable lambda rendered inside the constrained inner div.
 */
@Composable
fun HeroSection(
    id: String,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Section(
        attrs = HeroSectionStyle.toModifier()
            .id(id)
            .then(modifier)
            .toAttrs(),
    ) {
        Div(
            attrs = SectionContentStyle.toModifier()
                .then(contentModifier)
                .toAttrs(),
        ) {
            content()
        }
    }
}
