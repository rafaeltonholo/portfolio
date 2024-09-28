package dev.tonholo.marktdown.flavours

import org.intellij.markdown.MarkdownElementType

object MarktdownElementTypes {
    @JvmField
    val INLINE_HTML = MarkdownElementType(name = "INLINE_HTML")

    @JvmField
    val CUSTOM_ELEMENT = MarkdownElementType(name = "CUSTOM_ELEMENT")
}
