package dev.tonholo.portfolio.pages.projects

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.AspectRatio
import dev.tonholo.portfolio.components.atoms.Placeholder
import dev.tonholo.portfolio.components.layouts.ProjectLayout
import dev.tonholo.portfolio.components.molecules.ProjectBreadcrumb
import dev.tonholo.portfolio.components.organisms.projects.OutcomesGrid
import dev.tonholo.portfolio.components.organisms.projects.ProjectDetailHero
import dev.tonholo.portfolio.components.organisms.projects.ProjectOutcome
import dev.tonholo.portfolio.components.organisms.projects.ProjectSection
import dev.tonholo.portfolio.components.organisms.projects.ProjectSectionParagraphStyle
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.StaticContent
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Em
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

private const val PROJECT_SLUG_PARAM = "slug"
private const val PINTEREST_SLUG = "pinterest"

// `.project-cover` in the handoff CSS: 4px radius, 64px bottom margin above
// the first case-study section. Aspect ratio comes from the Placeholder atom.
private const val COVER_RADIUS_REM = 0.25 // 4px
private const val COVER_MARGIN_BOTTOM_REM = 4.0 // 64px

// Pinterest-only section labels that do not live on the shared Strings surface.
// The generic / context and / outcomes labels come from [Strings.ProjectDetail].
private const val LABEL_PROBLEM = "/ problem"
private const val LABEL_APPROACH = "/ approach"
private const val LABEL_LEARNINGS = "/ learnings"

// Pinterest case-study copy - verbatim from handoff pages2.jsx lines 125-181,
// with em dashes replaced by regular dashes.
private const val PINTEREST_CONTEXT_TITLE =
    "Pinterest is 9 years of code, and it shows."
private const val PINTEREST_CONTEXT_P1 =
    "By the time I joined, the Android codebase had grown into thousands of " +
        "modules, dozens of teams, and the kind of build times that made developers " +
        "dread clean installs. A clean build averaged 30 minutes on a high-end " +
        "machine. An incremental build, 15."
private const val PINTEREST_CONTEXT_P2 =
    "That cost compounds. Every engineer on the team was losing real hours every " +
        "week, and the delivery cadence suffered. Meanwhile, the product team wanted " +
        "features delivered on-demand - via Google Play Dynamic Feature Delivery - " +
        "which added another layer of complexity to an already intricate graph."

private const val PINTEREST_PROBLEM_TITLE =
    "The module graph had become the org chart."
private const val PINTEREST_PROBLEM_P1 =
    "Every team owned their modules. Every module had subtly different conventions. " +
        "Dynamic features didn't know how to find their dependencies without elaborate " +
        "service-locator dances. Worse, new features couldn't be added without shuffling " +
        "modules that had nothing to do with them - which made the whole graph fragile."

private const val PINTEREST_APPROACH_TITLE = "Smaller pieces, clearer contracts."
private const val PINTEREST_APPROACH_P1 =
    "I kicked off a quarterly effort with all the platform stakeholders in the same " +
        "channel. The first job was boring on purpose: take an inventory. We mapped " +
        "every module, flagged the load-bearing ones, and sorted them into a few tidy categories."

// Approach paragraph 2 is split around an <em> span.
private const val PINTEREST_APPROACH_P2_BEFORE = "Then we built a "
private const val PINTEREST_APPROACH_P2_EM =
    "centralized dynamic-feature dependency manager"
private const val PINTEREST_APPROACH_P2_AFTER =
    " - a tiny runtime library that exposed stable interfaces for every feature " +
        "module to consume, and handled the install/load lifecycle on the dynamic side. " +
        "Teams stopped owning their own discovery logic. The graph stopped punishing newcomers."

private const val PINTEREST_OUTCOMES_TITLE = "What changed, with numbers."
private const val PINTEREST_OUTCOMES_STAT1_VALUE = "-73%"
private const val PINTEREST_OUTCOMES_STAT1_LABEL = "reduction in average clean-build time"
private const val PINTEREST_OUTCOMES_STAT2_VALUE = "-62%"
private const val PINTEREST_OUTCOMES_STAT2_LABEL =
    "incremental-build time, for high-turnover modules"
private const val PINTEREST_OUTCOMES_STAT3_VALUE = "0 \u2192 8" // "0 → 8"
private const val PINTEREST_OUTCOMES_STAT3_LABEL =
    "dynamic features shipped on the new manager"
private const val PINTEREST_OUTCOMES_P1 =
    "More important than the numbers: the dynamic-feature manager stopped being the " +
        "thing engineers dreaded touching. Features got merged on the first review. " +
        "The platform became a platform again."

private const val PINTEREST_LEARNINGS_TITLE = "A few things I took away."

// Learnings paragraphs are split: bold lead + rest of sentence.
private const val PINTEREST_LEARNINGS_P1_BOLD = "Boring is good."
private const val PINTEREST_LEARNINGS_P1_REST =
    " The version of the system that won wasn't the clever one. It was the one that " +
        "did the minimum possible, in the most obvious way, and left no room for teams to reinvent."
private const val PINTEREST_LEARNINGS_P2_BOLD = "Incremental is everything."
private const val PINTEREST_LEARNINGS_P2_REST =
    " We couldn't rip the graph apart and rebuild it; too much else depended on it. " +
        "Every change had to ship, on its own, without breaking the 400 other modules " +
        "downstream. That constraint shaped the whole design."
private const val PINTEREST_LEARNINGS_P3_BOLD = "Talk to the people who live in it."
private const val PINTEREST_LEARNINGS_P3_REST =
    " Some of the most useful feedback came from engineers who had worked in the " +
        "codebase for five years and had a long list of papercuts. They knew where the bodies were buried."

/**
 * Dynamic project route "/projects/{slug}" - looks up the requested project
 * in [StaticContent.PROJECTS] and renders a case-study shell. Pinterest gets
 * the full five-section editorial case study; all other projects fall back to
 * the single context + outcomes layout using [Project.description] as body copy.
 *
 * The sticky [ProjectLayout] footer wires back/next navigation using the
 * project's index in the seed list.
 */
@Page(routeOverride = "/projects/{slug}")
@Composable
@Suppress("ModifierMissing")
fun DynamicProjectPage() {
    val pageContext = rememberPageContext()
    val strings = LocalStrings.current.projectDetail
    val slug = pageContext.route.params[PROJECT_SLUG_PARAM].orEmpty()
    val index = StaticContent.PROJECTS.indexOfFirst { it.slug == slug }
    val project = StaticContent.PROJECTS.getOrNull(index = index)
    val nextProject = StaticContent.PROJECTS.getOrNull(index = index + 1)

    if (project == null) {
        ProjectLayout(
            backRoute = Routes.PROJECTS,
            nextProjectName = null,
            nextRoute = null,
        ) {
            Text(strings.notFoundCopy)
        }
        return
    }

    ProjectLayout(
        backRoute = Routes.PROJECTS,
        nextProjectName = nextProject?.name,
        nextRoute = nextProject?.let { Routes.project(slug = it.slug) },
    ) {
        ProjectBreadcrumb(projectName = project.name)
        ProjectDetailHero(project = project)
        Placeholder(
            aspectRatio = AspectRatio.Hero16x9,
            label = strings.coverPlaceholderLabel,
            modifier = Modifier
                .borderRadius(COVER_RADIUS_REM.cssRem)
                .margin(bottom = COVER_MARGIN_BOTTOM_REM.cssRem),
        )
        if (slug == PINTEREST_SLUG) {
            PinterestCaseStudy(strings = strings)
        } else {
            DefaultProjectSections(project = project, strings = strings)
        }
    }
}

/**
 * Full five-section editorial case study for the Pinterest project. Mirrors
 * the `ProjectPage` function in the handoff `pages2.jsx` (lines 125-181).
 *
 * Sections: context, problem, approach, outcomes (with [OutcomesGrid]), learnings.
 */
@Composable
private fun PinterestCaseStudy(strings: Strings.ProjectDetail) {
    ProjectSection(label = strings.contextLabel, title = PINTEREST_CONTEXT_TITLE) {
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            Text(PINTEREST_CONTEXT_P1)
        }
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            Text(PINTEREST_CONTEXT_P2)
        }
    }
    ProjectSection(label = LABEL_PROBLEM, title = PINTEREST_PROBLEM_TITLE) {
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            Text(PINTEREST_PROBLEM_P1)
        }
    }
    ProjectSection(label = LABEL_APPROACH, title = PINTEREST_APPROACH_TITLE) {
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            Text(PINTEREST_APPROACH_P1)
        }
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            Text(PINTEREST_APPROACH_P2_BEFORE)
            Em { Text(PINTEREST_APPROACH_P2_EM) }
            Text(PINTEREST_APPROACH_P2_AFTER)
        }
    }
    ProjectSection(label = strings.outcomesLabel, title = PINTEREST_OUTCOMES_TITLE) {
        OutcomesGrid(
            outcomes = listOf(
                ProjectOutcome(
                    value = PINTEREST_OUTCOMES_STAT1_VALUE,
                    label = PINTEREST_OUTCOMES_STAT1_LABEL,
                ),
                ProjectOutcome(
                    value = PINTEREST_OUTCOMES_STAT2_VALUE,
                    label = PINTEREST_OUTCOMES_STAT2_LABEL,
                ),
                ProjectOutcome(
                    value = PINTEREST_OUTCOMES_STAT3_VALUE,
                    label = PINTEREST_OUTCOMES_STAT3_LABEL,
                ),
            ),
        )
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            Text(PINTEREST_OUTCOMES_P1)
        }
    }
    ProjectSection(label = LABEL_LEARNINGS, title = PINTEREST_LEARNINGS_TITLE) {
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            B { Text(PINTEREST_LEARNINGS_P1_BOLD) }
            Text(PINTEREST_LEARNINGS_P1_REST)
        }
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            B { Text(PINTEREST_LEARNINGS_P2_BOLD) }
            Text(PINTEREST_LEARNINGS_P2_REST)
        }
        P(attrs = ProjectSectionParagraphStyle.toModifier().toAttrs()) {
            B { Text(PINTEREST_LEARNINGS_P3_BOLD) }
            Text(PINTEREST_LEARNINGS_P3_REST)
        }
    }
}

/**
 * Fallback case-study layout for non-Pinterest projects (v1): a single context
 * section with [Project.description] as body copy, followed by an outcomes grid
 * built from [Project.stats].
 */
@Composable
private fun DefaultProjectSections(project: Project, strings: Strings.ProjectDetail) {
    ProjectSection(label = strings.contextLabel, title = project.name) {
        Text(project.description)
    }
    ProjectSection(label = strings.outcomesLabel, title = strings.outcomesTitle) {
        OutcomesGrid(
            outcomes = project.stats.map { stat ->
                ProjectOutcome(value = stat.value, label = stat.label)
            },
        )
    }
}
