package dev.tonholo.portfolio.features.home.layouts

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import dev.tonholo.portfolio.features.home.components.LanguageChanger
import dev.tonholo.portfolio.locale.Locale
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
) {

    Column(modifier = modifier) {
        LanguageChanger(onLocaleChange = onLocaleChange)
        // TODO: Replace the following with your own content
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Div {
                    Text("THIS PAGE INTENTIONALLY LEFT BLANK")
                }
                val strings = LocalStrings.current
                Div {
                    Text(strings.simple)
                }
                Div {
                    Text(Locale.current)
                }
            }
        }
    }
}
