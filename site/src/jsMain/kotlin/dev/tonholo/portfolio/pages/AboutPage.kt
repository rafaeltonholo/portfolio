package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.about.AboutHeader
import dev.tonholo.portfolio.components.organisms.about.AboutTabs
import dev.tonholo.portfolio.theme.layout.PageSectionShellStyle
import org.jetbrains.compose.web.dom.Div

/**
 * About route "/about" - renders the editorial About header on top of the
 * tabbed biography, approach, and beyond-work content block.
 *
 * The `routeOverride` trims Kobweb's default `AboutPage` -> `/about-page`
 * path convention down to `/about`, matching the route constant used
 * throughout the chrome navigation and the sitemap metadata.
 */
@Page(routeOverride = "/about")
@Composable
@Suppress("ModifierMissing")
fun AboutPage() {
    PageLayout {
        Div(attrs = PageSectionShellStyle.toModifier().toAttrs()) {
            AboutHeader()
            AboutTabs()
        }
    }
}
