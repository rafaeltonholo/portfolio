---
routeOverride: /articles/pt-br/how-a2ui-works
slug: how-a2ui-works
locale: pt-BR
title: 'Como o A2UI funciona: UI generativa como dados, não como código'
date: 17 de nov. de 2026
description: >
    Como o protocolo A2UI descreve a UI como dados: surfaces, a lista plana de componentes, data binding, e por que
    um modelo não deveria precisar escrever o formato que o renderer lê.
seoTitle: 'Como o A2UI funciona: UI generativa como dados, não como código'
authors:
    -   name: rafael
        avatar: https://rafael.tonholo.dev/images/writing.jpg
showThumbnail: false
thumbnail: /images/articles/how-a2ui-works/thumbnail.png
tags:
    - android
    - a2ui
    - generative-ui
---

Pede pra uma IA te ajudar a arrumar a mala de uma viagem e ela te devolve uma lista, em texto puro. Dá pra ler, mas não dá pra marcar nada.

Agora imagina que a resposta fosse um checklist de verdade, com checkboxes que você consegue tocar, desenhado pelo app com os componentes dele. Ninguém do time desenhou uma tela de "lista de mala" pra isso. O modelo descreveu a UI que queria e o app renderizou.

Essa é a ideia por trás da UI generativa, e o [A2UI](https://a2ui.org/) é um protocolo open-source pra isso. Já existe uma documentação boa de como plugar o renderer do AndroidX num app, então não vou só repetir ela aqui. Em vez disso, essa é uma série em duas partes. Nessa, vamos ver como o protocolo funciona de verdade e por que ele tem esse formato. Na [parte dois](/articles/pt-br/on-device-llm-a2ui-android), vamos ver o que acontece quando o "agente" não é um modelo gigante na nuvem, mas um pequeno rodando num celular de três anos atrás.

> [!NOTE]
> Essa parte segue a especificação A2UI v0.9.1, a versão que o [AndroidX A2UI Compose `1.0.0-alpha02`](https://developer.android.com/jetpack/androidx/releases/a2ui-compose) suporta, do jeito que eu li nos dias 13 e 14 de novembro de 2026. O AndroidX A2UI ainda está em alpha, então espere que alguns detalhes mudem.

## TL;DR

- O A2UI descreve a UI como dados: uma lista plana de componentes de um catálogo que o seu app registrou, mais um data model separado. O modelo nunca manda código.
- A lista plana existe pra streaming e atualizações parciais, mas é exatamente o formato que modelos pequenos erram.
- O que o usuário digita ou marca fica no data model local. O agente só fica sabendo quando uma action, como o toque num botão, pede isso.
- O A2UI separa o *wire format* (o que o renderer lê) do *inference format* (o que o modelo escreve). O formato compacto deles, o Express, foi feito pra modelos on-device, mas não existe um compilador em Kotlin que você possa colocar num app.

## Por que não deixar o modelo escrever a UI direto?

Antes de olhar pro A2UI em si, vale perguntar quais são as alternativas. Se você quer que um modelo produza uma UI interativa, você poderia:

- Pedir pra ele gerar código Compose. Só que você não consegue compilar e rodar um código que apareceu em runtime, e nem quer.
- Pedir HTML e mostrar numa WebView. Funciona, mas não parece nem se comporta como o resto do seu app, e agora você está renderizando um markup que você não controla.
- Montar um conjunto fixo de telas e deixar o modelo escolher uma via function calling. É seguro e nativo, mas aí você voltou a desenhar todas as telas na mão.

O A2UI fica no meio do caminho. O app registra um *catálogo* de componentes que sabe renderizar, e o agente descreve uma UI usando só esses componentes, em JSON puro. Olha como a especificação coloca isso[^spec]:

> The A2UI Protocol is designed for dynamically rendering user interfaces from a stream of JSON objects sent from a server (Agent). Its core philosophy emphasizes a clean separation of UI structure and application data, enabling progressive rendering as the client processes each message.

Pensa no catálogo como um cardápio que o app entrega pro agente. O agente pode combinar qualquer coisa do cardápio, mas não consegue inventar um prato novo. Um `CheckBox` na mensagem vira o checkbox do Compose do seu app, com o seu tema e o seu comportamento de acessibilidade.

## Anatomia de um stream A2UI

Bora olhar um de verdade. Esse é o checklist de mala do sample, escrito na mão, no formato v0.9.1 que o renderer do AndroidX suporta:

```json
{"version":"v0.9.1","createSurface":{"surfaceId":"packing","catalogId":"https://a2ui.org/specification/v0_9/catalogs/basic/catalog.json"}}
{"version":"v0.9.1","updateDataModel":{"surfaceId":"packing","path":"/","value":{"items":[{"name":"Towel","packed":false},{"name":"Sunscreen","packed":true}]}}}
{"version":"v0.9.1","updateComponents":{"surfaceId":"packing","components":[{"id":"root","component":"Column","children":["title","item0","item1"]},{"id":"title","component":"Text","text":"Weekend packing list","variant":"h2"},{"id":"item0","component":"CheckBox","label":{"path":"/items/0/name"},"value":{"path":"/items/0/packed"}},{"id":"item1","component":"CheckBox","label":{"path":"/items/1/name"},"value":{"path":"/items/1/packed"}}]}}
```

Cada linha é uma mensagem, e toda mensagem tem o mesmo formato: uma `version` e *exatamente uma* outra chave que diz que tipo de mensagem é. Vamos passar por elas.

### `createSurface`

Uma *surface* é a área onde a UI gerada vive. Você pode ter várias na tela ao mesmo tempo, cada uma com o próprio ID, componentes e dados. A mensagem tem dois campos obrigatórios:

- `surfaceId` dá o nome da surface. Toda mensagem seguinte dessa UI aponta pra ele.
- `catalogId` diz qual catálogo o agente vai usar.

Uma coisa que me surpreendeu: o `catalogId` parece uma URL, mas é só um nome. A especificação é bem clara nisso[^spec]:

> Note that `catalogId` is a string identifier, not a resolvable URI; while it is conventionally formatted as a URI ... it does not need to point to any deployed resource or downloadable file.

O app e o agente só precisam concordar sobre ele. Aliás, o ID que vem dentro do catálogo básico do AndroidX é o `v0_9`, mesmo a biblioteca falando v0.9.1.

### `updateDataModel`

Cada surface tem o seu próprio data model em JSON, totalmente separado dos componentes. Essa mensagem escreve nele num `path` no formato [JSON Pointer](https://datatracker.ietf.org/doc/html/rfc6901). Um path `/` substitui o modelo inteiro, e algo como `/items/1/packed` muda um valor só. Se o path ainda não existe, ele é criado; se a mensagem não tem `value`, a chave é removida.

### `updateComponents`

É aqui que a UI é descrita, e é a mais interessante. Repara que os componentes *não* são aninhados. O `Column` não contém os filhos; ele lista os IDs deles:

```json
{"id":"root","component":"Column","children":["title","item0","item1"]}
```

Cada componente tem:

- `id`, um nome único dentro da surface
- `component`, o tipo vindo do catálogo
- as propriedades que aquele tipo aceita, como `text`, `children` ou `value`

E um deles precisa ter o ID `root`, que é de onde a renderização começa.

Os checkboxes também não guardam o label e o estado direto. Eles fazem *bind* com o data model usando `{"path": ...}`, então mudar `/items/1/packed` depois atualiza o checkbox sem encostar nos componentes.

### `deleteSurface`

A última mensagem do servidor só remove uma surface, os componentes e os dados dela. Nada de mais.

## Por que uma lista plana?

Ok, mas por que esses IDs? Uma árvore aninhada seria bem mais fácil de ler. A especificação chama isso de *adjacency list model*[^spec]:

> This model allows the server to send component definitions in any order. Rendering can begin as soon as the `root` component is defined, with the client filling in or updating the rest of the tree progressively as additional definitions arrive.

Lembra que o A2UI foi pensado pra *streams*. Com uma lista plana:

- O renderer consegue mostrar o `root` antes do modelo terminar de escrever o resto. Um filho que ainda não existe é renderizado como um placeholder.
- O agente consegue atualizar um componente depois mandando só ele, com o mesmo ID.
- Um validador consegue checar todas as referências sem precisar andar por uma árvore profunda.

É um formato ótimo pra renderers. Mas será que é um formato ótimo pra um modelo *escrever*? Guarda essa dúvida, porque a parte dois coloca isso à prova.

## Data binding, e o que volta pro agente

Componentes de entrada como `CheckBox`, `TextField` e `Slider` fazem bind do valor nos dois sentidos. Quando o usuário marca uma caixa, o renderer escreve `true` imediatamente em `/items/0/packed` no data model local, e qualquer outra coisa ligada nesse path também atualiza.

O que ele *não* faz é avisar o agente. Da especificação[^spec]:

> User inputs (keystrokes, toggles) do **not** automatically trigger network requests to the server. The updated state is sent to the server only when a specific **User Action** is triggered (e.g., a `Button` click).

Um botão declara uma `action` com um nome de evento e um `context`, e o contexto pode fazer bind com paths de dados. Quando o usuário toca, o renderer resolve esses paths e manda uma mensagem `action` com os valores atuais. Então o agente só fica sabendo do que o botão pediu explicitamente, o que é uma propriedade de privacidade bem legal. Se uma surface realmente precisa do modelo inteiro em toda mensagem, o `createSurface` pode setar `sendDataModel: true`.

A mesma ideia de "dados, não código" vale pra lógica. Validações como "esse campo é obrigatório" ou "isso precisa ser um email" são escritas como chamadas a funções nomeadas do catálogo, tipo `required` ou `email`, então o agente nunca manda nada executável.

## v0.8, v0.9, v1.0: por que a versão importa

O site do A2UI agora documenta a v1.0 como a versão atual[^evolution], enquanto o renderer do AndroidX suporta a v0.9.1. As mensagens parecem parecidas, mas não são iguais: a v1.0 renomeia os bindings de `path` pra `@path`, pode colocar componentes dentro do `createSurface`, e adiciona mensagens novas. Não mande um exemplo da v1.0 pra um renderer v0.9.1.

A mudança mais interessante aconteceu antes, entre a v0.8 e a v0.9. Olha o que diz a especificação v0.9.1[^spec]:

> While v0.8 was optimized for LLMs that support structured output, v0.9 is designed to be embedded directly within a model's prompt. The LLM is then asked to produce JSON that matches the provided examples and schema descriptions.

E a parte honesta logo em seguida:

> The main disadvantage of this approach is that it requires more complex post-generation validation, as the LLM is not strictly constrained by the schema.

Em outras palavras, o A2UI espera que o modelo erre de vez em quando. O ciclo pretendido é *prompt, gerar, validar*, e quando a validação falha, você manda o erro de volta pro modelo pra ele tentar de novo.

## Wire format vs. inference format

Aqui vem a parte que eu achei mais interessante. A documentação do A2UI separa duas coisas:

- O *wire format* é o JSON que o renderer consome, que acabamos de ver.
- O *inference format* é o que o modelo realmente escreve, e que um compilador depois transforma em mensagens do wire.

Por que você ia querer que eles fossem diferentes? A [página de inference formats](https://a2ui.org/concepts/inference-formats/) lista os custos de fazer o modelo escrever o JSON do wire direto[^formats]:

> Repeated keys (`"id"`, `"component"`, `"@path"`) and hand-written component identifiers make raw wire JSON roughly three times longer than positional constructor calls.

E:

> Deeply nested braces, quotes, and manual component IDs increase syntax and reference errors, especially on smaller, faster models.

A resposta deles é o **A2UI Express**, uma linguagem compacta onde o modelo escreve chamadas de construtor aninhadas e um compilador faz o trabalho de achatar:

```text
<a2ui>
$/items = [{name: "Towel", packed: false}, {name: "Sunscreen", packed: true}]
root = Column([title, item0, item1])
title = Text("Weekend packing list", "h2")
item0 = CheckBox($/items/0/name, $/items/0/packed)
item1 = CheckBox($/items/1/name, $/items/1/packed)
</a2ui>
```

Esse é o mesmo checklist das três mensagens JSON lá de cima. Os argumentos são posicionais, seguindo a ordem das propriedades no schema do catálogo, então `Text("Weekend packing list", "h2")` significa `text` e depois `variant`. `$/path` faz bind com dados, e `$/path = value` escreve dados. O modelo pode até aninhar componentes inline, e o compilador inventa os IDs pra eles.

No benchmark do próprio projeto A2UI, rodado no Gemini Flash, o Express usou 45% menos tokens de prompt e 63% menos tokens de saída que o JSON do wire, com a mesma precisão[^formats]. E a [especificação do Express](https://github.com/a2ui-project/a2ui/blob/main/specification/proposals/express/a2ui_express.md) fala sem rodeio pra quem ele é[^express]:

> Small local models, such as Gemma 4 E2B and E4B, operate with limited context windows and reasoning budgets.

Mas tem um problema pra nós. Os compiladores do Express vivem nos SDKs de *agente* do A2UI, que existem pra TypeScript, Python e Dart[^sdks]. Do lado do Kotlin, o AndroidX entrega só o renderer. O repositório do A2UI tem sim um compilador Express experimental no SDK de agente JVM antigo, mas é uma biblioteca de servidor construída em cima de ANTLR, Jackson e do [Agent Development Kit](https://github.com/google/adk-python) do Google, e ela não é publicada no Maven. Quando o modelo roda *no celular*, o compilador também precisa rodar no celular.

## E agora?

Então é aqui que estamos: um protocolo ótimo pra renderers, um formato compacto feito pra modelos pequenos, e nenhum jeito de compilar esse formato dentro de um app Android.

Na [parte dois](/articles/pt-br/on-device-llm-a2ui-android), vamos rodar dois modelos num Pixel 7 Pro, ver o menor deles sofrer com o JSON do wire, escrever um compilador Express minúsculo em Kotlin e medir o quanto ele realmente ajuda.

Valeu por ler, e até lá!

## Fontes

[^spec]: Especificação do protocolo A2UI v0.9.1, https://a2ui.org/specification/v0.9.1-a2ui/

[^evolution]: Guia de evolução do A2UI v1.0, https://a2ui.org/specification/v1.0-evolution-guide/

[^formats]: Inference formats do A2UI, https://a2ui.org/concepts/inference-formats/

[^express]: Proposta do A2UI Express, https://github.com/a2ui-project/a2ui/blob/main/specification/proposals/express/a2ui_express.md

[^sdks]: SDKs do A2UI, https://a2ui.org/concepts/sdks/
