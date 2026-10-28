package dev.tonholo.portfolio.extensions

import kotlinx.browser.window
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LocalStorageExtensionTest {
    @BeforeTest
    fun setUp() {
        window.localStorage.clear()
    }

    @AfterTest
    fun tearDown() {
        window.localStorage.clear()
    }

    @Test
    fun `given key x and value y, when writeLocalStorage is called, then readLocalStorage returns y`() {
        // Arrange
        val key = "x"
        val value = "y"

        // Act
        writeLocalStorage(key = key, value = value)
        val actual = readLocalStorage(key = key)

        // Assert
        assertEquals(value, actual)
    }

    @Test
    fun `given a missing key, when readLocalStorage is called, then it returns null`() {
        // Arrange
        val key = "missing"

        // Act
        val actual = readLocalStorage(key = key)

        // Assert
        assertNull(actual)
    }

    @Test
    fun `given an overwritten key, when readLocalStorage is called, then latest value is returned`() {
        // Arrange
        val key = "rt_theme"
        writeLocalStorage(key = key, value = "light")

        // Act
        writeLocalStorage(key = key, value = "dark")
        val actual = readLocalStorage(key = key)

        // Assert
        assertEquals("dark", actual)
    }
}
