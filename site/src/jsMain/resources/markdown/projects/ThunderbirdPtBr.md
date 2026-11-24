---
layout: .components.layouts.ProjectMarkdownLayout
routeOverride: /projects/pt-br/thunderbird
slug: thunderbird
locale: pt-BR
coverImage: /images/projects/thunderbird/hero.png
title: Thunderbird para Android - uma lista de mensagens em Compose, login no Thundermail e um compiler plugin de privacidade
description: "O que construí no Thunderbird para Android: uma lista de mensagens em Compose e MVI ligada à tela legada, o login no Thundermail do design até a release, uma nova plataforma de feature flags e o pii-safe, um compiler plugin em Kotlin K2 que mantém dados pessoais fora dos logs."
---

{{{ .components.organisms.projects.ProjectSection ("/ contexto", "Um app de e-mail antigo e querido, que não pode parar de funcionar.")

De abril de 2025 a outubro de 2026 eu trabalhei no Thunderbird para Android, o app de e-mail open
source, como Senior Software Engineer, Mobile. Tudo é feito no aberto, no repositório público
[thunderbird-android](https://github.com/thunderbird/thunderbird-android). A mesma base de código
gera dois apps, o Thunderbird e o K-9 Mail, e o app tem mais de um milhão de usuários ativos por mês.

Ou seja, não dá pra parar tudo pra modernizar. Cada mudança precisa entrar enquanto o app continua
saindo pra beta e release, e tem que funcionar nos dois apps.

Nesse tempo, alguns problemas tomaram a maior parte do meu tempo:

- A lista de mensagens, a tela principal do app, era feita com Android Views legadas, com o estado
  espalhado por todo lado. Isso gerava bugs e inconsistências, e trocar tudo de uma vez não era uma
  opção.
- O Thunderbird estava lançando o Thundermail, o seu próprio serviço de e-mail, e o app Android
  precisava de um jeito de fazer login nele.
- Dados pessoais podiam acabar nos logs, e a única proteção era cada dev lembrar de esconder.
- As feature flags ficavam em seis classes de factory separadas, uma por app e tipo de build, então
  era fácil errar.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ views legadas para compose", "Uma lista de mensagens nova em Compose, ligada na tela antiga.")

A nova lista de mensagens é toda feita em Jetpack Compose e usa MVI. A parte difícil não foi a UI,
foi chegar lá sem reescrever tudo e sem congelar o desenvolvimento de features.

A ideia de usar uma máquina de estados baseada em ViewModel surgiu numa reunião técnica semanal, em
que o time olhou o problema junto. A partir daí, eu desenhei a máquina de estados da lista inteira
num diagrama, com todos os estados e transições, apresentei pro time e depois implementei. A
`MessageListStateMachine` é uma tradução direta desse diagrama.

Pra definição de cada tela ficar curta, criei uma `StateMachine` genérica com uma DSL em Kotlin.
Os side effects ficam em handlers separados, e a máquina de estados guarda um histórico dos estados,
com diffs e logs formatados. Quando algo dá errado, dá pra ler exatamente como a tela chegou ali.

Depois eu liguei a nova lista no `MessageListFragment` legado por meio de uma ponte nos dois
sentidos, pra máquina de estados em Compose poder usar o código de carregamento e as ações de item
que já existiam. Em cima disso vieram seleção, controle da mensagem ativa, rolagem, swipe actions,
ordenação e suporte a pastas unificadas. Foi o maior esforço do meu tempo lá, com uns 69 pull
requests mergeados.

A nova lista está pronta e integrada, mas ainda fica atrás de uma feature flag que vem desligada por
padrão. O trabalho foi pausado algumas vezes quando as prioridades mudaram, primeiro pelo
Thundermail, depois pelo trabalho de feature flags.

Fiz algo parecido no leitor de mensagens. Pra trazer de volta a lista de anexos, escrevi uma nova em
Compose e coloquei dentro da `MessageContainerView` legada, convertendo o `AttachmentViewInfo`
de Java pra Kotlin no caminho. Assim essa parte já fica pronta pra quando o leitor for pro Compose.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ thundermail", "Login no Thundermail, do design até a release.")

Eu construí o onboarding do Thundermail no Android: telas novas de onboarding, um fluxo de "Sign in
with Thundermail" via OAuth e uma entrada pra "Scan QR code". Funciona tanto no Thunderbird quanto no
K-9 Mail, então as partes em comum ficam num módulo compartilhado.

Também fiz algumas peças do design system que ele precisava, como o `ThundermailButtonPanel` e o
`TextDisplayMediumAutoResize`, e tratei os casos em que o login dá errado. Se a pessoa cancela, ou
se não tem navegador pra abrir a página do OAuth, o app agora mostra um erro claro em vez de ficar
travado na tela de redirecionamento.

Depois do lançamento, escrevi a RFC 0002 pra uma tela intermediária de Add Account do Thundermail e
construí essa tela.

O onboarding do Thundermail chegou no beta em 8 de maio de 2026 e na release em 3 de junho de 2026,
e hoje vem ligado por padrão.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ privacidade & padrões", "Privacidade checada pelo compilador, e documento de design antes do código.")

Num app de e-mail, as data classes carregam endereços de e-mail, nomes de pastas e dados de conta.
Logar uma delas sem querer pode vazar dados pessoais, e esconder isso na mão é fácil de esquecer.

Então eu projetei e construí o **pii-safe**, um compiler plugin em Kotlin K2
([#11532](https://github.com/thunderbird/thunderbird-android/pull/11532),
[#11533](https://github.com/thunderbird/thunderbird-android/pull/11533),
[#11534](https://github.com/thunderbird/thunderbird-android/pull/11534)). Funciona assim:

- O dev marca a classe com `@PiiSafe.HasPii`, e as propriedades sensíveis com `@PiiSafe.Mask` ou
  `@PiiSafe.Hide`. Um plugin Gradle (`PiiSafeGradleSubplugin`) aplica o compiler plugin no módulo.
- Checkers de FIR (`HasPiiPropagationChecker`, `HasPiiRequiresAnnotatedPropertyChecker`) quebram
  o build quando a marcação está incompleta.
- Um gerador de IR (`ToStringOverridePiiSafeBodyGenerator`) escreve um `toString()` seguro pras
  classes marcadas, mascarando ou escondendo os campos sensíveis, sem reflection em runtime.

Ele está mergeado e em uso. Quatro módulos aplicam o plugin, e as anotações são usadas no código de
mensagens, pastas e migração de banco de dados.

Eu também gosto de colocar as coisas no papel antes de escrever código. Enquanto estive lá, escrevi
4 RFCs, 1 technical design e 1 architecture decision record:

- [RFC 0002](https://github.com/thunderbird/thunderbird-android/blob/main/docs/engineering/rfcs/0002-thundermail-add-an-account-interim-screen.md), a tela intermediária de Add Account do Thundermail.
- [RFC 0003](https://github.com/thunderbird/thunderbird-android/blob/main/docs/engineering/rfcs/0003-render-inline-images-in-plain-text-messages.md), como renderizar imagens inline em mensagens de texto puro.
- [RFC 0004](https://github.com/thunderbird/thunderbird-android/blob/main/docs/engineering/rfcs/0004-feature-flag-new-architecture.md) e [Technical Design 0002](https://github.com/thunderbird/thunderbird-android/blob/main/docs/engineering/technical-designs/0002-feature-flag-declarative-catalog.md), a nova arquitetura de feature flags. Depois eu entreguei: um
  catálogo declarativo validado no build, chaves type-safe geradas, um plugin Gradle e uma tela de
  debug. Ele substituiu as seis classes de factory, e o código antigo já foi removido.
- [RFC 0006](https://github.com/thunderbird/thunderbird-android/blob/main/docs/engineering/rfcs/0006-remote-feature-flags.md), feature flags remotas, que construí em seguida. Por enquanto elas estão desligadas.
- [ADR 0010](https://github.com/thunderbird/thunderbird-android/blob/main/docs/engineering/adr/0010-adopt-project-wide-repository-pattern.md), um Repository pattern pro projeto todo, que ainda está como Proposed.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ resultados", "Resultados e impacto.")

{{{ .components.organisms.projects.ProjectOutcomes ("jun 2026", "login no Thundermail na release", "4 módulos", "usando o pii-safe", "32-53%", "menos tempo de CI nos pull requests") }}}

O login no Thundermail saiu do design e chegou na versão de release, e hoje dá pra entrar no
Thundermail direto do app no Android.

O pii-safe transformou privacidade em algo que o build checa, em vez de algo que cada dev precisa
lembrar. O catálogo de feature flags faz o mesmo com as flags: os erros agora aparecem no build.

Também separei o workflow de CI dos pull requests em jobs diferentes e liguei o cache do Gradle, o
que baixou o tempo de CI de 44-62 minutos pra 29-31 minutos em execuções lado a lado. Todo mundo que
contribui ganha isso em cada pull request.

A nova lista de mensagens ainda não chegou nas mãos dos usuários, mas a parte difícil está feita.
Ela roda numa máquina de estados que o time consegue ler e debugar, e se liga na tela legada sem
congelar features.

}}}
