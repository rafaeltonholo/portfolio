package dev.tonholo.marktdown.processor.visitor.commonMark

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
import dev.tonholo.marktdown.domain.MarktdownTag
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.content.HorizontalRule
import dev.tonholo.marktdown.domain.content.ImageElement
import dev.tonholo.marktdown.domain.content.LineBreak
import dev.tonholo.marktdown.domain.content.LinkDefinition
import dev.tonholo.marktdown.domain.content.ListElement
import dev.tonholo.marktdown.domain.content.MarktdownDocument
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.processor.extensions.pascalCase
import java.nio.file.Path
import kotlinx.datetime.LocalDateTime
import org.commonmark.ext.front.matter.YamlFrontMatterBlock
import org.commonmark.ext.front.matter.YamlFrontMatterNode
import org.commonmark.ext.gfm.tables.TableBlock
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.BlockQuote
import org.commonmark.node.BulletList
import org.commonmark.node.Code
import org.commonmark.node.CustomBlock
import org.commonmark.node.CustomNode
import org.commonmark.node.Document
import org.commonmark.node.Emphasis
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.HardLineBreak
import org.commonmark.node.Heading
import org.commonmark.node.HtmlBlock
import org.commonmark.node.HtmlInline
import org.commonmark.node.Image
import org.commonmark.node.IndentedCodeBlock
import org.commonmark.node.Link
import org.commonmark.node.LinkReferenceDefinition
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text
import org.commonmark.node.ThematicBreak
import org.commonmark.renderer.Renderer
import dev.tonholo.marktdown.domain.content.Link.Element as LinkElement

class MarktdownRenderer(
    private val packageName: String,
    private val fileName: String,
) : Renderer {
    private val visitor = MarktdownVisitor()
    private lateinit var metadataSpec: MarktdownMetadataImpl
    private val childrenSpec: MutableList<CodeBlock.Builder> = mutableListOf()
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

    override fun render(node: Node?, output: Appendable?) {
        requireNotNull(node) {
            "The node parameter should not be null"
        }
        requireNotNull(output) {
            "The output parameter should not be null"
        }
        node.accept(visitor)
        fileSpec.writeTo(output)
    }

    override fun render(node: Node?): String {
        requireNotNull(node) {
            "The node parameter should not be null"
        }
        return buildString { render(node, this) }
    }

    fun render(node: Node, output: Path) {
        node.accept(visitor)
        println("render output = $output")
        fileSpec.writeTo(output)
    }

    inner class MarktdownVisitor : AbstractVisitor() {
        private var currentBuilder: CodeBlock.Builder? = null
        private val listMemberName = MemberName(packageName = "kotlin.collections", simpleName = "listOf")
        private val Node.singleChild: Node?
            get() = if (firstChild == lastChild) firstChild else null

        private fun attachToParent(current: CodeBlock.Builder) {
            currentBuilder?.add(current.build())
                ?: childrenSpec.add(current)
        }

        private fun CodeBlock.Builder.visitChildren(
            parent: Node,
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

        override fun visit(blockQuote: BlockQuote) {
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

        override fun visit(bulletList: BulletList) {
            val parent = currentBuilder
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", ListElement.Unordered::class)
                .withIndent {
                    visitChildren(
                        parent = bulletList,
                        parentBuilder = this,
                        memberName = ListElement.Unordered::class.member(ListElement.Unordered::children.name),
                    )
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        override fun visit(code: Code) {
            val kClass = TextElement.InlineCode::class
            val codeBlock = CodeBlock
                .builder()
                .addStatement(
                    "%T(%N = %S),",
                    kClass,
                    kClass.member(TextElement.InlineCode::code.name),
                    code.literal,
                )
            attachToParent(codeBlock)
        }

        override fun visit(document: Document) {
            println("document=${document}")
            super.visit(document)
        }

        override fun visit(emphasis: Emphasis) {
            val parent = currentBuilder
            val codeBlock = CodeBlock
                .builder()
                .apply {
                    val child = emphasis.singleChild
                    if (child != null && child is Text) {
                        addStatement("%T(%S),", TextElement.EmphasisText::class, child.literal)
                    } else {
                        addStatement("%T(", TextElement.EmphasisText::class)
                        withIndent {
                            visitChildren(
                                parent = emphasis,
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

        override fun visit(fencedCodeBlock: FencedCodeBlock) {
            val kClass = CodeFence::class
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %S,",
                        kClass.member(CodeFence::language.name),
                        fencedCodeBlock.info,
                    )
                    addStatement(
                        "%N = %S,",
                        kClass.member(CodeFence::code.name),
                        fencedCodeBlock.literal,
                    )
                }
                .addStatement("),")
            attachToParent(codeBlock)
        }

        override fun visit(hardLineBreak: HardLineBreak) {
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T,", LineBreak::class)
                .indent()

            attachToParent(codeBlock)
        }

        override fun visit(heading: Heading) {
            val parent = currentBuilder
            val kClass = TextElement.Title::class
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %T.H%L,",
                        kClass.member(TextElement.Title::style.name),
                        TextElement.Title.Style::class,
                        heading.level,
                    )
                    val child = heading.singleChild
                    if (child != null && child is Text) {
                        addStatement("text = %S,", child.literal)
                    } else {
                        visitChildren(
                            parent = heading,
                            parentBuilder = this,
                            memberName = kClass.member(TextElement.Title::children.name),
                        )
                    }
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        override fun visit(thematicBreak: ThematicBreak) {
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T,", HorizontalRule::class)
            attachToParent(codeBlock)
        }

        override fun visit(htmlInline: HtmlInline) {
            // TODO??
            println("htmlInline=${htmlInline.literal}")
            super.visit(htmlInline)
        }

        override fun visit(htmlBlock: HtmlBlock) {
            // TODO??
            println("htmlBlock=${htmlBlock.literal}")
            super.visit(htmlBlock)
        }

        override fun visit(image: Image) {
            val kClass = ImageElement::class
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    add(
                        "%N = %T(%S),",
                        kClass.member(ImageElement::link.name),
                        MarktdownLink::class,
                        image.destination,
                    )
                    add(
                        "%N = %S,",
                        kClass.member(ImageElement::link.name),
                        image.title,
                    )
                }
                .addStatement("),")
            attachToParent(codeBlock)
        }

        override fun visit(indentedCodeBlock: IndentedCodeBlock) {
            val kClass = TextElement.InlineCode::class
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = \"\",",
                        kClass.member(CodeFence::language.name),
                    )
                    addStatement(
                        "%N = %S,",
                        kClass.member(CodeFence::code.name),
                        indentedCodeBlock.literal,
                    )
                }
                .addStatement("),")
            attachToParent(codeBlock)
        }

        override fun visit(link: Link) {
            val kClass = LinkElement::class
            val parent = currentBuilder
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    addStatement(
                        "%N = %T(%S),",
                        kClass.member(LinkElement::link.name),
                        MarktdownLink::class,
                        link.destination,
                    )
                    visitChildren(
                        parent = link,
                        parentBuilder = this,
                        memberName = kClass.member(LinkElement::children.name),
                    )
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        override fun visit(listItem: ListItem) {
            // TODO: Deal with indent.
            val parent = currentBuilder
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", ListElement.ListItem::class)
                .withIndent {
                    visitChildren(
                        parent = listItem,
                        parentBuilder = this,
                        memberName = ListElement.ListItem::class.member(ListElement.ListItem::children.name),
                    )
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        override fun visit(orderedList: OrderedList) {
            val parent = currentBuilder
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", ListElement.Ordered::class)
                .withIndent {
                    visitChildren(
                        parent = orderedList,
                        parentBuilder = this,
                        memberName = ListElement.Ordered::class.member(ListElement.Ordered::children.name),
                    )
                }
                .addStatement("),")
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        override fun visit(paragraph: Paragraph) {
            val parent = currentBuilder
            val codeBlock = CodeBlock
                .builder()
                .apply {
                    val child = paragraph.singleChild
                    if (child != null && child is Text) {
                        addStatement("%T(%S),", TextElement.Paragraph::class, child.literal)
                    } else {
                        addStatement("%T(", TextElement.Paragraph::class)
                        withIndent {
                            visitChildren(
                                parent = paragraph,
                                parentBuilder = this,
                                memberName = TextElement.Paragraph::class.member(TextElement.Paragraph::children.name),
                            )
                        }
                        addStatement("),")
                    }
                }
            currentBuilder = parent
            attachToParent(codeBlock)
        }

        override fun visit(softLineBreak: SoftLineBreak) {
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(\" \"),", TextElement.PlainText::class)
            attachToParent(codeBlock)
        }

        override fun visit(strongEmphasis: StrongEmphasis) {
            val parent = currentBuilder
            val codeBlock = CodeBlock
                .builder()
                .apply {
                    val child = strongEmphasis.singleChild
                    if (child != null && child is Text) {
                        addStatement("%T(%S),", TextElement.StrongText::class, child.literal)
                    } else {
                        addStatement("%T(", TextElement.StrongText::class)
                        withIndent {
                            visitChildren(
                                parent = strongEmphasis,
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

        override fun visit(text: Text) {
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(%S),", TextElement.PlainText::class, text.literal)
            attachToParent(codeBlock)
        }

        override fun visit(linkReferenceDefinition: LinkReferenceDefinition) {
            val kClass = LinkDefinition::class
            val codeBlock = CodeBlock
                .builder()
                .addStatement("%T(", kClass)
                .withIndent {
                    // TODO: change to parent.
                    addStatement(
                        "%N = %T(%S),",
                        kClass.member(LinkDefinition::destination.name),
                        TextElement.PlainText::class,
                        linkReferenceDefinition.title
                    )
                    addStatement(
                        "%N = %S,",
                        kClass.member(LinkDefinition::destination.name),
                        linkReferenceDefinition.destination,
                    )
                }
                .addStatement("),")
            attachToParent(codeBlock)
        }

        override fun visit(customBlock: CustomBlock) {
            when (customBlock) {
                is YamlFrontMatterBlock -> {
                    metadataSpec = MarktdownMetadataImpl()
                }

                is TableBlock -> {
//                    val parent = currentBuilder
//                    val codeBlock = CodeBlock
//                        .builder()
//                        .apply {
//                            addStatement("%T(", TableElement::class)
//                            // TODO: setup table
//                            withIndent {
//                                visitChildren(
//                                    parent = customBlock,
//                                    parentBuilder = this,
//                                    memberName = TableElement::class.member(TableElement::children.name),
//                                )
//                            }
//                            addStatement("),")
//                        }
//
//                    currentBuilder = parent
//                    attachToParent(codeBlock)
                }
            }
            println(customBlock)
            super.visit(customBlock)
        }

        override fun visit(customNode: CustomNode) {
            when (customNode) {
                is YamlFrontMatterNode -> {
                    when (customNode.key) {
                        MarktdownMetadata::documentTitle.name -> metadataSpec.update {
                            it.copy(documentTitle = customNode.values.first())
                        }

                        MarktdownMetadata::authors.name -> metadataSpec.update { metadata ->
                            // todo: figure out how to fill the whole entity.
                            println(customNode.values)
                            val prefix = "name: "
                            metadata.copy(
                                authors = customNode.values
                                    .filter { it.startsWith(prefix) }
                                    .map { Author(name = it.removePrefix(prefix)) }
                            )
                        }

                        MarktdownMetadata::publishedDateTime.name -> metadataSpec.update {
                            it.copy(publishedDateTime = LocalDateTime.parse(customNode.values.first()))
                        }

                        MarktdownMetadata::documentDescription.name -> metadataSpec.update {
                            it.copy(documentDescription = customNode.values.first())
                        }

                        MarktdownMetadata::crossPost.name -> metadataSpec.update {
                            it.copy(crossPost = MarktdownLink(customNode.values.first()))
                        }

                        MarktdownMetadata::tags.name -> metadataSpec.update {
                            it.copy(tags = customNode.values.map(::MarktdownTag))
                        }

                        MarktdownMetadata::lastUpdateDateTime.name -> metadataSpec.update {
                            it.copy(lastUpdateDateTime = LocalDateTime.parse(customNode.values.first()))
                        }

                        MarktdownMetadata::postThumbnail.name -> metadataSpec.update {
                            it.copy(postThumbnail = MarktdownLink(customNode.values.first()))
                        }
                    }
                }
//                is TableHead -> {
//
//                }
            }
            println(customNode)

            super.visit(customNode)
        }

        private fun MarktdownMetadataImpl.update(block: (MarktdownMetadataImpl) -> MarktdownMetadataImpl) {
            metadataSpec = block(this)
        }
    }
}

private fun MarktdownMetadata.build(memberName: MemberName): CodeBlock {
    val className = MarktdownMetadata::class.asClassName()
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
                addStatement("%N = %L,", className.member(author::avatar.name), avatar.toCodeBlock())
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
