package dev.tonholo.portfolio.state

import dev.tonholo.portfolio.locale.Locale
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LocaleStateTest {
    @BeforeTest
    fun setUp() {
        window.localStorage.clear()
    }

    @AfterTest
    fun tearDown() {
        window.localStorage.clear()
    }

    @Test
    fun `given EnCa initial, when set PtBr is called, then current is PtBr and storage has pt-BR`() {
        // Arrange
        val state = LocaleState(initial = Locale.EnCa)

        // Act
        state.set(next = Locale.PtBr)

        // Assert
        assertEquals(Locale.PtBr, state.current)
        assertEquals("pt-BR", window.localStorage.getItem("rt_lang"))
    }

    @Test
    fun `given PtBr initial, when set EnCa is called, then current is EnCa and storage has en-CA`() {
        // Arrange
        val state = LocaleState(initial = Locale.PtBr)

        // Act
        state.set(next = Locale.EnCa)

        // Assert
        assertEquals(Locale.EnCa, state.current)
        assertEquals("en-CA", window.localStorage.getItem("rt_lang"))
    }

    @Test
    fun `given LocaleState, when set is called, then documentElement lang is updated`() {
        // Arrange
        val state = LocaleState(initial = Locale.EnCa)

        // Act
        state.set(next = Locale.PtBr)

        // Assert
        val root = document.documentElement as? HTMLElement
        assertEquals("pt-BR", root?.lang)
    }
}
