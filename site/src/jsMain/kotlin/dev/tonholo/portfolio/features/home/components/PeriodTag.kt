package dev.tonholo.portfolio.features.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.TextTag
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import kotlinx.datetime.LocalDate

val PeriodTagStyle by ComponentStyle {
    base {
        Modifier
            .border {
                color(color = colorScheme.outlineVariant)
            }
            .color(colorScheme.outlineVariant)
    }
}

@Composable
fun PeriodTag(
    starting: LocalDate,
    ending: LocalDate?,
    modifier: Modifier = Modifier,
) {
    val presentColor = Theme.colorScheme.primary
    val isPresent = remember(ending) {
        ending == null
    }
    val periodTagModifier = PeriodTagStyle.toModifier() then modifier
        .thenIf(condition = isPresent) {
            Modifier.border {
                color(presentColor)
            }
        }
    val strings = LocalStrings.current
    val endingText = ending?.year ?: strings.screens.home.historySection.work.presentTag
    TextTag(
        text = "${starting.year} - $endingText",
        modifier = periodTagModifier,
        color = if (isPresent) presentColor else null,
    )
}
