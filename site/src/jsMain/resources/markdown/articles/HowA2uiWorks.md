---
title: 'How A2UI works: generative UI as data, not code'
date: nov 21, 2026
description: >
    How the A2UI protocol describes UI as data: surfaces, the flat component list, data binding, and why a model
    shouldn't have to write the format a renderer reads.
seoTitle: 'How A2UI works: generative UI as data, not code'
authors:
    -   name: rafael
        avatar: https://rafael.tonholo.dev/images/writing.jpg
reviewers:
    -   name: Anderson Matos
        link: https://andersonmatos.ai
showThumbnail: false
thumbnail: /images/articles/how-a2ui-works/thumbnail.png
tags:
    - android
    - a2ui
    - generative-ui
---

Ask an AI assistant to help you pack for a trip and you'll get a list, as plain text. You can read it, but you can't tick anything off.

Now imagine the answer was a real checklist instead, with checkboxes you can tap, drawn by the app with its own components. Nobody on the team designed a "packing list" screen for it. The model described the UI it wanted, and the app rendered it.

That's the idea behind generative UI, and [A2UI](https://a2ui.org/) is an open-source protocol for it. There's already good documentation on how to plug the AndroidX renderer into an app, so I won't just repeat it here. Instead, this is a series in two parts. In this one, let's look at how the protocol actually works and why it's shaped the way it is. In part two (coming soon), we'll see what happens when the "agent" isn't a big model in the cloud but a small one running on a three-year-old phone.

> [!NOTE]
> This part follows the A2UI v0.9.1 specification, the version that [AndroidX A2UI Compose `1.0.0-alpha02`](https://developer.android.com/jetpack/androidx/releases/a2ui-compose) supports, as I read it on November 13 and 14, 2026. AndroidX A2UI is still in alpha, so expect some details to change.

## TL;DR

- A2UI describes UI as data: a flat list of components from a catalog your app registered, plus a separate data model. The model never sends code.
- The flat list exists for streaming and partial updates, but it's exactly the shape small models get wrong.
- What the user types or ticks stays in the local data model. The agent only hears about it when an action, such as a button tap, asks for it.
- A2UI separates the *wire format* (what the renderer reads) from the *inference format* (what the model writes). Its compact Express format is designed for on-device models, but there's no Kotlin compiler you can ship in an app.

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

The app and the agent simply have to agree on it, and here the spec doesn't fully agree with itself. Its v0.9.1 examples use a `v0_9_1` ID, but the official v0.9.1 basic catalog file declares the `v0_9` one, the same ID baked into the AndroidX basic catalog, even though the library speaks v0.9.1. AndroidX `1.0.0-alpha02` maps the `v0_9_1` ID to the `v0_9` one, so either works there. The `version` field is just as forgiving: the v0.9.1 schema accepts both `"v0.9"` and `"v0.9.1"`.

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

It's a great format for renderers. Is it a great format for a model to *write*, though? Hold that thought, because part two puts it to the test.

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

The Express compilers live in A2UI's *agent* SDKs, which are available for TypeScript, Python and Dart[^sdks]. They can target v0.9, v0.9.1 or v1.0, so their output works with a v0.9.1 renderer like the AndroidX one, even though every example in the Express proposal is v1.0[^express].

There's a catch for us, though. When the model runs *on the phone*, the compiler has to run on the phone too, and none of those SDKs is for Kotlin. AndroidX ships the renderer only. The A2UI repository does have an experimental Express compiler in its legacy JVM agent SDK, but that's a server library built on ANTLR, Jackson and Google's [Agent Development Kit](https://github.com/google/adk-python), and it isn't published to Maven. So, today, there's no Express compiler you can ship inside an Android app.

## Wrapping up

In this article, you have learned how A2UI describes UI as data: a flat list of components from a catalog your app already knows, a separate data model, and actions that decide what goes back to the agent. The model never sends code, and your app stays in charge of how everything looks and behaves.

Most of that shape is there to help the renderer. The flat list lets the UI show up while the stream is still coming in, lets the agent update a single component, and makes validation simple. The local data model keeps what the user types on the device until a button asks for it.

Of course, that convenience has a cost, and the model pays it. The format a renderer wants to read isn't the format a model is best at writing. The A2UI team knows this: v0.9 moved from structured output to prompting plus validation, and Express exists so the model can write something shorter while a compiler does the flattening.

So the next time you look at an A2UI payload, remember: it was designed to be read by a renderer, not written by a model. With a big model in the cloud, that's a detail. With a small model on a phone, it might be the whole problem.

## What's next

In part two (coming soon), we'll run two models on a Pixel 7 Pro, watch the smaller one struggle with wire JSON, write a tiny Express compiler in Kotlin, and measure how much it actually helps.

Thanks for reading, and see you there!

## Sources

[^spec]: A2UI v0.9.1 protocol specification, https://a2ui.org/specification/v0.9.1-a2ui/

[^evolution]: A2UI v1.0 evolution guide, https://a2ui.org/specification/v1.0-evolution-guide/

[^formats]: A2UI inference formats, https://a2ui.org/concepts/inference-formats/

[^express]: A2UI Express proposal, https://github.com/a2ui-project/a2ui/blob/main/specification/proposals/express/a2ui_express.md

[^sdks]: A2UI SDKs, https://a2ui.org/concepts/sdks/
