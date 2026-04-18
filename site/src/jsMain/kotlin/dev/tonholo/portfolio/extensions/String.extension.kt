package dev.tonholo.portfolio.extensions

fun String.toKebabCase(): String =
    replace(Regex("([a-z0-9])([A-Z])"), "$1-$2").lowercase()
