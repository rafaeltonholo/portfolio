package dev.tonholo.portfolio.core.components.button

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.color.copy

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Theme.colorScheme.primary,
            contentColor = Theme.colorScheme.onPrimary,
            disabledContainerColor = Theme.colorScheme.primary.copy(alpha = 0.12f),
            disabledContentColor = Theme.colorScheme.onSurface.copy(alpha = 0.50f),
        ),
        content = content,
    )
}
