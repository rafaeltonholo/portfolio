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
import com.squareup.kotlinpoet.asTypeName
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
import dev.tonholo.marktdown.processor.renderer.RendererGenerator
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

class ComposeHtmlRendererGenerator(
    packageName: String,
    elementsToRender: Set<KClass<out MarktdownElement>>,
) : RendererGenerator(packageName, elementsToRender) {
    private val marktdownElementMemberName = MemberName(this.packageName, MarktdownElement::class.java.simpleName)
    private val marktdownDocumentMemberName = MemberName(this.packageName, "Marktdown")
    override fun createMarktdownDocumentRenderer(): FileSpec = FileSpec.builder(marktdownDocumentMemberName).apply {
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

    override fun createMarktdownElementRenderer(): FileSpec {
        return FileSpec.builder(marktdownElementMemberName).apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(marktdownElementMemberName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    addParameter(name = "element", typeNameOf<MarktdownElement>())
                    val packageName = "${this@ComposeHtmlRendererGenerator.packageName}.content"
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

    override fun KClass<out CodeFence>.createCodeFenceDefaultRenderer(): FileSpec = baseFileSpec {
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
    }

    override fun KClass<out HorizontalRule>.createHorizontalRuleDefaultRenderer(): FileSpec = baseFileSpec {
        val hr = MemberName(COMPOSE_WEB_DOM, "Hr")
        addCode("%M()", hr)
    }

    override fun KClass<out ImageElement>.createImageElementDefaultRenderer(): FileSpec = baseFileSpec {
        val image = MemberName(COMPOSE_WEB_DOM, "Img")
        addCode(
            """
                |%M(
                |   src = element.%N.%N,
                |   alt = element.%N.orEmpty(),
                |)
            """.trimMargin(),
            image,
            member(ImageElement::link.name),
            marktdownLinkValueMemberName,
            member(ImageElement::alt.name),
        )
    }

    override fun KClass<out LineBreak>.createLineBreakDefaultRenderer(): FileSpec = baseFileSpec {
        val br = MemberName(COMPOSE_WEB_DOM, "Br")
        addCode("%M()", br)
    }

    override fun KClass<out LinkDefinition>.createLinkDefinitionDefaultRenderer(): FileSpec = baseFileSpec {
        addCode("// TBD.")
    }

    override fun KClass<out TableElement>.createTableElementDefaultRenderer(): FileSpec = baseFileSpec {
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
                    member(TableElement::rows.name),
                )
                beginControlFlow("%M", tr)
                add(cellBlock("row.cells", td))
                endControlFlow()
                endControlFlow()
                endControlFlow()
                endControlFlow()
            }.build(),
        )
    }

    override fun KClass<out ListElement.ListItem>.createListItemDefaultRenderer(): FileSpec = baseFileSpec {
        val li = MemberName(COMPOSE_WEB_DOM, "Li")
        addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, li))
    }

    override fun KClass<out ListElement.Ordered>.createOrderedDefaultRenderer(): FileSpec = baseFileSpec {
        val ol = MemberName(COMPOSE_WEB_DOM, "Ol")
        addCode(
            buildCodeBlock {
                addStatement("%M(", ol)
                withIndent {
                    addStatement(
                        "attrs = element.%N?.let { startWith ->",
                        member(ListElement.Ordered::startWith.name),
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
            },
        )
    }

    override fun KClass<out ListElement.Task>.createTaskDefaultRenderer(): FileSpec = baseFileSpec {
        addCode("// TBD.")
    }

    override fun KClass<out ListElement.Unordered>.createUnorderedDefaultRenderer(): FileSpec = baseFileSpec {
        val ul = MemberName(COMPOSE_WEB_DOM, "Ul")
        addCode(
            buildCodeBlock {
                beginControlFlow("%M", ul)
                addStatement(
                    "element.%N.forEach { %M(it) }",
                    ListElement.Unordered::options.name,
                    marktdownElementMemberName,
                )
                endControlFlow()
            },
        )
    }

    override fun KClass<out TextElement.Blockquote>.createBlockquoteDefaultRenderer(): FileSpec = baseFileSpec {
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
    }

    override fun KClass<out Link.Element>.createElementDefaultRenderer(): FileSpec = baseFileSpec {
        val link = MemberName(COMPOSE_WEB_DOM, "A")
        addCode(
            createMarktdownParentCodeBlock(
                marktdownElementMemberName,
                link
            ) {
                "href = element.%N.%N" to arrayOf(
                    member(Link.Element::link.name),
                    marktdownLinkValueMemberName,
                )
            }
        )
    }

    override fun KClass<out TextElement.EmphasisText>.createEmphasisTextDefaultRenderer(): FileSpec = baseFileSpec {
        val composeElement = MemberName(COMPOSE_WEB_DOM, "Em")
        addCode(
            createMarktdownParentCodeBlock(
                marktdownElementMemberName,
                composeElement,
            )
        )
    }

    override fun KClass<out TextElement.Highlight>.createHighlightDefaultRenderer(): FileSpec = baseFileSpec {
        addCode(
            createMarktdownParentCodeBlock(
                marktdownElementMemberName,
                tagName = "mark"
            )
        )
    }

    override fun KClass<out TextElement.Paragraph>.createParagraphDefaultRenderer(): FileSpec = baseFileSpec {
        val composeElement = MemberName(COMPOSE_WEB_DOM, "P")
        addCode(
            createMarktdownParentCodeBlock(
                marktdownElementMemberName,
                composeElement,
            )
        )
    }

    override fun KClass<out Link.ReferenceElement>.createReferenceElementDefaultRenderer(): FileSpec = baseFileSpec {
        val composeElement = MemberName(COMPOSE_WEB_DOM, "A")
        addCode(
            createMarktdownParentCodeBlock(
                marktdownElementMemberName,
                composeElement,
            ) {
                "href = element.%N.%N" to arrayOf(
                    member(Link.ReferenceElement::destination.name),
                    marktdownLinkValueMemberName,
                )
            }
        )
    }

    override fun KClass<out TextElement.Strikethrough>.createStrikethroughDefaultRenderer(): FileSpec = baseFileSpec {
        addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, tagName = "mark"))
    }

    override fun KClass<out TextElement.StrongText>.createStrongTextDefaultRenderer(): FileSpec = baseFileSpec {
        val composeElement = MemberName(COMPOSE_WEB_DOM, "B")
        addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, composeElement))
    }

    override fun KClass<out TextElement.Subscript>.createSubscriptDefaultRenderer(): FileSpec = baseFileSpec {
        val composeElement = MemberName(COMPOSE_WEB_DOM, "Sub")
        addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, composeElement))
    }

    override fun KClass<out TextElement.Superscript>.createSuperscriptDefaultRenderer(): FileSpec = baseFileSpec {
        val composeElement = MemberName(COMPOSE_WEB_DOM, "Sup")
        addCode(createMarktdownParentCodeBlock(marktdownElementMemberName, composeElement))
    }

    override fun KClass<out TextElement.Title>.createTitleDefaultRenderer(): FileSpec = baseFileSpec {
        beginControlFlow("when (element.%N)", member(TextElement.Title::style.name))
        for (style in TextElement.Title.Style.entries) {
            beginControlFlow(
                "%M -> %M",
                TextElement.Title.Style::class.member(style.name),
                MemberName(COMPOSE_WEB_DOM, style.name),
            )
            addStatement(
                "element.%N.forEach { %M(it) }",
                member(TextElement.Title::children.name),
                marktdownElementMemberName,
            )
            endControlFlow()
        }
        endControlFlow()
    }

    override fun KClass<out TextElement.Emoji>.createEmojiDefaultRenderer(): FileSpec = baseFileSpec {
        addStatement("// TBD.")
    }

    override fun KClass<out TextElement.InlineCode>.createInlineCodeDefaultRenderer(): FileSpec = baseFileSpec {
        val code = MemberName(COMPOSE_WEB_DOM, "Code")
        beginControlFlow("%M", code)
        beginControlFlow("%M(this)", disposableEffect)
        addStatement(
            "scopeElement.innerHTML = element.%N",
            member(TextElement.InlineCode::code.name),
        )
        addStatement("onDispose { }")
        endControlFlow()
        endControlFlow()
    }

    override fun KClass<out Link.AutoLink>.createAutoLinkDefaultRenderer(): FileSpec = baseFileSpec {
        val composeElement = MemberName(COMPOSE_WEB_DOM, "A")
        val text = MemberName(COMPOSE_WEB_DOM, "Text")
        addCode(
            buildCodeBlock {
                beginControlFlow(
                    "%M(href = element.%N.%N)",
                    composeElement,
                    member(Link.AutoLink::link.name),
                    marktdownLinkValueMemberName,
                )
                addStatement("%M(value = element.%N)", text, member(Link.AutoLink::label.name))
                endControlFlow()
            }
        )
    }

    override fun KClass<out TextElement.PlainText>.createPlainTextDefaultRenderer(): FileSpec = baseFileSpec {
        val text = MemberName(COMPOSE_WEB_DOM, "Text")
        addCode("%M(value = element.%N)", text, member(TextElement.PlainText::text.name))
    }

    private inline fun <reified T : MarktdownElement> KClass<out T>.baseFileSpec(
        fnContent: FunSpec.Builder.() -> Unit
    ): FileSpec = FileSpec
        .builder(packageName.plus(".content"), fnName)
        .apply {
            defaultIndent()
            addFunction(
                FunSpec.builder(fnName).apply {
                    addAnnotation(Composable::class)
                    addAnnotation(MarktdownRenderer.Default::class)
                    addModifiers(KModifier.INTERNAL)
                    if (objectInstance == null) {
                        addParameter(name = "element", type = asTypeName())
                    }
                    fnContent()
                }.build(),
            )
        }
        .build()

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
            beginControlFlow(
                controlFlow = "%M<%T>(%S, $paramsStr)",
                composeElement,
                htmlElementClassName,
                tagName,
                *params
            )
        }
        addStatement(
            "element.%N.forEach { %M(it) }",
            MarktdownParent<*>::children.name,
            marktdownElementMemberName,
        )
        endControlFlow()
    }
}
