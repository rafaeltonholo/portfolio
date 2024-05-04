package dev.tonholo.marktdown.processor.ksp

import androidx.compose.runtime.Composable
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Location
import com.google.devtools.ksp.symbol.NonExistLocation
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import dev.tonholo.marktdown.domain.content.MarktdownElement
import dev.tonholo.marktdown.domain.content.TableElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.marktdown.domain.renderer.TableElementScope
import dev.tonholo.marktdown.processor.extensions.defaultIndent
import dev.tonholo.marktdown.processor.extensions.fileSpecOf
import dev.tonholo.marktdown.processor.extensions.function
import dev.tonholo.marktdown.processor.extensions.imports
import dev.tonholo.marktdown.processor.extensions.indented
import dev.tonholo.marktdown.processor.extensions.simpleName
import dev.tonholo.marktdown.processor.extensions.toMarktdownKClass
import dev.tonholo.marktdown.processor.ksp.extensions.argumentWithName
import dev.tonholo.marktdown.processor.ksp.extensions.findAnnotation
import dev.tonholo.marktdown.processor.renderer.RendererGenerator
import dev.tonholo.marktdown.processor.renderer.compose.html.ComposeHtmlRendererGenerator
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.reflect.KClass

private const val SCOPE_DELIMITER_START = "// [DEFAULT_SCOPE_INIT]"

class MarktdownCustomRendererProcessor(
    private val config: MarktdownCustomRendererConfig,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    private lateinit var rendererGenerator: RendererGenerator
    private val visitor = MarktdownCustomRendererVisitor()

    private val declarations = mutableListOf<KSFunctionDeclaration>()
    private val resolvedReceivers = mutableSetOf<KClass<out MarktdownElement>>()
    private val previousRoundDeclarations = mutableSetOf<KSFunctionDeclaration>()
    private var generatedInPreviousRounds = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("called process with declarations = $declarations, previousRoundDeclarations = $previousRoundDeclarations, generatedInPreviousRounds = $generatedInPreviousRounds")
        if (generatedInPreviousRounds) {
            logger.warn("Skipping code generation if is a new round and did already generated previously")
            return emptyList()
        }

        declarations.clear()

        resolver.getSymbolsWithAnnotation(MarktdownRenderer.Custom::class.qualifiedName.toString())
            .filter { it is KSFunctionDeclaration && it.validate() }
            .forEach { it.accept(visitor, Unit) }

        val roundDeclaration = declarations.filterNot { declaration ->
            previousRoundDeclarations.any { it.qualifiedName == declaration.qualifiedName }
        }
        previousRoundDeclarations += declarations

        val errors = validate(roundDeclaration)
        if (errors.isNotEmpty()) {
            errors.forEach { logger.error(it.message) }
            return emptyList()
        }

        rendererGenerator = ComposeHtmlRendererGenerator(
            packageName = config.packageName,
            elementsToRender = resolvedReceivers,
        )

        roundDeclaration.forEach(::generateFile)
        generatedInPreviousRounds = true
        return emptyList()
    }

    private fun validate(functions: List<KSFunctionDeclaration>): List<Error> {
        val receivers = mutableMapOf<KSType, MutableList<KSFunctionDeclaration>>()
        val errors = buildList {
            for (function in functions) {
                val receiver = function.extensionReceiver
                if (receiver == null) {
                    add(Error.ReceiverMissing(function))
                    continue
                }

                val receiverArgument = receiver.element
                    ?.typeArguments
                    ?.firstOrNull()
                    ?.type
                    ?.resolve()

                if (receiverArgument == null) {
                    add(Error.ReceiverMissingTypeArgument(function))
                    continue
                }

                receiverArgument.toMarktdownKClass()?.let {
                    resolvedReceivers += it
                } ?: logger.warn("Could not find MarktdownElement class for $receiverArgument")

                val annotation = function.findAnnotation<MarktdownRenderer.Custom>()
                val target = annotation?.argumentWithName(MarktdownRenderer.Custom::type.name) ?: continue

                // TODO: verify if scope.drawContent() is being called when MarktdownParent
                if (receiverArgument != target) {
                    add(
                        Error.ReceiverMissingCompatibleElementType(
                            element = target,
                            receiverArgument = receiverArgument,
                            function = function,
                        ),
                    )

                    continue
                }
                receivers.getOrPut(receiverArgument) {
                    mutableListOf()
                }.add(function)
            }

            val multipleReceivers = receivers.filter { it.value.size > 1 }
            if (multipleReceivers.isNotEmpty()) {
                for ((element, fnDeclarations) in multipleReceivers) {
                    add(
                        Error.MultipleCustomRenderer(
                            element = element,
                            functions = fnDeclarations,
                        )
                    )
                }
            }
        }

        return when {
            errors.isNotEmpty() -> errors
            else -> emptyList()
        }
    }

    data class DefaultRendererFile(
        val target: KSType,
        val location: Location,
        val function: KSFunctionDeclaration,
    )

    private fun generateFile(function: KSFunctionDeclaration) {
        logger.warn("generateFile() called with: function = $function")

        if (generatedInPreviousRounds) {
            logger.warn("Skipping since generated in another round.")
            return
        }

        val annotation = function.findAnnotation<MarktdownRenderer.Custom>()
        val target = annotation?.argumentWithName(MarktdownRenderer.Custom::type.name) ?: return

        val defaultRenderer = rendererGenerator.generateElementMap()[target.toMarktdownKClass()] ?: return
        val pkg = config.packageName.replace(".", "/")
        val targetFilename = buildString {
            target.declaration.parentDeclaration?.simpleName?.asString()?.let(::append)
            append(target.declaration.simpleName.asString())
        }
        val defaultRendererPath = Path(
            "${config.defaultRendererPath}/$pkg/renderer/content/$targetFilename.kt"
        )
        val defaultRendererImports = defaultRenderer.toBuilder().imports
        val defaultRendererContent = defaultRenderer
            .members
            .asSequence()
            .filterIsInstance<FunSpec>()
            .first()
            .body
            .toString()
            .splitToSequence("\n")
            .takeWhile { it.trim() != SCOPE_DELIMITER_START }
            .map { it.replace(MarktdownElementScope::class.java.name, MarktdownElementScope::class.java.simpleName) }
            .map { line ->
                // TODO: Deal with full qualifier in Composable calls
                defaultRendererImports.fold(initial = line) { acc, current ->
                    acc.replace(current.qualifiedName, current.simpleName)
                }
            }
            .joinToString("\n")
        defaultRendererPath.deleteIfExists()

        val customRenderPkg = "${config.packageName}.renderer.content"
        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(
                aggregating = false,
                function.containingFile!!
            ),
            packageName = customRenderPkg,
            fileName = targetFilename,
        )

        file.use { output ->
            val content = buildString {
                val type = ClassName(
                    packageName = buildString {
                        append(target.declaration.packageName.asString())
                        target.declaration
                            .parentDeclaration
                            ?.simpleName
                            ?.asString()
                            ?.let { simpleName ->
                                append(".$simpleName")
                            }
                    },
                    target.declaration.simpleName.asString(),
                )

                fileSpecOf(ClassName(customRenderPkg, targetFilename)) {
                    defaultIndent()
                    imports(defaultRendererImports)
                    imports(
                        type,
                        if (target.declaration.simpleName.asString() == TableElement::class.simpleName) {
                            TableElementScope::class.asClassName()
                        } else {
                            MarktdownElementScope::class.asClassName()
                        },
                    )

                    function(targetFilename) {
                        addAnnotation(Composable::class)
                        addModifiers(KModifier.INTERNAL)
                        addParameter(name = "element", type)
                        addCode(
                            buildCodeBlock {
                                add(defaultRendererContent.trimIndent())
                                addStatement("")
                                addStatement(
                                    "scope.%M()",
                                    MemberName(function.packageName.asString(), function.simpleName.asString()),
                                )
                            }
                        )
                    }
                }.writeTo(this)
            }
            output.write(content.toByteArray())
        }
    }

    inner class MarktdownCustomRendererVisitor : KSVisitorVoid() {
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            declarations += function
        }
    }
}

class MarktdownSymbolProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = with(environment) {
        MarktdownCustomRendererProcessor(
            config = MarktdownCustomRendererConfig(
                packageName = options[ARG_PACKAGE_NAME] ?: error("package name is required."),
                defaultRendererPath = options[ARG_DEFAULT_RENDERER_PATH]?.let {
                    Path(it)
                } ?: error("defaultRendererPath name is required."),
            ),
            codeGenerator = codeGenerator,
            logger = logger,
        )
    }

    companion object {
        const val ARG_PACKAGE_NAME = "marktdown.packageName"
        const val ARG_DEFAULT_RENDERER_PATH = "marktdown.defaultRendererPath"
    }
}

sealed class Error(
    val message: String,
) {
    data class ReceiverMissing(
        val function: KSFunctionDeclaration,
    ) : Error(
        message = buildString {
            appendLine("Missing required function receiver.")
            appendLine("All custom renderers should have 'MarktdownElementScope' as a function receiver.")
        }
    )

    data class ReceiverMissingTypeArgument(
        val function: KSFunctionDeclaration,
    ) : Error(
        message = "Receiver is missing type argument",
    )

    data class ReceiverMissingCompatibleElementType(
        val element: KSType,
        val receiverArgument: KSType,
        val function: KSFunctionDeclaration,
    ) : Error(
        message = buildString {
            appendLine("The MarktdownElementScope doesn't have a compatible Element type.")
            appendLine("Expected: ${element.declaration.qualifiedName?.asString()}")
            appendLine("Actual: ${receiverArgument.declaration.qualifiedName?.asString()}")
        }
    )

    data class MultipleCustomRenderer(
        val element: KSType,
        val functions: List<KSFunctionDeclaration>,
    ) : Error(message = buildString {
        appendLine("Found multiple custom renderers for the same element")
        appendLine("Element: '${element.declaration.qualifiedName?.asString()}'")
        appendLine("Custom renderers:")
        functions.forEach { f ->
            appendLine(f.simpleName.asString().indented(4))
            when (val location = f.location) {
                is FileLocation -> append("on ${location.filePath}, Line ${location.lineNumber}")
                NonExistLocation -> Unit
            }
        }
    })
}
