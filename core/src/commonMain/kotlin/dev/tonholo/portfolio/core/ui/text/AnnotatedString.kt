package dev.tonholo.portfolio.core.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import dev.tonholo.portfolio.core.ui.text.AnnotatedString.*

class AnnotatedString(
    val text: String,
    internal val stylesOrNull: List<Range<out Annotation>>? = null,
) : CharSequence by text {
    sealed interface Annotation
    sealed interface Style : Annotation

    /**
     * All [Style] that have been applied to a range of this String
     */
    val styles: List<Range<out Annotation>>
        get() = stylesOrNull ?: emptyList()

    /**
     * Return a substring for the AnnotatedString and include the styles in the range of [startIndex]
     * (inclusive) and [endIndex] (exclusive).
     *
     * @param startIndex the inclusive start offset of the range
     * @param endIndex the exclusive end offset of the range
     */
    override fun subSequence(startIndex: Int, endIndex: Int): AnnotatedString {
        require(startIndex <= endIndex) {
            "start ($startIndex) should be less or equal to end ($endIndex)"
        }
        if (startIndex == 0 && endIndex == text.length) return this
        val text = text.substring(startIndex, endIndex)
        return AnnotatedString(
            text = text,
            stylesOrNull = filterRanges(stylesOrNull, startIndex, endIndex),
        )
    }

    fun calculatedNestedStyles(): Map<Range<out Annotation>, List<Range<out Annotation>>> =
        buildMap<Range<out Annotation>, MutableList<Range<out Annotation>>> {
            var endRange = 0
            var currentStyle = styles.first()
            for (style in styles) {
                if (endRange < style.end) {
                    endRange = style.end
                    currentStyle = style
                    put(currentStyle, mutableListOf())
                    continue
                }
                getValue(currentStyle).add(style)
            }
        }.toMap()

    /**
     * The information attached on the text such as a [SpanStyle].
     *
     * @param item The object attached to [AnnotatedString]s.
     * @param start The start of the range where [item] takes effect. It's inclusive
     * @param end The end of the range where [item] takes effect. It's exclusive
     * @param tag The tag used to distinguish the different ranges. It is useful to store custom
     * data. And [Range]s with same tag can be queried with functions such as [getStringAnnotations].
     */
    @Immutable
    data class Range<T>(val item: T, val start: Int, val end: Int, val tag: String) {
        constructor(item: T, start: Int, end: Int) : this(item, start, end, "")

        init {
            require(start <= end) { "Reversed range is not supported" }
        }
    }

    class Builder(capacity: Int = 16) : Appendable {
        private data class MutableRange<T>(
            val item: T,
            val start: Int,
            var end: Int = Int.MIN_VALUE,
            val tag: String = ""
        ) {
            /**
             * Create an immutable [Range] object.
             *
             * @param defaultEnd if the end is not set yet, it will be set to this value.
             */
            fun toRange(defaultEnd: Int = Int.MIN_VALUE): Range<T> {
                val end = if (end == Int.MIN_VALUE) defaultEnd else end
                check(end != Int.MIN_VALUE) { "Item.end should be set first" }
                return Range(item = item, start = start, end = end, tag = tag)
            }
        }

        var text = StringBuilder(capacity)
        private val styleStack: MutableList<MutableRange<out Annotation>> = mutableListOf()

        /**
         * Holds all objects of type [AnnotatedString.Annotation] including [SpanStyle]s and
         * [ParagraphStyle]s in order.
         */
        private val annotations: MutableList<MutableRange<out Annotation>> = mutableListOf()

        override fun append(value: Char): Builder {
            text.append(value)
            return this
        }

        override fun append(value: CharSequence?): Builder {
            if (value is AnnotatedString) {
                append(value)
            } else {
                text.append(value)
            }
            return this
        }

        override fun append(value: CharSequence?, startIndex: Int, endIndex: Int): Builder {
            if (value is AnnotatedString) {
                append(value, startIndex, endIndex)
            } else {
                text.append(value, startIndex, endIndex)
            }
            return this
        }

        /**
         * Appends the given [AnnotatedString] to this [Builder].
         *
         * @param text the text to append
         */
        fun append(text: AnnotatedString) {
            val start = this.text.length
            this.text.append(text.text)
            // offset every style with start and add to the builder
            text.stylesOrNull?.fastForEach {
                annotations.add(MutableRange(it.item, start + it.start, start + it.end))
            }
        }

        /**
         * Appends the range of [text] between [start] (inclusive) and [end] (exclusive) to this
         * [Builder]. All spans and annotations from [text] between [start] and [end] will be copied
         * over as well.
         *
         * @param start The index of the first character in [text] to copy over (inclusive).
         * @param end The index after the last character in [text] to copy over (exclusive).
         */
        fun append(text: AnnotatedString, start: Int, end: Int) {
            val insertionStart = this.text.length
            this.text.append(text.text, start, end)
            // offset every style with insertionStart and add to the builder
            text.getLocalSpanStyles(start, end)?.fastForEach {
                addStyle(it.item, insertionStart + it.start, insertionStart + it.end)
            }
        }

        /**
         * Set a [Style] for the given [range].
         *
         * @param style [Style] to be applied
         * @param start the inclusive starting offset of the range
         * @param end the exclusive end offset of the range
         */
        fun addStyle(style: Style, start: Int, end: Int) {
            this.annotations.add(MutableRange(item = style, start = start, end = end))
        }

        /**
         * Set a [LinkAnnotation.Url] for the given [range].
         *
         * When clicking on the text in [range], the corresponding URL from the [url] annotation
         * will be opened using [androidx.compose.ui.platform.UriHandler].
         *
         * URLs may be treated specially by screen readers, including being identified while reading
         * text with an audio icon or being summarized in a links menu.
         *
         * @param url A [LinkAnnotation.Url] object that stores the URL being linked to.
         * @param start the inclusive starting offset of the range
         * @param end the exclusive end offset of the range
         * @see getStringAnnotations
         */
        @Suppress("SetterReturnsThis")
        fun addLink(url: LinkAnnotation.Url, start: Int, end: Int) {
            annotations.add(MutableRange(url, start, end))
        }

        /**
         * Applies the given [Style] to any appended text until a corresponding [pop] is
         * called.
         *
         * @sample androidx.compose.ui.text.samples.AnnotatedStringBuilderPushSample
         *
         * @param style generic [Style] to be applied
         */
        fun pushStyle(style: Style): Int {
            MutableRange(item = style, start = text.length).also {
                styleStack.add(it)
                this.annotations.add(it)
            }
            return styleStack.size - 1
        }

        /**
         * Applies the given [ParagraphStyle] to any appended text until a corresponding [pop] is
         * called.
         *
         * @sample androidx.compose.ui.text.samples.AnnotatedStringBuilderPushParagraphStyleSample
         * @param style [ParagraphStyle] to be applied
         */
        fun pushStyle(style: ParagraphStyle): Int {
            MutableRange(item = style, start = text.length, tag = style.tag).also {
                styleStack.add(it)
                this.annotations.add(it)
            }
            return styleStack.size - 1
        }

        /**
         * Attach the given [LinkAnnotation] to any appended text until a corresponding [pop] is
         * called.
         *
         * @param link A [LinkAnnotation] object that stores the URL or clickable tag being linked
         *   to.
         * @see getStringAnnotations
         * @see Range
         */
        @Suppress("BuilderSetStyle")
        fun pushLink(link: LinkAnnotation): Int {
            MutableRange(item = link, start = text.length).also {
                styleStack.add(it)
                annotations.add(it)
            }
            return styleStack.size - 1
        }

        /**
         * Ends the style or annotation that was added via a push operation before.
         *
         * @see pushStyle
         */
        fun pop() {
            check(styleStack.isNotEmpty()) { "Nothing to pop." }
            // pop the last element
            val item = styleStack.removeAt(styleStack.size - 1)
            item.end = text.length
        }

        /**
         * Ends the styles or annotation up to and `including` the [pushStyle]
         * that returned the given index.
         *
         * @param index the result of the a previous [pushStyle] in order
         * to pop to
         *
         * @see pop
         * @see pushStyle
         */
        fun pop(index: Int) {
            check(index < styleStack.size) { "$index should be less than ${styleStack.size}" }
            while ((styleStack.size - 1) >= index) {
                pop()
            }
        }

        /**
         * Constructs an [AnnotatedString] based on the configurations applied to the [Builder].
         */
        fun build(): AnnotatedString = AnnotatedString(
            text = text.toString(),
            stylesOrNull = this.annotations
                .fastMap { it.toRange(text.length) }
                .ifEmpty { null },
        )
    }
}

/**
 * Pushes [style] to the [AnnotatedString.Builder], executes [block] and then pops the [style].
 *
 * @param style [Style] to be applied
 * @param block function to be executed
 *
 * @return result of the [block]
 *
 * @see AnnotatedString.Builder.pushStyle
 * @see AnnotatedString.Builder.pop
 */
inline fun <R : Any> Builder.withStyle(
    style: Style,
    block: Builder.() -> R,
): R {
    val index = pushStyle(style)
    return try {
        block(this)
    } finally {
        pop(index)
    }
}

/**
 * Pushes [style] to the [AnnotatedString.Builder], executes [block] and then pops the [style].
 *
 * @param style [Style] to be applied
 * @param block function to be executed
 *
 * @return result of the [block]
 *
 * @see AnnotatedString.Builder.pushStyle
 * @see AnnotatedString.Builder.pop
 */
inline fun <R : Any> Builder.withStyle(
    style: ParagraphStyle,
    block: Builder.() -> R,
): R {
    val index = pushStyle(style)
    return try {
        block(this)
    } finally {
        pop(index)
    }
}

/**
 * Helper function used to find the [SpanStyle]s in the given paragraph range and also convert the
 * range of those [SpanStyle]s to paragraph local range.
 *
 * @param start The start index of the paragraph range, inclusive
 * @param end The end index of the paragraph range, exclusive
 * @return The list of converted [SpanStyle]s in the given paragraph range
 */
private fun AnnotatedString.getLocalSpanStyles(
    start: Int,
    end: Int
): List<Range<out SpanStyle>>? {
    if (start == end) return null
    val spanStyles = stylesOrNull?.filterIsInstance<Range<out SpanStyle>>() ?: return null
    // If the given range covers the whole AnnotatedString, return SpanStyles without conversion.
    if (start == 0 && end >= this.text.length) {
        return spanStyles
    }
    return spanStyles
        .fastFilter { intersect(start, end, it.start, it.end) }
        .fastMap {
            Range(
                item = it.item,
                start = it.start.coerceIn(start, end) - start,
                end = it.end.coerceIn(start, end) - start,
            )
        }
}


/**
 * Filter the range list based on [Range.start] and [Range.end] to include ranges only in the range
 * of [start] (inclusive) and [end] (exclusive).
 *
 * @param start the inclusive start offset of the text range
 * @param end the exclusive end offset of the text range
 */
private fun <T> filterRanges(ranges: List<Range<out T>>?, start: Int, end: Int): List<Range<T>>? {
    require(start <= end) { "start ($start) should be less than or equal to end ($end)" }
    val nonNullRange = ranges ?: return null

    return nonNullRange.fastFilter { intersect(start, end, it.start, it.end) }.fastMap {
        Range(
            item = it.item,
            start = maxOf(start, it.start) - start,
            end = minOf(end, it.end) - start,
            tag = it.tag
        )
    }.ifEmpty { null }
}

/**
 * Helper function that checks if the range [baseStart, baseEnd) contains the range
 * [targetStart, targetEnd).
 *
 * @return true if [baseStart, baseEnd) contains [targetStart, targetEnd), vice versa.
 * When [baseStart]==[baseEnd] it return true iff [targetStart]==[targetEnd]==[baseStart].
 */
internal fun contains(baseStart: Int, baseEnd: Int, targetStart: Int, targetEnd: Int) =
    (baseStart <= targetStart && targetEnd <= baseEnd) &&
        (baseEnd != targetEnd || (targetStart == targetEnd) == (baseStart == baseEnd))

/**
 * Helper function that checks if the range [lStart, lEnd) intersects with the range
 * [rStart, rEnd).
 *
 * @return [lStart, lEnd) intersects with range [rStart, rEnd), vice versa.
 */
internal fun intersect(lStart: Int, lEnd: Int, rStart: Int, rEnd: Int) =
    maxOf(lStart, rStart) < minOf(lEnd, rEnd) ||
        contains(lStart, lEnd, rStart, rEnd) || contains(rStart, rEnd, lStart, lEnd)

@DslMarker
annotation class AnnotatedStringDsl

@AnnotatedStringDsl
fun annotatedString(builderAction: Builder.() -> Unit): AnnotatedString =
    Builder().apply(builderAction).build()
