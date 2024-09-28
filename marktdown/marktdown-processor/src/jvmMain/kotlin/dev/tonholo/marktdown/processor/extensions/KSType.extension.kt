package dev.tonholo.marktdown.processor.extensions

import com.google.devtools.ksp.symbol.KSType
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.content.CustomElement
import dev.tonholo.marktdown.domain.content.HorizontalRule
import dev.tonholo.marktdown.domain.content.HtmlBlock
import dev.tonholo.marktdown.domain.content.ImageElement
import dev.tonholo.marktdown.domain.content.LineBreak
import dev.tonholo.marktdown.domain.content.Link.AutoLink
import dev.tonholo.marktdown.domain.content.Link.Element
import dev.tonholo.marktdown.domain.content.Link.ReferenceElement
import dev.tonholo.marktdown.domain.content.LinkDefinition
import dev.tonholo.marktdown.domain.content.ListElement.ListItem
import dev.tonholo.marktdown.domain.content.ListElement.Ordered
import dev.tonholo.marktdown.domain.content.ListElement.Task
import dev.tonholo.marktdown.domain.content.ListElement.Unordered
import dev.tonholo.marktdown.domain.content.MarktdownElement
import dev.tonholo.marktdown.domain.content.TableElement
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.domain.content.TextElement.Blockquote
import dev.tonholo.marktdown.domain.content.TextElement.Emoji
import dev.tonholo.marktdown.domain.content.TextElement.EmphasisText
import dev.tonholo.marktdown.domain.content.TextElement.Highlight
import dev.tonholo.marktdown.domain.content.TextElement.InlineCode
import dev.tonholo.marktdown.domain.content.TextElement.InlineHtml
import dev.tonholo.marktdown.domain.content.TextElement.Paragraph
import dev.tonholo.marktdown.domain.content.TextElement.PlainText
import dev.tonholo.marktdown.domain.content.TextElement.Strikethrough
import dev.tonholo.marktdown.domain.content.TextElement.StrongText
import dev.tonholo.marktdown.domain.content.TextElement.Subscript
import dev.tonholo.marktdown.domain.content.TextElement.Superscript
import dev.tonholo.marktdown.domain.content.TextElement.Title
import kotlin.reflect.KClass

fun KSType.toMarktdownKClass(): KClass<out MarktdownElement>? =
    when (this.declaration.qualifiedName?.asString()) {
        CodeFence::class.qualifiedName -> CodeFence::class
        HorizontalRule::class.qualifiedName -> HorizontalRule::class
        ImageElement::class.qualifiedName -> ImageElement::class
        LineBreak::class.qualifiedName -> LineBreak::class
        LinkDefinition::class.qualifiedName -> LinkDefinition::class
        TableElement::class.qualifiedName -> TableElement::class
        ListItem::class.qualifiedName -> ListItem::class
        Ordered::class.qualifiedName -> Ordered::class
        Task::class.qualifiedName -> Task::class
        Unordered::class.qualifiedName -> Unordered::class
        Blockquote::class.qualifiedName -> Blockquote::class
        Element::class.qualifiedName -> Element::class
        EmphasisText::class.qualifiedName -> EmphasisText::class
        Highlight::class.qualifiedName -> Highlight::class
        Paragraph::class.qualifiedName -> Paragraph::class
        ReferenceElement::class.qualifiedName -> ReferenceElement::class
        Strikethrough::class.qualifiedName -> Strikethrough::class
        StrongText::class.qualifiedName -> StrongText::class
        Subscript::class.qualifiedName -> Subscript::class
        Superscript::class.qualifiedName -> Superscript::class
        Title::class.qualifiedName -> Title::class
        Emoji::class.qualifiedName -> Emoji::class
        InlineCode::class.qualifiedName -> InlineCode::class
        AutoLink::class.qualifiedName -> AutoLink::class
        PlainText::class.qualifiedName -> PlainText::class
        InlineHtml::class.qualifiedName -> InlineHtml::class
        HtmlBlock::class.qualifiedName -> HtmlBlock::class
        CustomElement::class.qualifiedName -> CustomElement::class
        else -> null
    }
