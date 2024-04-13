package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.router.AppRoutes

@Page(AppRoutes.Resume.ROUTE)
@Composable
fun ResumePage() {
    Text("Resume")
}
