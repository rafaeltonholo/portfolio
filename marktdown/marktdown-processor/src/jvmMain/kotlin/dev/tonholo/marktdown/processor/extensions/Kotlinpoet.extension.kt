package dev.tonholo.marktdown.processor.extensions

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.Import
import com.squareup.kotlinpoet.asClassName

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
annotation class FileSpecDs

@FileSpecDs
fun FileSpec.Builder.imports(vararg import: ClassName) {
    import.forEach {
        addImport(it.packageName, it.simpleName)
    }
}

@FileSpecDs
fun FileSpec.Builder.imports(vararg imports: Import) = imports(imports.toList())

@FileSpecDs
fun FileSpec.Builder.imports(imports: List<Import>) {
    imports.forEach {
        addImport(it)
    }
}

@FileSpecDs
fun FileSpec.Builder.function(fnName: String, builder: FunSpec.Builder.() -> Unit) =
    addFunction(
        FunSpec.builder(fnName).apply(builder).build()
    )

fun FileSpec.Builder.defaultIndent() = indent(" ".repeat(4))

val Import.simpleName: String
    get() = qualifiedName.takeLastWhile { it != '.' }
