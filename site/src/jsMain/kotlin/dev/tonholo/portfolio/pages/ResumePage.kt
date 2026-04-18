package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.resume.ExperienceTimeline
import dev.tonholo.portfolio.components.organisms.resume.ResumeHeader
import dev.tonholo.portfolio.components.organisms.resume.SkillsGrid
import dev.tonholo.portfolio.state.StaticContent
import kotlinx.browser.window

// Resume PDF artifact lives under the static `public/docs` tree so it is
// served verbatim from the export. The download CTA opens it in a new tab.
private const val RESUME_PDF_PATH = "/docs/rafael-tonholo-resume.pdf"
private const val RESUME_PDF_TARGET = "_blank"

/**
 * Resume route "/resume" - renders the editorial resume header with a PDF
 * download CTA, the experience timeline, and the stacked skills + education
 * grid.
 *
 * The `routeOverride` collapses Kobweb's default `ResumePage` ->
 * `/resume-page` path to the expected `/resume` slug.
 */
@Page(routeOverride = "/resume")
@Composable
@Suppress("ModifierMissing")
fun ResumePage() {
    PageLayout {
        ResumeHeader(
            onDownloadPdf = {
                window.open(url = RESUME_PDF_PATH, target = RESUME_PDF_TARGET)
            },
        )
        ExperienceTimeline(experiences = StaticContent.EXPERIENCE)
        SkillsGrid(
            primaryStack = StaticContent.PRIMARY_STACK,
            alsoComfortable = StaticContent.ALSO_COMFORTABLE,
            education = StaticContent.EDUCATION,
        )
    }
}
