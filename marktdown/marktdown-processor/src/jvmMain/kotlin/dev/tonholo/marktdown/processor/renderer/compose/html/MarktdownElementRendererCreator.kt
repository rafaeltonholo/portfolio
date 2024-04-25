package dev.tonholo.marktdown.processor.renderer.compose.html

import androidx.compose.runtime.Composable
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.typeNameOf
import com.squareup.kotlinpoet.withIndent
import dev.tonholo.marktdown.domain.MarktdownLink
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.content.HorizontalRule
import dev.tonholo.marktdown.domain.content.ImageElement
import dev.tonholo.marktdown.domain.content.LineBreak
import dev.tonholo.marktdown.domain.content.Link
import dev.tonholo.marktdown.domain.content.LinkDefinition
import dev.tonholo.marktdown.domain.content.ListElement
import dev.tonholo.marktdown.domain.content.MarktdownDocument
import dev.tonholo.marktdown.domain.content.MarktdownElement
import dev.tonholo.marktdown.domain.content.MarktdownParent
import dev.tonholo.marktdown.domain.content.TableContent
import dev.tonholo.marktdown.domain.content.TableElement
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import java.nio.file.Path
import kotlin.reflect.KClass

private const val COMPOSE_WEB_DOM = "org.jetbrains.compose.web.dom"
private const val COMPOSE_WEB_CSS = "org.jetbrains.compose.web.css"
private val emMember = MemberName(COMPOSE_WEB_CSS, "em")
private val pxMember = MemberName(COMPOSE_WEB_CSS, "px")
private val marginMember = MemberName(COMPOSE_WEB_CSS, "margin")
private val paddingMember = MemberName(COMPOSE_WEB_CSS, "padding")
private val paddingLeftMember = MemberName(COMPOSE_WEB_CSS, "paddingLeft")
private val lineStyleClassName = ClassName(COMPOSE_WEB_CSS, "LineStyle", "Companion")
private val colorClassName = ClassName(COMPOSE_WEB_CSS, "Color")
private val cssBorderClassName = ClassName(COMPOSE_WEB_CSS, "CSSBorder")
private val htmlElementClassName = ClassName("org.w3c.dom", "HTMLElement")
private val marktdownLinkValueMemberName = MarktdownLink::class.asClassName().member(MarktdownLink::value.name)
private val disposableEffect = MemberName("androidx.compose.runtime", "DisposableEffect")

class MarktdownElementRendererCreator(
    packageName: String,
    private val elementsToRender: Set<KClass<out MarktdownElement>>,
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

    private val packageName = "$packageName.renderer"

    private val marktdownElementMemberName = MemberName(this.packageName, MarktdownElement::class.java.simpleName)
    private val marktdownDocumentMemberName = MemberName(this.packageName, "Marktdown")

    fun create(output: Path) {
        elementsToRender.forEach {
            it.createDefaultRender(marktdownElementMemberName, packageName = "$packageName.content")?.writeTo(output)
        }

        createMarktdownElementRenderer()
            .writeTo(output)

        createMarktdownDocumentRender()
            .writeTo(output)
    }

    private fun createMarktdownDocumentRender(): FileSpec = FileSpec.builder(marktdownDocumentMemberName).apply {
        defaultIndent()
        addFunction(
            FunSpec.builder(marktdownDocumentMemberName).apply {
                addAnnotation(Composable::class)
                addModifiers(KModifier.PUBLIC)
                addParameter(name = "document", typeNameOf<MarktdownDocument>())
                val composable = MemberName(COMPOSE_WEB_DOM, "Div")
                addCode(
                    createMarktdownParentCodeBlock(
                        marktdownElementMemberName,
                        composable,
                        propertyName = "document",
                    )
                )
            }.build(),
        )
    }.build()


    private fun createMarktdownElementRenderer(): FileSpec {
        return FileSpec.builder(marktdownElementMemberName).apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(marktdownElementMemberName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", typeNameOf<MarktdownElement>())
                    val packageName = "${this@MarktdownElementRendererCreator.packageName}.content"
                    val members = elementsToRender.associateWith {
                        MemberName(packageName, it.asClassName().simpleNames.joinToString(""))
                    }
                    beginControlFlow("when (element)")
                    members.forEach { (key, value) ->
                        addStatement(
                            if (key.objectInstance != null) {
                                "%T -> %M()"
                            } else {
                                "is %T -> %M(element = element)"
                            },
                            key,
                            value,
                        )
                    }
                    addStatement("else -> Unit")
                    endControlFlow()
                }.build(),
            )
        }.build()
    }
}

private inline fun <reified T : MarktdownElement> KClass<out T>.createDefaultRender(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec? {
    return when (asClassName()) {
        CodeFence::class.asClassName() -> createCodeFenceDefaultRenderer(packageName)
        HorizontalRule::class.asClassName() -> createHorizontalRuleDefaultRenderer(
            packageName
        )

        ImageElement::class.asClassName() -> createImageElementDefaultRenderer(packageName)
        LineBreak::class.asClassName() -> createLineBreakDefaultRenderer(packageName)
        LinkDefinition::class.asClassName() -> createLinkDefinitionDefaultRenderer(packageName)

        TableElement::class.asClassName() -> createTableElementDefaultRenderer(marktdownElementMemberName, packageName)
        ListElement.ListItem::class.asClassName() -> createListElementListItemDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        ListElement.Ordered::class.asClassName() -> createListElementOrderedDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        ListElement.Task::class.asClassName() -> createListElementTaskDefaultRenderer(packageName)

        ListElement.Unordered::class.asClassName() -> createListElementUnorderedDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.Blockquote::class.asClassName() -> createTextElementBlockquoteDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        Link.Element::class.asClassName() -> createLinkElementDefaultRenderer(marktdownElementMemberName, packageName)
        TextElement.EmphasisText::class.asClassName() -> createTextElementEmphasisTextDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.Highlight::class.asClassName() -> createTextElementHighlightDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.Paragraph::class.asClassName() -> createTextElementParagraphDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        Link.ReferenceElement::class.asClassName() -> createLinkReferenceElementDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.Strikethrough::class.asClassName() -> createTextElementStrikethroughDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.StrongText::class.asClassName() -> createTextElementStrongTextDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.Subscript::class.asClassName() -> createTextElementSubscriptDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.Superscript::class.asClassName() -> createTextElementSuperscriptDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.Title::class.asClassName() -> createTextElementTitleDefaultRenderer(
            marktdownElementMemberName,
            packageName
        )

        TextElement.Emoji::class.asClassName() -> createTextElementEmojiDefaultRenderer(packageName)

        TextElement.InlineCode::class.asClassName() -> createTextElementInlineCodeDefaultRenderer(packageName)

        Link.AutoLink::class.asClassName() -> createLinkAutoLinkDefaultRenderer(packageName)
        TextElement.PlainText::class.asClassName() -> createTextElementPlainTextDefaultRenderer(packageName)

        else -> null
    }
}

fun FileSpec.Builder.defaultIndent() = indent(" ".repeat(4))

private fun createCodeFenceDefaultRenderer(
    packageName: String,
): FileSpec = FileSpec
    .builder(packageName, CodeFence::class.java.simpleName)
    .apply {
        defaultIndent()
        addFunction(
            FunSpec.builder(CodeFence::class.java.simpleName).apply {
                addAnnotation(Composable::class)
                addModifiers(KModifier.INTERNAL)
                addParameter(name = "element", type = typeNameOf<CodeFence>())
                val pre = MemberName(COMPOSE_WEB_DOM, "Pre")
                val code = MemberName(COMPOSE_WEB_DOM, "Code")
                addCode(
                    """
                        |%M {
                        |   %M(
                        |       attrs = {
                        |           element.language?.let { classes("language-${'$'}it") }
                        |       },
                        |   ) {
                        |       %M(this) {
                        |           scopeElement.innerHTML = element.code
                        |           onDispose { }
                        |       }
                        |   }
                        |}
                        """.trimMargin(),
                    pre,
                    code,
                    disposableEffect,
                )
            }.build(),
        )
    }
    .build()

private fun createHorizontalRuleDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = HorizontalRule::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    val hr = MemberName(COMPOSE_WEB_DOM, "Hr")
                    addCode("%M()", hr)
                }.build(),
            )
        }
        .build()
}

private fun createImageElementDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = ImageElement::class.asClassName()
    val fnName = className.simpleName
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<ImageElement>())
                    val image = MemberName(COMPOSE_WEB_DOM, "Img")
                    addCode(
                        """
                            |%M(
                            |   src = element.%N.%N,
                            |   alt = element.%N.orEmpty(),
                            |)
                        """.trimMargin(),
                        image,
                        className.member(ImageElement::link.name),
                        marktdownLinkValueMemberName,
                        className.member(ImageElement::alt.name),
                    )
                }.build(),
            )
        }
        .build()
}

private fun createLineBreakDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = LineBreak::class.asClassName()
    val fnName = className.simpleName
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    val br = MemberName(COMPOSE_WEB_DOM, "Br")
                    addCode("%M()", br)
                }.build(),
            )
        }
        .build()
}

private fun createLinkDefinitionDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = LinkDefinition::class.asClassName()
    val fnName = className.simpleName
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<LinkDefinition>())

                }.build(),
            )
        }
        .build()
}

private fun createTableElementDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TableElement::class.asClassName()
    val fnName = className.simpleName
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TableElement>())
                    val table = MemberName(COMPOSE_WEB_DOM, "Table")
                    val tHead = MemberName(COMPOSE_WEB_DOM, "Thead")
                    val tBody = MemberName(COMPOSE_WEB_DOM, "Tbody")
                    val tr = MemberName(COMPOSE_WEB_DOM, "Tr")
                    val th = MemberName(COMPOSE_WEB_DOM, "Th")
                    val td = MemberName(COMPOSE_WEB_DOM, "Td")
                    val cellClassName = TableContent.Cell::class.asClassName()
                    val textAlign = MemberName(COMPOSE_WEB_CSS, "textAlign")
                    val attrs = CodeBlock.builder().apply {
                        beginControlFlow("attrs = ")
                        beginControlFlow("style")
                        addStatement(
                            "%M(cell.%N.name.lowercase())",
                            textAlign,
                            cellClassName.member(TableContent.Cell::alignment.name),
                        )
                        endControlFlow()
                        endControlFlow()
                    }.build()

                    val cellBlock: (String, MemberName) -> CodeBlock = { member: String, composableMember: MemberName ->
                        CodeBlock.builder().apply {
                            beginControlFlow("for (cell in $member)")
                            addStatement("%M(", composableMember)
                            withIndent {
                                add(attrs)
                            }
                            beginControlFlow(")")
                            addStatement(
                                "cell.%N.forEach { %M(it) }",
                                cellClassName.member(TableContent.Cell::content.name),
                                marktdownElementMemberName,
                            )
                            endControlFlow()
                            endControlFlow()
                        }.build()
                    }
                    addCode(
                        CodeBlock.builder().apply {
                            beginControlFlow("%M", table)
                            beginControlFlow("%M", tHead)
                            beginControlFlow("%M", tr)
                            add(cellBlock("element.header.row.cells", th))
                            endControlFlow()
                            endControlFlow()
                            beginControlFlow("%M", tBody)
                            beginControlFlow(
                                "for (row in element.%N)",
                                TableElement::class.asClassName().member(TableElement::rows.name),
                            )
                            beginControlFlow("%M", tr)
                            add(cellBlock("row.cells", td))
                            endControlFlow()
                            endControlFlow()
                            endControlFlow()
                            endControlFlow()
                        }.build(),
                    )
                }.build(),
            )
        }
        .build()
}

private fun createListElementListItemDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = ListElement.ListItem::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<ListElement.ListItem>())
                    val li = MemberName(COMPOSE_WEB_DOM, "Li")
                    addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, li))
                }.build(),
            )
        }
        .build()
}

private fun createListElementOrderedDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = ListElement.Ordered::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<ListElement.Ordered>())
                    val ol = MemberName(COMPOSE_WEB_DOM, "Ol")
                    addCode(buildCodeBlock {
                        addStatement("%M(", ol)
                        withIndent {
                            addStatement(
                                "attrs = element.%N?.let { startWith ->",
                                ListElement.Ordered::class.asClassName().member(ListElement.Ordered::startWith.name),
                            )
                            withIndent {
                                addStatement("{")
                                withIndent {
                                    addStatement("attr(\"start\", startWith.toString())")
                                }
                                addStatement("}")
                            }
                            addStatement("}")
                        }
                        beginControlFlow(")")
                        addStatement(
                            "element.%N.forEach { %M(it) }",
                            ListElement.Ordered::options.name,
                            marktdownElementMemberName,
                        )
                        endControlFlow()
                    })
                }.build(),
            )
        }
        .build()
}

private fun createListElementTaskDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = ListElement.Task::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<ListElement.Task>())
                    addCode("// TBD.")
                }.build(),
            )
        }
        .build()
}

private fun createListElementUnorderedDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = ListElement.Unordered::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<ListElement.Unordered>())
                    val ul = MemberName(COMPOSE_WEB_DOM, "Ul")
                    addCode(buildCodeBlock {
                        beginControlFlow("%M", ul)
                        addStatement(
                            "element.%N.forEach { %M(it) }",
                            ListElement.Unordered::options.name,
                            marktdownElementMemberName,
                        )
                        endControlFlow()
                    })
                }.build(),
            )
        }
        .build()
}

private fun createTextElementBlockquoteDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.Blockquote::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.Blockquote>())
                    val blockquote = MemberName(COMPOSE_WEB_DOM, "Blockquote")
                    addCode(buildCodeBlock {
                        addStatement("%M(", blockquote)
                        withIndent {
                            beginControlFlow("attrs = ")
                            beginControlFlow("style")
                            addStatement("%M(1.%M)", marginMember, emMember)
                            addStatement("%M(1.%M)", paddingMember, emMember)
                            addStatement("property(")
                            withIndent {
                                // TODO: Create a LocalDefaultStyle to fill style instead.
                                addStatement("propertyName = %S,", "border-left")
                                addStatement("value = %T().apply {", cssBorderClassName)
                                withIndent {
                                    addStatement("width = 2.%M", pxMember)
                                    addStatement("color = %M", colorClassName.member("white"))
                                    addStatement("style = %M", lineStyleClassName.member("Solid"))
                                }
                                addStatement("},")
                            }
                            addStatement(")")
                            addStatement("%M(2.%M)", paddingLeftMember, emMember)
                            endControlFlow()
                            endControlFlow()
                        }
                        beginControlFlow(")")
                        addStatement(
                            "element.%N.forEach { %M(it) }",
                            TextElement.Blockquote::children.name,
                            marktdownElementMemberName,
                        )
                        endControlFlow()
                    })
                }.build(),
            )
        }
        .build()
}

private fun createLinkElementDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = Link.Element::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<Link.Element>())
                    val link = MemberName(COMPOSE_WEB_DOM, "A")
                    addCode(
                        createMarktdownParentCodeBlock(
                            marktdownElementMemberName,
                            link
                        ) {
                            "href = element.%N.%N" to arrayOf(
                                Link.Element::class.asClassName().member(Link.Element::link.name),
                                marktdownLinkValueMemberName,
                            )
                        }
                    )
                }.build(),
            )
        }
        .build()
}

private fun createTextElementEmphasisTextDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.EmphasisText::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.EmphasisText>())
                    val composeElement = MemberName(COMPOSE_WEB_DOM, "Em")
                    addCode(
                        createMarktdownParentCodeBlock(
                            marktdownElementMemberName,
                            composeElement,
                        )
                    )
                }.build(),
            )
        }
        .build()
}

private fun createTextElementHighlightDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.Highlight::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.Highlight>())
                    addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, tagName = "mark"))
                }.build(),
            )
        }
        .build()
}

private fun createTextElementParagraphDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.Paragraph::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.Paragraph>())
                    val composeElement = MemberName(COMPOSE_WEB_DOM, "P")
                    addCode(
                        createMarktdownParentCodeBlock(
                            marktdownElementMemberName,
                            composeElement,
                        )
                    )
                }.build(),
            )
        }
        .build()
}

private fun createLinkReferenceElementDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = Link.ReferenceElement::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<Link.ReferenceElement>())
                    val composeElement = MemberName(COMPOSE_WEB_DOM, "A")
                    addCode(
                        createMarktdownParentCodeBlock(
                            marktdownElementMemberName,
                            composeElement,
                        ) {
                            "href = element.%N.%N" to arrayOf(
                                Link.Element::class.asClassName().member(Link.ReferenceElement::destination.name),
                                marktdownLinkValueMemberName,
                            )
                        }
                    )
                }.build(),
            )
        }
        .build()
}

private fun createTextElementStrikethroughDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.Strikethrough::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.Strikethrough>())
                    addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, tagName = "mark"))

                }.build(),
            )
        }
        .build()
}

private fun createTextElementStrongTextDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.StrongText::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.StrongText>())
                    val composeElement = MemberName(COMPOSE_WEB_DOM, "B")
                    addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, composeElement))
                }.build(),
            )
        }
        .build()
}

private fun createTextElementSubscriptDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.Subscript::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.Subscript>())
                    val composeElement = MemberName(COMPOSE_WEB_DOM, "Sub")
                    addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, composeElement))
                }.build(),
            )
        }
        .build()
}

private fun createTextElementSuperscriptDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.Superscript::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.Superscript>())
                    val composeElement = MemberName(COMPOSE_WEB_DOM, "Sup")
                    addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, composeElement))
                }.build(),
            )
        }
        .build()
}

private fun createTextElementTitleDefaultRenderer(
    marktdownElementMemberName: MemberName,
    packageName: String,
): FileSpec {
    val className = TextElement.Title::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.Title>())
                    beginControlFlow("when (element.%N)", className.member(TextElement.Title::style.name))
                    for (style in TextElement.Title.Style.entries) {
                        beginControlFlow(
                            "%M -> %M",
                            TextElement.Title.Style::class.asClassName().member(style.name),
                            MemberName(COMPOSE_WEB_DOM, style.name),
                        )
                        addStatement(
                            "element.%N.forEach { %M(it) }",
                            className.member(TextElement.Title::children.name),
                            marktdownElementMemberName,
                        )
                        endControlFlow()
                    }
                    endControlFlow()
                }.build(),
            )
        }
        .build()
}

private fun createTextElementEmojiDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = TextElement.Emoji::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.Emoji>())
                    addStatement("// TBD.")
                }.build(),
            )
        }
        .build()
}

private fun createTextElementInlineCodeDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = TextElement.InlineCode::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.InlineCode>())
                    val code = MemberName(COMPOSE_WEB_DOM, "Code")
                    beginControlFlow("%M", code)
                    beginControlFlow("%M(this)", disposableEffect)
                    addStatement(
                        "scopeElement.innerHTML = element.%N",
                        className.member(TextElement.InlineCode::code.name),
                    )
                    addStatement("onDispose { }")
                    endControlFlow()
                    endControlFlow()
                }.build(),
            )
        }
        .build()
}

private fun createLinkAutoLinkDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = Link.AutoLink::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<Link.AutoLink>())
                    val kClass = Link.AutoLink::class
                    val composeElement = MemberName(COMPOSE_WEB_DOM, "A")
                    val text = MemberName(COMPOSE_WEB_DOM, "Text")
                    addCode(
                        buildCodeBlock {
                            beginControlFlow(
                                "%M(href = element.%N.%N)",
                                composeElement,
                                kClass.member(Link.AutoLink::link.name),
                                marktdownLinkValueMemberName,
                            )
                            addStatement("%M(value = element.%N)", text, kClass.member(Link.AutoLink::label.name))
                            endControlFlow()
                        }
                    )
                }.build(),
            )
        }
        .build()
}

private fun createTextElementPlainTextDefaultRenderer(
    packageName: String,
): FileSpec {
    val className = TextElement.PlainText::class.asClassName()
    val fnName = className.simpleNames.joinToString("")
    return FileSpec
        .builder(packageName, fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", type = typeNameOf<TextElement.PlainText>())
                    val text = MemberName(COMPOSE_WEB_DOM, "Text")
                    addCode("%M(value = element.%N)", text, className.member(TextElement.PlainText::text.name))
                }.build(),
            )
        }
        .build()
}

private fun createMarktdownParentCodeBlock(
    marktdownElementMemberName: MemberName,
    holderMemberName: MemberName,
    propertyName: String = "element",
    paramBuilder: (() -> Pair<String, Array<out Any>>)? = null,
): CodeBlock = buildCodeBlock {
    if (paramBuilder == null) {
        beginControlFlow("%M", holderMemberName)
    } else {
        val (paramsStr, params) = paramBuilder()
        beginControlFlow("%M($paramsStr)", holderMemberName, *params)
    }
    addStatement(
        "$propertyName.%N.forEach { %M(it) }",
        MarktdownParent<*>::children.name,
        marktdownElementMemberName,
    )
    endControlFlow()
}

private fun createMarktdownParentCodeBlock(
    marktdownElementMemberName: MemberName,
    tagName: String,
    paramBuilder: (() -> Pair<String, Array<out Any>>)? = null,
): CodeBlock = buildCodeBlock {
    val composeElement = MemberName(COMPOSE_WEB_DOM, "TagElement")
    if (paramBuilder == null) {
        beginControlFlow("%M<%T>(%S, null)", composeElement, htmlElementClassName, tagName)
    } else {
        val (paramsStr, params) = paramBuilder()
        beginControlFlow("%M<%T>(%S, $paramsStr)", composeElement, htmlElementClassName, tagName, *params)
    }
    addStatement(
        "element.%N.forEach { %M(it) }",
        MarktdownParent<*>::children.name,
        marktdownElementMemberName,
    )
    endControlFlow()
}

