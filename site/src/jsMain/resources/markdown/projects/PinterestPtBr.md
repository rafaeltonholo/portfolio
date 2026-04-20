---
layout: .components.layouts.ProjectMarkdownLayout
slug: pinterest-pt-br
title: Pinterest - Modularizando uma base de código de nove anos
description: Modularização incremental e um gerenciador central de dependências para features dinâmicas, entregue sem freeze, em uma base com milhões de usuários e nove anos de história.
---

{{{ .components.organisms.projects.ProjectSection ("/ contexto", "Pinterest são 9 anos de código, e isso aparece.")

Quando entrei, a base de código Android já tinha crescido para milhares de arquivos, dezenas de times e o tipo de tempo de build que fazia desenvolvedores temerem um clean install. Um clean build levava em média 30 minutos em uma máquina de ponta com um i9 e 64GB de memória. Um build incremental, 15.

Esse custo se acumula. Todo engenheiro do time perdia horas reais toda semana e a cadência de entrega sofria. Ao mesmo tempo, o time de produto queria entregar features sob demanda via Google Play Feature Delivery, o que adicionava mais uma camada de complexidade a um grafo já intricado.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ problema", "O grafo de módulos virou o organograma.")

Cada time possuía seus módulos. Cada módulo tinha convenções sutilmente diferentes. Dynamic features não sabiam como encontrar suas dependências sem lookups elaborados baseados em reflection, que crashavam em runtime sempre que uma feature não estava pronta. Novas tarefas não conseguiam aterrissar sem mexer em módulos que não tinham nada a ver com elas, e isso deixava o grafo inteiro frágil.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ abordagem", "Pedaços menores, contratos mais claros.")

Começamos com comunicação aberta. Product engineers, platform engineers e o time de release no mesmo canal, alinhados com o plano para que a modularização nunca bloqueasse o trabalho de features. Essa colaboração foi a coisa mais importante que fizemos.

A primeira tarefa técnica era tediosa de propósito: fazer inventário. Identificamos classes e recursos com o menor número de dependências, extraímos para novos módulos e só depois subimos no grafo em direção aos componentes mais acoplados. Passos pequenos, entrega contínua, sem freeze.

Daí construímos um gerenciador central de dependências para features dinâmicas. Uma pequena biblioteca em runtime que expunha interfaces Kotlin estáveis para cada feature module, cuidava do ciclo de instalação e carregamento e deixava os consumidores dependerem de contratos em vez de reflection. Os times pararam de ser donos da própria lógica de discovery. Os crashes de runtime por features faltantes sumiram.

Também gastamos tempo de verdade deletando coisas. Dependências não usadas, código morto e arestas entre módulos que não deveriam existir. Menos arestas, menos rebuilds desnecessários, menos surpresas.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ resultados", "O que mudou, com números.")

{{{ .components.organisms.projects.ProjectOutcomes ("-30%", "de redução no tempo médio de build", "estável", "features dinâmicas, sem crashes de reflection", "escala", "arquitetura pronta para os próximos 9 anos") }}}

Mais importante que os números: o gerenciador de features dinâmicas deixou de ser a coisa que os engenheiros temiam tocar. Features eram mergeadas no primeiro review. A plataforma voltou a ser uma plataforma.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ aprendizados", "Algumas coisas que levei comigo.")

**Tedioso é bom.** A versão que venceu não foi a esperta. Foi a que fez o mínimo possível, da forma mais óbvia, e não deixou espaço para os times reinventarem. Interfaces com tipo seguro ganham de reflection todas as vezes.

**Incremental é tudo.** Não dava para arrancar o grafo inteiro e reconstruir; muita coisa dependia dele. Cada mudança tinha que entregar, sozinha, sem quebrar as centenas de módulos do lado de fora. Essa restrição moldou o design inteiro.

**Converse com quem vive no código.** Alguns dos feedbacks mais úteis vieram de engenheiros que trabalhavam na base há anos e tinham uma longa lista de incômodos. Eles sabiam onde os corpos estavam enterrados, e a maior parte das nossas melhores decisões saiu dessas conversas.

}}}
