package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val ExperiencesStyle by ComponentStyle {
    base {
        Modifier.width(100.percent)
    }

    Breakpoint.MD {
        Modifier.margin { top(0.px) }
            .flex(value = 1)
    }
}

@Composable
fun Experiences(
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val experiencesModifier = ExperiencesStyle.toModifier() then modifier

    Column(modifier = experiencesModifier) {
        WorkHistoryList(
            title = "",
            experiences = listOf(),
        )
    }
}


