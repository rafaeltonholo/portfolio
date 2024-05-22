package dev.tonholo.portfolio.features.resume.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.chip.Chip
import dev.tonholo.portfolio.core.components.chip.ChipDefaults
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.SkillSection
import org.jetbrains.compose.web.css.FlexWrap

val SkillsSectionStyle = CssStyle {
    base {
        Modifier.gap(24.dp)
    }
}

val SkillsTagsStyle = CssStyle {
    base {
        Modifier
            .flexWrap(FlexWrap.Wrap)
            .gap(24.dp)
    }
}

@Composable
fun SkillsSection(
    section: SkillSection,
    modifier: Modifier = Modifier,
) {
    Column(modifier = SkillsSectionStyle.toModifier() then modifier) {
        Text(
            text = section.title,
            style = Theme.typography.titleLarge,
        )
        Row(
            modifier = SkillsTagsStyle.toModifier(),
        ) {
            section.skills.forEach { skill ->
                Chip(
                    selected = false,
                    colors = ChipDefaults.colors(
                        contentColor = Theme.colorScheme.onBackgroundVariant,
                    ),
                ) {
                    Text(text = skill.name)
                }
            }
        }
    }
}
