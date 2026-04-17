package dev.tonholo.portfolio.features.resume.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.ExperienceSection
import dev.tonholo.portfolio.resources.workExperience.Experience
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

val ExperienceSectionStyle = CssStyle {
    base {
        Modifier
            .backgroundColor(colorScheme.surfaceVariant)
            .color(colorScheme.onSurfaceVariant)
            .gap(24.dp)
            .borderRadius(8.dp)
            .padding(24.dp)
    }
}

val ExperienceItemTitleStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .gap(8.dp)
    }

    cssRule("> :first-child") {
        Modifier.flex(1)
    }
}

@Composable
fun ExperienceSection(
    section: ExperienceSection,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = ExperienceSectionStyle.toModifier() then modifier,
    ) {
        Text(
            text = section.title,
            style = Theme.typography.titleLarge,
        )
        section.experiences.forEach { experience ->
            ExperienceItem(
                experience = experience,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ExperienceItem(
    experience: Experience,
    modifier: Modifier = Modifier,
) {
    val present = LocalStrings.current.present
    val monthNames = LocalStrings.current.monthNames
    val period = remember(experience.starting, experience.ending) {
        buildString {
            append(experience.starting.formatToPeriod(monthNames))
            append(" - ")
            append(experience.ending.formatToPeriod(monthNames, default = present))
        }
    }
    Column(modifier = modifier) {
        Row(
            modifier = ExperienceItemTitleStyle.toModifier(),
        ) {
            Text(
                text = experience.position,
                style = Theme.typography.titleMedium,
            )
            Text(
                text = period,
                style = Theme.typography.titleMedium,
            )
        }
        Text(
            text = experience.company,
            style = Theme.typography.bodyMedium,
        )
    }
}

private fun LocalDate.formatToPeriod(monthNames: MonthNames): String {
    return format(
        LocalDate.Format {
            monthName(monthNames)
            char(' ')
            year()
        },
    )
}

private fun LocalDate?.formatToPeriod(monthNames: MonthNames, default: String): String =
    this?.formatToPeriod(monthNames) ?: default
