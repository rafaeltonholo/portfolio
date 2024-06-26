package dev.tonholo.marktdown.processor.extensions

private fun String.replaceDividers(): String {
    val pattern = "([_\\-. ])[a-zA-Z0-9]".toRegex()
    return replace(pattern) { it.value.last().uppercase() }
}

fun String.pascalCase(): String = replaceDividers()
    .replaceFirstChar { it.uppercaseChar() }

fun CharSequence.indented(indentSize: Int = 2): String = " ".repeat(indentSize) + this
