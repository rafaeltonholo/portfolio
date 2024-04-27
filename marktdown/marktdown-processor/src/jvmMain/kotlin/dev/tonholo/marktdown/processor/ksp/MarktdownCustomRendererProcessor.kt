package dev.tonholo.marktdown.processor.ksp

import androidx.compose.runtime.Composable
import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

class MarktdownCustomRendererProcessor(
    private val config: MarktdownCustomRendererConfig,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    private val visitor = MarktdownCustomRendererVisitor()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("Process")
        val customRenderers = resolver.getSymbolsWithAnnotation(
            annotationName = MarktdownRenderer.Custom::class.qualifiedName.toString(),
        )
        val invalidSymbols = customRenderers.filterNot { it.validate() }.toList()
        logger.warn("invalidSymbols = $invalidSymbols")

        customRenderers
            .filter { it is KSFunctionDeclaration && it.validate() }
            .forEach { it.accept(visitor, Unit) }
        return invalidSymbols
    }

    inner class MarktdownCustomRendererVisitor : KSVisitorVoid() {
        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            logger.warn("visitFunctionDeclaration() called with: function = $function, data = $data")
            super.visitFunctionDeclaration(function, data)
            val annotationTypeParam = function.annotations
                .firstOrNull { it.shortName.getShortName() == MarktdownRenderer.Custom::class.simpleName }
                ?.arguments
                ?.firstOrNull()
                ?.value as KSType

            val isRequiredParameterMissing = function.parameters.none {
                val paramType = it.type.resolve()
                it.name?.getShortName() == "element" && paramType == annotationTypeParam
            }

            if (isRequiredParameterMissing) {
                error("Missing required parameter on ${function.qualifiedName?.asString()}")
            }

            logger.warn("Function = ${function.qualifiedName?.asString()}")
            logger.warn("annotationTypeParam = $annotationTypeParam")
            logger.warn("annotationTypeParam.declaration = ${annotationTypeParam.declaration}")
            logger.warn("annotationTypeParam.declaration.closestClassDeclaration() = ${annotationTypeParam.declaration.closestClassDeclaration()}")
            logger.warn("annotationTypeParam.declaration.parentDeclaration = ${annotationTypeParam.declaration.parentDeclaration}")
            logger.warn("annotationTypeParam.declaration.qualifiedName = ${annotationTypeParam.declaration.qualifiedName?.asString()}")
            logger.warn("annotationTypeParam.declaration.packageName = ${annotationTypeParam.declaration.packageName.asString()}")
            logger.warn("annotationTypeParam.declaration.packageName = ${annotationTypeParam.declaration.simpleName.asString()}")

            val pkg = config.packageName.replace(".", "/")
            val targetFile = buildString {
                annotationTypeParam.declaration.parentDeclaration?.simpleName?.asString()?.let(::append)
                append(annotationTypeParam.declaration.simpleName.asString())
            }
            val defaultRendererPath = Path(
                "${config.defaultRendererPath}/$pkg/renderer/content/$targetFile.kt"
            )
            defaultRendererPath.deleteIfExists()

            val customRenderPkg = "${config.packageName}.renderer.content"
            val file = codeGenerator.createNewFile(
                Dependencies(false, function.containingFile!!),
                packageName = customRenderPkg,
                targetFile,
            )

            file.use {
                val content = buildString {
                    val type = ClassName(
                        packageName = buildString {
                            append(annotationTypeParam.declaration.packageName.asString())
                            annotationTypeParam.declaration
                                .parentDeclaration
                                ?.simpleName
                                ?.asString()
                                ?.let { simpleName ->
                                    append(".$simpleName")
                                }
                        },
                        annotationTypeParam.declaration.simpleName.asString(),
                    )
                    FileSpec.builder(customRenderPkg, targetFile)
                        .addImport(type.packageName, type.simpleName)
                        .addFunction(
                            FunSpec.builder(targetFile).apply {
                                addAnnotation(Composable::class)
                                addModifiers(KModifier.INTERNAL)
                                addParameter(name = "element", type)
                                addCode(
                                    "%M(element = element)",
                                    MemberName(function.packageName.asString(), function.simpleName.asString())
                                )
                            }.build(),
                        )
                        .build()
                        .writeTo(this)
                }
                it.write(content.toByteArray())
            }
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
