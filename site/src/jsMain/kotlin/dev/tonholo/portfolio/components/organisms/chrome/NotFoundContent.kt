package dev.tonholo.portfolio.components.organisms.chrome

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.components.atoms.VisuallyHiddenStyle
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.ShellStyle
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import kotlinx.browser.window
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val SHELL_PADDING_TOP_REM = 5.0 // 80px
private const val SHELL_PADDING_BOTTOM_REM = 5.0 // 80px
private const val ERROR_META_BOTTOM_REM = 1.5 // 24px
private const val TITLE_MARGIN_BOTTOM_REM = 1.0 // 16px
private const val LEAD_FONT_SIZE_REM = 1.25 // 20px
private const val LEAD_LINE_HEIGHT = 1.55
private const val LEAD_MAX_WIDTH_CH = 60.0
private const val LEAD_MARGIN_BOTTOM_REM = 2.5 // 40px
private const val SHELL_COMMAND_FONT_SIZE_REM = 0.875 // 14px
private const val SHELL_COMMAND_GAP_REM = 0.5 // 8px
private const val SHELL_COMMAND_MARGIN_BOTTOM_REM = 1.5 // 24px
private const val RECOVERY_LABEL_MARGIN_BOTTOM_REM = 0.75 // 12px
private const val RECOVERY_LIST_GAP_REM = 0.5 // 8px
private const val RECOVERY_LINK_LETTER_SPACING_EM = -0.005

private const val META_LABEL = "// error"
private const val RECOVERY_LABEL = "// try one of"
private const val PROMPT = "$"

private data class RecoveryTarget(val label: String, val route: String)

/**
 * Shared 404 body used by the explicit `/404` page export and by the client-
 * side `setErrorPage` registration.
 *
 * Renders with the site's ambient [PageLayout][dev.tonholo.portfolio.components.layouts.PageLayout]
 * chrome (nav + footer) already wrapped by the caller so the user keeps their
 * bearings, and leans into the IDE voice: a shell-style command ghost above
 * the title and a muted list of recovery routes that read like terminal
 * output. Every recovery link is a real `<a href>` so middle-click / cmd-click
 * behave naturally.
 */
@Suppress("ModifierMissing")
@Composable
fun NotFoundContent() {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.notFound

    val path = window.location.pathname + window.location.search
    val recoveryTargets = listOf(
        RecoveryTarget(label = strings.recoveryHome, route = Routes.HOME),
        RecoveryTarget(label = strings.recoveryProjects, route = Routes.PROJECTS),
        RecoveryTarget(label = strings.recoveryArticles, route = Routes.ARTICLES),
        RecoveryTarget(label = strings.recoveryResume, route = Routes.RESUME),
    )

    Div(
        attrs = ShellStyle
            .toModifier()
            .padding(
                top = SHELL_PADDING_TOP_REM.cssRem,
                bottom = SHELL_PADDING_BOTTOM_REM.cssRem,
            )
            .toAttrs(),
    ) {
        MetaLabel(
            text = META_LABEL,
            variant = MetaLabelVariant.Accent,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = ERROR_META_BOTTOM_REM.cssRem),
        )
        H1(
            attrs = NotFoundTitleStyle
                .toModifier()
                .apply(Typography.h2)
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(strings.title)
        }
        P(
            attrs = NotFoundLeadStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(strings.lead)
        }
        Div(
            attrs = NotFoundShellLineStyle
                .toModifier()
                .color(tokens.muted.muted)
                .toAttrs(),
        ) {
            Span(
                attrs = Modifier
                    .color(tokens.accent.accent)
                    .fontWeight(FontWeight.SemiBold)
                    .toAttrs(),
            ) {
                ComposeText(PROMPT)
            }
            Span(attrs = Modifier.toAttrs()) {
                ComposeText(strings.shellCommandPrefix + path)
            }
        }
        // Visually-hidden section heading paired with the mono "// try one
        // of" label. Gives the recovery list an h2 under the page h1 so the
        // document outline never skips to the footer h3.
        H2(attrs = VisuallyHiddenStyle.toModifier().toAttrs()) {
            ComposeText("Recovery routes")
        }
        MetaLabel(
            text = RECOVERY_LABEL,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = RECOVERY_LABEL_MARGIN_BOTTOM_REM.cssRem),
        )
        Ul(attrs = NotFoundRecoveryListStyle.toModifier().toAttrs()) {
            recoveryTargets.forEach { target ->
                Li(attrs = Modifier.toAttrs()) {
                    Link(
                        path = target.route,
                        modifier = NotFoundRecoveryLinkStyle
                            .toModifier()
                            .color(tokens.accent.accent),
                        variant = UndecoratedLinkVariant then UncoloredLinkVariant,
                    ) {
                        ComposeText(target.label)
                    }
                }
            }
        }
    }
}

/**
 * Base `CssStyle` for the 404 heading. Pulled into the page so it sits in the
 * same layer as the rest of the chrome.
 */
val NotFoundTitleStyle = CssStyle {
    base {
        Modifier
            .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM_REM.cssRem)
            .letterSpacing(RECOVERY_LINK_LETTER_SPACING_EM.em)
    }
}

/**
 * Base `CssStyle` for the 404 lede paragraph. Serif body copy capped at 60ch
 * so the recovery narrative reads like editorial copy rather than a dump.
 */
val NotFoundLeadStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.BODY)
            .fontSize(LEAD_FONT_SIZE_REM.cssRem)
            .lineHeight(LEAD_LINE_HEIGHT)
            .maxWidth(LEAD_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
            .margin(top = 0.cssRem, bottom = LEAD_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the IDE shell-style command line (`$ cat /bad-path`).
 */
val NotFoundShellLineStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .gap(SHELL_COMMAND_GAP_REM.cssRem)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(SHELL_COMMAND_FONT_SIZE_REM.cssRem)
            .margin(bottom = SHELL_COMMAND_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the recovery routes list. Unordered list with markers
 * stripped so each row reads as a mono directory entry.
 */
val NotFoundRecoveryListStyle = CssStyle {
    base {
        Modifier
            .listStyle(type = ListStyleType.None)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(RECOVERY_LIST_GAP_REM.cssRem)
            .padding(left = 0.cssRem)
            .margin(topBottom = 0.cssRem)
    }
}

/**
 * Base `CssStyle` for each recovery link inside [NotFoundRecoveryListStyle].
 * Mono 14px so they sit in the same tonal register as the shell command
 * above; color is applied inline so the accent token resolves per theme.
 */
val NotFoundRecoveryLinkStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(SHELL_COMMAND_FONT_SIZE_REM.cssRem)
            .textDecorationLine(TextDecorationLine.None)
    }
}
