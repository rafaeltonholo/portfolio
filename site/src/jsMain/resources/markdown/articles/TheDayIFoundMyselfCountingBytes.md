---
title: The day I found myself counting bytes, literally.
date: dec 16, 2024
authors:
    -   name: rafael
        avatar: https://rafael.tonholo.dev/images/writing.jpg
description: >
    The story of debugging a JVM MethodTooLargeException in svg-to-compose when generating the 
    Brazilian flag, and how counting bytecode bytes solved Compose Previews.
tags:
    - compose
    - kotlin
    - tooling
---

## Introduction

I have an interesting story to share from the day I found myself counting how many bytes of JVM bytecode instructions a
code generator was producing. I'll try to keep it concise, but no promises.

When writing functions by hand, we strive to keep them small, focused, and maintainable. But what about generated code?

Do we even care about the length of a generated method that developers rarely inspect? I never did—until I encountered
this:

```
org.objectweb.asm.MethodTooLargeException: Method too large: dev/tonholo/svgToCompose/playground/ui/icon/jvm/svg/BrasilKt.getBrasil() Landroidx/compose/ui/graphics/vector/ImageVector;
    at org.objectweb.asm.MethodWriter.computeMethodInfoSize(MethodWriter.java:2088)
    at org.objectweb.asm.ClassWriter.toByteArray(ClassWriter.java:512)
    at kotlin.sequences.TransformingSequence$iterator$1.next(Sequences.kt:210)
    at kotlin.sequences.FilteringSequence$iterator$1.calcNext(Sequences.kt:170)
    at kotlin.sequences.FilteringSequence$iterator$1.hasNext(Sequences.kt:194)
    at kotlin.sequences.SequencesKt___SequencesKt.firstOrNull(_Sequences.kt:172)
```

## When generated code throws `MethodTooLargeException`

Some context first. I was building [svg-to-compose](https://github.com/rafaeltonholo/svg-to-compose), a CLI tool and
Gradle plugin that converts Vector Graphics (SVG or Android Vector Drawables) into Jetpack Compose `ImageVector` code,
similar to how Google authored Material Icons.

While writing the parser from scratch, someone opened an issue asking for support for a specific SVG: the British flag.
I added support and included the flag as a sample in the repository. Naturally, I decided to add my beloved Brazilian
flag to the samples as well.

The Brazilian flag is surprisingly complex: a green field, a yellow rhombus, a blue celestial globe with 27 five-pointed
stars across 9 constellations, and the curved white banner with the national motto.

After implementing support for the remaining SVG tags: the Android Studio Preview stopped working completely. It crashed
with `MethodTooLargeException`.

I had never seen that exception before. My initial assumption was simple: the generated function was simply too many
lines of code. If I split it across multiple lines, surely the issue would disappear.

Except it didn't. When inspecting other icons I had generated previously, several had even more lines in their Kotlin
properties without throwing any exceptions at all. Lines of code were not the problem.

## The 64 KiB JVM bytecode limit

The problem wasn't Kotlin source lines—it was Java Virtual Machine bytecode.

According to
the [Java Virtual Machine Specification (Section 4.7.3)](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.7.3),
the bytecode of any method cannot exceed `65,535` bytes (64 KiB):

```
Code_attribute {
    u2 attribute_name_index;
    u4 attribute_length;
    u2 max_stack;
    u2 max_locals;
    u4 code_length; // unsigned 16-bit integer: max 65535 bytes!
    u1 code[code_length];
    ...
}
```

Google's `ImageVector.Builder` pattern generates a single top-level property with an accessor:

```kotlin
val Brasil: ImageVector
get() {
    if (_brasil != null) return _brasil!!
    _brasil = ImageVector.Builder(
        name = "Brasil",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        // hundreds of path commands inlined here!
    }.build()
    return _brasil!!
}
```

When generating an intricate vector like `brasil.svg`, every `moveTo`, `curveTo`, `lineTo`, gradient definition, and
fill attribute inside that single getter emits instructions: loading constants onto the stack, pushing floats, and
invoking builder methods.

To make matters worse, Android Studio's Compose Preview engine uses ASM to instrument composables on the fly. That
instrumentation adds roughly `13 KiB` of synthetic instructions behind the scenes. An icon that compiles fine for an
Android runtime build can easily exceed the 65,535-byte limit inside the IDE Preview!

## Literally counting bytes: `MethodSizeAccountable`

Splitting by raw line count doesn't work because different commands have vastly different bytecode footprints. A single
path with multiple cubic Bézier curves and gradients generates far more bytecode than dozens of simple lines.

To solve this deterministically, I introduced an accounting model directly into `svg-to-compose`:

```kotlin
interface MethodSizeAccountable {
    val approximateByteSize: Int

    companion object {
        // Threshold set to half the 64 KiB JVM limit to leave headroom for Compose Previews
        const val METHOD_SIZE_THRESHOLD = 32767
        const val ICON_BASE_STRUCTURE_BYTE_SIZE = 73
        const val FLOAT_APPROXIMATE_BYTE_SIZE = 4
        const val BOOLEAN_APPROXIMATE_BYTE_SIZE = 2
    }
}
```

Every parsed node and path command implements `MethodSizeAccountable`, estimating its bytecode weight:

- Each path wrapper accounts for its base overhead (~133 bytes) plus parameters and inner commands.
- Groups account for transforms, scale, translation, rotation, and clipping paths (~90 bytes base).
- Float arguments account for `4` bytes each; booleans account for `2` bytes.
- Color brushes and gradient stops add their respective stack costs.

We then sum the estimated bytecode size for the entire icon structure:

> **Total Size** = Base Structure (73 bytes) + Σ (Node Byte Sizes)

## Chunking methods: `NodeChunker`

Once the generator detects that an icon exceeds `METHOD_SIZE_THRESHOLD` (32,767 bytes), it activates `NodeChunker`:

```kotlin
internal class NodeChunker(private val logger: Logger) {
    fun chunkIfNeeded(
        contents: IconFileContents,
        nameResolver: (contents: IconFileContents, index: Int) -> String,
    ): ChunkResult {
        val byteSize = MethodSizeAccountable.ICON_BASE_STRUCTURE_BYTE_SIZE +
                contents.nodes.sumOf { it.approximateByteSize }

        if (byteSize <= MethodSizeAccountable.METHOD_SIZE_THRESHOLD) {
            return ChunkResult(nodes = contents.nodes, chunkFunctions = null)
        }

        val chunks = ceil(byteSize.toFloat() / MethodSizeAccountable.METHOD_SIZE_THRESHOLD).roundToInt()
        val chunkSize = max(1, contents.nodes.size / chunks)

        logger.warn(
            "Potential large icon detected. Splitting icon's content in $chunks chunks to avoid " +
                    "compilation issues without affecting rendering performance.",
        )

        val chunkFunctions = contents.nodes.chunked(chunkSize) { chunk ->
            ImageVectorNode.ChunkFunction(
                functionName = nameResolver(contents, i++),
                nodes = chunk.toList(),
            )
        }

        return ChunkResult(nodes = chunkFunctions, chunkFunctions = chunkFunctions)
    }
}
```

Instead of emitting one massive method, the code generator splits the nodes into separate private helper functions:

```kotlin
val Brasil: ImageVector
get() {
    if (_brasil != null) return _brasil!!
    _brasil = ImageVector.Builder(
        name = "Brasil",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        _brasilChunk1()
        _brasilChunk2()
    }.build()
    return _brasil!!
}

private fun ImageVector.Builder._brasilChunk1() {
    // First partition of path and group commands
}

private fun ImageVector.Builder._brasilChunk2() {
    // Second partition of path and group commands
}
```

## The payoff

With chunking in place:

1. Every individual method stays comfortably under 32 KiB, leaving plenty of room for ASM instrumentation.
2. The Android Studio Preview for `Brasil.svg` works smoothly without any crashes.
3. Zero runtime performance penalty: delegating builder calls across two helper functions is optimized away by the JVM
   JIT and R8/ProGuard.

Building code generators forces you to look past the syntax and think about the bytecode your abstractions produce. It
was the only time in my career where I had to count bytes instruction by instruction—and it turned an obscure compiler
limitation into a robust, automated feature.
