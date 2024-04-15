package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.kotlin.wrapper.highlightjs.compose.html.component.CodeBlock
import dev.tonholo.kotlin.wrapper.highlightjs.core.language.SupportedLanguages
import dev.tonholo.kotlin.wrapper.highlightjs.core.style.SupportedStyle
import dev.tonholo.portfolio.core.components.Scaffold
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.foundation.Modifier
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.sections.AppBar
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.px

@Page(AppRoutes.Articles.ROUTE)
@Composable
fun ArticlesPage() {
    val context = rememberPageContext()
    val colorMode = ColorMode.current

    Scaffold(
        topBar = {
            AppBar(
                onLocaleChange = { },
                onHomeClick = {
                    context.router.navigateTo(Route.Home)
                },
                onResumeClick = {
                    context.router.navigateTo(Route.Resume)
                },
            )
        }
    ) {
        Column {
            Text("Articles")
            CodeBlock(
                code = """
                    |package com.redspace.shubi
                    |
                    |import android.os.Bundle
                    |// several other imports
                    |import com.redspace.shubi.shared.Greeting
                    |// other imports
                    |
                    |@AndroidEntryPoint
                    |class MainActivity : AppCompatActivity() {
                    |    // some properties
                    |    override fun onCreate(savedInstanceState: Bundle?) {
                    |        // some configurations
                    |        super.onCreate(savedInstanceState)
                    |        setContent { Content() }
                    |        // other code
                    |    }
                    |
                    |    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
                    |    @Composable
                    |    private fun Content() {
                    |        // some compose stuff
                    |        Box(
                    |            modifier = Modifier
                    |                .fillMaxWidth()
                    |                .background(Color.Magenta),
                    |        ) { test ->
                    |            val longValue = 1234567890
                    |            Text(
                    |                // Here is the usage of the shared class!
                    |                // Plain, simple, kotlin code.
                    |                text = Greeting().greet(),
                    |                style = MaterialTheme.typography.headlineLarge,
                    |            )
                    |        }
                    |    }
                    |}
                    """.trimMargin(),
                isDarkMode = colorMode.isDark,
                supportedLanguage = SupportedLanguages.Kotlin,
                style = SupportedStyle.AndroidStudio,
                bottomContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Link(
                            path = "https://rafael.tonholo.dev",
                            modifier = Modifier.weight(1),
                        )
                        Text(
                            text = languageName,
                            modifier = Modifier.alignSelf(AlignSelf.FlexEnd)
                        )
                    }
                }
            )
            Box(modifier = Modifier.height(16.px))
            CodeBlock(
                code = """
                    |public final class Greeting {
                    |   public final String greet() {
                    |       return "Hello, World!";
                    |   }
                    |}
                """.trimMargin(),
                isDarkMode = colorMode.isDark,
                SupportedLanguages.Java,
            )
            Box(modifier = Modifier.height(16.px))
            CodeBlock(
                code = """
                    |n = int(input('Type a number, and its factorial will be printed: '))
                    |
                    |if n < 0:
                    |    raise ValueError('You must enter a non-negative integer')
                    |
                    |factorial = 1
                    |for i in range(2, n + 1):
                    |    factorial *= i
                    |
                    |print(factorial)
                """.trimMargin(),
                isDarkMode = colorMode.isDark,
                SupportedLanguages.Python,
            )
        }
    }
}
