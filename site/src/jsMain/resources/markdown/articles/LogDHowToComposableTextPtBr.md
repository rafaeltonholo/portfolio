---
routeOverride: /articles/pt-br/log-d-how-to-composable-text
slug: log-d-how-to-composable-text
locale: pt-BR
title: 'Log.d("ComoFazer", "@Composable fun Text()")'
description: >
    Um mergulho aprofundado em como o @Composable Text funciona no Jetpack Compose, do Material 3 até 
    o BasicText, TextModifier e formatação de texto em apps multiplatform.
date: 03 de out. de 2024
authors:
    -   name: rafael
        avatar: https://rafael.tonholo.dev/images/writing.jpg
tags:
    - compose
    - android
    - kotlin
---

## Introdução

Este artigo é o primeiro de uma série que explora os fundamentos do Jetpack Compose. Para começar com o pé direito,
vamos mergulhar no componente `@Composable Text`: como ele funciona por baixo dos panos, como ele é posicionado na tela
e como formatar texto no Android e no Compose Multiplatform.

Vamos lá? Vamos começar pelo início.

## O que é um Text no Compose?

O componente `@Composable Text()`, conforme definido em sua documentação, é:

> [!NOTE]
> Elemento de alto nível que exibe texto e fornece informações de semântica/acessibilidade.

Embora o nome sugira que `Text` seja parte da fundação do Compose, ele pertence a `androidx.compose.material3` (ou
`androidx.compose.material` se você estiver no Material 2). Mas o que isso significa na prática?

O Google fornece funcionalidades integradas de estilização e temas para acelerar o desenvolvimento de interfaces.
Olhando para a implementação de `Text` no Material 3:

```kotlin
@Composable
fun Text(/* params */) {
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current
        }
    }

    BasicText(
        text,
        modifier,
        style.merge(/* styling params */),
        onTextLayout,
        overflow,
        softWrap,
        maxLines,
        minLines,
    )
}
```

Podemos ver que ele delega para `BasicText`, envolvendo-o com os padrões do Material Theme — como resolver
`LocalTextStyle.current` e `LocalContentColor.current` a partir dos composition locals.

Isso significa que seu texto é estilizado diretamente pelo `MaterialTheme` ativo. Se você quiser ignorar completamente a
estilização do Material e usar uma primitiva de nível mais baixo, você pode recorrer diretamente ao `BasicText`.

## Como o Text desenha na tela?

Vale notar que o composable `Text` não desenha nada diretamente no canvas da tela. Como um wrapper de alto nível, ele
lida com estilização e composition locals antes de delegar para o `BasicText`.

Nos bastidores, o `BasicText` depende de um composable `Layout` cuja lógica de medição e desenho reside em um
`Modifier`:

- Para uma `String` simples, ele usa um modificador de texto baseado em `TextStringSimpleElement` e
  `TextStringSimpleNode`.
- Para uma `AnnotatedString`, ele usa `TextAnnotatedStringElement` e `TextAnnotatedStringNode`.

Esses nós de modificadores medem as restrições de layout e desenham o layout de texto diretamente no canvas. Ao
renderizar uma `AnnotatedString`, o Compose também oferece suporte a elementos inline (inline children), executando uma
etapa extra de layout para posicionar composables embutidos.

## Exibindo texto no seu app

Em sua forma mais simples, exibir texto requer apenas uma `String` (ou `AnnotatedString`):

```kotlin
@Composable
fun Text(text: String, /* other params */)
@Composable
fun Text(text: AnnotatedString, /* other params */)
```

No sistema tradicional de Views do Android, definimos texto via `TextView` e recursos de string. No Compose — incluindo
o Compose Multiplatform (CMP) — o manuseio de recursos é igualmente fluido.

### Strings e plurais

Dentro de qualquer composable, você pode chamar `stringResource()` para strings padrão e `pluralStringResource()` para
plurais:

```kotlin
@Composable
fun MyAwesomeElement(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // Jetpack Compose (Android)
        Text(text = stringResource(id = R.string.my_awesome_text))
        Text(
            text = pluralStringResource(
                id = R.plurals.my_awesome_items,
                count = 1,
            ),
        )

        // Compose Multiplatform (CMP)
        Text(text = stringResource(resource = Res.string.my_awesome_text))
        Text(
            text = pluralStringResource(
                resource = Res.plurals.my_awesome_items,
                quantity = 1,
            ),
        )
    }
}
```

Em projetos Android, você passa o ID `@StringRes` (`R.string.*` / `R.plurals.*`). No Compose Multiplatform, o plugin do
Gradle gera o objeto de acesso `Res` (`Res.string.*` e `Res.plurals.*`), recebendo `quantity` em vez de `count`.

## Formatando texto

Tanto `stringResource()` quanto `pluralStringResource()` aceitam argumentos de formatação seguindo a sintaxe padrão de
formato de string:

```
%[argument_index$][flags][width][.precision]conversion
```

Onde:

- `%` é o delimitador inicial obrigatório.
- `argument_index$` (opcional) indica a posição baseada em 1 do argumento na lista (ex: `%1$s`, `%2$d`).
- `flags` (opcional) modifica o formato da saída (ex: `-` para alinhamento à esquerda, `0` para preenchimento com
  zeros).
- `width` (opcional) especifica o número mínimo de caracteres.
- `.precision` (opcional) restringe o número de caracteres ou casas decimais.
- `conversion` (obrigatório) especifica como o argumento deve ser formatado.

### Conversões de formato

| Conversão    | Categoria       | Descrição                                                  |
|--------------|-----------------|------------------------------------------------------------|
| `'s'`, `'S'` | Geral           | Representação em String via `toString()` ou `Formattable`. |
| `'b'`, `'B'` | Geral           | Resultado booleano (`"true"` / `"false"`).                 |
| `'d'`        | Inteiro         | Número inteiro decimal.                                    |
| `'x'`, `'X'` | Inteiro         | Número inteiro hexadecimal.                                |
| `'o'`        | Inteiro         | Número inteiro octal.                                      |
| `'f'`        | Ponto flutuante | Número decimal em ponto flutuante.                         |
| `'e'`, `'E'` | Ponto flutuante | Notação científica computadorizada.                        |
| `'g'`, `'G'` | Ponto flutuante | Formato científico ou decimal dependendo da precisão.      |
| `'c'`, `'C'` | Caractere       | Caractere Unicode.                                         |
| `'%'`        | Literal         | Sinal de porcentagem literal (`%`).                        |
| `'n'`        | Separador       | Quebra de linha específica da plataforma.                  |

### Passando argumentos de formatação

Considere o seguinte `strings.xml`:

```xml

<resources>
    <string name="string_arg">%s test</string>
    <string name="limited_string_arg">%.5s test</string>
    <string name="decimal_arg">%d test</string>
    <string name="float_arg">%.2f test</string>
    <string name="multiple_args">%1$.2f test, %2$s, %1$f</string>
    <plurals name="items">
        <item quantity="one">%d item</item>
        <item quantity="other">%d itens</item>
    </plurals>
</resources>
```

No Compose, passe os valores diretamente como varargs:

```kotlin
Text(text = stringResource(R.string.string_arg, "Compose"))
Text(text = stringResource(R.string.multiple_args, 3.14159, "Kotlin"))
Text(text = pluralStringResource(R.plurals.items, count = 5, 5))
```
