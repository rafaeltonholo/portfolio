package dev.tonholo.marktdown.processor.extensions

import dev.tonholo.marktdown.processor.visitor.MarktdownElementTypes
import dev.tonholo.marktdown.processor.visitor.MarktdownRenderer
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.ASTNodeImpl
import org.intellij.markdown.ast.CompositeASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.ast.getTextInNode

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

private val startTagRegex = Regex("^<(\\w+[^>]*)>")
private val endTagRegex = Regex("^</\\w+[^>]*>")

// TODO: improve with a better caching. Maybe an LRU cache?
private val nodeChildrenToRenderCache = mutableMapOf<ASTNode, List<ASTNode>>()

context(MarktdownRenderer)
val ASTNode.literal
    get() = getTextInNode(content)

context(MarktdownRenderer)
internal val ASTNode.childrenToRender: List<ASTNode>
    get() = nodeChildrenToRenderCache[this] ?: if (children.size > 1) {
        val attachToParent: ASTNodeImpl.() -> Unit = {
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

        var newChildren = children
        val hasHtmlBlock = newChildren.any {
            it.type == MarkdownElementTypes.HTML_BLOCK
        }

        if (hasHtmlBlock) {
            newChildren = foldHtmlBlock(newChildren.iterator(), attachToParent)
        }

        val hasInlineHtml = newChildren.any {
            it.type == MarkdownTokenTypes.HTML_TAG && it.parent?.type != MarktdownElementTypes.INLINE_HTML
        }

        if (hasInlineHtml) {
            newChildren = foldInlineHtml(newChildren.iterator(), attachToParent)
        }

        if (newChildren.any { it.type == MarkdownTokenTypes.TEXT }) {
            newChildren = foldTextElements(newChildren.iterator(), attachToParent)
        }

        newChildren.also {
            nodeChildrenToRenderCache[this] = it
        }
    } else {
        children
    }

context(MarktdownRenderer)
private fun foldTextElements(
    iterator: Iterator<ASTNode>,
    attachToParent: ASTNodeImpl.() -> Unit
): List<ASTNode> {
    val newChildren = mutableListOf<ASTNode>()
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
                    ).apply(attachToParent)
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
//            println("Folded children = ${newChildren.map { it.type }}")
//            println("children.size = ${children.size}")
//            println("newChildren.size = ${newChildren.size}")
    return newChildren
}

context(MarktdownRenderer)
private fun foldInlineHtml(
    iterator: Iterator<ASTNode>,
    attachToParent: ASTNodeImpl.() -> Unit
): List<ASTNode> {
    val newChildren = mutableListOf<ASTNode>()

    val inlineHtmlMap = mutableMapOf<ASTNode, MutableList<ASTNode>>()
    val stack = ArrayDeque<ASTNode>()

    fun createInlineHtmlNode() {
        val current = stack.removeLast()
        val inlineHtmlChildren = requireNotNull(inlineHtmlMap[current]) {
            "inlineHtmlMap should contain current node"
        }

        val openingTag = inlineHtmlChildren.first()
        val closingTag = inlineHtmlChildren.last()
        check(endTagRegex.matches(closingTag.literal)) {
            // Although markdown supports incomplete inline html, there is
            // not an effective way to detect it without messing up with the whole model.
            // For that reason, I'm dropping the support here.
            // TODO: Further investigation is needed to understand if we can support it.
            buildString {
                appendLine("Incomplete inline html node detected.")
                appendLine("Missing: ${openingTag.literal.replaceFirst(startTagRegex, "</$1>")}")
                appendLine("Where:")
                closingTag.parent?.let { parent ->
                    appendLine("start index offset = ${parent.startOffset}".indented())
                    appendLine("end index offset = ${parent.endOffset}".indented())
                    appendLine("text = ${parent.literal}".indented())
                } ?: run {
                    appendLine("start index offset = ${closingTag.startOffset}".indented())
                    appendLine("end index offset = ${closingTag.endOffset}".indented())
                    val text = content.substring(closingTag.startOffset..closingTag.endOffset)
                    appendLine("text = $text".indented())
                }
            }
        }

        newChildren += CompositeASTNode(
            type = MarktdownElementTypes.INLINE_HTML,
            children = inlineHtmlChildren.toList(),
        ).apply(attachToParent)

        inlineHtmlMap.remove(current)
    }

    while (iterator.hasNext()) {
        val current = iterator.next()
        when {
            current.type == MarkdownTokenTypes.HTML_TAG && startTagRegex.matches(current.literal) -> {
                inlineHtmlMap[current] = mutableListOf(current)
                stack.addLast(current)
            }

            current.type != MarkdownTokenTypes.HTML_TAG && stack.isNotEmpty() -> {
                inlineHtmlMap[stack.last()]?.add(current)
            }

            current.type == MarkdownTokenTypes.HTML_TAG
                && stack.isNotEmpty()
                && endTagRegex.matches(current.literal) -> {
                inlineHtmlMap[stack.last()]?.add(current)
                createInlineHtmlNode()
            }

            else -> newChildren += current
        }
    }

    if (stack.isNotEmpty()) {
        check(stack.size == 1) {
            "At this point, the inline html stack should contain only one node."
        }
        createInlineHtmlNode()
    }
    return newChildren
}

private val htmlBlockTypes = setOf(MarkdownElementTypes.HTML_BLOCK, MarkdownTokenTypes.EOL)

private fun foldHtmlBlock(
    iterator: Iterator<ASTNode>,
    attachToParent: ASTNodeImpl.() -> Unit
): List<ASTNode> {
    val newChildren = mutableListOf<ASTNode>()
    var htmlBlockChildren: MutableList<ASTNode>? = null
    fun createHtmlBlockNodeIfNeeded() {
        htmlBlockChildren?.let { children ->
            newChildren += CompositeASTNode(
                type = MarkdownElementTypes.HTML_BLOCK,
                children = children,
            ).apply(attachToParent)
            htmlBlockChildren = null
        }
    }

    while (iterator.hasNext()) {
        val current = iterator.next()
        when {
            current.type == MarkdownElementTypes.HTML_BLOCK && htmlBlockChildren == null -> {
                htmlBlockChildren = mutableListOf<ASTNode>().apply { addAll(current.children) }
            }

            current.type in htmlBlockTypes -> {
                htmlBlockChildren?.let { children ->
                    children += current.children
                }
            }

            else -> {
                createHtmlBlockNodeIfNeeded()
                newChildren += current
            }
        }
    }

    createHtmlBlockNodeIfNeeded()

    return newChildren
}
