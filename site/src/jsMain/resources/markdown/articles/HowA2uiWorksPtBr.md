---
routeOverride: /articles/pt-br/how-a2ui-works
slug: how-a2ui-works
locale: pt-BR
title: 'Como o A2UI funciona: UI generativa como dados, não como código'
date: 21 de nov. de 2026
description: >
    Como o protocolo A2UI descreve a UI como dados: surfaces, a lista plana de componentes, data binding, e por que
    um modelo não deveria precisar escrever o formato que o renderer lê.
seoTitle: 'Como o A2UI funciona: UI generativa como dados, não como código'
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

Peça pra uma IA te ajudar a arrumar a mala para uma viagem e ela vai te entregar uma lista com apenas texto. Até dá pra ler, mas não vai dar pra marcar nada.

Agora imagina que a resposta fosse um checklist de verdade, com checkboxes que você consegue interagir, desenhado pelo app com os componentes dele. Ninguém do time criou uma tela de "checklist de viagem" pra isso. O modelo descreveu a UI que queria e o app simplesmente renderizou.

Essa é a ideia por trás da UI generativa, e o [A2UI](https://a2ui.org/) é um protocolo open-source pra isso. Já existe uma documentação boa de como usar o renderer do AndroidX num app, então não vou repetir isso aqui. A ideia é ir um pouco mais a fundo, em duas partes. Nesta primeira, vamos ver como o protocolo funciona de verdade e por que ele tem esse formato. Na parte dois (em breve), vamos ver o que acontece quando o "agente" não é um modelo gigante na nuvem, mas um pequeno rodando num celular de três anos atrás.

> [!NOTE]
> Essa parte segue a especificação A2UI v0.9.1, a versão que o [AndroidX A2UI Compose `1.0.0-alpha02`](https://developer.android.com/jetpack/androidx/releases/a2ui-compose) suporta, do jeito que eu li nos dias 13 e 14 de novembro de 2026. O AndroidX A2UI ainda está em alpha, então espere que alguns detalhes mudem. As citações da especificação e da documentação do A2UI, que são em inglês, aparecem aqui em tradução livre.

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

> O Protocolo A2UI foi projetado para renderizar dinamicamente interfaces de usuário a partir de um stream de objetos JSON enviados por um servidor (Agente). Sua filosofia central enfatiza uma separação clara entre a estrutura da UI e os dados da aplicação, permitindo a renderização progressiva à medida que o cliente processa cada mensagem.

Pensa no catálogo como um cardápio que o app entrega pro agente. O agente pode combinar qualquer coisa do cardápio, mas não consegue inventar um prato novo. Um `CheckBox` na mensagem vira o checkbox do Compose do seu app, com o seu tema e o seu comportamento de acessibilidade.

## Anatomia de um stream A2UI

Bora olhar um de verdade. Esse é o checklist de viagem do sample, escrito à mão, no formato v0.9.1 que o renderer do AndroidX suporta:

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

> Observe que `catalogId` é um identificador do tipo string, e não uma URI resolvível; embora seja formatado por convenção como uma URI ... ele não precisa apontar para nenhum recurso publicado ou arquivo para download.

O app e o agente só precisam concordar sobre ele, e aqui nem a spec concorda totalmente com ela mesma. Os exemplos da v0.9.1 usam um ID `v0_9_1`, mas o arquivo oficial do catálogo básico da v0.9.1 declara o `v0_9`, o mesmo ID que vem dentro do catálogo básico do AndroidX, mesmo a biblioteca falando v0.9.1. O AndroidX `1.0.0-alpha02` mapeia o ID `v0_9_1` pro `v0_9`, então os dois funcionam ali. O campo `version` também é tolerante: o schema da v0.9.1 aceita tanto `"v0.9"` quanto `"v0.9.1"`.

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

A última mensagem do servidor só remove uma surface, os componentes e os dados dela. Nada demais.

## Por que uma lista plana?

Tá, mas pra que esses IDs? Uma árvore aninhada seria bem mais fácil de ler. A especificação chama isso de *adjacency list model*[^spec]:

> Esse modelo permite que o servidor envie as definições de componentes em qualquer ordem. A renderização pode começar assim que o componente `root` for definido, com o cliente preenchendo ou atualizando o restante da árvore progressivamente, à medida que novas definições chegam.

Lembra que o A2UI foi pensado pra *streams*. Com uma lista plana:

- O renderer consegue mostrar o `root` antes do modelo terminar de escrever o resto. Um filho que ainda não existe é renderizado como um placeholder.
- O agente consegue atualizar um componente depois mandando só ele, com o mesmo ID.
- Um validador consegue checar todas as referências sem precisar andar por uma árvore profunda.

É um formato ótimo pra renderers. Mas será que é um formato ótimo pra um modelo *escrever*? Segura essa dúvida, porque na parte dois a gente vê isso mais a fundo.

## Data binding, e o que volta pro agente

Componentes de entrada como `CheckBox`, `TextField` e `Slider` fazem bind do valor nos dois sentidos. Quando o usuário marca uma caixa, o renderer escreve `true` imediatamente em `/items/0/packed` no data model local, e qualquer outra coisa ligada nesse path também atualiza.

O que ele *não* faz é avisar o agente. Da especificação[^spec]:

> As entradas do usuário (teclas digitadas, alternâncias) **não** disparam requisições de rede para o servidor automaticamente. O estado atualizado é enviado ao servidor somente quando uma **User Action** específica é disparada (por exemplo, o clique em um `Button`).

Um botão declara uma `action` com um nome de evento e um `context`, e o contexto pode fazer bind com paths de dados. Quando o usuário toca, o renderer resolve esses paths e manda uma mensagem `action` com os valores atuais. Então o agente só fica sabendo do que o botão pediu explicitamente, o que é essencial para privacidade. Se uma surface realmente precisa do modelo inteiro em toda mensagem, o `createSurface` pode setar `sendDataModel: true`.

A mesma ideia de "dados, não código" vale pra lógica. Validações como "esse campo é obrigatório" ou "isso precisa ser um email" são escritas como chamadas a funções nomeadas do catálogo, tipo `required` ou `email`, então o agente nunca manda nada executável.

## v0.8, v0.9, v1.0: por que a versão importa

O site do A2UI agora documenta a v1.0 como a versão atual[^evolution], enquanto o renderer do AndroidX suporta a v0.9.1. As mensagens parecem parecidas, mas não são iguais: a v1.0 renomeia os bindings de `path` pra `@path`, pode colocar componentes dentro do `createSurface`, e adiciona mensagens novas. Não mande um exemplo da v1.0 pra um renderer v0.9.1.

A mudança mais interessante aconteceu antes, entre a v0.8 e a v0.9. Olha o que diz a especificação v0.9.1[^spec]:

> Enquanto a v0.8 foi otimizada para LLMs que suportam structured output, a v0.9 foi projetada para ser incorporada diretamente ao prompt de um modelo. Em seguida, pede-se ao LLM que produza um JSON que corresponda aos exemplos e às descrições de schema fornecidos.

E a parte honesta logo em seguida:

> A principal desvantagem dessa abordagem é que ela exige uma validação pós-geração mais complexa, já que o LLM não é estritamente restringido pelo schema.

Em outras palavras, o A2UI espera que o modelo erre de vez em quando. O ciclo pretendido é *prompt, gerar, validar*, e quando a validação falha, você manda o erro de volta pro modelo pra ele tentar de novo.

## Wire format vs. inference format

Aqui vem a parte que eu achei mais interessante. A documentação do A2UI separa duas coisas:

- O *wire format* é o JSON que o renderer consome, que acabamos de ver.
- O *inference format* é o que o modelo realmente escreve, e que um compilador depois transforma em mensagens do wire.

Por que você ia querer que eles fossem diferentes? A [página de inference formats](https://a2ui.org/concepts/inference-formats/) lista os custos de fazer o modelo escrever o JSON do wire direto[^formats]:

> Chaves repetidas (`"id"`, `"component"`, `"@path"`) e identificadores de componentes escritos à mão tornam o JSON bruto do wire aproximadamente três vezes mais longo do que chamadas de construtor posicionais.

E:

> Chaves profundamente aninhadas, aspas e IDs de componentes manuais aumentam os erros de sintaxe e de referência, especialmente em modelos menores e mais rápidos.

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

> Modelos locais pequenos, como o Gemma 4 E2B e o E4B, operam com janelas de contexto e orçamentos de raciocínio limitados.

Os compiladores do Express vivem nos SDKs de *agente* do A2UI, que existem pra TypeScript, Python e Dart[^sdks]. Eles conseguem gerar v0.9, v0.9.1 ou v1.0, então a saída deles funciona com um renderer v0.9.1 como o do AndroidX, mesmo que todos os exemplos da proposta do Express sejam v1.0[^express].

Mas aí a gente tem um problema. Quando o modelo roda *no celular*, o compilador também precisa rodar no celular, e nenhum desses SDKs é pra Kotlin. O AndroidX entrega só o renderer. O repositório do A2UI tem sim um compilador Express experimental no SDK de agente JVM antigo, mas é uma biblioteca de servidor construída em cima de ANTLR, Jackson e do [Agent Development Kit](https://github.com/google/adk-python) do Google, e ela não é publicada no Maven. Então, hoje, não existe um compilador Express que você possa colocar dentro de um app Android, e talvez a gente precise escrever o nosso pra isso acontecer. Mas como?

## Pra fechar

Neste artigo, você aprendeu como o A2UI descreve a UI como dados: uma lista plana de componentes de um catálogo que o seu app já conhece, um data model separado, e actions que decidem o que volta pro agente. O modelo nunca manda código, e o seu app continua no controle de como tudo aparece e se comporta.

A maior parte desse formato existe pra ajudar o renderer. A lista plana deixa a UI aparecer enquanto o stream ainda está chegando, deixa o agente atualizar um único componente e deixa a validação simples. O data model local guarda o que o usuário digita no próprio aparelho até um botão pedir.

É claro que essa facilidade tem um custo, e quem paga é o modelo. O formato que o renderer quer ler não é o formato que um modelo escreve melhor. O time do A2UI sabe disso: a v0.9 trocou o structured output por prompt mais validação, e o Express existe pra que o modelo escreva algo mais curto enquanto um compilador faz o trabalho de achatar.

Então, da próxima vez que você olhar um payload do A2UI, lembra: ele foi feito pra ser lido por um renderer, não escrito por um modelo. Com um modelo grande na nuvem, isso é um detalhe. Com um modelo pequeno num celular, pode ser o problema todo.

## E agora?

Na parte dois (em breve), vamos rodar dois modelos num Pixel 7 Pro, ver o menor deles sofrer com o JSON do wire, escrever um compilador Express minúsculo em Kotlin e medir o quanto ele realmente ajuda.

Valeu demais por ler, e até a próxima!

## Fontes

[^spec]: Especificação do protocolo A2UI v0.9.1, https://a2ui.org/specification/v0.9.1-a2ui/

[^evolution]: Guia de evolução do A2UI v1.0, https://a2ui.org/specification/v1.0-evolution-guide/

[^formats]: Inference formats do A2UI, https://a2ui.org/concepts/inference-formats/

[^express]: Proposta do A2UI Express, https://github.com/a2ui-project/a2ui/blob/main/specification/proposals/express/a2ui_express.md

[^sdks]: SDKs do A2UI, https://a2ui.org/concepts/sdks/
