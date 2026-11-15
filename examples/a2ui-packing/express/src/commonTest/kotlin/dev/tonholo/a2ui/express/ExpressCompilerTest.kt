package dev.tonholo.a2ui.express

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray

class ExpressCompilerTest {
    private val compiler = ExpressCompiler(catalog = basicCatalog)

    private fun wire(json: String): JsonArray = Json.parseToJsonElement(json) as JsonArray

    private fun compile(source: String, surfaceId: String = "main") =
        JsonArray(compiler.compile(response = source, surfaceId = surfaceId))

    @Test
    fun `given the flight example from the A2UI docs - when compiling - then it produces the documented v0_9_1 messages`() {
        val source = """
            <a2ui>
            $/flight/number = "NH 105"
            $/flight/gate = "B12"
            root = Card(
              Column([
                Text($/flight/number),
                Text($/flight/gate, "caption"),
                Button(Text("Board now"), action=Event("check_in", {flight: $/flight/number}))
              ])
            )
            </a2ui>
        """.trimIndent()

        val messages = compile(source)

        val expected = wire(
            """
            [
              {"version": "v0.9.1", "createSurface": {"surfaceId": "main", "catalogId": "https://a2ui.org/specification/v0_9/catalogs/basic/catalog.json"}},
              {"version": "v0.9.1", "updateDataModel": {"surfaceId": "main", "path": "/", "value": {"flight": {"number": "NH 105", "gate": "B12"}}}},
              {"version": "v0.9.1", "updateComponents": {"surfaceId": "main", "components": [
                {"id": "root", "component": "Card", "child": "root_child"},
                {"id": "root_child", "component": "Column", "children": ["root_child_children_0", "root_child_children_1", "root_child_children_2"]},
                {"id": "root_child_children_0", "component": "Text", "text": {"path": "/flight/number"}},
                {"id": "root_child_children_1", "component": "Text", "text": {"path": "/flight/gate"}, "variant": "caption"},
                {"id": "root_child_children_2", "component": "Button", "child": "root_child_children_2_child", "action": {"event": {"name": "check_in", "context": {"flight": {"path": "/flight/number"}}}}},
                {"id": "root_child_children_2_child", "component": "Text", "text": "Board now"}
              ]}}
            ]
            """,
        )
        assertEquals(expected, messages)
    }

    @Test
    fun `given components assigned to variables - when compiling - then variable names become component ids`() {
        val source = """
            <a2ui>
            root = Column([title, item0])
            title = Text("Beach day", "h2")
            item0 = CheckBox("Towel", false)
            </a2ui>
        """.trimIndent()

        val components = compile(source).components()

        val expected = wire(
            """
            [
              {"id": "root", "component": "Column", "children": ["title", "item0"]},
              {"id": "title", "component": "Text", "text": "Beach day", "variant": "h2"},
              {"id": "item0", "component": "CheckBox", "label": "Towel", "value": false}
            ]
            """,
        )
        assertEquals(expected, components)
    }

    @Test
    fun `given an underscore placeholder - when compiling - then the skipped optional argument is omitted`() {
        val source = """
            <a2ui>
            root = Column([title], _, "center")
            title = Text("Hi")
            </a2ui>
        """.trimIndent()

        val root = compile(source).components()[0]

        assertEquals(
            Json.parseToJsonElement("""{"id": "root", "component": "Column", "children": ["title"], "align": "center"}"""),
            root,
        )
    }

    @Test
    fun `given prose and a markdown fence around the block - when compiling - then only the block is read`() {
        val source = """
            Sure! Here is your checklist:
            ```
            <a2ui>
            root = Text("Hi")
            </a2ui>
            ```
            Have a nice trip.
        """.trimIndent()

        val components = compile(source).components()

        assertEquals(wire("""[{"id": "root", "component": "Text", "text": "Hi"}]"""), components)
    }

    @Test
    fun `given data assignments that refine a list item - when compiling - then they fold into one data model`() {
        val source = """
            <a2ui>
            $/items = [{name: "Towel", packed: false}, {name: "Hat", packed: false}]
            $/items/1/packed = true
            root = CheckBox($/items/1/name, $/items/1/packed)
            </a2ui>
        """.trimIndent()

        val dataModel = compile(source).message("updateDataModel")

        assertEquals(
            Json.parseToJsonElement(
                """{"surfaceId": "main", "path": "/", "value": {"items": [{"name": "Towel", "packed": false}, {"name": "Hat", "packed": true}]}}""",
            ),
            dataModel,
        )
    }

    @Test
    fun `given data and components - when compiling - then the data model is sent before the components`() {
        val messages = compile("<a2ui>\n$/name = \"Towel\"\nroot = Text($/name)\n</a2ui>")

        assertEquals(listOf("createSurface", "updateDataModel", "updateComponents"), messages.map { it.messageType() })
    }

    @Test
    fun `given no data assignments - when compiling - then no data model message is sent`() {
        val messages = compile("<a2ui>\nroot = Text(\"Hi\")\n</a2ui>")

        assertEquals(listOf("createSurface", "updateComponents"), messages.map { it.messageType() })
    }

    @Test
    fun `given a response without an a2ui block - when compiling - then it is rejected`() {
        val error = assertFailsWith<ExpressException> { compile("I can't help with that.") }

        assertEquals("No <a2ui> block found", error.message)
    }

    @Test
    fun `given a component outside the catalog - when compiling - then the line is reported`() {
        val source = "<a2ui>\nroot = Column([chart])\nchart = Chart($/sales)\n</a2ui>"

        val error = assertFailsWith<ExpressException> { compile(source) }

        assertEquals("Line 2: Chart is not in the catalog", error.message)
    }

    @Test
    fun `given a block without root - when compiling - then it is rejected`() {
        val error = assertFailsWith<ExpressException> { compile("<a2ui>\ntitle = Text(\"Hi\")\n</a2ui>") }

        assertEquals("No root component defined", error.message)
    }

    @Test
    fun `given a reference to an undefined variable - when compiling - then the line is reported`() {
        val error = assertFailsWith<ExpressException> { compile("<a2ui>\nroot = Column([title, footer])\ntitle = Text(\"Hi\")\n</a2ui>") }

        assertEquals("Line 1: footer is not defined", error.message)
    }

    @Test
    fun `given a missing required argument - when compiling - then the line is reported`() {
        val error = assertFailsWith<ExpressException> { compile("<a2ui>\nroot = CheckBox(\"Towel\")\n</a2ui>") }

        assertEquals("Line 1: CheckBox is missing value", error.message)
    }

    @Test
    fun `given too many positional arguments - when compiling - then the line is reported`() {
        val error = assertFailsWith<ExpressException> { compile("<a2ui>\nroot = Text(\"Hi\", \"h2\", \"extra\")\n</a2ui>") }

        assertEquals("Line 1: Text takes at most 2 arguments", error.message)
    }

    @Test
    fun `given an unknown keyword argument - when compiling - then the line is reported`() {
        val error = assertFailsWith<ExpressException> { compile("<a2ui>\nroot = Text(\"Hi\", size=12)\n</a2ui>") }

        assertEquals("Line 1: Text has no parameter size", error.message)
    }

    @Test
    fun `given an unterminated string - when compiling - then the line is reported`() {
        val error = assertFailsWith<ExpressException> { compile("<a2ui>\nroot = Text(\"Hi)\n</a2ui>") }

        assertEquals("Line 1: Unterminated string", error.message)
    }

    @Test
    fun `given a component that contains itself - when compiling - then it is rejected`() {
        val error = assertFailsWith<ExpressException> { compile("<a2ui>\nroot = Column([inner])\ninner = Column([root])\n</a2ui>") }

        assertEquals("Line 2: root refers back to itself", error.message)
    }
}
