package dev.tonholo.portfolio.features.resume.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.Education
import dev.tonholo.portfolio.resources.pages.EducationSection

val EducationSectionStyle = CssStyle {
    base {
        Modifier.gap(24.dp)
    }
}

val EducationItemTitleStyle = CssStyle {
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
fun EducationSection(
    section: EducationSection,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = EducationSectionStyle.toModifier() then modifier,
    ) {
        Text(
            text = section.title,
            style = Theme.typography.titleLarge,
        )
        section.educations.forEach { education ->
            EducationItem(
                education = education,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun EducationItem(
    education: Education,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = EducationItemTitleStyle.toModifier(),
        ) {
            Text(
                text = education.title,
                style = Theme.typography.titleMedium,
            )
            Text(
                text = education.date.year.toString(),
                style = Theme.typography.titleMedium,
            )
        }
        Text(
            text = education.school,
            style = Theme.typography.bodyMedium,
        )
    }
}
