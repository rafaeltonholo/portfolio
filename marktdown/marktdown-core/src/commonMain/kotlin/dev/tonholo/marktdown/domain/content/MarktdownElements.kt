package dev.tonholo.marktdown.domain.content

import dev.tonholo.marktdown.domain.MarktdownLink
import kotlin.jvm.JvmInline

sealed interface MarktdownRenderer {
    fun render() {}
}

sealed interface MarktdownElement : MarktdownRenderer

sealed interface MarktdownParent<out T : MarktdownElement> : MarktdownElement {
    val children: List<T>
}

sealed interface TextElement : MarktdownElement {
    data class PlainText(val text: String) : TextElement
    data class InlineCode(val code: String) : TextElement
    data class Paragraph(
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<MarktdownElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): Paragraph =
                Paragraph(children.toList())

            operator fun invoke(text: String): Paragraph =
                Paragraph(PlainText(text))
        }
    }

    data class BoldText(
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): BoldText =
                BoldText(children.toList())

            operator fun invoke(text: String): BoldText =
                BoldText(PlainText(text))
        }
    }

    data class ItalicText(override val children: List<TextElement>) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): ItalicText =
                ItalicText(children.toList())

            operator fun invoke(text: String): ItalicText =
                ItalicText(PlainText(text))
        }
    }

    data class Highlight(
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): Highlight =
                Highlight(children.toList())

            operator fun invoke(text: String): Highlight =
                Highlight(PlainText(text))
        }
    }

    data class Subscript(
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): Subscript =
                Subscript(children.toList())

            operator fun invoke(text: String): Subscript =
                Subscript(PlainText(text))
        }
    }

    data class Superscript(
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): Superscript =
                Superscript(children.toList())

            operator fun invoke(text: String): Superscript =
                Superscript(PlainText(text))
        }
    }

    data class Emoji(val shortcode: String) : TextElement

    data class Title(
        override val children: List<TextElement>,
        val style: Style,
        val id: TitleId? = null,
    ) : TextElement, MarktdownParent<TextElement> {
        @JvmInline
        value class TitleId(val value: String)
        enum class Style {
            H1, H2, H3, H4, H5, H6
        }

        companion object {
            operator fun invoke(
                vararg children: TextElement,
                style: Style,
                id: TitleId? = null,
            ): Title = Title(children = children.toList(), style = style, id = id)

            operator fun invoke(
                text: String,
                style: Style,
                id: TitleId? = null,
            ): Title = Title(PlainText(text), style = style, id = id)
        }
    }

    data class Blockquote(
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<TextElement>

    data class Strikethrough(
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): Strikethrough =
                Strikethrough(children.toList())

            operator fun invoke(text: String): Strikethrough =
                Strikethrough(PlainText(text))
        }
    }
}

data class CodeBlock(
    val language: String,
    val code: String,
) : MarktdownElement

data class ImageElement(
    val link: MarktdownLink,
    val alt: String? = null,
) : MarktdownElement

sealed interface Link : TextElement {
    data class Element(
        val link: MarktdownLink,
        val title: TextElement? = null,
    ) : Link {
        companion object {
            operator fun invoke(
                link: MarktdownLink,
                title: String,
            ): Element = Element(
                link = link,
                title = TextElement.PlainText(title),
            )
        }
    }

    data class ReferenceElement(
        val text: TextElement,
        val anchor: String,
    ) : Link

    data class FootnoteElement(
        val text: TextElement,
        val anchor: String,
    ) : Link
}

data class ReferenceElement(
    val anchor: String,
    val link: MarktdownLink,
    val linkTitle: String = link.value,
) : MarktdownElement

data object HorizontalRule : MarktdownElement

data class TableElement(
    val header: TableContent.Header,
    val content: TableContent.Row,
) : MarktdownElement

sealed interface TableContent : MarktdownElement {
    data class Header(
        val columns: List<MarktdownElement>,
    ) : TableContent

    data class Row(
        val content: List<MarktdownElement>,
    ) : TableContent
}

sealed interface ListElement : MarktdownElement {
    data class Ordered(
        val options: List<TextElement>,
    ) : ListElement

    data class Numbered(
        val options: List<TextElement>,
    ) : ListElement

    data class Task(
        val options: List<Pair<Boolean, TextElement>>,
    ) : ListElement
}
