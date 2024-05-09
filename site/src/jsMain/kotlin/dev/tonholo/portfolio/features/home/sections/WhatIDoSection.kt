package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.icons
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.HomeAboutSection

val WhatIDoStyle by ComponentStyle {
    base {
        Modifier
            .background(colorScheme.surface)
            .borderRadius(8.dp)
            .padding(vertical = 80.dp)
            .gap(40.dp)
    }
    cssRule("p") {
        Modifier.padding(horizontal = 120.dp)
    }
}

@Composable
fun WhatIDoSection(
    about: HomeAboutSection,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = WhatIDoStyle.toModifier() then modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.gap(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = about.title,
                style = Theme.typography.displaySmall,
            )
            Paragraph(
                text = about.description,
                style = Theme.typography.bodyLarge,
            )
        }
        Row(
            modifier = Modifier.gap(24.dp),
        ) {
            Image(
                src = Theme.icons.AndroidLogo(),
                alt = about.androidLogoContentDescription,
            )
            Image(
                src = Theme.icons.Kotlin(),
                alt = about.kotlinLogoContentDescription,
            )
            Image(
                src = Theme.icons.Compose(),
                alt = about.composeLogoContentDescription,
            )
        }
    }
}
