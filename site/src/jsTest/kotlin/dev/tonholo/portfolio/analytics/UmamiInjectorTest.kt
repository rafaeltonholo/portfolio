package dev.tonholo.portfolio.analytics

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UmamiInjectorTest {
    @Test
    fun `given localhost hostname, when shouldInject is called, then it returns false`() {
        // Arrange
        val hostname = "localhost"

        // Act
        val actual = UmamiInjector.shouldInject(hostname)

        // Assert
        assertFalse(actual)
    }

    @Test
    fun `given 127_0_0_1 hostname, when shouldInject is called, then it returns false`() {
        // Arrange
        val hostname = "127.0.0.1"

        // Act
        val actual = UmamiInjector.shouldInject(hostname)

        // Assert
        assertFalse(actual)
    }

    @Test
    fun `given production hostname, when shouldInject is called, then it returns true`() {
        // Arrange
        val hostname = "tonholo.dev"

        // Act
        val actual = UmamiInjector.shouldInject(hostname)

        // Assert
        assertTrue(actual)
    }
}
