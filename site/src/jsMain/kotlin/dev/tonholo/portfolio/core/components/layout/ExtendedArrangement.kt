package dev.tonholo.portfolio.core.components.layout

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.ui.Alignment
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.dp

@Immutable
interface ExtendedArrangement {

    @Stable
    interface Horizontal : ExtendedArrangement {
        val spacing: Dp get() = 0.dp
        val horizontalArrangement: Arrangement.Horizontal

        companion object {
            operator fun invoke(source: Arrangement.Horizontal, spacing: Dp = 0.dp): Horizontal =
                object : Horizontal {
                    override val horizontalArrangement: Arrangement.Horizontal = source
                    override val spacing: Dp = spacing
                }
        }
    }

    @Stable
    interface Vertical : ExtendedArrangement {
        val spacing: Dp get() = 0.dp
        val verticalArrangement: Arrangement.Vertical

        companion object {
            operator fun invoke(source: Arrangement.Vertical, spacing: Dp = 0.dp): Vertical =
                object : Vertical {
                    override val verticalArrangement: Arrangement.Vertical = source
                    override val spacing: Dp = spacing
                }
        }
    }

    @Stable
    interface HorizontalOrVertical : Horizontal, Vertical {
        override val spacing: Dp get() = 0.dp
        val source: Arrangement.HorizontalOrVertical
        override val horizontalArrangement: Arrangement.Horizontal get() = source
        override val verticalArrangement: Arrangement.Vertical get() = source

        companion object {
            operator fun invoke(source: Arrangement.HorizontalOrVertical, spacing: Dp = 0.dp): HorizontalOrVertical =
                object : HorizontalOrVertical {
                    override val source: Arrangement.HorizontalOrVertical = source
                    override val spacing: Dp = spacing
                }
        }
    }

    companion object INSTANCE {
        @Stable
        val End = Horizontal(source = Arrangement.End)

        @Stable
        val Start = Horizontal(source = Arrangement.Start)

        @Stable
        val Top = Vertical(source = Arrangement.Top)

        @Stable
        val Bottom = Vertical(source = Arrangement.Bottom)

        @Stable
        val Center = HorizontalOrVertical(source = Arrangement.Center)

        @Stable
        val SpaceEvenly = HorizontalOrVertical(source = Arrangement.SpaceEvenly)

        @Stable
        val SpaceBetween = HorizontalOrVertical(source = Arrangement.SpaceBetween)

        @Stable
        val SpaceAround = HorizontalOrVertical(source = Arrangement.SpaceAround)

        @Stable
        fun spacedBy(space: Dp): HorizontalOrVertical =
            HorizontalOrVertical(spacing = space, source = Arrangement.Center)

        @Stable
        fun spacedBy(space: Dp, alignment: Alignment.Horizontal): Horizontal =
            Horizontal(
                spacing = space,
                source = when (alignment) {
                    Alignment.CenterHorizontally -> Arrangement.Center
                    Alignment.End -> Arrangement.End
                    Alignment.FromStyle -> Arrangement.FromStyle
                    Alignment.Start -> Arrangement.Start
                },
            )

        @Stable
        fun spacedBy(space: Dp, alignment: Alignment.Vertical): Vertical =
            Vertical(
                spacing = space,
                source = when (alignment) {
                    Alignment.Bottom -> Arrangement.Bottom
                    Alignment.CenterVertically -> Arrangement.Center
                    Alignment.FromStyle -> Arrangement.FromStyle
                    Alignment.Top -> Arrangement.Top
                },
            )
    }
}
