package dev.tonholo.portfolio.showcase.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ShowcaseTheme(
    darkTheme: Boolean = isDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

@Composable
expect fun isDarkTheme(): Boolean
