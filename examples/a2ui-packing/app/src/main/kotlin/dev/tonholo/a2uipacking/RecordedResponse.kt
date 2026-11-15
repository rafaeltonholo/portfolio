package dev.tonholo.a2uipacking

const val PACKING_SURFACE_ID = "packing"

/** A known-good Express response, so the compiler and renderer can be checked without a model. */
val recordedExpressResponse = """
    |<a2ui>
    |$/items = [{name: "Towel", packed: false}, {name: "Sunscreen", packed: true}]
    |root = Column([title, item0, item1])
    |title = Text("Weekend packing list", "h2")
    |item0 = CheckBox($/items/0/name, $/items/0/packed)
    |item1 = CheckBox($/items/1/name, $/items/1/packed)
    |</a2ui>
""".trimMargin()

fun deleteSurfaceMessage(surfaceId: String): String =
    """{"version":"v0.9.1","deleteSurface":{"surfaceId":"$surfaceId"}}"""
