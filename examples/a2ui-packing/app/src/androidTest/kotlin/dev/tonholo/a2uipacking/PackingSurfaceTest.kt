package dev.tonholo.a2uipacking

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class PackingSurfaceTest {
    @get:Rule
    val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun givenARecordedExpressResponse_whenLoadingIt_thenTheTitleAndBoundCheckboxesAreRendered() {
        compose.onNodeWithText("Load example").performClick()

        compose.onNodeWithText("Weekend packing list").assertIsDisplayed()
        compose.onNodeWithText("Towel").assertIsDisplayed()
        compose.onNodeWithText("Sunscreen").assertIsDisplayed()
    }
}
