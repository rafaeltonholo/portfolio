package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.core.Page
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.home.HeroEditorial
import dev.tonholo.portfolio.components.organisms.home.HiringCta
import dev.tonholo.portfolio.components.organisms.home.NowStrip
import dev.tonholo.portfolio.components.organisms.home.SelectedWorkGrid
import dev.tonholo.portfolio.components.organisms.home.SnippetOfTheWeek
import dev.tonholo.portfolio.components.organisms.home.UsesGrid
import dev.tonholo.portfolio.extensions.openInNewTab
import dev.tonholo.portfolio.extensions.scrollToElementId
import dev.tonholo.portfolio.state.StaticContent

// Anchor IDs applied to each home section. Kept together so the hero CTAs
// and the scroll targets stay in sync if a section ever gets renamed.
private const val SELECTED_WORK_ID = "selected-work-grid"
private const val NOW_STRIP_ID = "now-strip"
private const val SNIPPET_ID = "snippet-of-the-week"
private const val USES_ID = "uses-grid"
private const val HIRING_CTA_ID = "hiring-cta"

// Resume PDF lives under `resources/public/docs/` and ships alongside the
// static bundle; the hero "download CV" CTA opens it in a new tab.
private const val RESUME_PDF_URL = "/docs/rafael-tonholo-resume.pdf"

/**
 * Home route "/" - renders the full editorial stack: hero, selected-work
 * grid, now strip, snippet of the week, uses grid, and the hiring CTA. Each
 * project card is a real `<a href>` anchor so browser navigation
 * affordances (middle-click, cmd-click, copy-link-address) work.
 */
@Page
@Composable
@Suppress("ModifierMissing")
fun HomePage() {
    PageLayout {
        HeroEditorial(
            onFeaturedWorkClick = { scrollToElementId(id = SELECTED_WORK_ID) },
            onDownloadCvClick = { openInNewTab(url = RESUME_PDF_URL) },
            onCurrentlyBuildingClick = { scrollToElementId(id = NOW_STRIP_ID) },
            onWritingClick = { scrollToElementId(id = SNIPPET_ID) },
            onHireClick = { scrollToElementId(id = HIRING_CTA_ID) },
        )
        SelectedWorkGrid(
            projects = StaticContent.PROJECTS,
            modifier = Modifier.id(SELECTED_WORK_ID),
        )
        NowStrip(
            modifier = Modifier.id(NOW_STRIP_ID),
        )
        SnippetOfTheWeek(
            modifier = Modifier.id(SNIPPET_ID),
        )
        UsesGrid(
            modifier = Modifier.id(USES_ID),
        )
        HiringCta(
            modifier = Modifier.id(HIRING_CTA_ID),
        )
    }
}
