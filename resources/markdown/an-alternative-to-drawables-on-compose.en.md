---
title: An Alternative to Drawables on Jetpack Compose
description: >
    TBD.
summary: Using ImageVector's to display icons on Jetpack Compose as an alternative to Android Drawables
authors:
    - name: Rafael Tonholo
      avatar: https://rafael.tonholo.dev/images/writing.jpg
publishedDateTime: 2024-06-8T11:32:34-03:00
---

# Introduction
When we create an app, we will always need to use Images or icons. Android never supported svgs since the beginning 
of its time and that didn't change with the arrival with compose.

Instead, Android relies in something called Android Drawables, or Android Graphics Vector as I like to call, which 
is an xml version of the svg, that can be created by using [Android Studio Vector Assets tool](https://developer.android.com/studio/write/vector-asset-studio#running).

With the arrival of Jetpack Compose, and we migrating from creating views using xml to Kotlin, I got the hope of for
ours resources, we would migrate to Kotlin as well, but until today that didn't happen and I'm not sure if it will 
ever happen.

Don't get me wrong, I don't hate xml. I just wanted to have the full power of Kotlin to handle my resources as well.

There are some advantages of using resources as xml, like automatically localization via `values-<language-tag>` or
using different images/icons for different sizes of screen by using `drawable-<density-qualifier>`, but in some cases
that is not required.

Additionally, when using a svg (Scalable Vector Graphic), it doesn't make much sense having different versions on 
different screen sizes, since svg is just a bunch of commands to draw a vector into a screen, so independently of 
the screen size, it will not lose quality if you scale it up or down.

# Loading vectors on Jetpack Compose
As we already now, Android can't load directly an svg natively, rather needs to use avgs[^1] for it. Compose provides 
a few ways to load local images. The `painterResource(@DrawableRes id: Int): Painter` is a function that supports all 
types of local images: `AnimatedVectorDrawable`, `BitmapDrawable` (in case of PNG, JPG, WEBP), `ColorDrawable` and 
`VectorDrawable`, thus is the recommended one to load local images, however, that it is not the only way to load 
them.

If you need to use a vector in your application and require to do some modifications in the vector, 
you can also use `ImageVector.vectorResource(@DrawableRes id: Int): ImageVector`.

If you read the documentation of Compose explaining about Images, when they refers to vectors, they always talk about 
`ImageVector` and we can spot the following:

> You can create a custom ImageVector either by importing an existing vector drawable XML file 
> (imported into Android Studio using the import [tool](https://developer.android.com/studio/write/vector-asset-studio#running)) 
> or implementing the class and issuing path commands manually.

And that is exactly how they are doing with [Material Icons](https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary) 
on Compose.

The Material Icons library really got my attention. As it provides all their icons by using only Kotlin, why couldn't 
I do the same?

# Investigating how Material Icons create their icons
If you navigate to Google Fonts, you can find out all Material Icons inside the "Icons" tab. Getting the Check icon 
as the subject of study, here is how it looks like:

<svg xmlns="http://www.w3.org/2000/svg" height="128px" viewBox="0 0 24 24" width="128px" style="fill: var(--silk-color); width: 100%; margin: 0 auto;">
    <path d="M9 16.17 4.83 12 l-1.42 1.41 L9 19 21 7 l-1.41-1.41z"/>
</svg>
<a href="https://fonts.google.com/icons?selected=Material+Symbols+Outlined:check:FILL@0;wght@400;GRAD@0;opsz@24&icon.query=done+&icon.size=24&icon.color=%23e8eaed"
   style="width: 100%;text-align: center;display: block;">
Google Fonts: Check Icon
</a>

Lets check how that icon is being created inside the Material Icons library. Breaking it down, first it creates a 
property with a backing field where it will create the icon by using the  `materialIcon` function, name it as 
`Filled.Check` and store it in the backing field. In case the icon was previously created, it just returns it 
without creating it again: 

```kotlin
// [!code focus:7]
public val Icons.Filled.Check: ImageVector
    get() {
        if (_check != null) {
            return _check!!
        }
        _check = materialIcon(name = "Filled.Check") {
            materialPath {
                moveTo(9.0f, 16.17f)
                lineTo(4.83f, 12.0f)
                lineToRelative(-1.42f, 1.41f)
                lineTo(9.0f, 19.0f)
                lineTo(21.0f, 7.0f)
                lineToRelative(-1.41f, -1.41f)
                close()
            }
// [!code focus:6]
        }
        return _check!!
    }

private var _check: ImageVector? = null
```

The content inside the lambda function of `materialIcon` is where the commands to draw the path is. These commands 
will be used later by `VectorPainter` to draw the icon into a `Canvas` on Compose:
```kotlin
public val Icons.Filled.Check: ImageVector
  get() {
    if (_check != null) {
      return _check!!
    }
    _check = materialIcon(name = "Filled.Check") {
// [!code focus:10]
      materialPath {
        moveTo(9.0f, 16.17f)
        lineTo(4.83f, 12.0f)
        lineToRelative(-1.42f, 1.41f)
        lineTo(9.0f, 19.0f)
        lineTo(21.0f, 7.0f)
        lineToRelative(-1.41f, -1.41f)
        close()
      }
    }
    return _check!!
  }

private var _check: ImageVector? = null
```

But how did they get those `moveTo`, `lineTo`, `lineToRelative` and `close`?

## Understanding the svg path instructions
First, lets verify the check icon's svg code:

```xml
<svg xmlns="http://www.w3.org/2000/svg" 
     height="24" 
     viewBox="0 0 24 24" 
     width="24">
    <path d="M9 16.17 4.83 12 l-1.42 1.41 L9 19 21 7 l-1.41-1.41z" />
</svg>
```

The svg for the Check icon contains in a single `path` tag, with a `d` attribute, which is the attribute who 
define the "draw" instructions to the canvas.

A path instruction is composed by:
1. A command identified by a letter, like `M`, `L`, or `l`. If the letter is uppercase, it means the command 
is an absolute command otherwise a relative command.
2. A set of numbers that will be the arguments for that command.

The path instructions are like if you have a pen and a paper and for each command you need to follow some instructions.

Taking that path as an example, lets break it down. The `M9 16.17 4.83 12 l-1.42 1.41 L9 19 21 7 l-1.41-1.41z` 
will become:
- `M 9 16.17`: The `M` command stands to a move to absolute command. The `move to` command expects two arguments `(x, y)`.
- `4.83 12`: Although the `4.83 12` doesn't come with the command letter, as per svg specification <sup>[2][move-to-command]</sup>, 
every subsequent command after the `move to` command will be a `line to`(`L`), preserving if it is absolute or relative.
- `l -1.42 1.41`: The `l` command stands to a line to relative command. The `line to` command expects two arguments 
(x, y). That means, it will take the previous position `(4.83, 12)` and draw a line on `x` by `-1.42` and on `y` by `1.41`.
- `L 9 19`: The `L` command stands for line to absolute. It means it will draw a line directly to the position `(9, 19)`.
- `21 7`: When there is no command specified in a following command, it takes the previous one and continue. In this 
case, the previous one was a `line to` absolute command, thus this will be the same. It means, it will draw a line 
directly to the position `(21, 7)`.
- `l -1.41 -1.41`: As we previously saw, this is a relative `line to` command, thus it will draw a line from the 
position `(21, 7)`, relatively by `(-1.41, -1.41)`.
- `z`: The `z` command is an special command that tell the canvas that the path should be closed. It could be 
lowercase or uppercase, but the result will be the same.

Using the pen and paper example, we could simple translate `M9 16.17 4.83 12 l-1.42 1.41 L9 19 21 7 l-1.41-1.41z` to:
1. Remove the pen from the paper, and move to the informed position. If it is absolute, you move from
   the top left corner to the desired position otherwise, from where you were. In this case, we start 
   from `(0, 0) -> (9, 16.17)`
2. Put the pen down and draw a line from `(9, 16.17) -> (4.83, 12)` in the canvas.
3. Draw a line from `(4.83, 12) -> (3.41, 13.41)`. As this command was a relative command, we need to account 
the previous pen position.
4. Draw a line from `(3.41, 13.41) -> (9, 19)`.
5. Draw a line from `(9, 19) -> (21, 7)`.
6. Draw a line from `(21, 7) -> (19.59, 5.59)`
7. Close the current path by drawing a straight line from the current point to current path's initial point.

<(DrawSvgStepByStep)/>

## Translating svg path instructions to Compose
Now that we have understood each path instruction from the Check icon, it is time to translate them to Compose.

> [!NOTE]
> As we are focusing on [`ImageVector`](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/vector/ImageVector), 
we are going to use the available methods for it, however that is also applicable for a [`DrawScope`](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/drawscope/DrawScope) 
used on [`Canvas`](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/package-summary#Canvas(android.graphics.Canvas)) 
and [`VectorPainter`](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/vector/VectorPainter), 
with a few changes.

Summarizing the path instructions the Check item uses, we will have:
- `M` <sup>[2][move-to-command]</sup>, which is an absolute `move to` command, that translates to `moveTo(x, y)`
- `L` <sup>[3][line-to-command]</sup>, which is an absolute `line to` command, that translates to `lineTo(x, y)`
- `l` <sup>[3][line-to-command]</sup>, which is a relative `line to` command, that translates to `lineToRelative(dx, dy)`
- `z`, which is a `close path` command, that translates to `close()`

The svg path also accepts other commands providing a variety of drawing instructions which allows you to create complex
 images. All path commands are supported by compose and we can translate them. Those are:
- The **curve to** command <sup>[4][curve-to-command]</sup>:
  - Represented by `C` (absolute) or `c` (relative)
  - Accepts 6 arguments
  - Translates to:
```kotlin
    curveTo(
        x1 /*: Float */, y1 /*: Float */,
        x2 /*: Float */, y2 /*: Float */,
        x3 /*: Float */, y3 /*: Float */,
    )
    curveToRelative(
        dx1 /*: Float */, dy1 /*: Float */,
        dx2 /*: Float */, dy2 /*: Float */,
        dx3 /*: Float */, dy3 /*: Float */,
    )
```
- The **smooth curve to** command <sup>[4][curve-to-command]</sup>:
  - Represented by `S` (absolute) or `s` (relative)
  - Accepts 4 arguments
  - Translates to:
```kotlin
    reflectiveCurveTo(
        x1 /*: Float */, y1 /*: Float */,
        x2 /*: Float */, y2 /*: Float */,
    )
    reflectiveCurveToRelative(
        dx1 /*: Float */, dy1 /*: Float */,
        dx2 /*: Float */, dy2 /*: Float */,
    )
```
- The **quadratic Bézier curve to** command <sup>[5][quad-to-command]</sup>:
  - Represented by `Q` (absolute) or `q` (relative)
  - Accepts 4 arguments
  - Translates to:
```kotlin
    quadTo(
        x1 /*: Float */, y1 /*: Float */,
        x2 /*: Float */, y2 /*: Float */,
    )
    quadToRelative(
        dx1 /*: Float */, dy1 /*: Float */,
        dx2 /*: Float */, dy2 /*: Float */,
    )
```
- The **smooth quadratic Bézier curve to** command <sup>[5][quad-to-command]</sup>:
  - Represented by `T` (absolute) or `t` (relative)
  - Accepts 2 arguments
  - Translates to:
```kotlin
    reflectiveQuadTo(x1 /*: Float */, y1 /*: Float */)
    reflectiveQuadToRelative(dx1 /*: Float */, dy1 /*: Float */)
```
- The **elliptical arc** command <sup>[6][arc-to-command]</sup>:
  - Represented by `A` (absolute) or `a` (relative)
  - Accepts 7 arguments
  - Translates to:
```kotlin
    arcTo(
        horizontalEllipseRadius /*: Float */, verticalEllipseRadius /*: Float */, 
        theta /*: Float */, 
        isMoreThanHalf /*: Boolean */, 
        isPositiveArc /*: Boolean */, 
        x1 /*: Float */, x2 /*: Float */,
    )
    arcToRelative(
        a /*: Float */, b /*: Float */,
        theta /*: Float */,
        isMoreThanHalf /*: Boolean */,
        isPositiveArc /*: Boolean */,
        dx1 /*: Float */, dx2 /*: Float */,
    )
```
- The **horizontal line to** command <sup>[3][line-to-command]</sup>:
  - Represented by `H` (absolute) or `h` (relative)
  - Accepts 1 arguments
  - Translates to: 
```kotlin
    horizontalLineTo(x /*: Float */)
    horizontalLineToRelative(x /*: Float */)
``` 
- The **vertical line to** command <sup>[3][line-to-command]</sup>:
    - Represented by `V` (absolute) or `v` (relative)
    - Accepts 1 arguments
    - Translates to: 
```kotlin
    verticalLineTo(y /*: Float */)
    verticalLineToRelative(y /*: Float */)
```

# Drawing our own icons
Now that we understood how a path instruction translates to a Compose `ImageVector` method, it is time to try creating
 an icon by ourselves. I'm going to use an open-source icon in this example, but you should be able to use your own icons.

The icon I'm going to use as the subject of study is the [smiley](https://github.com/primer/octicons/blob/main/icons/smiley-24.svg),
 from Github Octicons:

<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="128" height="128" style="fill: var(--silk-color); width: 100%; margin: 0 auto;">
    <path d="M8.456 14.494a.75.75 0 0 1 1.068.17 3.08 3.08 0 0 0 .572.492A3.381 3.381 0 0 0 12 15.72c.855 0 1.487-.283 1.904-.562a3.081 3.081 0 0 0 .572-.492l.021-.026a.75.75 0 0 1 1.197.905l-.027.034c-.013.016-.03.038-.052.063-.044.05-.105.119-.184.198a4.569 4.569 0 0 1-.695.566A4.88 4.88 0 0 1 12 17.22a4.88 4.88 0 0 1-2.736-.814 4.57 4.57 0 0 1-.695-.566 3.253 3.253 0 0 1-.236-.261c-.259-.332-.223-.824.123-1.084Z"/>
    <path d="M12 1c6.075 0 11 4.925 11 11s-4.925 11-11 11S1 18.075 1 12 5.925 1 12 1ZM2.5 12a9.5 9.5 0 0 0 9.5 9.5 9.5 9.5 0 0 0 9.5-9.5A9.5 9.5 0 0 0 12 2.5 9.5 9.5 0 0 0 2.5 12Z" />
    <path d="M9 10.75a1.25 1.25 0 1 1-2.5 0 1.25 1.25 0 0 1 2.5 0ZM16.25 12a1.25 1.25 0 1 0 0-2.5 1.25 1.25 0 0 0 0 2.5Z"/>
</svg>
<a href="https://primer.style/foundations/icons/smiley-24" style="width: 100%;text-align: center;display: block;">
Github Prime Design System - Octicons: smiley
</a>

## Creating the `ImageVector`
We are going to follow almost the same approach Material Icons library does to create their icons. So let's start by 
creating the icon scaffold:
```kotlin
// [!code word:public:1]
public val Smiley: ImageVector get() {
  if (_smiley != null) {
    return _smiley!!
  }
  // [!code highlight:4]
  _smiley = materialIcon(name = "Octicons.Smiley") {
    // Paths.
  }
  return _smiley!!
}
private var _smiley: ImageVector? = null
```

There are two points about the scaffold:
1. As we are not creating a library, we don't need to define our icon property as `public`, since that is the default 
Kotlin visibility.
2. Material Icon library uses the `materialIcon(name)` function to create their icons which hides a few implementation 
details. In some cases, it would be enough if we follow the Material Design guidelines for Icons, however that won't 
be the case in the majority of projects.

Having that said, let's change a bit our scaffold. We are going to remove the `public` keyword and create the `ImageVector`
by using the `ImageVector.Builder`. Two major differences were introduced in the new icon scaffolding:
1. As `materialIcon()` hides a few implementation, now we need to set some properties related to the icon "canvas":
    1. The `defaultWidth` is the `width` from the `svg` tag attribute. If the number is omitted, it should take
       100% of the viewport width <sup>[7][svg-width-attr]</sup>.
    2. The `defaultHeight` is the `height` from the `svg` tag attribute. If the number is omitted, it should take
       100% of the viewport height <sup>[8][svg-height-attr]</sup>.
    3. The `viewportWidth` is the `viewBox` 3th number from the `svg` tag attribute.
    4. The `viewportHeight` is the `viewBox` 4th number from the `svg` tag attribute.
```kotlin
val Smiley: ImageVector get() {
  val current = _smiley
  if (current != null) {
    return current
  }
  return ImageVector
    .Builder(
      name = "Octicons.Smiley",
      // [!code focus:5]
      defaultWidth = 24.0.dp,
      defaultHeight = 24.0.dp,
      viewportWidth = 24.0f,
      viewportHeight = 24.0f,
    )
    .apply {
      // Paths.
    }
    .build()
    .also { _smiley = it }
}

private var _smiley: ImageVector? = null
```
2. As we are directly using the `ImageVector.Builder`, we need to call the `build()` function in order to build our `ImageVector`.
```kotlin
val Smiley: ImageVector get() {
  val current = _smiley
  if (current != null) {
    return current
  }
  return ImageVector
    .Builder(
      name = "Octicons.Smiley",
      defaultWidth = 24.0.dp,
      defaultHeight = 24.0.dp,
      viewportWidth = 24.0f,
      viewportHeight = 24.0f,
    )
    .apply {
      // Paths.
    }
    .build() // [!code focus]
    .also { _smiley = it }
}

private var _smiley: ImageVector? = null
```

We also perform a few changes to remove the `!!` marker, which I honestly don't like, ending up with the following 
icon scaffold:
```kotlin
val Smiley: ImageVector get() {
  val current = _smiley
  if (current != null) {
    return current
  }
  return ImageVector
    .Builder(
      name = "Octicons.Smiley",
      defaultWidth = 24.0.dp,
      defaultHeight = 24.0.dp,
      viewportWidth = 24.0f,
      viewportHeight = 24.0f,
    )
    .apply {
      // Paths.
    }
    .build()
    .also { _smiley = it }
}

private var _smiley: ImageVector? = null
```
## Create the path instructions
Now that we have our icon scaffold, it is time instruct the `ImageVector.Builder` how it will draw the icon when it is 
required. We need first to check svg code to understand how it instructs the canvas to draw:
```xml
<svg xmlns="http://www.w3.org/2000/svg" 
     viewBox="0 0 24 24" 
     width="24" 
     height="24">
    <path d="M8.456 14.494a.75.75 0 0 1 1.068.17 3.08 3.08 0 0 0 .572.492
             A3.381 3.381 0 0 0 12 15.72c.855 0 1.487-.283 1.904-.562
             a3.081 3.081 0 0 0 .572-.492l.021-.026a.75.75 0 0 1 1.197.905
             l-.027.034c-.013.016-.03.038-.052.063-.044.05-.105.119-.184.198
             a4.569 4.569 0 0 1-.695.566A4.88 4.88 0 0 1 12 17.22
             a4.88 4.88 0 0 1-2.736-.814 4.57 4.57 0 0 1-.695-.566 
             3.253 3.253 0 0 1-.236-.261c-.259-.332-.223-.824.123-1.084Z"/>
    <path d="M12 1c6.075 0 11 4.925 11 11s-4.925 11-11 11S1 18.075 1 12 5.925 1 12 1Z
             M2.5 12a9.5 9.5 0 0 0 9.5 9.5 9.5 9.5 0 0 0 9.5-9.5A9.5 9.5 0 0 0 12 2.5 
             9.5 9.5 0 0 0 2.5 12Z" />
    <path d="M9 10.75a1.25 1.25 0 1 1-2.5 0 1.25 1.25 0 0 1 2.5 0ZM16.25 12
             a1.25 1.25 0 1 0 0-2.5 1.25 1.25 0 0 0 0 2.5Z"/>
</svg>
```
> [!NOTE]
> The `ImageVector` file will be a bit length. If you don't want to follow step by step, you can checkout the full file
> in this link: [Octicons.Smiley.kt](/articles/compose-icon/Octicons.Smiley.kt)

While examining the above code, we can understand that the icon is composed by 3 paths:
1. For the mouth
```kotlin
val Smiley: ImageVector get() {
  val current = _smiley
  if (current != null) {
    return current
  }
  return ImageVector
    .Builder(
      name = "Octicons.Smiley",
      defaultWidth = 24.0.dp,
      defaultHeight = 24.0.dp,
      viewportWidth = 24.0f,
      viewportHeight = 24.0f,
    )
    .apply {
      // [!code focus:117]
      // M8.456 14.494 a.75 .75 0 0 1 1.068 .17 3.08 3.08 0 0 0 .572 .492 A3.381 3.381 0 0 0 12 15.72 c.855 0 1.487 -.283 1.904 -.562 a3.081 3.081 0 0 0 .572 -.492 l.021 -.026 a.75 .75 0 0 1 1.197 .905 l-.027 .034 c-.013 .016 -.03 .038 -.052 .063 -.044 .05 -.105 .119 -.184 .198 a4.569 4.569 0 0 1 -.695 .566 A4.88 4.88 0 0 1 12 17.22 a4.88 4.88 0 0 1 -2.736 -.814 4.57 4.57 0 0 1 -.695 -.566 3.253 3.253 0 0 1 -.236 -.261 c-.259 -.332 -.223 -.824 .123 -1.084Z
      path(fill = SolidColor(Color(0xFF000000))) {
        // M 8.456 14.494
        moveTo(x = 8.456f, y = 14.494f)
        // a 0.75 0.75 0 0 1 1.068 0.17
        arcToRelative(
          a = 0.75f, b = 0.75f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = true,
          dx1 = 1.068f, dy1 = 0.17f,
        )
        // a 3.08 3.08 0 0 0 0.572 0.492
        arcToRelative(
          a = 3.08f, b = 3.08f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = false,
          dx1 = 0.572f, dy1 = 0.492f,
        )
        // A 3.381 3.381 0 0 0 12 15.72
        arcTo(
          horizontalEllipseRadius = 3.381f,
          verticalEllipseRadius = 3.381f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = false,
          x1 = 12.0f, y1 = 15.72f,
        )
        // c 0.855 0 1.487 -0.283 1.904 -0.562
        curveToRelative(
          dx1 = 0.855f, dy1 = 0.0f,
          dx2 = 1.487f, dy2 = -0.283f,
          dx3 = 1.904f, dy3 = -0.562f,
        )
        // a 3.081 3.081 0 0 0 0.572 -0.492
        arcToRelative(
          a = 3.081f, b = 3.081f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = false,
          dx1 = 0.572f, dy1 = -0.492f,
        )
        // l 0.021 -0.026
        lineToRelative(dx = 0.021f, dy = -0.026f)
        // a 0.75 0.75 0 0 1 1.197 0.905
        arcToRelative(
          a = 0.75f, b = 0.75f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = true,
          dx1 = 1.197f, dy1 = 0.905f,
        )
        // l -0.027 0.034
        lineToRelative(dx = -0.027f, dy = 0.034f)
        // c -0.013 0.016 -0.03 0.038 -0.052 0.063
        curveToRelative(
          dx1 = -0.013f, dy1 = 0.016f,
          dx2 = -0.03f, dy2 = 0.038f,
          dx3 = -0.052f, dy3 = 0.063f,
        )
        // c -0.044 0.05 -0.105 0.119 -0.184 0.198
        curveToRelative(
          dx1 = -0.044f, dy1 = 0.05f,
          dx2 = -0.105f, dy2 = 0.119f,
          dx3 = -0.184f, dy3 = 0.198f,
        )
        // a 4.569 4.569 0 0 1 -0.695 0.566
        arcToRelative(
          a = 4.569f, b = 4.569f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = true,
          dx1 = -0.695f, dy1 = 0.566f,
        )
        // A 4.88 4.88 0 0 1 12 17.22
        arcTo(
          horizontalEllipseRadius = 4.88f,
          verticalEllipseRadius = 4.88f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = true,
          x1 = 12.0f, y1 = 17.22f,
        )
        // a 4.88 4.88 0 0 1 -2.736 -0.814
        arcToRelative(
          a = 4.88f, b = 4.88f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = true,
          dx1 = -2.736f, dy1 = -0.814f,
        )
        // a 4.57 4.57 0 0 1 -0.695 -0.566
        arcToRelative(
          a = 4.57f, b = 4.57f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = true,
          dx1 = -0.695f, dy1 = -0.566f,
        )
        // a 3.253 3.253 0 0 1 -0.236 -0.261
        arcToRelative(
          a = 3.253f, b = 3.253f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = true,
          dx1 = -0.236f, dy1 = -0.261f,
        )
        // c -0.259 -0.332 -0.223 -0.824 0.123 -1.084z
        curveToRelative(
          dx1 = -0.259f, dy1 = -0.332f,
          dx2 = -0.223f, dy2 = -0.824f,
          dx3 = 0.123f, dy3 = -1.084f,
        )
        close()
      }
    }
    .build()
    .also { _smiley = it }
}

private var _smiley: ImageVector? = null
```
2. For the face circle
```kotlin
val Smiley: ImageVector get() {
  val current = _smiley
  if (current != null) {
    return current
  }
  return ImageVector
    .Builder(
      name = "Octicons.Smiley",
      defaultWidth = 24.0.dp,
      defaultHeight = 24.0.dp,
      viewportWidth = 24.0f,
      viewportHeight = 24.0f,
    )
    .apply {
      // M8.456 14.494 a.75 .75 0 0 1 1.068 .17 3.08 3.08 0 0 0 .572 .492 A3.381 3.381 0 0 0 12 15.72 c.855 0 1.487 -.283 1.904 -.562 a3.081 3.081 0 0 0 .572 -.492 l.021 -.026 a.75 .75 0 0 1 1.197 .905 l-.027 .034 c-.013 .016 -.03 .038 -.052 .063 -.044 .05 -.105 .119 -.184 .198 a4.569 4.569 0 0 1 -.695 .566 A4.88 4.88 0 0 1 12 17.22 a4.88 4.88 0 0 1 -2.736 -.814 4.57 4.57 0 0 1 -.695 -.566 3.253 3.253 0 0 1 -.236 -.261 c-.259 -.332 -.223 -.824 .123 -1.084Z
      path { /* mouth path instructions */ }
      // [!code focus:56]
      // M12 1 c6.075 0 11 4.925 11 11 s-4.925 11 -11 11 S1 18.075 1 12 5.925 1 12 1Z M2.5 12 a9.5 9.5 0 0 0 9.5 9.5 9.5 9.5 0 0 0 9.5 -9.5 A9.5 9.5 0 0 0 12 2.5 9.5 9.5 0 0 0 2.5 12Z
      path(fill = SolidColor(Color(0xFF000000))) {
        // M 12 1
        moveTo(x = 12.0f, y = 1.0f)
        // c 6.075 0 11 4.925 11 11
        curveToRelative(
          dx1 = 6.075f, dy1 = 0.0f,
          dx2 = 11.0f, dy2 = 4.925f,
          dx3 = 11.0f, dy3 = 11.0f,
        )
        // s -4.925 11 -11 11
        reflectiveCurveToRelative(dx1 = -4.925f, dy1 = 11.0f, dx2 = -11.0f, dy2 = 11.0f)
        // S 1 18.075 1 12
        reflectiveCurveTo(x1 = 1.0f, y1 = 18.075f, x2 = 1.0f, y2 = 12.0f)
        // S 5.925 1 12 1z
        reflectiveCurveTo(x1 = 5.925f, y1 = 1.0f, x2 = 12.0f, y2 = 1.0f)
        close()
        // M 2.5 12
        moveTo(x = 2.5f, y = 12.0f)
        // a 9.5 9.5 0 0 0 9.5 9.5
        arcToRelative(
          a = 9.5f, b = 9.5f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = false,
          dx1 = 9.5f, dy1 = 9.5f,
        )
        // a 9.5 9.5 0 0 0 9.5 -9.5
        arcToRelative(
          a = 9.5f, b = 9.5f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = false,
          dx1 = 9.5f, dy1 = -9.5f,
        )
        // A 9.5 9.5 0 0 0 12 2.5
        arcTo(
          horizontalEllipseRadius = 9.5f,
          verticalEllipseRadius = 9.5f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = false,
          x1 = 12.0f, y1 = 2.5f,
        )
        // A 9.5 9.5 0 0 0 2.5 12z
        arcTo(
          horizontalEllipseRadius = 9.5f,
          verticalEllipseRadius = 9.5f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = false,
          x1 = 2.5f, y1 = 12.0f,
        )
        close()
      }
    }
    .build()
    .also { _smiley = it }
}

private var _smiley: ImageVector? = null
```
3. For the two eyes

```kotlin
val Smiley: ImageVector get() {
  val current = _smiley
  if (current != null) {
    return current
  }
  return ImageVector
    .Builder(
      name = "Octicons.Smiley",
      defaultWidth = 24.0.dp,
      defaultHeight = 24.0.dp,
      viewportWidth = 24.0f,
      viewportHeight = 24.0f,
    )
    .apply {
      // M8.456 14.494 a.75 .75 0 0 1 1.068 .17 3.08 3.08 0 0 0 .572 .492 A3.381 3.381 0 0 0 12 15.72 c.855 0 1.487 -.283 1.904 -.562 a3.081 3.081 0 0 0 .572 -.492 l.021 -.026 a.75 .75 0 0 1 1.197 .905 l-.027 .034 c-.013 .016 -.03 .038 -.052 .063 -.044 .05 -.105 .119 -.184 .198 a4.569 4.569 0 0 1 -.695 .566 A4.88 4.88 0 0 1 12 17.22 a4.88 4.88 0 0 1 -2.736 -.814 4.57 4.57 0 0 1 -.695 -.566 3.253 3.253 0 0 1 -.236 -.261 c-.259 -.332 -.223 -.824 .123 -1.084Z
      path { /* mouth path instructions */ }
      // M12 1 c6.075 0 11 4.925 11 11 s-4.925 11 -11 11 S1 18.075 1 12 5.925 1 12 1Z M2.5 12 a9.5 9.5 0 0 0 9.5 9.5 9.5 9.5 0 0 0 9.5 -9.5 A9.5 9.5 0 0 0 12 2.5 9.5 9.5 0 0 0 2.5 12Z
      path { /* circle face path instructions */ }
      // [!code focus:42]
      // M9 10.75 a1.25 1.25 0 1 1 -2.5 0 1.25 1.25 0 0 1 2.5 0Z M16.25 12 a1.25 1.25 0 1 0 0 -2.5 1.25 1.25 0 0 0 0 2.5Z
      path(fill = SolidColor(Color(0xFF000000))) {
        // M 9 10.75
        moveTo(x = 9.0f, y = 10.75f)
        // a 1.25 1.25 0 1 1 -2.5 0
        arcToRelative(
          a = 1.25f, b = 1.25f,
          theta = 0.0f,
          isMoreThanHalf = true,
          isPositiveArc = true,
          dx1 = -2.5f, dy1 = 0.0f,
        )
        // a 1.25 1.25 0 0 1 2.5 0z
        arcToRelative(
          a = 1.25f, b = 1.25f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = true,
          dx1 = 2.5f, dy1 = 0.0f,
        )
        close()
        // M 16.25 12
        moveTo(x = 16.25f, y = 12.0f)
        // a 1.25 1.25 0 1 0 0 -2.5
        arcToRelative(
          a = 1.25f, b = 1.25f,
          theta = 0.0f,
          isMoreThanHalf = true,
          isPositiveArc = false,
          dx1 = 0.0f, dy1 = -2.5f,
        )
        // a 1.25 1.25 0 0 0 0 2.5z
        arcToRelative(
          a = 1.25f, b = 1.25f,
          theta = 0.0f,
          isMoreThanHalf = false,
          isPositiveArc = false,
          dx1 = 0.0f, dy1 = 2.5f,
        )
        close()
      }
    }
    .build()
    .also { _smiley = it }
}

private var _smiley: ImageVector? = null
```

## Using the custom `ImageVector` on Compose 
After all those instructions created, the icon is ready to use! To use it on Compose it is quite simple. You will only 
need to use the `Image` or `Icon` Composable function with the parameter `imageVector`:
```kotlin
@Composable
fun MyComposable(modifier: Modifier = Modifier) {
  Column(modifier = modifier) {
    Image(imageVector = Smiley, contentDescription = "A beautiful face")
    // or...
    Icon(imageVector = Smiley, contentDescription = "A beautiful face")
  }
}
```

If your `ImageVector` is supposed to preserve its color, you must use `Image` Composable function. The `Icon` Composable
 function tints your icon based in the `tint` parameter, which defaults to **`LocalContentColor.current`**.

And as we expected, we should have both the SVG and `ImagVector`'s render identical, as we can spot bellow:

|            Octicon Smiley as svg             | Octicon Smiley as `ImageVector` (preview using compose-web wasm) |
|:--------------------------------------------:|:----------------------------------------------------------------:|
| ![smiley](/articles/compose-icon/smiley.svg) |                 <(Showcase preview="smiley" )/>                  |

# Avoiding repetitive work and handling complex icons
As we see in the previous section, we can create the `ImageVector`s by parsing the svg's `path`s and using the 
`ImageVector`'s path methods depending in the path instruction command, however, we also see that even for simple 
icons, like the [Octicon Smiley](https://primer.style/foundations/icons/smiley-24) requires a lot of work.

Additionally, svgs can be way more complex than just having simple paths. For example, svg also accepts groups,
 template objects (`use` tags), gradient, and transformations. Transformations make it even harder because you 
 need to calculate the transformation before parsing the svg path to the `ImageVector`, since it doesn't accepts
 transformation.

The [Android Studio Vector Assets tool](https://developer.android.com/studio/write/vector-asset-studio#running)
 does it for us when we create a drawable via Android Studio, so it would be safe to convert drawables directly,
 however I started to miss some features that are not implemented, or even not planned to be implemented on drawables
 that directly affect us when using vectors on Compose, and also limiting us.

Let's say I would like to use the Brazilian flag inside my application. Here is what it looks like if we parse it 
 to a drawable:

|            Brazilian flag as svg             | Brazilian flag as avg (preview using compose-web wasm) |
|:--------------------------------------------:|:------------------------------------------------------:|
| ![brasil](/articles/compose-icon/brasil.svg) |           <(Showcase preview="brasil-avg")/>           |

As you can see, we lost all the stars and if we zoom it a bit, we can also spot the letters a bit messed up:

<(Showcase preview="brasil-avg-zoomed" id="brasil-zoomed")/>

## A CLI-tool to convert both SVG and AVG to `ImageVector`s
Having that in mind, I decided to write my own CLI-tool to convert both SVG and AVG to `ImageVector`s.

<https://github.com/rafaeltonholo/svg-to-compose>

[move-to-command]: <https://www.w3.org/TR/SVG11/single-page.html#paths-PathDataMovetoCommands> "move to command"
[line-to-command]: <https://www.w3.org/TR/SVG11/single-page.html#paths-PathDataLinetoCommands> "line to command"
[curve-to-command]: <https://www.w3.org/TR/SVG11/single-page.html#paths-PathDataCubicBezierCommands> "curve to command"
[quad-to-command]: <https://www.w3.org/TR/SVG11/single-page.html#paths-PathDataQuadraticBezierCommands> "quad to command"
[arc-to-command]: <https://www.w3.org/TR/SVG11/single-page.html#paths-PathDataEllipticalArcCommands> "elliptical arc command"
[svg-width-attr]: <https://www.w3.org/TR/SVG11/single-page.html#struct-SVGElementWidthAttribute> "svg width attribute"
[svg-height-attr]: <https://www.w3.org/TR/SVG11/single-page.html#struct-SVGElementHeightAttribute> "svg height attribute"
[^1]: Android Vector Graphics, also know as Android Drawables
