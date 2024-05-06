package dev.tonholo.portfolio.features.home.components

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.button.LinkButton
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.LineStyle

object ProjectCardVars {
    val MinWidth by StyleVariable<CSSLengthOrPercentageNumericValue>()
    val MinHeight by StyleVariable<CSSLengthOrPercentageNumericValue>()
    val MaxWidth by StyleVariable<CSSLengthOrPercentageNumericValue>()
    val MaxHeight by StyleVariable<CSSLengthOrPercentageNumericValue>()
}

val ProjectCardStyle by ComponentStyle {
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
    src: String,
    modifier: Modifier = Modifier,
    maxWidth: Dp = Dp.Unspecified,
    minWidth: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    minHeight: Dp = Dp.Unspecified,
) {
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
        LinkButton(
            path = src,
            color = Theme.colorScheme.primary,
            modifier = Modifier.alignSelf(AlignSelf.End),
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
