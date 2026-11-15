package dev.tonholo.a2ui.express

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ExpressCatalogTest {
    @Test
    fun `given the basic catalog schema - when reading it - then the catalog id is kept`() {
        val catalog = ExpressCatalog.fromSchema(basicCatalogSchema)

        assertEquals("https://a2ui.org/specification/v0_9/catalogs/basic/catalog.json", catalog.catalogId)
    }

    @Test
    fun `given the basic catalog schema - when making prompt signatures - then parameters follow schema order and mark optional ones`() {
        val catalog = ExpressCatalog.fromSchema(basicCatalogSchema)

        val signatures = catalog.promptSignatures(components = listOf("Column", "Text", "CheckBox"))

        assertEquals(
            """
            Column(children, justify?, align?)
            Text(text, variant?: h1|h2|h3|h4|h5|caption|body)
            CheckBox(label, value)
            """.trimIndent(),
            signatures,
        )
    }

    @Test
    fun `given a component outside the catalog - when making prompt signatures - then it is rejected`() {
        val catalog = ExpressCatalog.fromSchema(basicCatalogSchema)

        val error = assertFailsWith<IllegalArgumentException> {
            catalog.promptSignatures(components = listOf("Chart"))
        }

        assertEquals("Chart is not in the catalog", error.message)
    }
}
