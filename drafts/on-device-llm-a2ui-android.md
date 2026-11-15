# Generative UI on a phone: A2UI, an on-device model, and a tiny compiler in Kotlin

Ask an AI assistant to help you pack for a trip and you'll get a list, as plain text. You can read it, but you can't tick anything off.

Now imagine the answer was a real checklist instead, with checkboxes you can tap, drawn by the app with its own components. Nobody on the team designed a "packing list" screen for it. The model described the UI it wanted, and the app rendered it.

That's the idea behind generative UI, and [A2UI](https://a2ui.org/) is an open-source protocol for it. There's already good documentation on how to plug the AndroidX renderer into an app, so I won't just repeat it here. Instead, let's look at how the protocol actually works, why it's shaped the way it is, and what happens when the "agent" isn't a big model in the cloud but a small one running on a three-year-old phone.

Spoiler: the smallest model I tried couldn't write valid A2UI at all. So we'll write a tiny compiler in Kotlin that lets models speak a much more compact language instead, and then measure how much that actually helps.

> [!NOTE]
> Everything here was tested on November 13 and 14, 2026, on a Pixel 7 Pro, with [AndroidX A2UI Compose `1.0.0-alpha02`](https://developer.android.com/jetpack/androidx/releases/a2ui-compose), [LiteRT-LM `0.19.1`](https://github.com/google-ai-edge/LiteRT-LM/releases), Qwen3-0.6B and Gemma 4 E2B. The full sample lives in [`examples/a2ui-packing`](https://github.com/rafaeltonholo/portfolio/tree/main/examples/a2ui-packing). AndroidX A2UI is still in alpha, so expect some details to change.

## TL;DR

- A2UI describes UI as data: a flat list of components from a catalog your app registered, plus a separate data model. The model never sends code.
- The flat list exists for streaming and partial updates, but it's exactly the shape small models get wrong. Qwen3-0.6B nested every component it wrote.
- A2UI separates the *wire format* (what the renderer reads) from the *inference format* (what the model writes). Its compact Express format is designed for on-device models, but there's no Kotlin compiler you can ship in an app.
- So I wrote a small one in Kotlin Multiplatform common code and benchmarked JSON against Express on a Pixel 7 Pro.
- Model size mattered more than format: Qwen3-0.6B produced 0 usable surfaces from JSON and 1 of 5 from Express, while Gemma 4 E2B handled both.
- Express still paid off with Gemma: a 37% shorter prompt, about a third faster to the first token, and checklists that showed every item the model generated. Its JSON surfaces often dropped items.

## Why not just let the model write the UI?

Before looking at A2UI itself, it's worth asking what the alternatives are. If you want a model to produce interactive UI, you could:

- Ask it to generate Compose code. Except you can't compile and run code that showed up at runtime, and you really don't want to.
- Ask it for HTML and show it in a WebView. That works, but it doesn't look or behave like the rest of your app, and you're now rendering untrusted markup.
- Build a fixed set of screens and let the model pick one through function calling. Safe and native, but you're back to designing every screen yourself.

A2UI sits in between. The app registers a *catalog* of components it knows how to render, and the agent describes a UI using only those components, as plain JSON. Here's how the spec puts it[^spec]:

> The A2UI Protocol is designed for dynamically rendering user interfaces from a stream of JSON objects sent from a server (Agent). Its core philosophy emphasizes a clean separation of UI structure and application data, enabling progressive rendering as the client processes each message.

Think of the catalog as a menu the app hands to the agent. The agent can combine anything on the menu, but it can't add new dishes. A `CheckBox` in the message becomes your app's Compose checkbox, with your theme and your accessibility behaviour.

## Anatomy of an A2UI stream

Let's look at a real one. This is the packing checklist from the sample, written by hand, in the v0.9.1 format the AndroidX renderer supports:

```json
{"version":"v0.9.1","createSurface":{"surfaceId":"packing","catalogId":"https://a2ui.org/specification/v0_9/catalogs/basic/catalog.json"}}
{"version":"v0.9.1","updateDataModel":{"surfaceId":"packing","path":"/","value":{"items":[{"name":"Towel","packed":false},{"name":"Sunscreen","packed":true}]}}}
{"version":"v0.9.1","updateComponents":{"surfaceId":"packing","components":[{"id":"root","component":"Column","children":["title","item0","item1"]},{"id":"title","component":"Text","text":"Weekend packing list","variant":"h2"},{"id":"item0","component":"CheckBox","label":{"path":"/items/0/name"},"value":{"path":"/items/0/packed"}},{"id":"item1","component":"CheckBox","label":{"path":"/items/1/name"},"value":{"path":"/items/1/packed"}}]}}
```

Every line is one message, and every message has the same shape: a `version` and *exactly one* other key that says what kind of message it is. Let's go through them.

### `createSurface`

A *surface* is the area where the generated UI lives. You can have several on screen at once, each with its own ID, components and data. The message has two required fields:

- `surfaceId` names the surface. Every later message for this UI points to it.
- `catalogId` says which catalog the agent will use.

One thing that surprised me: `catalogId` looks like a URL, but it's just a name. The spec is clear about it[^spec]:

> Note that `catalogId` is a string identifier, not a resolvable URI; while it is conventionally formatted as a URI ... it does not need to point to any deployed resource or downloadable file.

The app and the agent simply have to agree on it. In fact, the ID baked into the AndroidX basic catalog is the `v0_9` one, even though the library speaks v0.9.1.

### `updateDataModel`

Each surface has its own JSON data model, completely separate from the components. This message writes into it at a [JSON Pointer](https://datatracker.ietf.org/doc/html/rfc6901) `path`. A path of `/` replaces the whole model, and something like `/items/1/packed` changes a single value. If the path doesn't exist yet, it's created; if the message has no `value`, the key is removed.

### `updateComponents`

This is where the UI is described, and it's the interesting one. Notice that the components are *not* nested. The `Column` doesn't contain its children; it lists their IDs:

```json
{"id":"root","component":"Column","children":["title","item0","item1"]}
```

Each component has:

- `id`, a name unique within the surface
- `component`, the type from the catalog
- the properties that type accepts, such as `text`, `children` or `value`

And one of them must have the ID `root`, which is where rendering starts.

The checkboxes don't hold their label and state directly either. They *bind* to the data model with `{"path": ...}`, so changing `/items/1/packed` later updates the checkbox without touching the components.

### `deleteSurface`

The last server message just removes a surface, its components and its data. Nothing fancy.

## Why a flat list?

Ok, but why the IDs? A nested tree would be much easier to read. The spec calls this the *adjacency list model*[^spec]:

> This model allows the server to send component definitions in any order. Rendering can begin as soon as the `root` component is defined, with the client filling in or updating the rest of the tree progressively as additional definitions arrive.

Remember that A2UI was designed for *streams*. With a flat list:

- The renderer can show `root` before the model has finished writing the rest. A child that doesn't exist yet just renders as a placeholder.
- The agent can update one component later by sending only that component with the same ID.
- A validator can check every reference without walking a deep tree.

It's a great format for renderers. Is it a great format for a model to *write*, though? Hold that thought.

## Data binding, and what goes back to the agent

Input components like `CheckBox`, `TextField` and `Slider` bind their value both ways. When the user ticks a box, the renderer immediately writes `true` to `/items/0/packed` in the local data model, and anything else bound to that path updates too.

What it does *not* do is tell the agent. From the spec[^spec]:

> User inputs (keystrokes, toggles) do **not** automatically trigger network requests to the server. The updated state is sent to the server only when a specific **User Action** is triggered (e.g., a `Button` click).

A button declares an `action` with an event name and a `context`, and the context can bind to data paths. When the user taps it, the renderer resolves those paths and sends an `action` message with the current values. So the agent only hears about what the button explicitly asks for, which is a nice privacy property. If a surface really needs the whole model on every message, `createSurface` can set `sendDataModel: true`.

The same "data, not code" idea applies to logic. Validation such as "this field is required" or "this must be an email" is written as calls to named functions from the catalog, like `required` or `email`, so the agent never sends anything executable.

## v0.8, v0.9, v1.0: why the version matters

The A2UI site now documents v1.0 as the current version[^evolution], while the AndroidX renderer supports v0.9.1. The messages look similar, but not the same: v1.0 renames bindings from `path` to `@path`, can put components inside `createSurface`, and adds new messages. Don't send a v1.0 example to a v0.9.1 renderer.

The more interesting change happened earlier, between v0.8 and v0.9. Here's the v0.9.1 spec[^spec]:

> While v0.8 was optimized for LLMs that support structured output, v0.9 is designed to be embedded directly within a model's prompt. The LLM is then asked to produce JSON that matches the provided examples and schema descriptions.

And the honest part right after it:

> The main disadvantage of this approach is that it requires more complex post-generation validation, as the LLM is not strictly constrained by the schema.

In other words, A2UI expects the model to get it wrong sometimes. The intended loop is *prompt, generate, validate*, and when validation fails, you send the error back to the model so it can try again.

## Wire format vs. inference format

Here's the part I found most interesting. The A2UI docs separate two things:

- The *wire format* is the JSON the renderer consumes, which we just went through.
- The *inference format* is whatever the model actually writes, which a compiler then turns into wire messages.

Why would you want those to be different? The [inference formats page](https://a2ui.org/concepts/inference-formats/) lists the costs of making a model write wire JSON directly[^formats]:

> Repeated keys (`"id"`, `"component"`, `"@path"`) and hand-written component identifiers make raw wire JSON roughly three times longer than positional constructor calls.

And:

> Deeply nested braces, quotes, and manual component IDs increase syntax and reference errors, especially on smaller, faster models.

Their answer is **A2UI Express**, a compact language where the model writes nested constructor calls and a compiler does the flattening:

```text
<a2ui>
$/items = [{name: "Towel", packed: false}, {name: "Sunscreen", packed: true}]
root = Column([title, item0, item1])
title = Text("Weekend packing list", "h2")
item0 = CheckBox($/items/0/name, $/items/0/packed)
item1 = CheckBox($/items/1/name, $/items/1/packed)
</a2ui>
```

That's the same checklist as the three JSON messages above. Arguments are positional, following the order of the properties in the catalog schema, so `Text("Weekend packing list", "h2")` means `text` and then `variant`. `$/path` binds to data, and `$/path = value` writes data. The model can even nest components inline, and the compiler will invent IDs for them.

In the A2UI project's own benchmark, run on Gemini Flash, Express used 45% fewer prompt tokens and 63% fewer output tokens than wire JSON, with the same accuracy[^formats]. And the [Express spec](https://github.com/a2ui-project/a2ui/blob/main/specification/proposals/express/a2ui_express.md) says outright who it's for[^express]:

> Small local models, such as Gemma 4 E2B and E4B, operate with limited context windows and reasoning budgets.

There's a catch for us, though. The Express compilers live in A2UI's *agent* SDKs, which are available for TypeScript, Python and Dart[^sdks]. On the Kotlin side, AndroidX ships the renderer only. The A2UI repository does have an experimental Express compiler in its legacy JVM agent SDK, but that's a server library built on ANTLR, Jackson and Google's [Agent Development Kit](https://github.com/google/adk-python), and it isn't published to Maven. When the model runs *on the phone*, the compiler has to run on the phone too.

## Picking the model

Before writing a compiler, we need a model. The easiest option on Android is Gemini Nano through the [ML Kit Prompt API](https://developer.android.com/ai/gemini-nano): the system manages the model, and you don't ship anything[^nano]. I tried it on my Pixel 7 Pro, and:

```text
status=UNAVAILABLE
```

Gemini Nano is only offered on [a list of recent devices](https://developers.google.com/ml-kit/genai), and the AICore installed on this phone is just a stub. That's the trade-off worth knowing about: the system model is the smoothest path where it exists, but if you want to reach older devices, you have to bring your own model.

That's what [LiteRT-LM](https://github.com/google-ai-edge/LiteRT-LM/blob/main/docs/api/kotlin/getting_started.md) is for. You give it a `.litertlm` model file and it runs it with a Kotlin API. I used two models from the LiteRT community on Hugging Face:

- [Qwen3-0.6B](https://huggingface.co/litert-community/Qwen3-0.6B), about 600 MB, a really small model
- [Gemma 4 E2B](https://huggingface.co/litert-community/gemma-4-E2B-it-litert-lm), about 2.6 GB, the size the Express spec was designed for

You may have seen that [LiteRT now ships in Google Play services](https://developers.googleblog.com/smaller-apps-and-faster-gpu-inference-with-litert-in-google-play-services/), so apps no longer need to bundle their own ML runtime[^litert]. That announcement covers LiteRT for custom `.tflite` models. Running a language model still goes through the LiteRT-LM library, which the sample adds as a regular dependency.

## First attempt: let the model write JSON

I started the obvious way: describe the protocol in the prompt, give one example of the two messages, and ask the model for a checklist. Here's what Qwen3-0.6B sent back when I asked it to pack for a beach trip (trimmed):

```json
{
  "updateComponents": {
    "surfaceId": "packing",
    "components": [
      {
        "id": "root",
        "component": "Column",
        "children": [
          {
            "id": "title",
            "component": "Text",
            "text": "Board game night",
            "variant": "h2"
          },
          ...
```

See the problem? Well, two of them. The title is copied straight from the prompt's example, but the bigger one is `children`: it should be a list of IDs, and the model put the components *inside* it. Three of the five requests came back nested like this, even though the prompt literally showed a flat list. The other two were flat, but forgot the `updateDataModel` message, so every checkbox pointed at data that didn't exist.

Remember that thought we held? This is the shape that's great for renderers and hard for small models. Which is exactly the job of an inference format: let the model write the tree, and let a compiler flatten it.

## Writing an Express compiler in Kotlin

I didn't need all of Express to test the idea. The sample supports a subset: assignments, inline nesting, positional and keyword arguments, `_` to skip an optional argument, strings, numbers, booleans, lists, maps, `$/path` bindings, data assignments and `Event(...)` actions. Templates, validation checks, functions and multiple catalogs are left out.

It lives in its own module, [`express`](https://github.com/rafaeltonholo/portfolio/tree/main/examples/a2ui-packing/express), and everything is in `commonMain` with no platform code. It compiles for Android and the JVM, and the tests run on the JVM.

The pipeline is a classic one:

```mermaid
flowchart TD
    A[Model response] --> B[Find the a2ui block]
    B --> C[Lexer]
    C --> D[Parser]
    D --> E[Flatten from root]
    D --> F[Fold data assignments]
    E --> G[updateComponents]
    F --> H[updateDataModel]
```

Using it looks like this:

```kotlin
val catalog = ExpressCatalog.fromSchema(catalogJson)
val compiler = ExpressCompiler(catalog)

val messages: List<JsonObject> = compiler.compile(response = modelOutput, surfaceId = "packing")
messages.forEach { processor.processInput(parser, it.toString()) }
```

A few decisions worth pointing out.

### One source for the signatures

Since arguments are positional, the compiler has to know that `CheckBox` takes `label` and then `value`. That order comes from the catalog's JSON schema, and the prompt needs the very same list. If those two ever disagree, the model writes arguments in one order and the compiler reads them in another.

So `ExpressCatalog` reads the schema once, and both sides use it. The prompt gets its signatures from the catalog:

```kotlin
catalog.promptSignatures(listOf("Column", "Text", "CheckBox"))
```

```text
Column(children, justify?, align?)
Text(text, variant?: h1|h2|h3|h4|h5|caption|body)
CheckBox(label, value)
```

I checked the property names for these components in the AndroidX catalog classes, and they match the published v0.9 schema, whose order the compiler follows.

### Flattening

The flattener walks the tree from `root`. A component assigned to a variable keeps the variable name as its ID, and an inline component gets an ID made from its parent and the slot it fills. So in:

```text
root = Card(Column([Text($/flight/number)]))
```

the `Column` becomes `root_child` and the `Text` becomes `root_child_children_0`. That's the same naming the reference compilers use, which let me test against the example in the A2UI docs: the compiler has to produce exactly the messages the docs show.

### Errors a model can act on

When something's wrong, the compiler throws an `ExpressException` with the line and a short reason, such as `Line 2: Chart is not in the catalog` or `Line 1: CheckBox is missing value`. Short reasons matter, because the A2UI loop is to send the error back to the model and ask it to fix that line.

### A renderer race

My first device run rendered the title but left the checkboxes as grey placeholders, and they never filled in. The compiler was sending `updateComponents` before `updateDataModel`, which the protocol allows, so I swapped the order, the placeholders went away, and I moved on thinking the renderer ignored data that arrived after its components.

Ok, but was the order really the problem? When I came back to record it for this article, I couldn't make it happen on demand, so I tested it properly. The [probe](https://github.com/rafaeltonholo/portfolio/blob/main/examples/a2ui-packing/app/src/androidTest/kotlin/dev/tonholo/a2uipacking/RendererRaceProbe.kt) sends the same recorded checklist to AndroidX A2UI `1.0.0-alpha02` in both orders, with and without a one-second pause between the messages. It collects messages either on a background thread, the way the [official guide](https://developer.android.com/develop/ui/compose/agentic/render-surfaces) does[^render], or on the main thread:

| Collector | Order | Pause | Stuck on placeholders |
| --- | --- | --- | --- |
| Background | components, then data | none | 4 of 32 runs |
| Background | data, then components | none | 0 of 13 runs |
| Main | components, then data | none | 0 of 13 runs |
| Either | either | 1 s | 0 of 20 runs |

Every run is in [`renderer-race.txt`](https://github.com/rafaeltonholo/portfolio/blob/main/examples/a2ui-packing/benchmark-results/renderer-race.txt).

{{{ .components.molecules.ArticleFigureRow

![The Weekend packing list title with two grey placeholder bars where the checkboxes should be](/images/articles/on-device-llm-a2ui-android/renderer-race-stuck.gif "A real failing run: components first, data right after. The checkboxes keep shimmering and never fill in.")

![A loading spinner, then the Weekend packing list appears and Towel gets ticked](/images/articles/on-device-llm-a2ui-android/renderer-race-data-first.gif "Data first, components 2.5 seconds later: it renders when they arrive, and Towel ticks. The pause before the tap is sped up.")

}}}

So my first explanation was wrong. The renderer does pick up data that arrives later: with a pause between the messages, it rendered every time. What I hit is an intermittent race, about one run in eight, and I only saw it with the collector on a background thread and the data landing right after the components. Don't read the zeros as proof, though: at one in eight, 13 clean runs can still be luck, so I can't rule it out in the other cells. I haven't found the root cause, and it's an alpha, so it may be gone by the time you read this. The compiler still sends the data first, which avoided it in every run I did, and the protocol is fine with that order.

## What happened on the Pixel

Time to put it to the test. The [benchmark](https://github.com/rafaeltonholo/portfolio/blob/main/examples/a2ui-packing/app/src/androidTest/kotlin/dev/tonholo/a2uipacking/FormatBenchmarkTest.kt) asks each model for the same five checklists, a beach trip, a rainy hike, a ski weekend, a business trip and a picnic, once as JSON and once as Express. Both prompts describe the same UI and use the same example. Every response goes through a structural check: a flat list with a `root`, children that exist, only `Column`, `Text` and `CheckBox`, and bindings that point at real data. Everything ran on the Pixel's CPU, and the raw results are in [`benchmark-results`](https://github.com/rafaeltonholo/portfolio/tree/main/examples/a2ui-packing/benchmark-results).

> [!NOTE]
> I ran each request twice, but at a temperature of 0.2 both runs came out identical, so think of this as five requests per cell. That's enough to see big differences, not small ones.

### Did it produce a surface?

| Model | JSON | Express |
| --- | --- | --- |
| Qwen3-0.6B | 0 of 5 | 2 of 5 (1 usable) |
| Gemma 4 E2B | 5 of 5 | 4 of 5 |

Qwen couldn't write JSON at all, and Express only partly helped. One of its two passing responses was just a title with no checkboxes, which is valid A2UI but not much of a packing list. The failures were all real mistakes, not compiler gaps: once it wrote XML inside the `<a2ui>` block, and twice it wrote an empty `Column()`, one of them after a stray `$` that broke the line before it.

Gemma was a different story: it wrote valid JSON every time. One detail, though: it followed the prompt to the letter, which meant components first and data second, the order that occasionally tripped the renderer race from earlier, and no `createSurface`, because the prompt didn't ask for one. Either way, the app would have to fix up those messages before showing them. Its one Express failure was the ski weekend, where it got stuck repeating `"Ski Socks (extra)"` until it hit the 512-token limit.

So the honest first conclusion is that model size mattered more than format. At 0.6B parameters, getting the format right left nothing for the actual task.

### What did the surface show?

Passing the check doesn't mean the checklist is any good, though. Here's what Gemma generated for each request, as items in the data model versus checkboxes on screen:

| Format | Beach | Hike | Ski | Business | Picnic |
| --- | --- | --- | --- | --- | --- |
| JSON | 3 / 3 | 6 / 2 | 6 / 3 | 5 / 2 | 3 / 2 |
| Express | 6 / 6 | 7 / 6 | looped | 7 / 7 | 5 / 5 |

With JSON, Gemma usually filled the data model with five or six items but only wrote two or three checkboxes, the same number as the prompt's example. The rest of the items were there, but nothing on screen showed them. With Express, the checkboxes matched the items in all but one case. My guess is that writing `item3 = CheckBox(...)` one line at a time is easier to keep going than a long JSON array of near-identical objects, but I only have five samples, so take that as a hypothesis.

Here's the business trip from the benchmark, rendered in the app from the saved responses. JSON on the left: five items in the data model, two checkboxes on screen. Express on the right: seven and seven. For the JSON side, I added the `createSurface` that the JSON prompt doesn't ask for, and nothing else changed.

{{{ .components.molecules.ArticleFigureRow

![The packing app showing a Two-Day Business Trip Packing checklist with two checkboxes, Business Suit and Laptop](/images/articles/on-device-llm-a2ui-android/business-trip-json.png "Gemma's JSON response: five items in the data model, but only two checkboxes. Travel Documents, Medication and Snacks never made it to the screen.")

![The packing app showing a Two-Day Business Trip Packing Checklist with seven checkboxes, from Suitcase to Snacks](/images/articles/on-device-llm-a2ui-android/business-trip-express.png "Gemma's Express response for the same request: seven items, seven checkboxes.")

}}}

### How long did it take?

| Model | Prompt | Time to first token | Total, valid runs only |
| --- | :---: | :---: | --- |
| Gemma 4 E2B, JSON | 1,112 characters | 4.6 s | 33 s for a median of 5 items |
| Gemma 4 E2B, Express | 704 characters | 3.0 s | 37 s for a median of 6.5 items |

The Express prompt is 37% shorter, and the first token arrived about a third sooner, consistent with less prompt to read. Qwen showed the same drop, from 9.9 s to 6.7 s. Generation speed itself didn't change with the format.

What about the output savings from that benchmark? I didn't see them here. Per checklist item, Gemma spent about 42 output tokens with JSON and 41 with Express. That's less surprising than it sounds: the prompt's example gives every checkbox its own variable, which is Express at its least compact, and the item names make up a big part of the output in both formats. Templates, which let a single line repeat a component for every item in a list, would probably change that, but the sample doesn't support them yet. LiteRT-LM reports output token counts, and those are the only token numbers I used.

I didn't compare the two models' speed with each other. Qwen generated about 4 tokens per second and Gemma about 7.5, even though Qwen is much smaller, and I haven't figured out why.

### And in the app

Finally, the real thing: Gemma 4 E2B, the compiler and the AndroidX renderer in the sample app, with the request "Pack for a rainy weekend in Halifax". It produced a title and five checkboxes (raincoat, warm socks, a cozy blanket, books and hot cocoa, which honestly sounds like a good weekend), and ticking one updated the local data model right away. I ran it three times while testing and got the same five items every time, and the two runs I timed took 34 and 30 seconds.

{{{ .components.molecules.ArticleVideo(src = "/images/articles/on-device-llm-a2ui-android/packing-app-run.mp4", poster = "/images/articles/on-device-llm-a2ui-android/packing-app-run-poster.png", caption = "Gemma 4 E2B on a Pixel 7 Pro. The 30 seconds of generation are sped up six times; the counter on the card shows the real time.") }}}

## Handling user actions

The sample stops at rendering, so this part is design guidance rather than tested code. The compiler already turns `Event("done", {items: $/items})` into an action, but I haven't run the round trip on the device.

When the app receives an action, it should:

1. Check that the event name is one the app expects.
2. Validate the values and decide which ones the next turn needs.
3. Either handle a known action in app code or send a bounded description of it to the model.
4. Compile and process the model's next response for the right surface.

Don't let the model choose arbitrary app operations just because an event name showed up in generated data. A button labelled "Book trip" is still only a request, and the app decides what it's allowed to do and when the user has to confirm it.

## And Kotlin Multiplatform?

The compiler is the part that was easiest to share: it's pure Kotlin in `commonMain`, with kotlinx.serialization as its only dependency. The prompts, the validation and the conversation state could live there too.

What doesn't come along for free is the rest. The AndroidX renderer is Android only, though there's a [community KMP port](https://github.com/BinaryTape/a2ui-kmp) worth checking. LiteRT-LM documents a Kotlin API for Android and the JVM and a separate Swift API in early preview, so an iOS target would need its own inference integration.

## A few things worth testing

Before shipping something like this, test the whole loop on the devices and models you intend to support:

- Recorded responses, both valid and broken: nested components, unknown components, missing arguments, truncated output.
- Bound data, not just literal text, in the renderer you actually ship.
- Messages that arrive over time, not all at once. A model streams, so test with pauses between messages, and on a real clock: inside a Compose test, `delay()` runs on a virtual clock, and my first version of the race test never sent its second message at all.
- Model download, storage, cancellation and app backgrounding.
- Time to first useful UI, memory and battery across representative devices.
- Accessibility, focus and readable fallback states for the generated UI.

Model output is variable, so one good run proves very little. Keep a set of requests and recorded responses, and run them again whenever the prompt, model, catalog or renderer changes.

## Conclusion

A2UI's core idea is simple: the app owns a catalog of components, and the agent describes UI as data, never code. The flat component list and the separate data model make a lot of sense for streaming renderers, and the spec is refreshingly honest that models will get things wrong and need validation.

What surprised me is how much the *inference format* matters once the model runs on the phone. A 0.6B model couldn't write the wire format at all. A 2B-class model could, but it still built better checklists, and started faster, when it wrote Express and let a compiler do the flattening. And because that compiler is a few hundred lines of plain Kotlin in `commonMain`, it can live right next to the model, on Android today and anywhere Kotlin runs later.

If you try this yourself, the whole sample is on GitHub in [`examples/a2ui-packing`](https://github.com/rafaeltonholo/portfolio/tree/main/examples/a2ui-packing): the Express compiler, the app, the benchmark and the race probe. Start with a recorded response and the real renderer, then add the model and measure on the devices you care about. Five requests on one phone taught me more than any amount of reading the docs.

Thanks for reading this far! I hope this gives you a starting point for experimenting with on-device AI and A2UI.

## Sources

[^spec]: A2UI v0.9.1 protocol specification, https://a2ui.org/specification/v0.9.1-a2ui/

[^evolution]: A2UI v1.0 evolution guide, https://a2ui.org/specification/v1.0-evolution-guide/

[^formats]: A2UI inference formats, https://a2ui.org/concepts/inference-formats/

[^express]: A2UI Express proposal, https://github.com/a2ui-project/a2ui/blob/main/specification/proposals/express/a2ui_express.md

[^sdks]: A2UI SDKs, https://a2ui.org/concepts/sdks/

[^nano]: Gemini Nano on Android, https://developer.android.com/ai/gemini-nano

[^litert]: Smaller apps and faster GPU inference with LiteRT in Google Play services, https://developers.googleblog.com/smaller-apps-and-faster-gpu-inference-with-litert-in-google-play-services/

[^render]: Render A2UI surfaces in your app, https://developer.android.com/develop/ui/compose/agentic/render-surfaces
