package dev.tonholo.portfolio.state

import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.window
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ThemeStateTest {
    @BeforeTest
    fun setUp() {
        window.localStorage.clear()
    }

    @AfterTest
    fun tearDown() {
        window.localStorage.clear()
    }

    @Test
    fun `given initial LIGHT, when toggle is called, then current becomes DARK and storage has dark`() {
        // Arrange
        val state = ThemeState(initial = ColorMode.LIGHT)

        // Act
        state.toggle()

        // Assert
        assertEquals(ColorMode.DARK, state.current)
        assertEquals("dark", window.localStorage.getItem("rt_theme"))
    }

    @Test
    fun `given initial DARK, when toggle is called, then current becomes LIGHT and storage has light`() {
        // Arrange
        val state = ThemeState(initial = ColorMode.DARK)

        // Act
        state.toggle()

        // Assert
        assertEquals(ColorMode.LIGHT, state.current)
        assertEquals("light", window.localStorage.getItem("rt_theme"))
    }

    @Test
    fun `given two toggles, when current is read, then it matches the initial mode`() {
        // Arrange
        val state = ThemeState(initial = ColorMode.LIGHT)

        // Act
        state.toggle()
        state.toggle()

        // Assert
        assertEquals(ColorMode.LIGHT, state.current)
        assertEquals("light", window.localStorage.getItem("rt_theme"))
    }
}
