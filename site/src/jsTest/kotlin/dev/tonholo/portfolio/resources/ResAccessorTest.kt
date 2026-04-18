package dev.tonholo.portfolio.resources

import kotlin.test.Test
import kotlin.test.assertNotNull

class ResAccessorTest {
    @Test
    fun `given Res_string_app_name accessor, when referenced, then it is not null`() {
        // Arrange / Act / Assert
        assertNotNull(Res.string.app_name)
    }
}
