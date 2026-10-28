---
title: 'Log.d("HowTo", "@Composable fun Text()")'
date: oct 03, 2024
authors:
    -   name: rafael
        avatar: https://rafael.tonholo.dev/images/writing.jpg
description: >
    An in-depth look at how @Composable Text works in Jetpack Compose, from Material 3 down to BasicText, 
    TextModifier, and string formatting in multiplatform apps.
tags:
    - compose
    - android
    - kotlin
---

## Introduction

This article is the first in a series exploring Jetpack Compose essentials. To kick things off, we dive into the
`@Composable Text` component: how it works under the hood, how it is laid out on the screen, and how to format text
across Android and Compose Multiplatform.

Shall we? Let's start from the beginning.

## What is a Text on Compose?

The `@Composable Text()` component, as defined in its documentation, is:

> [!NOTE]
> High-level element that displays text and provides semantics/accessibility information.

Although the name suggests that `Text` is part of the foundation of Compose, it belongs to `androidx.compose.material3`
(or `androidx.compose.material` if you are on Material 2). But what does that mean in practice?

Google provides built-in styling and theming functionalities to speed up UI development. Looking at the implementation
of `Text` inside Material 3:

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

We can see that it delegates to `BasicText`, wrapping it with Material Theme defaults—such as resolving
`LocalTextStyle.current` and `LocalContentColor.current` from the composition locals.

This means your text is directly styled by the active `MaterialTheme`. If you want to bypass Material styling entirely
and use a lower-level primitive, you can reach for `BasicText` directly.

## How does Text draw on the screen?

It is worth noting that the `Text` composable does not draw anything directly onto the screen canvas. As a high-level
wrapper, it handles styling and composition locals before delegating to `BasicText`.

Behind the scenes, `BasicText` relies on a `Layout` composable whose measurement and drawing logic live in a `Modifier`:

- For a plain `String`, it uses a text modifier backed by `TextStringSimpleElement` and `TextStringSimpleNode`.
- For an `AnnotatedString`, it uses `TextAnnotatedStringElement` and `TextAnnotatedStringNode`.

These modifier nodes measure the layout constraints and draw the text layout directly onto the canvas. When rendering an
`AnnotatedString`, Compose also supports inline children, walking an extra layout step to place embedded composables.

## Showing text in your app

At its simplest, showing text requires just a `String` (or `AnnotatedString`):

```kotlin
@Composable
fun Text(text: String, /* other params */)
@Composable
fun Text(text: AnnotatedString, /* other params */)
```

In the traditional Android View system, we set text via `TextView` and string resources. In Compose—including Compose
Multiplatform (CMP)—resource handling is just as seamless.

### Strings and plurals

Inside any composable, you can call `stringResource()` for standard strings and `pluralStringResource()` for plurals:

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

In Android projects, you pass the `@StringRes` ID (`R.string.*` / `R.plurals.*`). In Compose Multiplatform, the Gradle
plugin generates the `Res` accessor object (`Res.string.*` and `Res.plurals.*`), taking `quantity` instead of `count`.

## Formatting text

Both `stringResource()` and `pluralStringResource()` accept format arguments following standard format string syntax:

```
%[argument_index$][flags][width][.precision]conversion
```

Where:

- `%` is the required leading delimiter.
- `argument_index$` (optional) indicates the 1-based position of the argument in the argument list (e.g. `%1$s`,
  `%2$d`).
- `flags` (optional) modifies the output format (e.g., `-` for left-justification, `0` for zero-padding).
- `width` (optional) specifies the minimum number of characters.
- `.precision` (optional) restricts the number of characters or decimal places.
- `conversion` (required) specifies how the argument should be formatted.

### Format conversions

| Conversion   | Category       | Description                                              |
|--------------|----------------|----------------------------------------------------------|
| `'s'`, `'S'` | General        | String representation via `toString()` or `Formattable`. |
| `'b'`, `'B'` | General        | Boolean result (`"true"` / `"false"`).                   |
| `'d'`        | Integral       | Decimal integer.                                         |
| `'x'`, `'X'` | Integral       | Hexadecimal integer.                                     |
| `'o'`        | Integral       | Octal integer.                                           |
| `'f'`        | Floating point | Decimal floating-point number.                           |
| `'e'`, `'E'` | Floating point | Computerized scientific notation.                        |
| `'g'`, `'G'` | Floating point | Scientific or decimal format depending on precision.     |
| `'c'`, `'C'` | Character      | Unicode character.                                       |
| `'%'`        | Literal        | Literal percent sign (`%`).                              |
| `'n'`        | Separator      | Platform-specific newline.                               |

### Passing format arguments

Consider the following `strings.xml`:

```xml

<resources>
    <string name="string_arg">%s test</string>
    <string name="limited_string_arg">%.5s test</string>
    <string name="decimal_arg">%d test</string>
    <string name="float_arg">%.2f test</string>
    <string name="multiple_args">%1$.2f test, %2$s, %1$f</string>
    <plurals name="items">
        <item quantity="one">%d item</item>
        <item quantity="other">%d items</item>
    </plurals>
</resources>
```

In Compose, pass the values directly as varargs:

```kotlin
Text(text = stringResource(R.string.string_arg, "Compose"))
Text(text = stringResource(R.string.multiple_args, 3.14159, "Kotlin"))
Text(text = pluralStringResource(R.plurals.items, count = 5, 5))
```
