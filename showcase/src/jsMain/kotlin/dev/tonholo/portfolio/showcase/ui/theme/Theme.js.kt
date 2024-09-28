package dev.tonholo.portfolio.showcase.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams

@Composable
actual fun isDarkTheme(): Boolean {
    val queryString = URLSearchParams(window.location.search)
    return if (queryString.has("dark")) {
        queryString.get("dark") == "true"
    } else {
        isSystemInDarkTheme()
    }
}
