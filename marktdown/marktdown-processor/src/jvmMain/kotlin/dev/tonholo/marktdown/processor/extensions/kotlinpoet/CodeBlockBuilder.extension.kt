package dev.tonholo.marktdown.processor.extensions.kotlinpoet

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.withIndent
import dev.tonholo.marktdown.processor.extensions.KotlinpoetDsl

@KotlinpoetDsl
fun codeBlockBuilder(builder: CodeBlock.Builder.() -> Unit) = CodeBlock.builder().apply(builder)

@DslMarker
annotation class CodeBlockDsl

@CodeBlockDsl
inline fun CodeBlock.Builder.withControlFlow(
    controlFlow: String,
    vararg args: Any?,
    builder: CodeBlock.Builder.() -> Unit,
) {
    beginControlFlow(controlFlow, *args)
    builder()
    endControlFlow()
}

@CodeBlockDsl
inline fun CodeBlock.Builder.withNullableLet(
    statement: String,
    itName: String? = null,
    vararg args: Any?,
    builder: CodeBlock.Builder.() -> Unit,
) {
    addStatement("$statement?.let {${itName?.let { " $it ->" }.orEmpty()}", *args)
    withIndent {
        builder()
    }
    addStatement("}")
}

@CodeBlockDsl
inline fun <reified T> CodeBlock.Builder.withConstructor(
    argName: String? = null,
    trailingComma: Boolean = false,
    builder: CodeBlock.Builder.() -> Unit,
) {
    if (argName == null) {
        addStatement("%T(", T::class)
    } else {
        addStatement("%N = %T(", argName, T::class)
    }
    withIndent {
        builder()
    }
    addStatement(")${",".takeIf { trailingComma }.orEmpty()}")
}

@CodeBlockDsl
inline fun CodeBlock.Builder.withSet(
    argName: String? = null,
    trailingComma: Boolean = false,
    builder: CodeBlock.Builder.() -> Unit,
) {
    val setOfMemberName = MemberName("kotlin.collections", "setOf")
    if (argName == null) {
        addStatement("%M(", setOfMemberName)
    } else {
        addStatement("%N = %M(", argName, setOfMemberName)
    }
    withIndent {
        builder()
    }
    addStatement(")${",".takeIf { trailingComma }.orEmpty()}")
}

@CodeBlockDsl
inline fun CodeBlock.Builder.withMap(
    argName: String? = null,
    trailingComma: Boolean = false,
    builder: CodeBlock.Builder.() -> Unit,
) {
    val memberName = MemberName("kotlin.collections", "mapOf")
    if (argName == null) {
        addStatement("%M(", memberName)
    } else {
        addStatement("%N = %M(", argName, memberName)
    }
    withIndent {
        builder()
    }
    addStatement(")${",".takeIf { trailingComma }.orEmpty()}")
}
