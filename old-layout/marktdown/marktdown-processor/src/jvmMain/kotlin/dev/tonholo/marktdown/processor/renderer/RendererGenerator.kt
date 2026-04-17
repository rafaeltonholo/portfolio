package dev.tonholo.marktdown.processor.renderer

import com.squareup.kotlinpoet.FileSpec
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.content.HorizontalRule
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
import dev.tonholo.marktdown.domain.content.TextElement.Paragraph
import dev.tonholo.marktdown.domain.content.TextElement.PlainText
import dev.tonholo.marktdown.domain.content.TextElement.Strikethrough
import dev.tonholo.marktdown.domain.content.TextElement.StrongText
import dev.tonholo.marktdown.domain.content.TextElement.Subscript
import dev.tonholo.marktdown.domain.content.TextElement.Superscript
import dev.tonholo.marktdown.domain.content.TextElement.Title
import dev.tonholo.marktdown.processor.extensions.fnName
import kotlin.reflect.KClass

private const val RENDERER_PACKAGE_SUFFIX = ".renderer"

abstract class RendererGenerator(
    packageName: String,
    protected val elementsToRender: Set<KClass<out MarktdownElement>>,
) {
    protected val packageName: String = if (packageName.endsWith(RENDERER_PACKAGE_SUFFIX)) {
        packageName
    } else {
        "$packageName$RENDERER_PACKAGE_SUFFIX"
    }

    fun generateElementMap(): Map<KClass<out MarktdownElement>, FileSpec> = buildMap {
        putAll(
            elementsToRender.mapNotNull {
                when (it) {
                    CodeFence::class -> it to CodeFence::class.createCodeFenceDefaultRenderer()
                    HorizontalRule::class -> it to HorizontalRule::class.createHorizontalRuleDefaultRenderer()
                    ImageElement::class -> it to ImageElement::class.createImageElementDefaultRenderer()
                    LineBreak::class -> it to LineBreak::class.createLineBreakDefaultRenderer()
                    LinkDefinition::class -> it to LinkDefinition::class.createLinkDefinitionDefaultRenderer()
                    TableElement::class -> it to TableElement::class.createTableElementDefaultRenderer()
                    ListItem::class -> it to ListItem::class.createListItemDefaultRenderer()
                    Ordered::class -> it to Ordered::class.createOrderedDefaultRenderer()
                    Task::class -> it to Task::class.createTaskDefaultRenderer()
                    Unordered::class -> it to Unordered::class.createUnorderedDefaultRenderer()
                    Blockquote::class -> it to Blockquote::class.createBlockquoteDefaultRenderer()
                    Element::class -> it to Element::class.createElementDefaultRenderer()
                    EmphasisText::class -> it to EmphasisText::class.createEmphasisTextDefaultRenderer()
                    Highlight::class -> it to Highlight::class.createHighlightDefaultRenderer()
                    Paragraph::class -> it to Paragraph::class.createParagraphDefaultRenderer()
                    ReferenceElement::class -> it to ReferenceElement::class.createReferenceElementDefaultRenderer()
                    Strikethrough::class -> it to Strikethrough::class.createStrikethroughDefaultRenderer()
                    StrongText::class -> it to StrongText::class.createStrongTextDefaultRenderer()
                    Subscript::class -> it to Subscript::class.createSubscriptDefaultRenderer()
                    Superscript::class -> it to Superscript::class.createSuperscriptDefaultRenderer()
                    Title::class -> it to Title::class.createTitleDefaultRenderer()
                    Emoji::class -> it to Emoji::class.createEmojiDefaultRenderer()
                    InlineCode::class -> it to InlineCode::class.createInlineCodeDefaultRenderer()
                    AutoLink::class -> it to AutoLink::class.createAutoLinkDefaultRenderer()
                    PlainText::class -> it to PlainText::class.createPlainTextDefaultRenderer()
                    TextElement.InlineHtml::class ->
                        it to TextElement.InlineHtml::class.createInlineHtmlDefaultRenderer()
                    else -> null
                }
            },
        )
    }

    fun generate(exclusion: Set<String>): List<FileSpec> {
        return buildList {
            addAll(
                generateElementMap()
                    .filterNot { it.key.fnName in exclusion }
                    .values
            )

            add(createMarktdownElementRenderer())
            add(createMarktdownDocumentRenderer())
        }
    }

    protected fun FileSpec.Builder.defaultIndent() = indent(" ".repeat(4))
    protected abstract fun createMarktdownDocumentRenderer(): FileSpec
    protected abstract fun createMarktdownElementRenderer(): FileSpec
    protected abstract fun KClass<out CodeFence>.createCodeFenceDefaultRenderer(): FileSpec
    protected abstract fun KClass<out HorizontalRule>.createHorizontalRuleDefaultRenderer(): FileSpec
    protected abstract fun KClass<out ImageElement>.createImageElementDefaultRenderer(): FileSpec
    protected abstract fun KClass<out LineBreak>.createLineBreakDefaultRenderer(): FileSpec
    protected abstract fun KClass<out LinkDefinition>.createLinkDefinitionDefaultRenderer(): FileSpec
    protected abstract fun KClass<out TableElement>.createTableElementDefaultRenderer(): FileSpec
    protected abstract fun KClass<out ListItem>.createListItemDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Ordered>.createOrderedDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Task>.createTaskDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Unordered>.createUnorderedDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Blockquote>.createBlockquoteDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Element>.createElementDefaultRenderer(): FileSpec
    protected abstract fun KClass<out EmphasisText>.createEmphasisTextDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Highlight>.createHighlightDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Paragraph>.createParagraphDefaultRenderer(): FileSpec
    protected abstract fun KClass<out ReferenceElement>.createReferenceElementDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Strikethrough>.createStrikethroughDefaultRenderer(): FileSpec
    protected abstract fun KClass<out StrongText>.createStrongTextDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Subscript>.createSubscriptDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Superscript>.createSuperscriptDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Title>.createTitleDefaultRenderer(): FileSpec
    protected abstract fun KClass<out Emoji>.createEmojiDefaultRenderer(): FileSpec
    protected abstract fun KClass<out InlineCode>.createInlineCodeDefaultRenderer(): FileSpec
    protected abstract fun KClass<out AutoLink>.createAutoLinkDefaultRenderer(): FileSpec
    protected abstract fun KClass<out PlainText>.createPlainTextDefaultRenderer(): FileSpec
    protected abstract fun KClass<out TextElement.InlineHtml>.createInlineHtmlDefaultRenderer(): FileSpec
}
