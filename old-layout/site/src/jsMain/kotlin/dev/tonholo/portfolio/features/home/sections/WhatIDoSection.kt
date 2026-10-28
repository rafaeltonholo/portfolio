package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.cssRule
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.icons
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.HomeAboutSection
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.dom.Div

val WhatIDoStyle = CssStyle {
    base {
        Modifier
            .background(colorScheme.surface)
            .borderRadius(8.dp)
            .padding(vertical = 80.dp, horizontal = 20.dp)
            .gap(40.dp)
    }

    cssRule(Breakpoint.LG, "p") {
        Modifier.padding(horizontal = 100.dp)
    }
}

val WhatIDoLogosContentStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .gap(12.dp)
    }
    Breakpoint.LG {
        Modifier
            .flexDirection(FlexDirection.Row)
            .gap(24.dp)
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
        Div(
            attrs = WhatIDoLogosContentStyle.toAttrs(),
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
