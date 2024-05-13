package dev.tonholo.marktdown.processor.renderer

import com.squareup.kotlinpoet.asClassName
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.content.HorizontalRule
import dev.tonholo.marktdown.domain.content.ImageElement
import dev.tonholo.marktdown.domain.content.LineBreak
import dev.tonholo.marktdown.domain.content.Link
import dev.tonholo.marktdown.domain.content.LinkDefinition
import dev.tonholo.marktdown.domain.content.ListElement
import dev.tonholo.marktdown.domain.content.MarktdownElement
import dev.tonholo.marktdown.domain.content.TableElement
import dev.tonholo.marktdown.domain.content.TextElement
import java.nio.file.Path
import kotlin.reflect.KClass

class MarktdownElementRendererCreator(
    private val rendererGenerator: RendererGenerator,
) {
    companion object {
        val marktdownElements: Map<String, KClass<out MarktdownElement>> = mapOf(
            CodeFence::class.java.name to CodeFence::class,
            HorizontalRule::class.java.name to HorizontalRule::class,
            ImageElement::class.java.name to ImageElement::class,
            LineBreak::class.java.name to LineBreak::class,
            LinkDefinition::class.java.name to LinkDefinition::class,
            TableElement::class.java.name to TableElement::class,
            ListElement.ListItem::class.constructor to ListElement.ListItem::class,
            ListElement.Ordered::class.constructor to ListElement.Ordered::class,
            ListElement.Task::class.constructor to ListElement.Task::class,
            ListElement.Unordered::class.constructor to ListElement.Unordered::class,
            TextElement.Blockquote::class.constructor to TextElement.Blockquote::class,
            Link.Element::class.constructor to Link.Element::class,
            TextElement.EmphasisText::class.constructor to TextElement.EmphasisText::class,
            TextElement.Highlight::class.constructor to TextElement.Highlight::class,
            TextElement.Paragraph::class.constructor to TextElement.Paragraph::class,
            Link.ReferenceElement::class.constructor to Link.ReferenceElement::class,
            TextElement.Strikethrough::class.constructor to TextElement.Strikethrough::class,
            TextElement.StrongText::class.constructor to TextElement.StrongText::class,
            TextElement.Subscript::class.constructor to TextElement.Subscript::class,
            TextElement.Superscript::class.constructor to TextElement.Superscript::class,
            TextElement.Title::class.constructor to TextElement.Title::class,
            TextElement.Emoji::class.constructor to TextElement.Emoji::class,
            TextElement.InlineCode::class.constructor to TextElement.InlineCode::class,
            Link.AutoLink::class.constructor to Link.AutoLink::class,
            TextElement.PlainText::class.constructor to TextElement.PlainText::class,
        )
        private val KClass<out MarktdownElement>.constructor
            get() = "${asClassName().simpleNames.joinToString(".")}("
    }

    fun create(output: Path, exclusion: Set<String> = setOf()) {
        for (fileSpec in rendererGenerator.generate(exclusion)) {
            fileSpec.writeTo(output)
        }
    }
}
