package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.init.InitKobweb
import com.varabyte.kobweb.core.init.InitKobwebContext
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.chrome.NotFoundContent

/**
 * Explicit `/404` route whose export path produces `404.html` in the static
 * bundle. GitHub Pages (and most static hosts) serve the sibling
 * `404.html` for any unknown URL, so direct hits to a stale article or
 * project slug land on the IDE-themed recovery screen instead of the
 * default browser 404.
 *
 * The `NotFoundPage` is also wired as the SPA router's error page via
 * [initErrorPage] so in-app navigation to an unregistered route renders the
 * same content without a round trip.
 */
@Page(routeOverride = "/404")
@Composable
@Suppress("ModifierMissing")
fun NotFoundPage() {
    PageLayout {
        NotFoundContent()
    }
}

/**
 * Replaces Kobweb's default `DefaultErrorPage` ("Page Not Found" on a blank
 * page) with the site-themed [NotFoundContent] wrapped in the shared
 * [PageLayout] chrome. The router invokes this whenever a client-side
 * navigation targets a path that no registered `@Page` matches.
 */
@InitKobweb
fun initErrorPage(ctx: InitKobwebContext) {
    ctx.router.setErrorPage {
        PageLayout {
            NotFoundContent()
        }
    }
}
