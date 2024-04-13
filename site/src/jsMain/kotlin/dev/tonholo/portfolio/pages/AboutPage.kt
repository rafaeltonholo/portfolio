package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.router.AppRoutes

@Page(AppRoutes.About.ROUTE)
@Composable
fun AboutPage() {
    Text("About")
}
