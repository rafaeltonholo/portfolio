package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import dev.tonholo.portfolio.core.components.Scaffold
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.sections.AppBar

@Page(AppRoutes.Articles.ROUTE)
@Composable
fun ArticlesPage() {
    val context = rememberPageContext()
    Scaffold(
        topBar = {
            AppBar(
                onLocaleChange = {  },
                onHomeClick = {
                    context.router.navigateTo(Route.Home)
                },
                onResumeClick = {
                    context.router.navigateTo(Route.Resume)
                },
            )
        }
    ) {
        Text("Articles")
    }
}
