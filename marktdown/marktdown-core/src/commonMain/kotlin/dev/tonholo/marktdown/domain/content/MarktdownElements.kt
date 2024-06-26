package dev.tonholo.marktdown.domain.content

import dev.tonholo.marktdown.domain.MarktdownLink
import kotlin.jvm.JvmInline

sealed interface MarktdownElement

sealed interface MarktdownParent<out T : MarktdownElement> : MarktdownElement {
    val children: List<T>
}

sealed interface TextElement : MarktdownElement {
    data class PlainText(val text: String) : TextElement
    data class InlineCode(val code: String) : TextElement
    data class Paragraph(
        override val children: List<MarktdownElement>,
    ) : TextElement, MarktdownParent<MarktdownElement> {
        companion object {
            operator fun invoke(vararg children: MarktdownElement): Paragraph =
                Paragraph(children.toList())

            operator fun invoke(text: String): Paragraph =
                Paragraph(PlainText(text))
        }
    }

    data class StrongText(
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): StrongText =
                StrongText(children.toList())

            operator fun invoke(text: String): StrongText =
                StrongText(PlainText(text))
        }
    }

    data class EmphasisText(override val children: List<TextElement>) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(vararg children: TextElement): EmphasisText =
                EmphasisText(children.toList())

            operator fun invoke(text: String): EmphasisText =
                EmphasisText(PlainText(text))
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
        val style: Style,
        override val children: List<TextElement>,
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
        override val children: List<MarktdownElement>,
        val type: Type? = null,
    ) : TextElement, MarktdownParent<MarktdownElement> {
        enum class Type {
            NOTE,
            WARNING,
            CAUTION,
            IMPORTANT,
            TIP,
        }
    }

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

    data class InlineHtml(
        val tagName: String,
        override val children: List<TextElement>,
    ) : TextElement, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(tagName: String, vararg children: TextElement): InlineHtml =
                InlineHtml(tagName, children.toList())

            operator fun invoke(tagName: String, text: String): InlineHtml =
                InlineHtml(tagName, PlainText(text))
        }
    }
}

data class CodeFence(
    val language: String?,
    val code: String,
) : MarktdownElement

data class ImageElement(
    val link: MarktdownLink,
    val alt: String? = null,
) : MarktdownElement

sealed interface Link : TextElement {
    data class Element(
        val link: MarktdownLink,
        override val children: List<TextElement>,
    ) : Link, MarktdownParent<TextElement> {
        companion object {
            operator fun invoke(
                link: MarktdownLink,
                title: String,
            ): Element = Element(
                link = link,
                children = listOf(TextElement.PlainText(title)),
            )

            operator fun invoke(
                link: MarktdownLink,
                vararg children: TextElement,
            ): Element = Element(
                link = link,
                children = children.toList(),
            )
        }
    }

    data class AutoLink(
        val link: MarktdownLink,
        val label: String,
    ) : Link

    data class ReferenceElement(
        val destination: MarktdownLink,
        val title: String,
        override val children: List<MarktdownElement>,
    ) : Link, MarktdownParent<MarktdownElement> {
        companion object {
            operator fun invoke(
                destination: MarktdownLink,
                title: String,
                label: String,
            ): ReferenceElement = ReferenceElement(
                destination = destination,
                title = title,
                TextElement.PlainText(label),
            )

            operator fun invoke(
                destination: MarktdownLink,
                title: String,
                vararg children: MarktdownElement,
            ): ReferenceElement = ReferenceElement(
                destination = destination,
                title = title,
                children = children.toList(),
            )
        }
    }
}

data class LinkDefinition(
    val destination: MarktdownLink,
    val label: String,
    val title: String? = null,
) : MarktdownElement

data object EndOfLine : MarktdownElement
data object HorizontalRule : MarktdownElement
data object LineBreak : MarktdownElement

data class TableElement(
    val header: TableContent.Header,
    val rows: List<TableContent.Row>,
) : MarktdownElement

sealed interface TableContent : MarktdownElement {
    data class Header(
        val row: Row,
    ) : TableContent

    data class Row(
        val cells: List<Cell>,
    ) : TableContent

    data class Cell(
        val content: List<MarktdownElement>,
        val alignment: Alignment = Alignment.START,
    ) : TableContent {
        companion object {
            operator fun invoke(
                vararg content: MarktdownElement,
                alignment: Alignment = Alignment.START,
            ): Cell = Cell(
                content.toList(),
                alignment = alignment,
            )
        }
    }

    enum class Alignment { START, CENTER, END }
}

sealed interface ListElement : MarktdownElement {
    data class Unordered(
        override val children: List<ListItem>,
    ) : ListElement, MarktdownParent<ListItem> {
        companion object {
            operator fun invoke(
                vararg options: ListItem,
            ): Unordered = Unordered(
                children = options.toList(),
            )
        }
    }

    data class Ordered(
        override val children: List<ListItem>,
        val startWith: Int? = null,
    ) : ListElement, MarktdownParent<ListItem> {
        companion object {
            operator fun invoke(
                startWith: Int? = null,
                vararg options: ListItem,
            ): Ordered = Ordered(
                children = options.toList(),
                startWith = startWith,
            )

            operator fun invoke(
                vararg options: ListItem,
            ): Ordered = Ordered(
                children = options.toList(),
            )
        }
    }

    data class Task(
        val options: List<Pair<Boolean, ListItem>>,
    ) : ListElement

    data class ListItem(
        override val children: List<MarktdownElement>
    ) : ListElement, MarktdownParent<MarktdownElement>
}

data class HtmlBlock(
    val content: String,
) : MarktdownElement
