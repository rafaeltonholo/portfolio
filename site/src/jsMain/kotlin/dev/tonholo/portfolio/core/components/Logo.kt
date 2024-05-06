package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Text(
        text = "Rafael Tonholo",
        style = Theme.typography.titleSmall
            .copy(
                fontWeight = FontWeight.Medium,
                color = Theme.colorScheme.onSurface,
            ),
        modifier = modifier,
    )
}
