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
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.AboutSection

val WhatIDoStyle by ComponentStyle {
    base {
        Modifier
            .background(colorScheme.surface)
            .borderRadius(8.dp)
            .padding(vertical = 80.dp)
            .gap(40.dp)
    }
}

@Composable
fun WhatIDoSection(
    about: AboutSection,
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
            Text(
                text = about.description,
                style = Theme.typography.bodyLarge,
            )
        }
        Row(
            modifier = Modifier.gap(24.dp),
        ) {
            // TODO: add content description.
            Image(
                src = Theme.icons.AndroidLogo(),
            )
            Image(
                src = Theme.icons.Kotlin(),
            )
            Image(
                src = Theme.icons.Compose(),
            )
        }
    }
}
