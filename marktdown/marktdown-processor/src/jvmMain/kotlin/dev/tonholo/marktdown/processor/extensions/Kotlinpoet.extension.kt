package dev.tonholo.marktdown.processor.extensions

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.Import
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.withIndent

@DslMarker
annotation class KotlinpoetDsl

@KotlinpoetDsl
inline fun <reified T> annotationSpec(builder: AnnotationSpec.Builder.() -> Unit): AnnotationSpec {
    return AnnotationSpec.builder(T::class.asClassName()).apply(builder).build()
}

@KotlinpoetDsl
fun fileSpecOf(className: ClassName, builder: FileSpec.Builder.() -> Unit): FileSpec =
    FileSpec.builder(className).apply(builder).build()

@DslMarker
annotation class FileSpecDsl

@FileSpecDsl
fun FileSpec.Builder.imports(vararg import: ClassName) {
    import.forEach {
        addImport(it.packageName, it.simpleName)
    }
}

@FileSpecDsl
fun FileSpec.Builder.imports(vararg imports: Import) = imports(imports.toList())

@FileSpecDsl
fun FileSpec.Builder.imports(imports: List<Import>) {
    imports.forEach {
        addImport(it)
    }
}

@FileSpecDsl
fun FileSpec.Builder.function(fnName: String, builder: FunSpec.Builder.() -> Unit) =
    addFunction(
        FunSpec.builder(fnName).apply(builder).build()
    )

fun FileSpec.Builder.defaultIndent() = indent(" ".repeat(4))

val Import.simpleName: String
    get() = qualifiedName.takeLastWhile { it != '.' }

@DslMarker
annotation class FunSpecDsl

fun FunSpec.Builder.parameters(vararg parameters: Pair<String, TypeName>) {
    parameters.forEach {
        addParameter(it.first, it.second)
    }
}

fun FunSpec.Builder.parameters(vararg parameters: ParameterSpec) {
    addParameters(parameters.toList())
}

@FunSpecDsl
fun FunSpec.Builder.code(builder: CodeBlock.Builder.() -> Unit) {
    addCode(buildCodeBlock(builder))
}

fun FunSpec.Builder.modifiers(vararg modifiers: KModifier) {
    addModifiers(modifiers.toList())
}

@DslMarker
annotation class ParameterSpecDsl

@ParameterSpecDsl
fun parameter(
    name: String,
    type: TypeName,
    vararg modifiers: KModifier,
    builder: ParameterSpec.Builder.() -> Unit,
): ParameterSpec = ParameterSpec.builder(name, type, *modifiers).apply(builder).build()
