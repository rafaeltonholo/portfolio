package dev.tonholo.portfolio.features.home.components

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.components.button.LinkButton
import dev.tonholo.portfolio.core.components.button.TextButton
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.dp
import kotlinx.browser.window
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.keywords.auto

object ProjectCardVars {
    val MinWidth by StyleVariable<CSSLengthOrPercentageNumericValue>()
    val MinHeight by StyleVariable<CSSLengthOrPercentageNumericValue>()
    val MaxWidth by StyleVariable<CSSLengthOrPercentageNumericValue>()
    val MaxHeight by StyleVariable<CSSLengthOrPercentageNumericValue>()
}

val ProjectCardStyle = CssStyle {
    base {
        Modifier
            .padding(24.dp)
            .border {
                width(1.dp)
                style(LineStyle.Solid)
                color(colorScheme.outline)
            }
            .borderRadius(8.dp)
            .gap(16.dp)
            .backgroundColor(colorScheme.surfaceVariant)
            .minWidth(ProjectCardVars.MinWidth.value())
            .minHeight(ProjectCardVars.MinHeight.value())
            .maxWidth(ProjectCardVars.MaxWidth.value())
            .maxHeight(ProjectCardVars.MinHeight.value())
    }
}

@Composable
fun ProjectCard(
    name: String,
    description: String,
    src: String?,
    playStoreSrc: String?,
    modifier: Modifier = Modifier,
    maxWidth: Dp = Dp.Unspecified,
    minWidth: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    minHeight: Dp = Dp.Unspecified,
) {
    val analytics = LocalAnalyticsManager.current
    val strings = LocalStrings.current
    Column(
        modifier = ProjectCardStyle.toModifier() then modifier
            .thenIf(minWidth != Dp.Unspecified) {
                Modifier.setVariable(ProjectCardVars.MinWidth, minWidth)
            }
            .thenIf(minHeight != Dp.Unspecified) {
                Modifier.setVariable(ProjectCardVars.MinHeight, minHeight)
            }
            .thenIf(maxWidth != Dp.Unspecified) {
                Modifier.setVariable(ProjectCardVars.MaxWidth, maxWidth)
            }
            .thenIf(maxHeight != Dp.Unspecified) {
                Modifier.setVariable(ProjectCardVars.MaxHeight, maxHeight)
            },
    ) {
        Text(
            text = name,
            style = Theme.typography.headlineSmall,
        )
        Paragraph(
            text = description,
            style = Theme.typography.bodyLarge,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.End),
            modifier = Modifier
                .fillMaxWidth()
                .margin {
                    top(auto.unsafeCast<CSSLengthNumericValue>())
                },
        ) {
            playStoreSrc?.let { src ->
                TextButton(
                    onClick = { window.open(url = src) },
                ) {
                    Text(
                        text = strings.viewInPlayStore,
                        style = Theme.typography.labelLarge.copy(
                            color = Theme.colorScheme.primary,
                        ),
                    )
                }
            }
            src?.let { src ->
                LinkButton(
                    path = src,
                    color = Theme.colorScheme.primary,
                    modifier = Modifier
                        .onClick {
                            analytics.track(
                                AnalyticEvent.ViewProject(
                                    projectName = name,
                                    projectUrl = src,
                                )
                            )
                        },
                ) {
                    Text(
                        text = strings.viewProject,
                        style = Theme.typography.labelLarge.copy(
                            color = Theme.colorScheme.primary,
                        ),
                    )
                }
            }
        }
    }
}
