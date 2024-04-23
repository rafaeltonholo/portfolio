package dev.tonholo.marktdown.processor.visitor

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent
import dev.tonholo.marktdown.domain.Author
import dev.tonholo.marktdown.domain.MarktdownLink
import dev.tonholo.marktdown.domain.MarktdownMetadata
import dev.tonholo.marktdown.domain.MarktdownMetadataImpl
import dev.tonholo.marktdown.domain.MarktdownMetadataMap
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.content.EndOfLine
import dev.tonholo.marktdown.domain.content.HorizontalRule
import dev.tonholo.marktdown.domain.content.ImageElement
import dev.tonholo.marktdown.domain.content.LineBreak
import dev.tonholo.marktdown.domain.content.Link.AutoLink
import dev.tonholo.marktdown.domain.content.Link.ReferenceElement
import dev.tonholo.marktdown.domain.content.LinkDefinition
import dev.tonholo.marktdown.domain.content.ListElement
import dev.tonholo.marktdown.domain.content.MarktdownDocument
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.processor.extensions.pascalCase
import java.nio.file.Path
import java.util.*
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toLocalDateTime
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.CompositeASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.ast.accept
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.ast.impl.ListCompositeNode
import org.intellij.markdown.ast.visitors.RecursiveVisitor
import org.intellij.markdown.flavours.gfm.GFMElementTypes
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.html.ReferenceLinksGeneratingProvider
import org.intellij.markdown.html.entities.EntityConverter
import org.intellij.markdown.html.makeXssSafeDestination
import org.intellij.markdown.parser.LinkMap
import kotlin.reflect.KClass
import dev.tonholo.marktdown.domain.content.Link.Element as LinkElement

class MarktdownRenderer(
    private val packageName: String,
    private val fileName: String,
    private val content: String,
    private val useSafeLinks: Boolean = true,
    private val absolutizeAnchorLinks: Boolean = false,
    private val frontMatterMetadata: Map<String, Any>?,
) {
    private val visitor = MarktdownVisitor()
    private lateinit var metadataSpec: MarktdownMetadata
    private val childrenSpec: MutableList<CodeBlock.Builder> = mutableListOf()
    private val linkDefinitionSpec: MutableList<CodeBlock.Builder> = mutableListOf()
    private lateinit var referenceLinksGeneratingProvider: ReferenceLinksGeneratingProvider
    private val rootPropertySpec: PropertySpec
        get() {
            return PropertySpec
                .builder(
                    name = fileName.pascalCase(),
                    type = MarktdownDocument::class,
                )
                .initializer(
                    buildCodeBlock {
                        val className = MarktdownDocument::class.asClassName()
                        addStatement("%T(", MarktdownDocument::class)
                        withIndent {
                            if (::metadataSpec.isInitialized) {
                                add(
                                    metadataSpec
                                        .build(className.member(MarktdownDocument::metadata.name))
                                )
                            } else {
                                addStatement(
                                    "%N = null,",
                                    className.member(MarktdownDocument::metadata.name),
                                )
                            }
                            add(
                                childrenSpec.toList().toCodeBlock(
                                    memberName = className.member(MarktdownDocument::children.name),
                                ) {
                                    it.build()
                                }
                            )
                            if (linkDefinitionSpec.isNotEmpty()) {
                                add(
                                    linkDefinitionSpec.toList().toCodeBlock(
                                        memberName = className.member(MarktdownDocument::linkDefinitions.name),
                                    ) {
                                        it.build()
                                    }
                                )
                            }
                        }
                        addStatement(")")
                    },
                )
                .build()
        }

    private val fileSpec
        get() = FileSpec
            .builder(
                packageName = packageName,
                fileName = fileName.pascalCase(),
            )
            .addProperty(rootPropertySpec)
            .build()

    fun render(node: ASTNode?, output: Appendable?) {
        requireNotNull(node) {
            "The node parameter should not be null"
        }
        requireNotNull(output) {
            "The output parameter should not be null"
        }
        node.accept(visitor)
        frontMatterMetadata
            ?.mapValues { (_, value) ->
                when (value) {
                    is Date -> Instant
                        .fromEpochMilliseconds(value.time)
                        .toLocalDateTime(FixedOffsetTimeZone(UtcOffset.ZERO))

                    else -> value
                }
            }
            ?.let { map ->
                val metadata = MarktdownMetadataMap(map)
                metadataSpec = metadata
            }
        fileSpec.writeTo(output)
    }

    fun render(node: ASTNode?): String {
        requireNotNull(node) {
            "The node parameter should not be null"
        }
        return buildString { render(node, this) }
    }

    fun render(node: ASTNode, output: Path) {
        node.accept(visitor)
        println("render output = $output")
        fileSpec.writeTo(output)
    }

    inner class MarktdownVisitor : RecursiveVisitor() {
        private var currentBuilder: CodeBlock.Builder? = null
        private val listMemberName = MemberName(packageName = "kotlin.collections", simpleName = "listOf")
        private val ASTNode.literal get() = getTextInNode(content)

        private var isStrikeThroughOpen = false
        private val strikeThroughChildren = mutableListOf<CodeBlock.Builder>()
        private val ASTNode.singleChild: ASTNode?
            get() = childrenToRender.singleOrNull()

        private val List<ASTNode>.codeBlockContent: String
            get() = asSequence()
                .filter {
                    it.type == MarkdownTokenTypes.CODE_FENCE_CONTENT ||
                        it.type == MarkdownTokenTypes.CODE_LINE ||
                        it.type == MarkdownTokenTypes.EOL
                }
                .fold(initial = "") { content, child ->
                    content + when (child.type) {
                        MarkdownTokenTypes.CODE_FENCE_CONTENT -> EntityConverter.replaceEntities(
                            text = child.literal,
                            processEntities = false,
                            processEscapes = false,
                        )

                        MarkdownTokenTypes.CODE_LINE ->
                            HtmlGenerator.trimIndents(
                                text = EntityConverter.replaceEntities(
                                    text = child.literal,
                                    processEntities = false,
                                    processEscapes = false,
                                ),
                                indent = 4,
                            )

                        else -> "\n"
                    }
                }
                .trimStart('\n') // FENCE_LANG adds a EOL that adds an extra \n at the beginning.
                .trim('\n') // CODE_FENCE starts with EOL and ends with EOL event if it doesn't explicit have it.

        private val CharSequence.linkDestination: CharSequence
            get() =
                LinkMap.normalizeDestination(this, false).let {
                    if (useSafeLinks) makeXssSafeDestination(it) else it
                }

        private val CharSequence.escaped: CharSequence
            get() =
                EntityConverter.replaceEntities(
                    subSequence(1, lastIndex),
                    processEntities = true,
                    processEscapes = false
                )

        private fun attachToParent(current: CodeBlock.Builder) {
            if (isStrikeThroughOpen) {
                strikeThroughChildren.add(current)
            } else {
                fun add(current: CodeBlock.Builder) {
                    currentBuilder?.add(current.build())
                        ?: childrenSpec.add(current)
                }
                add(current)
            }
        }

        override fun visitNode(node: ASTNode) {
            println("--- start Visit node ---")
            println("node: ${node.type}")
            println("node.type: ${node.type}")
            println("node.startOffset: ${node.startOffset}")
            println("node.endOffset: ${node.endOffset}")
            println("node.parent: ${node.parent?.type}")
            println("node.children: ${node.children.map { it.type }}")
            println("node.litreal: ${node.literal}")
            println("--- end Visit node ---")

//            super.visitNode(node)
            when (node.type) {
                // Elements
                MarkdownElementTypes.MARKDOWN_FILE -> {
                    referenceLinksGeneratingProvider = ReferenceLinksGeneratingProvider(
                        linkMap = LinkMap.buildLinkMap(node, content),
                        baseURI = null,
                        absolutizeAnchorLinks,
                    )
                    visitChildren(parent = node)
                }

                MarkdownElementTypes.UNORDERED_LIST -> visitUnorderedList(node)
                MarkdownElementTypes.ORDERED_LIST -> visitOrderedList(node)
                MarkdownElementTypes.LIST_ITEM -> visitListItem(node)
                MarkdownElementTypes.BLOCK_QUOTE -> visitBlockQuote(node)
                MarkdownElementTypes.CODE_FENCE -> visitCodeFence(node)
                MarkdownElementTypes.CODE_BLOCK -> visitCodeBlock(node)
                MarkdownElementTypes.CODE_SPAN -> visitCodeSpan(code = node)
                MarkdownElementTypes.HTML_BLOCK -> Unit
                MarkdownElementTypes.PARAGRAPH -> visitParagraph(node)
                MarkdownElementTypes.EMPH -> visitEmphasis(node)
                MarkdownElementTypes.STRONG -> visitStrong(node)
                MarkdownElementTypes.LINK_DEFINITION -> visitLinkDefinition(node)
                MarkdownElementTypes.LINK_LABEL -> Unit
                MarkdownElementTypes.LINK_DESTINATION -> Unit
                MarkdownElementTypes.LINK_TITLE -> Unit
                MarkdownElementTypes.LINK_TEXT -> Unit
                MarkdownElementTypes.INLINE_LINK -> visitInlineLink(node)
                MarkdownElementTypes.FULL_REFERENCE_LINK,
                MarkdownElementTypes.SHORT_REFERENCE_LINK -> visitReferenceLink(node)

                MarkdownElementTypes.IMAGE -> visitImage(node)
                MarkdownElementTypes.AUTOLINK -> visitAutoLink(node)
                MarkdownElementTypes.SETEXT_1,
                MarkdownElementTypes.SETEXT_2,
                MarkdownElementTypes.ATX_1,
                MarkdownElementTypes.ATX_2,
                MarkdownElementTypes.ATX_3,
                MarkdownElementTypes.ATX_4,
                MarkdownElementTypes.ATX_5,
                MarkdownElementTypes.ATX_6 -> visitHeading(node)

                GFMElementTypes.STRIKETHROUGH -> visitStrikeThrough(node)

                // Tokens
                MarkdownTokenTypes.TEXT -> visitText(node)
                MarkdownTokenTypes.CODE_LINE -> Unit
                MarkdownTokenTypes.BLOCK_QUOTE -> Unit
                MarkdownTokenTypes.HTML_BLOCK_CONTENT -> Unit
                MarkdownTokenTypes.SINGLE_QUOTE -> Unit
                MarkdownTokenTypes.DOUBLE_QUOTE -> Unit
                MarkdownTokenTypes.LPAREN -> Unit
                MarkdownTokenTypes.RPAREN -> Unit
                MarkdownTokenTypes.LBRACKET -> Unit
                MarkdownTokenTypes.RBRACKET -> Unit
                MarkdownTokenTypes.LT -> Unit
                MarkdownTokenTypes.GT -> Unit
                MarkdownTokenTypes.COLON -> Unit
                MarkdownTokenTypes.EXCLAMATION_MARK -> Unit
                MarkdownTokenTypes.HARD_LINE_BREAK -> visitHardLineBreak()
                MarkdownTokenTypes.EOL -> visitEndOfLine()
                MarkdownTokenTypes.LINK_ID -> Unit
                MarkdownTokenTypes.ATX_HEADER -> Unit
                MarkdownTokenTypes.ATX_CONTENT -> Unit
                MarkdownTokenTypes.SETEXT_1 -> Unit
                MarkdownTokenTypes.SETEXT_2 -> Unit
                MarkdownTokenTypes.SETEXT_CONTENT -> Unit
                MarkdownTokenTypes.EMPH -> Unit
                MarkdownTokenTypes.BACKTICK -> Unit
                MarkdownTokenTypes.ESCAPED_BACKTICKS -> Unit
                MarkdownTokenTypes.LIST_BULLET -> Unit
                MarkdownTokenTypes.URL -> Unit
                MarkdownTokenTypes.HORIZONTAL_RULE -> visitHorizontalRule()
                MarkdownTokenTypes.LIST_NUMBER -> Unit
                MarkdownTokenTypes.FENCE_LANG -> Unit
                MarkdownTokenTypes.CODE_FENCE_START -> Unit
                MarkdownTokenTypes.CODE_FENCE_CONTENT -> Unit
                MarkdownTokenTypes.CODE_FENCE_END -> Unit
                MarkdownTokenTypes.LINK_TITLE -> Unit
                MarkdownTokenTypes.AUTOLINK -> Unit
                MarkdownTokenTypes.EMAIL_AUTOLINK -> Unit
                MarkdownTokenTypes.HTML_TAG -> Unit
                MarkdownTokenTypes.BAD_CHARACTER -> Unit
                MarkdownTokenTypes.WHITE_SPACE -> {
                    attachToParent(" ".toCodeBlockBuilder())
                }
            }
        }

        private fun visitHeading(node: ASTNode) {
            val parent = currentBuilder
            val kClass = TextElement.Title::class
            val level = node.type.name.dropWhile { it != '_' }.drop(1)
            val childrenToConsider = node.children
                .filter { it.type == MarkdownTokenTypes.ATX_CONTENT || it.type == MarkdownTokenTypes.SETEXT_CONTENT }
                .flatMap { it.childrenToRender }
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %T.H%L,",
                        kClass.member(TextElement.Title::style.name),
                        TextElement.Title.Style::class,
                        level,
                    )
                    val child = childrenToConsider.singleOrNull()
                    if (child != null && child.type == MarkdownTokenTypes.TEXT) {
                        addStatement("text = %S,", child.literal)
                    } else {
                        println("Visiting children ${childrenToConsider.map { it.type }}")
                        visitChildren(
                            children = childrenToConsider,
                            parentBuilder = this,
                            memberName = kClass.member(TextElement.Title::children.name),
                        )
                    }
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun visitText(node: ASTNode) {
            if (node.parent?.type == MarkdownElementTypes.LINK_TEXT && node.literal in linkBoundaries) {
                return
            }
            val text = node.literal.toString()
            attachToParent(text.toCodeBlockBuilder())
        }

        private fun visitCodeSpan(code: ASTNode) {
            val kClass = TextElement.InlineCode::class
            val nodes = code.children.subList(1, code.children.size - 1)
            val output = nodes.joinToString(separator = "") {
                EntityConverter.replaceEntities(text = it.literal, processEntities = false, processEscapes = false)
            }.trim()
            val codeBlock = CodeBlock
                .builder()
                .addStatement(
                    "%T(%N = %S),",
                    kClass,
                    kClass.member(TextElement.InlineCode::code.name),
                    output,
                )
            attachToParent(codeBlock)
        }

        private fun visitStrikeThrough(node: ASTNode) {
            val parent = currentBuilder
            val kClass = TextElement.Strikethrough::class
            val childrenToConsider = node.childrenToRender.filterNot { it.type == GFMTokenTypes.TILDE }
            val codeBlock = CodeBlock
                .builder()
                .apply {
                    val child = childrenToConsider.singleOrNull()
                    when {
                        child != null && child.type == MarkdownTokenTypes.TEXT -> {
                            addStatement("%T(%S),", kClass, child.literal)
                        }

                        else -> {
                            addStatement("%T(", kClass)
                            withIndent {
                                visitChildren(
                                    children = childrenToConsider,
                                    parentBuilder = this,
                                    memberName = kClass.member(TextElement.Strikethrough::children.name),
                                )
                            }
                            addStatement("),")
                        }
                    }
                }

            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun visitEmphasis(emphasis: ASTNode) {
            val parent = currentBuilder
            // Each strong follows the structure:
            //   - 1 EMPH elements
            //   - 1 CONTENT element
            //   - 1 EMPH elements
            // Here we extract only the content to use.
            val childrenToConsider = emphasis.childrenToRender
                .drop(1)
                .take(1)
            val codeBlock = CodeBlock
                .builder()
                .apply {
                    val child = childrenToConsider.singleOrNull()
                    if (child != null && child.type == MarkdownTokenTypes.TEXT) {
                        addStatement("%T(%S),", TextElement.EmphasisText::class, child.literal)
                    } else {
                        addStatement("%T(", TextElement.EmphasisText::class)
                        withIndent {
                            visitChildren(
                                children = childrenToConsider,
                                parentBuilder = this,
                                memberName = TextElement.EmphasisText::class.member(TextElement.EmphasisText::children.name),
                            )
                        }
                        addStatement("),")
                    }
                }

            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun visitStrong(strongEmphasis: ASTNode) {
            val parent = currentBuilder
            // Each strong follows the structure:
            //   - 2 EMPH elements
            //   - 1 CONTENT element
            //   - 2 EMPH elements
            // Here we extract only the content to use.
            val childrenToConsider = strongEmphasis.childrenToRender
                .drop(2)
                .take(1)
            val codeBlock = CodeBlock
                .builder()
                .apply {
                    val child = childrenToConsider.singleOrNull()
                    if (child != null && child.type == MarkdownTokenTypes.TEXT) {
                        addStatement("%T(%S),", TextElement.StrongText::class, child.literal)
                    } else {
                        addStatement("%T(", TextElement.StrongText::class)
                        withIndent {
                            visitChildren(
                                children = childrenToConsider,
                                parentBuilder = this,
                                memberName = TextElement.StrongText::class.member(TextElement.StrongText::children.name),
                            )
                        }
                        addStatement("),")
                    }
                }

            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun visitAutoLink(autoLink: ASTNode) {
            val linkText = autoLink.literal
            val kClass = AutoLink::class
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %T(%S),",
                        kClass.member(AutoLink::link.name),
                        MarktdownLink::class,
                        linkText.linkDestination,
                    )
                    addStatement("%N = %S,", kClass.member(AutoLink::label.name), linkText.escaped)
                }
                .addStatement("),")
            attachToParent(codeBlock)
        }

        private fun visitInlineLink(link: ASTNode) {
            val kClass = LinkElement::class
            val parent = currentBuilder
            val destination = link.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
            val childrenToConsider = link.children
                .filter { it.type == MarkdownElementTypes.LINK_TEXT }
                .flatMap {
                    if (it is CompositeASTNode) {
                        it.childrenToRender
                    } else {
                        listOf(it)
                    }
                }
            val child = childrenToConsider.singleOrNull()
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %T(%S),",
                        kClass.member(LinkElement::link.name),
                        MarktdownLink::class,
                        destination?.literal?.toString().orEmpty(),
                    )
                    if (child != null && child.type == MarkdownTokenTypes.TEXT) {
                        addStatement("%S,", child.literal.escaped)
                    } else {
                        visitChildren(
                            children = childrenToConsider,
                            parentBuilder = this,
                            memberName = kClass.member(LinkElement::children.name),
                        )
                    }
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun visitReferenceLink(node: ASTNode) {
            referenceLinksGeneratingProvider.getRenderInfo(
                text = content,
                node = node,
            )?.let { info ->
                val kClass = ReferenceElement::class
                val parent = currentBuilder
                val childrenToConsider = info.label.childrenToRender
                val child = childrenToConsider.singleOrNull()
                val codeBlock = CodeBlock
                    .builder()
                    .addStatement("%T(", kClass)
                    .withIndent {
                        addStatement(
                            "%N = %T(%S),",
                            kClass.member(ReferenceElement::destination.name),
                            MarktdownLink::class,
                            info.destination,
                        )
                        addStatement("%N = %S,", kClass.member(ReferenceElement::title.name), info.title)
                        if (child != null && child.type == MarkdownTokenTypes.TEXT) {
                            addStatement("%S,", child.literal.escaped)
                        } else {
                            visitChildren(
                                children = childrenToConsider,
                                parentBuilder = this,
                                memberName = kClass.member(ReferenceElement::children.name),
                            )
                        }
                    }
                    .addStatement("),")
                currentBuilder = parent
                attachToParent(codeBlock)
            } ?: println("info is null") // TODO.
        }

        private fun visitLinkDefinition(linkDefinition: ASTNode) {
            val kClass = LinkDefinition::class
            val parent = currentBuilder
            val label = linkDefinition
                .findChildOfType(MarkdownElementTypes.LINK_LABEL)
                ?.literal
                ?: return
            val destination = linkDefinition
                .findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
                ?.literal
                ?: return
            val title = linkDefinition
                .findChildOfType(MarkdownElementTypes.LINK_TITLE)
                ?.literal

            val codeBlock = CodeBlock.builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %T(%S),",
                        kClass.member(LinkDefinition::destination.name),
                        MarktdownLink::class,
                        destination.linkDestination,
                    )
                    addStatement(
                        "%N = %S,",
                        kClass.member(LinkDefinition::label.name),
                        label.escaped,
                    )
                    title?.let {
                        addStatement(
                            "%N = %S,",
                            kClass.member(LinkDefinition::title.name),
                            it.escaped,
                        )
                    }
                }
                .addStatement("),")
            currentBuilder = parent
            linkDefinitionSpec.add(codeBlock)
        }

        private fun visitParagraph(paragraph: ASTNode) {
            val parent = currentBuilder
            val kClass = TextElement.Paragraph::class
            val codeBlock = CodeBlock
                .builder()
                .apply {
                    val child = paragraph.singleChild
                    if (child != null && child.type == MarkdownTokenTypes.TEXT) {
                        addStatement("%T(%S),", kClass, child.literal)
                    } else {
                        addStatement("%T(", kClass)
                        withIndent {
                            visitChildren(
                                parent = paragraph,
                                parentBuilder = this,
                                memberName = kClass.member(TextElement.Paragraph::children.name),
                            )
                        }
                        addStatement("),")
                    }
                }
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun createCodeFence(language: String?, content: String) {
            val kClass = CodeFence::class
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %S,",
                        kClass.member(CodeFence::language.name),
                        language,
                    )
                    addStatement(
                        "%N = %S,",
                        kClass.member(CodeFence::code.name),
                        content,
                    )
                }
                .addStatement("),")
            attachToParent(codeBlock)
        }

        private fun visitCodeFence(codeFence: ASTNode) {
            val language = codeFence.children
                .firstOrNull {
                    it.type == MarkdownTokenTypes.FENCE_LANG
                }
                ?.literal

            createCodeFence(
                language = language.toString(),
                content = codeFence.children.codeBlockContent
            )
        }

        private fun visitCodeBlock(node: ASTNode) {
            createCodeFence(
                language = null,
                content = node.children.codeBlockContent,
            )
        }

        private fun visitList(
            list: ASTNode,
            kClass: KClass<*>,
            memberName: MemberName,
            extra: CodeBlock.Builder.() -> Unit = {},
        ) {
            val parent = currentBuilder
            val childrenToConsider = list
                .children
                .filter { it.type == MarkdownElementTypes.LIST_ITEM }
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    extra()
                    visitChildren(
                        children = childrenToConsider,
                        parentBuilder = this,
                        memberName = memberName,
                    )
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun visitOrderedList(orderedList: ASTNode) {
            val kClass = ListElement.Ordered::class
            visitList(
                list = orderedList,
                kClass = kClass,
                memberName = kClass.member(ListElement.Ordered::options.name),
            ) {
                orderedList.findChildOfType(MarkdownElementTypes.LIST_ITEM)
                    ?.findChildOfType(MarkdownTokenTypes.LIST_NUMBER)
                    ?.literal
                    ?.toString()
                    ?.trim()
                    ?.let {
                        val number = it
                            .substring(0, it.length - 1)
                            .trimStart('0')
                            .toIntOrNull()
                        if (number != null && number > 1) {
                            addStatement(
                                "%N = %L,",
                                kClass.member(ListElement.Ordered::startWith.name),
                                number,
                            )
                        }
                    }
            }
        }

        private fun visitUnorderedList(unorderedList: ASTNode) {
            visitList(
                list = unorderedList,
                kClass = ListElement.Unordered::class,
                memberName = ListElement.Unordered::class.member(ListElement.Unordered::options.name),
            )
        }

        private fun visitListItem(listItem: ASTNode) {
            // TODO: Deal with indent.
            val parent = currentBuilder
            val listNode = listItem.parent
            assert(listNode is ListCompositeNode)

            val isLoose = (listNode as ListCompositeNode).loose
            val childToConsider = listItem
                .childrenToRender
                .flatMap { child ->
                    if (child.type == MarkdownElementTypes.PARAGRAPH && !isLoose) {
                        child.childrenToRender
                    } else {
                        listOf(child)
                    }
                }
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", ListElement.ListItem::class)
                .withIndent {
                    visitChildren(
                        children = childToConsider,
                        parentBuilder = this,
                        memberName = ListElement.ListItem::class.member(ListElement.ListItem::children.name),
                    )
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun visitBlockQuote(blockQuote: ASTNode) {
            val parent = currentBuilder
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", TextElement.Blockquote::class)
                .withIndent {
                    visitChildren(
                        parent = blockQuote,
                        parentBuilder = this,
                        memberName = TextElement.Blockquote::class.member(TextElement.Blockquote::children.name),
                    )
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        private fun visitHorizontalRule() {
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T,", HorizontalRule::class)
            attachToParent(codeBlock)
        }

        private fun visitHardLineBreak() {
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T,", LineBreak::class)
            attachToParent(codeBlock)
        }

        private fun visitEndOfLine() {
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T,", EndOfLine::class)
            attachToParent(codeBlock)
        }

        private fun visitImage(image: ASTNode) {
            val kClass = ImageElement::class
            val link = image
                .findChildOfType(MarkdownElementTypes.INLINE_LINK)
                ?.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)
                ?.literal
                ?: return
            val alt = image
                .findChildOfType(MarkdownElementTypes.INLINE_LINK)
                ?.findChildOfType(MarkdownElementTypes.LINK_TEXT)
                ?.literal
                ?: return

            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %T(%S),",
                        kClass.member(ImageElement::link.name),
                        MarktdownLink::class,
                        link.linkDestination,
                    )
                    addStatement(
                        "%N = %S,",
                        kClass.member(ImageElement::alt.name),
                        alt.escaped,
                    )
                }
                .addStatement("),")
            attachToParent(codeBlock)
        }

        private fun visitChildren(parent: ASTNode) {
            if (parent is CompositeASTNode) {
                for (child in parent.childrenToRender) {
                    visitNode(child)
                }
            }
        }

        private fun CodeBlock.Builder.visitChildren(
            parent: ASTNode,
            parentBuilder: CodeBlock.Builder,
            memberName: MemberName,
        ) {
            addStatement(
                "%N = %M(",
                memberName,
                listMemberName,
            )
            currentBuilder = parentBuilder
            withIndent {
                visitChildren(parent)
            }
            addStatement("),")
        }

        private fun CodeBlock.Builder.visitChildren(
            children: List<ASTNode>,
            parentBuilder: CodeBlock.Builder,
            memberName: MemberName,
        ) {
            check(children.isNotEmpty())
            currentBuilder = parentBuilder

            if (children.size > 1) {
                addStatement(
                    "%N = %M(",
                    memberName,
                    listMemberName,
                )
                withIndent {
                    for (child in children) {
                        println("Visiting child.type = ${child.type}")
                        visitNode(child)
                    }
                }
                addStatement("),")
            } else {
                visitNode(children.single())
            }
        }

        private fun String.toCodeBlockBuilder(): CodeBlock.Builder = CodeBlock
            .builder()
            .addStatement("%T(%S),", TextElement.PlainText::class, this)
    }
}

private val textTokens = setOf(
    MarkdownTokenTypes.TEXT,
    MarkdownTokenTypes.WHITE_SPACE,
    MarkdownTokenTypes.SINGLE_QUOTE,
    MarkdownTokenTypes.DOUBLE_QUOTE,
    MarkdownTokenTypes.LPAREN,
    MarkdownTokenTypes.RPAREN,
    MarkdownTokenTypes.LBRACKET,
    MarkdownTokenTypes.RBRACKET,
    MarkdownTokenTypes.LT,
    MarkdownTokenTypes.GT,
    MarkdownTokenTypes.COLON,
    MarkdownTokenTypes.EXCLAMATION_MARK,
)

private val linkBoundaries = setOf(
    MarkdownTokenTypes.LBRACKET.name,
    MarkdownTokenTypes.RBRACKET.name,
)

private val ASTNode.childrenToRender: List<ASTNode>
    get() {
        return if (children.size > 1 && children.any { it.type == MarkdownTokenTypes.TEXT }) {
            println("folding text elements")
            val newChildren = mutableListOf<ASTNode>()
            val iterator = children.iterator()
            var startTextOffset: Int? = null
            var endTextOffset: Int? = null
            while (iterator.hasNext()) {
                val current = iterator.next()
                when {
                    current.type in textTokens && startTextOffset == null -> {
                        startTextOffset = current.startOffset
                        endTextOffset = current.endOffset
                    }

                    current.type in textTokens && startTextOffset != null -> {
                        endTextOffset = current.endOffset
                    }

                    else -> {
                        if (startTextOffset != null && endTextOffset != null) {
                            newChildren += LeafASTNode(
                                type = MarkdownTokenTypes.TEXT,
                                startOffset = startTextOffset,
                                endOffset = endTextOffset,
                            ).apply {
                                // Well... This is quite hacky, maybe a crime.
                                // I wouldn't want to do that, but I need to attach the new
                                // Text node to the parent, and without it, I would need to
                                // recreate the parent just to attach that node.
                                // This is error prone. If the API changes, that might be
                                // problematic, however, for now, it works.
                                // Please, don't be mad.
                                @Suppress(
                                    "INVISIBLE_SETTER",
                                )
                                parent = this@childrenToRender
                            }
                            startTextOffset = null
                            endTextOffset = null
                        }
                        newChildren += current
                    }
                }
            }

            if (startTextOffset != null && endTextOffset != null) {
                newChildren += LeafASTNode(
                    type = MarkdownTokenTypes.TEXT,
                    startOffset = startTextOffset,
                    endOffset = endTextOffset,
                )
            }
            println("Folded children = ${newChildren.map { it.type }}")
            println("children.size = ${children.size}")
            println("newChildren.size = ${newChildren.size}")
            newChildren
        } else {
            children
        }
    }

private fun MarktdownMetadata.build(memberName: MemberName): CodeBlock {
    val className = MarktdownMetadataImpl::class.asClassName()
    return buildCodeBlock {
        addStatement("%N = %T(", memberName, className)
        withIndent {
            addStatement(
                "%N = %S,",
                className.member(MarktdownMetadata::documentTitle.name),
                documentTitle
            )
            add(
                authors.toCodeBlock(className.member(MarktdownMetadata::authors.name)) {
                    it.toCodeBlock()
                }
            )
            addStatement(
                "%N = %L,",
                className.member(MarktdownMetadata::publishedDateTime.name),
                publishedDateTime.toCodeBlock()
            )
        }
        addStatement("),")
    }
}

private fun Author.toCodeBlock(): CodeBlock {
    val author = this
    return buildCodeBlock {
        val className = author::class.asClassName()
        addStatement("%T(", className)
        withIndent {
            addStatement("%N = %S,", className.member(author::name.name), author.name)
            if (author.links.isNotEmpty()) {
                add(
                    author.links.toCodeBlock(className.member(author::links.name)) {
                        it.toCodeBlock()
                    }
                )
            }
            author.avatar?.let { avatar ->
                addStatement(
                    "%N = %T(%S),",
                    className.member(author::avatar.name),
                    avatar::class,
                    avatar.value,
                )
            }
        }
        addStatement("),")
    }
}

private fun MarktdownLink.toCodeBlock(): CodeBlock = buildCodeBlock {
    addStatement(
        "%T(%S),",
        MarktdownLink::class,
        value,
    )
}

private inline fun <reified T> List<T>.toCodeBlock(memberName: MemberName, transform: (T) -> CodeBlock): CodeBlock {
    val listMemberName = MemberName(packageName = "kotlin.collections", simpleName = "listOf")
    return buildCodeBlock {
        addStatement("%N = %M(", memberName, listMemberName)
        withIndent {
            forEach {
                add(transform(it))
            }
        }
        addStatement("),")
    }
}

private fun LocalDateTime.toCodeBlock(): CodeBlock {
    val localDateTimeClassName = LocalDateTime::class.asClassName()
    val localDateTimeCompanionClassName = LocalDateTime.Companion::class.asClassName()
    return CodeBlock.of(
        "%T.%N(%S)",
        localDateTimeClassName,
        localDateTimeCompanionClassName.member("parse"),
        this,
    )
}
