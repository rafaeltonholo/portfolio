package dev.tonholo.portfolio.core.collections

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableList<T>(val source: List<T>) : List<T> by source

fun <T> List<T>.toImmutable(): ImmutableList<T> = ImmutableList(this)

fun <T> immutableListOf(vararg elements: T): ImmutableList<T> = elements.toList().toImmutable()
