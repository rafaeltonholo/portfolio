package dev.tonholo.portfolio.components.organisms.resume

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Button
import dev.tonholo.portfolio.components.atoms.ButtonVariant
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.components.layouts.HeroSection
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text as ComposeText

// Internal grid layout. Handoff `.resume-head`:
//   display: grid; grid-template-columns: 1fr auto; gap: 32px; align-items: end;
// Below [Breakpoint.LG] the grid collapses to a single column. The 64px
// bottom margin from the handoff is now owned by the outer section's
// padding.section bottom token, so the header organism no longer applies
// it.
private val HEAD_GRID_GAP = 32.dp
private const val HEAD_GRID_COPY_FR = 1

// Copy-column spacing.
//   `.meta` + inline `style={{marginBottom:16}}` on the mono header.
//   `.resume-head p` pushes 16px down from the h1 via `margin-top: 16px`.
private val META_MARGIN_BOTTOM = 16.dp
private val PARAGRAPH_MARGIN_TOP = 16.dp

// Handoff `.resume-head p` serif paragraph cap.
private const val PARAGRAPH_MAX_WIDTH_CH = 50.0

// Mono eyebrow is a technical literal (file name) and stays untranslated.
private const val META_TEXT = "// cv.json"
private const val DOWNLOAD_ICON = "arrow-down"

/**
 * Top section of the Resume page. Ports `.resume-head` from `pages2.jsx`
 * lines 6-13 and `styles.css` lines 1107-1127: a two-column grid holding the
 * editorial copy block on the left and the download CTA on the right.
 *
 * The grid collapses to a single column below [Breakpoint.LG] (900px) to match
 * the handoff `@media (max-width: 900px)` rule on `.resume-head`.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param onDownloadPdf invoked when the accent CTA is pressed. Defaults to a
 *   no-op so the organism can be previewed without wiring.
 */
@Composable
fun ResumeHeader(modifier: Modifier = Modifier, onDownloadPdf: () -> Unit = {}) {
    val strings = LocalStrings.current.resume

    HeroSection(id = "resume-header", modifier = modifier) {
        Div(attrs = ResumeHeaderGridStyle.toModifier().toAttrs()) {
            ResumeHeaderCopyColumn()
            Button(
                text = strings.downloadCta,
                variant = ButtonVariant.Accent,
                onClick = onDownloadPdf,
                trailing = { PhosphorIcon(name = DOWNLOAD_ICON) },
            )
        }
    }
}

/**
 * Left column: accent mono meta label, display title (two lines separated by
 * `<br>`), and a serif body paragraph capped at 50ch.
 */
@Composable
private fun ResumeHeaderCopyColumn() {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.resume

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = META_TEXT,
            variant = MetaLabelVariant.Accent,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM),
        )
        H1(
            attrs = Modifier
                .apply(Typography.h1)
                .color(tokens.ink.ink)
                .margin(topBottom = 0.cssRem)
                .toAttrs(),
        ) {
            ComposeText(strings.titleLine1)
            Br()
            ComposeText(strings.titleLine2)
        }
        P(
            attrs = ResumeHeaderParagraphStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(strings.body)
        }
    }
}

/**
 * Internal two-column grid. Single column at the mobile-first base; switches
 * to `1fr auto` with `align-items: end` from [Breakpoint.LG] so the
 * Download-PDF button sits flush with the paragraph baseline.
 */
val ResumeHeaderGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { size(HEAD_GRID_COPY_FR.fr) }
            .gap(HEAD_GRID_GAP)
            .alignItems(AlignItems.Start)
    }
    Breakpoint.LG {
        Modifier
            .gridTemplateColumns {
                size(HEAD_GRID_COPY_FR.fr)
                size(auto)
            }
            .alignItems(AlignItems.End)
    }
}

/**
 * Base `CssStyle` for `.resume-head p`. Ports the serif paragraph under the
 * display title: [Typography.serifLead] voice, 50ch max-width, 16px top margin
 * above the h1, zero bottom margin. Colour is applied inline from `ink-2`.
 */
val ResumeHeaderParagraphStyle = CssStyle {
    base {
        Modifier
            .apply(Typography.serifLead)
            .maxWidth(PARAGRAPH_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
            .margin(top = PARAGRAPH_MARGIN_TOP, bottom = 0.cssRem)
    }
}
