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
import dev.tonholo.marktdown.domain.MarktdownTag
import dev.tonholo.marktdown.domain.content.CodeFance
import dev.tonholo.marktdown.domain.content.HorizontalRule
import dev.tonholo.marktdown.domain.content.ImageElement
import dev.tonholo.marktdown.domain.content.LineBreak
import dev.tonholo.marktdown.domain.content.Link.FootnoteElement
import dev.tonholo.marktdown.domain.content.ListElement
import dev.tonholo.marktdown.domain.content.MarktdownDocument
import dev.tonholo.marktdown.domain.content.TextElement
import kotlinx.datetime.LocalDateTime
import org.commonmark.ext.front.matter.YamlFrontMatterBlock
import org.commonmark.ext.front.matter.YamlFrontMatterNode
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
import dev.tonholo.marktdown.domain.content.Link.Element as LinkElement

class MarktdownVisitor(
    val packageName: String,
    val fileName: String,
) : AbstractVisitor() {
    private lateinit var metadataSpec: MarktdownMetadata
    private val childrenSpec: MutableList<CodeBlock.Builder> = mutableListOf()
    private val rootPropertySpec: PropertySpec
        get() {
            return PropertySpec
                .builder(
                    name = fileName,
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
                                    "%N = null",
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
    val fileSpec
        get() = FileSpec
            .builder(
                packageName = packageName,
                fileName = fileName,
            )
            .addProperty(rootPropertySpec)
            .build()

    private var currentBuilder: CodeBlock.Builder? = null
    private val listMemberName = MemberName(packageName = "kotlin.collections", simpleName = "listOf")

    private fun attachToParent(current: CodeBlock.Builder) {
        currentBuilder?.add("%L,", current.build())
            ?: childrenSpec.add(current)
    }

    private fun CodeBlock.Builder.visitChildren(
        parent: Node,
        parentBuilder: CodeBlock.Builder,
        memberName: MemberName,
    ) {
        add(
            "%N = %M(",
            memberName,
            listMemberName,
        )
        currentBuilder = parentBuilder
        visitChildren(parent)
        add(")")
    }

    override fun visit(blockQuote: BlockQuote) {
        println("blockQuote=${blockQuote}")
        val parent = currentBuilder
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", TextElement.Blockquote::class)
            .withIndent {
                visitChildren(
                    parent = blockQuote,
                    parentBuilder = this,
                    memberName = TextElement.Blockquote::class.member(TextElement.Blockquote::children.name),
                )
            }
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
    }

    override fun visit(bulletList: BulletList) {
        println("bulletList=${bulletList}")
        val parent = currentBuilder
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", ListElement.Bullet::class)
            .withIndent {
                visitChildren(
                    parent = bulletList,
                    parentBuilder = this,
                    memberName = ListElement.Bullet::class.member(ListElement.Bullet::options.name),
                )
            }
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
    }

    override fun visit(code: Code) {
        println("code=${code.literal}")
        val kClass = TextElement.InlineCode::class
        val codeBlock = CodeBlock
            .builder()
            .add(
                "%T(%N = %S)",
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
        println("emphasis=${emphasis}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", TextElement.ItalicText::class)
            .withIndent {
                visitChildren(
                    parent = emphasis,
                    parentBuilder = this,
                    memberName = TextElement.ItalicText::class.member(TextElement.ItalicText::children.name),
                )
            }
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
    }

    override fun visit(fencedCodeBlock: FencedCodeBlock) {
        println("fencedCodeBlock=${fencedCodeBlock.literal}, ${fencedCodeBlock.info}")
        val kClass = CodeFance::class
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", kClass)
            .withIndent {
                add(
                    "%N = %S,",
                    kClass.member(CodeFance::language.name),
                    fencedCodeBlock.info,
                )
                add(
                    "%N = %S,",
                    kClass.member(CodeFance::code.name),
                    fencedCodeBlock.literal,
                )
            }
            .add(")")
        attachToParent(codeBlock)
//        super.visit(fencedCodeBlock)
    }

    override fun visit(hardLineBreak: HardLineBreak) {
        println("hardLineBreak=${hardLineBreak}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T", LineBreak::class)
            .indent()

        attachToParent(codeBlock)
//        super.visit(hardLineBreak)
    }

    override fun visit(heading: Heading) {
        println("heading=${heading}")
        val parent = currentBuilder
        val kClass = TextElement.Title::class
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", kClass)
            .withIndent {
                add(
                    "%N = %T.H%L,",
                    kClass.member(TextElement.Title::style.name),
                    TextElement.Title.Style::class,
                    heading.level,
                )
                visitChildren(
                    parent = heading,
                    parentBuilder = this,
                    memberName = kClass.member(TextElement.Title::children.name),
                )
            }
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
    }

    override fun visit(thematicBreak: ThematicBreak) {
        println("thematicBreak=${thematicBreak.literal}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T", HorizontalRule::class)
        attachToParent(codeBlock)
//        super.visit(thematicBreak)
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
        println("image=${image}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", kClass)
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
            .add(")")
        attachToParent(codeBlock)
//        super.visit(image)
    }

    override fun visit(indentedCodeBlock: IndentedCodeBlock) {
        val kClass = TextElement.InlineCode::class
        println("indentedCodeBlock=${indentedCodeBlock.literal}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", kClass)
            .withIndent {
                add(
                    "%N = \"\",",
                    kClass.member(CodeFance::language.name),
                )
                add(
                    "%N = %S,",
                    kClass.member(CodeFance::code.name),
                    indentedCodeBlock.literal,
                )
            }
            .add(")")
        attachToParent(codeBlock)
//        super.visit(indentedCodeBlock)
    }

    override fun visit(link: Link) {
        val kClass = LinkElement::class
        val parent = currentBuilder
        println("link=${link}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", kClass)
            .withIndent {
                add(
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
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
//        super.visit(link)
    }

    override fun visit(listItem: ListItem) {
        // TODO: Deal with indent.
        val parent = currentBuilder
        println("listItem=${listItem}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", ListElement.Bullet::class)
            .withIndent {
                visitChildren(
                    parent = listItem,
                    parentBuilder = this,
                    memberName = ListElement.Bullet::class.member(ListElement.Bullet::options.name),
                )
            }
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
//        super.visit(listItem)
    }

    override fun visit(orderedList: OrderedList) {
        println("orderedList=${orderedList}")
        val parent = currentBuilder
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", ListElement.Numbered::class)
            .withIndent {
                visitChildren(
                    parent = orderedList,
                    parentBuilder = this,
                    memberName = ListElement.Numbered::class.member(ListElement.Numbered::options.name),
                )
            }
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
//        super.visit(orderedList)
    }

    override fun visit(paragraph: Paragraph) {
        println("paragraph=${paragraph}")
        val parent = currentBuilder
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", TextElement.Paragraph::class)
            .withIndent {
                visitChildren(
                    parent = paragraph,
                    parentBuilder = this,
                    memberName = TextElement.Paragraph::class.member(TextElement.Paragraph::children.name),
                )
            }
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
//        super.visit(paragraph)
    }

    override fun visit(softLineBreak: SoftLineBreak) {
        println("softLineBreak=${softLineBreak}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T(\" \")", TextElement.PlainText::class)
        attachToParent(codeBlock)
//        super.visit(softLineBreak)
    }

    override fun visit(strongEmphasis: StrongEmphasis) {
        println("strongEmphasis=${strongEmphasis}")
        val parent = currentBuilder
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", TextElement.BoldText::class)
            .withIndent {
                visitChildren(
                    parent = strongEmphasis,
                    parentBuilder = this,
                    memberName = TextElement.BoldText::class.member(TextElement.BoldText::children.name),
                )
            }
            .add(")")
        currentBuilder = parent
        attachToParent(codeBlock)
//        super.visit(strongEmphasis)
    }

    override fun visit(text: Text) {
        println("text=${text.literal}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T(%S)", TextElement.PlainText::class, text.literal)
        attachToParent(codeBlock)
//        super.visit(text)
    }

    override fun visit(linkReferenceDefinition: LinkReferenceDefinition) {
        val kClass = FootnoteElement::class
        println("linkReferenceDefinition=${linkReferenceDefinition}")
        val codeBlock = CodeBlock
            .builder()
            .add("%T(", kClass)
            .withIndent {
                // TODO: change to parent.
                add(
                    "%N = %T(%S),",
                    kClass.member(FootnoteElement::text.name),
                    TextElement.PlainText::class,
                    linkReferenceDefinition.title
                )
                add(
                    "%N = %S,",
                    kClass.member(FootnoteElement::anchor.name),
                    linkReferenceDefinition.destination,
                )
            }
            .add(")")
        attachToParent(codeBlock)
//        super.visit(linkReferenceDefinition)
    }

    override fun visit(customBlock: CustomBlock) {
        println("customBlock=${customBlock}")
        if (customBlock is YamlFrontMatterBlock) {
            metadataSpec = MarktdownMetadata()
        }
        super.visit(customBlock)
    }

    override fun visit(customNode: CustomNode) {
        if (customNode is YamlFrontMatterNode) {
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
        super.visit(customNode)
    }

    private fun MarktdownMetadata.update(block: (MarktdownMetadata) -> MarktdownMetadata) {
        metadataSpec = block(this)
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
        addStatement(")")
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
        addStatement(")")
    }
}

fun LocalDateTime.toCodeBlock(): CodeBlock {
    val localDateTimeClassName = LocalDateTime::class.asClassName()
    val localDateTimeCompanionClassName = LocalDateTime.Companion::class.asClassName()
    return CodeBlock.of(
        "%T.%N(%S)",
        localDateTimeClassName,
        localDateTimeCompanionClassName.member("parse"),
        this,
    )
}
