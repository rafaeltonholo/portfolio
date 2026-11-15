package dev.tonholo.a2ui.express

import kotlinx.serialization.json.JsonPrimitive

internal sealed interface Statement {
    val line: Int

    data class Component(val name: String, val value: Expression, override val line: Int) : Statement

    data class Data(val path: String, val value: Expression, override val line: Int) : Statement
}

internal sealed interface Expression {
    val line: Int

    data class Text(val value: String, override val line: Int) : Expression

    data class Number(val literal: String, override val line: Int) : Expression {
        fun toJson(): JsonPrimitive = literal.toLongOrNull()?.let(::JsonPrimitive) ?: JsonPrimitive(literal.toDouble())
    }

    data class Bool(val value: Boolean, override val line: Int) : Expression

    data class Null(override val line: Int) : Expression

    data class Skip(override val line: Int) : Expression

    data class Path(val path: String, override val line: Int) : Expression

    data class Reference(val name: String, override val line: Int) : Expression

    data class ListOf(val items: List<Expression>, override val line: Int) : Expression

    data class MapOf(val entries: List<Pair<String, Expression>>, override val line: Int) : Expression

    data class Call(
        val name: String,
        val positional: List<Expression>,
        val keywords: List<Pair<String, Expression>>,
        override val line: Int,
    ) : Expression
}
