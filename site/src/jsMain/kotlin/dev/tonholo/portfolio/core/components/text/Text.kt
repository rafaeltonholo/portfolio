package dev.tonholo.portfolio.core.components.text

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariantBase
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.H5
import org.jetbrains.compose.web.dom.H6
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLSpanElement

sealed interface TextKind : ComponentKind

val BaseTextStyle = CssStyle<TextKind> {
    base {
        typography.bodyLarge.toModifier()
    }
}

val DisplayLargeVariant = BaseTextStyle.addVariantBase {
    typography.displayLarge.toModifier()
}
val DisplayMediumVariant = BaseTextStyle.addVariantBase {
    typography.displayMedium.toModifier()
}
val DisplaySmallVariant = BaseTextStyle.addVariantBase {
    typography.displaySmall.toModifier()
}
val HeadlineLargeVariant = BaseTextStyle.addVariantBase {
    typography.headlineLarge.toModifier()
}
val HeadlineMediumVariant = BaseTextStyle.addVariantBase {
    typography.headlineMedium.toModifier()
}
val HeadlineSmallVariant = BaseTextStyle.addVariantBase {
    typography.headlineSmall.toModifier()
}
val TitleLargeVariant = BaseTextStyle.addVariantBase {
    typography.titleLarge.toModifier()
}
val TitleMediumVariant = BaseTextStyle.addVariantBase {
    typography.titleMedium.toModifier()
}
val TitleSmallVariant = BaseTextStyle.addVariantBase {
    typography.titleSmall.toModifier()
}
val BodyLargeVariant = BaseTextStyle.addVariantBase {
    typography.bodyLarge.toModifier()
}
val BodyMediumVariant = BaseTextStyle.addVariantBase {
    typography.bodyMedium.toModifier()
}
val BodySmallVariant = BaseTextStyle.addVariantBase {
    typography.bodySmall.toModifier()
}
val LabelLargeVariant = BaseTextStyle.addVariantBase {
    typography.labelLarge.toModifier()
}
val LabelMediumVariant = BaseTextStyle.addVariantBase {
    typography.labelMedium.toModifier()
}
val LabelSmallVariant = BaseTextStyle.addVariantBase {
    typography.labelSmall.toModifier()
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    ref: ElementRefScope<HTMLSpanElement>? = null,
) {
    when {
        style?.isStyle(Theme.typography.displayLarge) == true -> H1(
            attrs = BaseTextStyle
                .toModifier(DisplayLargeVariant)
                .then(style.toModifier(diff = Theme.typography.displayLarge))
                .then(modifier)
                .toAttrs(),
        ) {
            Text(value = text)
        }

        style?.isStyle(Theme.typography.displayMedium) == true -> H2(
            attrs = BaseTextStyle
                .toModifier(DisplayMediumVariant)
                .then(style.toModifier(diff = Theme.typography.displayMedium))
                .then(modifier)
                .toAttrs(),
        ) {
            Text(value = text)
        }

        style?.isStyle(Theme.typography.displaySmall) == true -> H3(
            attrs = BaseTextStyle
                .toModifier(DisplaySmallVariant)
                .then(style.toModifier(diff = Theme.typography.displaySmall))
                .then(modifier)
                .toAttrs(),
        ) {
            Text(value = text)
        }

        style?.isStyle(Theme.typography.headlineLarge) == true -> H4(
            attrs = BaseTextStyle
                .toModifier(HeadlineLargeVariant)
                .then(style.toModifier(diff = Theme.typography.headlineLarge))
                .then(modifier)
                .toAttrs(),
        ) {
            Text(value = text)
        }

        style?.isStyle(Theme.typography.headlineMedium) == true -> H5(
            attrs = BaseTextStyle
                .toModifier(HeadlineMediumVariant)
                .then(style.toModifier(diff = Theme.typography.headlineMedium))
                .then(modifier)
                .toAttrs(),
        ) {
            Text(value = text)
        }

        style?.isStyle(Theme.typography.headlineSmall) == true -> H6(
            attrs = BaseTextStyle
                .toModifier(HeadlineSmallVariant)
                .then(style.toModifier(diff = Theme.typography.headlineSmall))
                .then(modifier)
                .toAttrs(),
        ) {
            Text(value = text)
        }

        style?.isStyle(Theme.typography.titleLarge) == true -> P(
            attrs = BaseTextStyle
                .toModifier(TitleLargeVariant)
                .then(style.toModifier(Theme.typography.titleLarge))
                .then(modifier)
                .toAttrs(),
        ) {
            Text(value = text)
        }

        style?.isStyle(Theme.typography.titleMedium) == true -> P(
            attrs = BaseTextStyle
                .toModifier(TitleMediumVariant)
                .then(style.toModifier(Theme.typography.titleMedium))
                .then(modifier)
                .toAttrs(),
        ){
            Text(value = text)
        }

        style?.isStyle(Theme.typography.titleSmall) == true -> P(
            attrs = BaseTextStyle
                .toModifier(TitleSmallVariant)
                .then(style.toModifier(Theme.typography.titleSmall))
                .then(modifier)
                .toAttrs(),
        ) {
            Text(value = text)
        }

        style?.isStyle(Theme.typography.bodyLarge) == true -> SpanText(
            text = text,
            modifier = BaseTextStyle
                .toModifier(BodyLargeVariant)
                .then(style.toModifier(Theme.typography.bodyLarge))
                .then(modifier),
        )

        style?.isStyle(Theme.typography.bodyMedium) == true -> SpanText(
            text = text,
            modifier = BaseTextStyle
                .toModifier(BodyMediumVariant)
                .then(style.toModifier(Theme.typography.bodyMedium))
                .then(modifier),
        )

        style?.isStyle(Theme.typography.bodySmall) == true -> SpanText(
            text = text,
            modifier = BaseTextStyle
                .toModifier(BodySmallVariant)
                .then(style.toModifier(Theme.typography.bodySmall))
                .then(modifier),
        )

        style?.isStyle(Theme.typography.labelLarge) == true -> SpanText(
            text = text,
            modifier = BaseTextStyle
                .toModifier(LabelLargeVariant)
                .then(style.toModifier(Theme.typography.labelLarge))
                .then(modifier),
        )

        style?.isStyle(Theme.typography.labelMedium) == true -> SpanText(
            text = text,
            modifier = BaseTextStyle
                .toModifier(LabelMediumVariant)
                .then(style.toModifier(Theme.typography.labelMedium))
                .then(modifier),
        )

        style?.isStyle(Theme.typography.labelSmall) == true -> SpanText(
            text = text,
            modifier = BaseTextStyle
                .toModifier(LabelSmallVariant)
                .then(style.toModifier(Theme.typography.labelSmall))
                .then(modifier),
        )
//        style?.isStyle(Theme.typography.labelMedium) == true -> P(
//            attrs = BaseTextStyle
//                .toModifier(LabelMediumVariant)
//                .then(style.toModifier(diff = Theme.typography.labelMedium))
//                .then(modifier)
//                .toAttrs(),
//        ) {
//            Text(value = text)
//        }


        else -> SpanText(
            text = text,
            modifier = BaseTextStyle.toModifier() then style.toModifier() then modifier,
            ref = ref
        )
    }
}

private fun TextStyle.isStyle(style: TextStyle): Boolean =
    this.fontFamily == style.fontFamily
        && this.fontStyle == style.fontStyle
        && this.fontWeight == style.fontWeight
        && this.fontSize == style.fontSize
        && this.lineHeight == style.lineHeight
        && this.letterSpacing == style.letterSpacing
