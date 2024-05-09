package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.navigation.Link
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.ui.theme.Theme

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Link(
        path = Route.Home,
        modifier = modifier
            .textDecorationLine(TextDecorationLine.None),
    ) {
        Text(
            text = "Rafael Tonholo",
            style = Theme.typography.titleSmall
                .copy(
                    fontWeight = FontWeight.Medium,
                    color = Theme.colorScheme.onSurface,
                ),
        )
    }
}
