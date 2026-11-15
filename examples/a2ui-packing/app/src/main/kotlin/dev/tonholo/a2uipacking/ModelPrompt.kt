package dev.tonholo.a2uipacking

/** Components the packing prompts allow; the catalog renders their signatures for the Express prompt. */
val packingComponents = listOf("Column", "Text", "CheckBox")

/**
 * Asks for a packing checklist in A2UI Express.
 *
 * @param signatures the allowed component signatures, from `ExpressCatalog.promptSignatures`.
 */
fun expressPrompt(request: String, signatures: String): String = """
    |/no_think
    |Create a packing checklist for the user's request in A2UI Express.
    |Reply with one <a2ui> block and no other text.
    |Components:
    |$signatures
    |$/path = value sets data. $/path as an argument binds to data.
    |Example:
    |<a2ui>
    |$/items = [{name: "Dice", packed: false}, {name: "Snacks", packed: false}]
    |root = Column([title, item0, item1])
    |title = Text("Board game night", "h2")
    |item0 = CheckBox($/items/0/name, $/items/0/packed)
    |item1 = CheckBox($/items/1/name, $/items/1/packed)
    |</a2ui>
    |Request: $request
""".trimMargin()

/** Asks for the same packing checklist as plain A2UI v0.9.1 wire messages, one JSON object per line. */
fun jsonPrompt(request: String): String = """
    |/no_think
    |Create a packing checklist for the user's request as A2UI v0.9.1 messages for surfaceId packing.
    |Reply with exactly two JSON objects, one per line, and no other text.
    |Line 1 is updateComponents: a Column with id root, a Text title with variant h2, and one CheckBox per item.
    |Components are a flat list. Parents refer to children by id. Each CheckBox label and value bind to the data model with {"path": ...}.
    |Line 2 is updateDataModel with path /items holding the items.
    |Example:
    |{"version":"v0.9.1","updateComponents":{"surfaceId":"packing","components":[{"id":"root","component":"Column","children":["title","item0","item1"]},{"id":"title","component":"Text","text":"Board game night","variant":"h2"},{"id":"item0","component":"CheckBox","label":{"path":"/items/0/name"},"value":{"path":"/items/0/packed"}},{"id":"item1","component":"CheckBox","label":{"path":"/items/1/name"},"value":{"path":"/items/1/packed"}}]}}
    |{"version":"v0.9.1","updateDataModel":{"surfaceId":"packing","path":"/items","value":[{"name":"Dice","packed":false},{"name":"Snacks","packed":false}]}}
    |Request: $request
""".trimMargin()
