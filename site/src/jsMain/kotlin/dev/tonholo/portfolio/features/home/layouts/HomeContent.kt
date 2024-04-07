package dev.tonholo.portfolio.features.home.layouts

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import dev.tonholo.portfolio.features.home.components.LanguageChanger
import dev.tonholo.portfolio.features.home.sections.AppBar
import dev.tonholo.portfolio.features.home.sections.Summary

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
) {

    Column(modifier = modifier) {
        LanguageChanger(onLocaleChange = onLocaleChange)
        AppBar()
        Row {
            Column {
                Summary()
            }
        }
    }
}
