package dev.tonholo.portfolio.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import org.jetbrains.compose.web.dom.ContentBuilder
import org.w3c.dom.svg.SVGSVGElement

@Stable
@Immutable
@Suppress("PropertyName")
sealed interface IconScheme {
    companion object {
        val Default: IconScheme = Light
    }

    val AndroidLogo: Icon.Asset
    val Compose: Icon.Asset
    val Github: Icon.Asset
    val LinkedIn: Icon.Asset
    val Kotlin: Icon.Asset
    val Twitter: Icon.Asset

    @Immutable
    data object Dark : IconScheme {
        override val AndroidLogo: Icon.Asset = Icon.Asset(
            src = "icons/android-logo-dark.svg",
        )
        override val Compose: Icon.Asset = Default.Compose
        override val Github: Icon.Asset = Icon.Asset(
            src = "icons/github-dark.svg",
        )
        override val LinkedIn: Icon.Asset = Icon.Asset(
            src = "icons/linkedin-dark.svg",
        )
        override val Kotlin: Icon.Asset = Icon.Asset(
            src = "icons/kotlin-dark.svg",
        )
        override val Twitter: Icon.Asset = Icon.Asset(
            src = "icons/twitter-dark.svg",
        )
    }

    @Immutable
    data object Light : IconScheme {
        override val AndroidLogo: Icon.Asset = Icon.Asset(
            src = "icons/android-logo-light.svg",
        )
        override val Compose: Icon.Asset = Icon.Asset(
            src = "icons/compose.svg",
        )
        override val Github: Icon.Asset = Icon.Asset(
            src = "icons/github-light.svg",
        )
        override val LinkedIn: Icon.Asset = Icon.Asset(
            src = "icons/linkedin-light.svg",
        )
        override val Kotlin: Icon.Asset = Icon.Asset(
            src = "icons/kotlin-light.svg",
        )
        override val Twitter: Icon.Asset = Icon.Asset(
            src = "icons/twitter-light.svg",
        )
    }
}

sealed interface Icon {
    data class Svg(val builder: ContentBuilder<SVGSVGElement>) : Icon
    data class Asset(val src: String) : Icon {
        operator fun invoke(): String = src
    }
}

val LocalIconScheme = staticCompositionLocalOf<IconScheme> { IconScheme.Light }
