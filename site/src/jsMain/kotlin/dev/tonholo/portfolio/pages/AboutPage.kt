package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.about.AboutHeader
import dev.tonholo.portfolio.components.organisms.about.AboutTabs

/**
 * About route "/about" - renders the editorial About header on top of the
 * tabbed biography, approach, and beyond-work content block.
 */
@Page
@Composable
@Suppress("ModifierMissing")
fun AboutPage() {
    PageLayout {
        AboutHeader()
        AboutTabs()
    }
}
