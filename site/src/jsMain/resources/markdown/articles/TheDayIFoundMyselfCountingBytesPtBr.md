---
routeOverride: /articles/pt-br/the-day-i-found-myself-counting-bytes
slug: the-day-i-found-myself-counting-bytes
locale: pt-BR
title: 'O dia em que me vi contando bytes, literalmente.'
date: 16 de dez. de 2024
description: A história de depurar uma MethodTooLargeException da JVM no svg-to-compose ao gerar a bandeira do Brasil, e como contar bytes de bytecode resolveu os Compose Previews.
tags:
  - compose
  - kotlin
  - tooling
---

## Introdução

Tenho uma história interessante para compartilhar do dia em que me vi contando quantos bytes de instruções de bytecode da JVM um gerador de código estava produzindo. Vou tentar ser conciso, mas sem promessas.

Ao escrever funções manualmente, nos esforçamos para mantê-las pequenas, focadas e fáceis de manter. Mas e quanto ao código gerado?

Será que nos importamos com o tamanho de um método gerado que os desenvolvedores raramente inspecionam? Eu nunca me importei — até me deparar com isto:

```
org.objectweb.asm.MethodTooLargeException: Method too large: dev/tonholo/svgToCompose/playground/ui/icon/jvm/svg/BrasilKt.getBrasil() Landroidx/compose/ui/graphics/vector/ImageVector;
    at org.objectweb.asm.MethodWriter.computeMethodInfoSize(MethodWriter.java:2088)
    at org.objectweb.asm.ClassWriter.toByteArray(ClassWriter.java:512)
    at kotlin.sequences.TransformingSequence$iterator$1.next(Sequences.kt:210)
    at kotlin.sequences.FilteringSequence$iterator$1.calcNext(Sequences.kt:170)
    at kotlin.sequences.FilteringSequence$iterator$1.hasNext(Sequences.kt:194)
    at kotlin.sequences.SequencesKt___SequencesKt.firstOrNull(_Sequences.kt:172)
```

## Quando o código gerado lança `MethodTooLargeException`

Um pouco de contexto primeiro. Eu estava desenvolvendo o [svg-to-compose](https://github.com/rafaeltonholo/svg-to-compose), uma ferramenta de linha de comando e plugin Gradle que converte Vector Graphics (SVG ou Android Vector Drawables) em código `ImageVector` do Jetpack Compose, de forma semelhante a como o Google criou os Material Icons.

Enquanto escrevia o parser do zero, alguém abriu uma issue pedindo suporte para um SVG específico: a bandeira do Reino Unido. Adicionei o suporte e incluí a bandeira como exemplo no repositório. Naturalmente, decidi adicionar a nossa bandeira do Brasil aos exemplos também.

A bandeira brasileira é surpreendentemente complexa: um campo verde, um losango amarelo, uma esfera celeste azul com 27 estrelas de cinco pontas distribuídas em 9 constelações, e a faixa branca curvada com o lema nacional.

Depois de implementar o suporte para as tags de SVG restantes: o Android Studio Preview parou de funcionar completamente. Ele travava com `MethodTooLargeException`.

Eu nunca tinha visto essa exceção antes. Minha suposição inicial foi simples: a função gerada tinha simplesmente muitas linhas de código. Se eu a dividisse em várias linhas, com certeza o problema desapareceria.

Só que não desapareceu. Ao inspecionar outros ícones que eu havia gerado anteriormente, vários tinham ainda mais linhas em suas propriedades Kotlin sem lançar nenhuma exceção. A quantidade de linhas de código não era o problema.

## O limite de 64 KiB de bytecode da JVM

O problema não eram as linhas de código Kotlin — era o bytecode da Java Virtual Machine.

De acordo com a [Java Virtual Machine Specification (Seção 4.7.3)](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.7.3), o bytecode de qualquer método não pode ultrapassar `65.535` bytes (64 KiB):

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

O padrão `ImageVector.Builder` do Google gera uma propriedade única de nível superior com um getter:

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
            // centenas de comandos de path embutidos aqui!
        }.build()
        return _brasil!!
    }
```

Ao gerar um vetor intrincado como `brasil.svg`, cada `moveTo`, `curveTo`, `lineTo`, definição de gradiente e atributo de preenchimento dentro daquele getter emite instruções: carregar constantes na pilha, empilhar floats e invocar métodos do builder.

Para piorar a situação, o motor de Compose Preview do Android Studio usa ASM para instrumentar composables em tempo real. Essa instrumentação adiciona cerca de `13 KiB` de instruções sintéticas nos bastidores. Um ícone que compila perfeitamente para um build em tempo de execução no Android pode facilmente ultrapassar o limite de 65.535 bytes dentro do Preview da IDE!

## Literalmente contando bytes: `MethodSizeAccountable`

Dividir por contagem de linhas brutas não funciona porque comandos diferentes possuem pegadas de bytecode muito diferentes. Um único caminho com múltiplas curvas de Bézier cúbicas e gradientes gera muito mais bytecode do que dezenas de linhas simples.

Para resolver isso de forma determinística, introduzi um modelo de contabilidade diretamente no `svg-to-compose`:

```kotlin
interface MethodSizeAccountable {
    val approximateByteSize: Int

    companion object {
        // Limite definido para metade do limite de 64 KiB da JVM para deixar folga aos Compose Previews
        const val METHOD_SIZE_THRESHOLD = 32767
        const val ICON_BASE_STRUCTURE_BYTE_SIZE = 73
        const val FLOAT_APPROXIMATE_BYTE_SIZE = 4
        const val BOOLEAN_APPROXIMATE_BYTE_SIZE = 2
    }
}
```

Cada nó e comando de caminho analisado implementa `MethodSizeAccountable`, estimando seu peso de bytecode:
- Cada wrapper de caminho contabiliza sua sobrecarga básica (~133 bytes) mais parâmetros e comandos internos.
- Grupos contabilizam transformações, escala, translação, rotação e clipping paths (~90 bytes base).
- Argumentos float contabilizam `4` bytes cada; booleanos contabilizam `2` bytes.
- Pincéis de cor e paradas de gradiente adicionam seus respectivos custos na pilha.

Nós então somamos o tamanho estimado de bytecode para toda a estrutura do ícone:

> **Tamanho Total** = Estrutura Base (73 bytes) + Σ (Tamanhos em Bytes dos Nós)

## Dividindo métodos em pedaços: `NodeChunker`

Assim que o gerador detecta que um ícone ultrapassa o `METHOD_SIZE_THRESHOLD` (32.767 bytes), ele ativa o `NodeChunker`:

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

Em vez de emitir um único método massivo, o gerador de código divide os nós em funções auxiliares privadas separadas:

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
    // Primeira partição de comandos de path e group
}

private fun ImageVector.Builder._brasilChunk2() {
    // Segunda partição de comandos de path e group
}
```

## O resultado

Com o particionamento em vigor:
1. Cada método individual permanece confortavelmente abaixo de 32 KiB, deixando bastante espaço para a instrumentação do ASM.
2. O Compose Preview no Android Studio para `Brasil.svg` funciona perfeitamente sem falhas.
3. Sem impacto no desempenho de tempo de execução: a delegação de chamadas de builder em duas funções auxiliares é otimizada pelo JIT da JVM e pelo R8/ProGuard.

Construir geradores de código força você a olhar além da sintaxe e pensar no bytecode que suas abstrações produzem. Foi a única vez na minha carreira em que precisei contar bytes instrução por instrução — e isso transformou uma limitação obscura do compilador em um recurso robusto e automatizado.
