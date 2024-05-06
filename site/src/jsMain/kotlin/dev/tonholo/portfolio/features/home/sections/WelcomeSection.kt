package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import dev.tonholo.portfolio.core.components.SocialMediaRow
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.WelcomeSection

@Composable
fun WelcomeSection(
    welcomeSection: WelcomeSection,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = welcomeSection.title,
                    style = Theme.typography.displayMedium,
                )
                Paragraph(
                    text = welcomeSection.description,
                    style = Theme.typography.titleLarge,
                    modifier = Modifier.margin { top(16.dp) },
                )
            }
            SocialMediaRow()
        }
        Image(
            src = "images/welcome-pic.png",
            modifier = Modifier
                .margin { left(24.dp) }
                .size(552.dp, 414.dp)
                .borderRadius {
                    topLeft(200.dp)
                    bottomLeft(200.dp)
                }
        )
    }
}


///* Image */
//
//width: 552px;
//height: 414px;
//
//background: url(.jpg);
//border-radius: 200px 0px 0px 200px;
//
///* Inside auto layout */
//flex: none;
//order: 1;
//flex-grow: 1;
