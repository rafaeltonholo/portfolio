package dev.tonholo.portfolio.theme.typography

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.theme.layout.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Validates that [applyInlineCodeStyle] and related tokens match the `@old-layout` inline code styling.
 */
class InlineCodeStyleTest {
    @Test
    fun `given inline code tokens, when checked, then values match old-layout specifications`() {
        // Arrange & Act & Assert
        assertEquals(expected = 4.dp, actual = INLINE_CODE_PADDING_HORIZONTAL)
        assertEquals(expected = 2.dp, actual = INLINE_CODE_PADDING_VERTICAL)
        assertEquals(expected = 4.dp, actual = INLINE_CODE_BORDER_RADIUS)
        assertEquals(expected = 32.dp, actual = INLINE_CODE_LINE_HEIGHT)
        assertEquals(
            expected = Color.rgba(r = 0x40, g = 0x40, b = 0x40, a = 0.9f),
            actual = INLINE_CODE_BG_DARK,
        )
        assertEquals(
            expected = Color.rgba(r = 0xDC, g = 0xDC, b = 0xDC, a = 0.9f),
            actual = INLINE_CODE_BG_LIGHT,
        )
    }

    @Test
    fun `given modifier, when applyInlineCodeStyle is called, then modifier is created without error`() {
        // Arrange
        val inputModifier: Modifier = Modifier

        // Act
        val actualDarkModifier: Modifier = inputModifier.applyInlineCodeStyle(isDark = true)
        val actualLightModifier: Modifier = inputModifier.applyInlineCodeStyle(isDark = false)

        // Assert
        assertNotNull(actualDarkModifier)
        assertNotNull(actualLightModifier)
    }
}
