---
layout: .components.layouts.ProjectMarkdownLayout
routeOverride: /projects/pt-br/thunderbird
slug: thunderbird
locale: pt-BR
coverImage: /images/projects/thunderbird/hero.png
title: Thunderbird para Android - Máquinas de estado em Compose, onboarding do Thundermail e arquitetura com foco em privacidade
description: "Modernizando um cliente de e-mail open source de 15 anos: integrando views legadas do Android a máquinas de estado em Jetpack Compose, entregando o onboarding desktop-para-mobile do Thundermail e garantindo privacidade com compiler plugin em Kotlin K2 e RFCs na Mozilla."
---

{{{ .components.organisms.projects.ProjectSection ("/ contexto", "Trazendo os padrões modernos da Mozilla para um cliente open source lendário.")

O Thunderbird para Android convive lado a lado com o K-9 Mail, compartilhando a mesma base de código multi-módulo sob diferentes sabores de aplicativo (flavors) na Mozilla. Construído sobre uma base consolidada com mais de 15 anos de desenvolvimento contínuo e milhões de usuários ativos, evoluir a plataforma apresenta um desafio de engenharia singular: **modernizar uma base de código legada de grande escala em produção, preservando a confiabilidade essencial do e-mail em ambos os apps**.

Diversas necessidades arquiteturais e de produto se destacaram:

**1. Modernização Arquitetural:** A lista e o leitor de mensagens estavam fortemente acoplados às Android Views tradicionais e a estados mutáveis complexos. A equipe necessitava de uma estratégia segura e incremental para migrar esses fluxos centrais para **Jetpack Compose** e **Unidirectional Data Flow (UDF)**, sem arriscar regressões com múltiplos provedores IMAP, POP3 e Exchange.

**2. Onboarding Integrado ao Ecossistema:** O lançamento do novo serviço de e-mail hospedado da Mozilla, o **Thundermail**, exigia uma experiência fluida de integração e migração de contas sem atrito a partir do Thunderbird Desktop via leitura de QR code.

**3. Liderança em Padrões e Privacidade:** Sendo um projeto da Mozilla, a privacidade do usuário e a conformidade com especificações técnicas são inegociáveis. O app precisava de proteção reforçada contra rastreamento de teclado e RFCs formais para padronizar feature flags, entrega de notificações e tratamento do padrão RFC MIME em e-mails.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ views legadas para compose", "Integrando Android Views a uma máquina de estados em Compose.")

O coração de qualquer cliente de e-mail reside na listagem e na leitura de mensagens. Em ambos os aplicativos, `MessageListFragment` e `MessageContainerView` eram profundamente entrelaçados com Views legadas do Android, cursores diretos de banco de dados e eventos fragmentados. Uma reescrita total representaria alto risco e paralisaria a entrega de novas funcionalidades.

Para superar esse cenário, projetei e implementei uma **ponte bidirecional conectando os mecanismos de carregamento legados a uma moderna máquina de estados em Jetpack Compose**.

Encapsulamos as ações da lista de mensagens — como rastreamento de mensagem ativa e com foco, modos de seleção (`selectedMessages`, selecionar tudo e desselecionar tudo) e efeitos colaterais de rolagem suave — em uma máquina de estados unidirecional e reativa. Isso permitiu que os componentes em Compose comandassem a interface do usuário em perfeita sincronia com os serviços em segundo plano e os observadores do banco de dados.

Adicionalmente, implementei o **suporte a Pastas Unificadas** (Unified Folders) dentro dessa nova arquitetura. A máquina de estados resolve dinamicamente os selos de indicação de conta e critérios de ordenação por pasta, assegurando comportamento coeso tanto na exibição de uma pasta única quanto em uma caixa de entrada unificada com múltiplas contas.

No leitor de mensagens, converti estruturas de dados legadas em Java (`AttachmentViewInfo.java` para Kotlin) e introduzi mapeadores `AttachmentUiItem`. Essa iniciativa permitiu integrar **componentes de anexos renderizados em Compose diretamente na `MessageContainerView` legada**, estabelecendo uma transição gradual e segura para UI declarativa.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ thundermail & sincronização", "Onboarding desktop-para-mobile e migração via QR code.")

Para viabilizar o lançamento estratégico do **Thundermail**, liderei a concepção e implementação do fluxo de onboarding no cliente mobile.

Fui o autor da RFC formal para a **tela intermediária de adição de conta do Thundermail**, estabelecendo o fluxo de experiência do usuário que direciona novos e antigos usuários entre configurações IMAP tradicionais e a autenticação no Thundermail. Com o intuito de preservar a identidade visual da marca, desenvolvi componentes customizados do design system, incluindo o `ThundermailButtonPanel` e tipografia auto-ajustável (`UsingBrandTypography`, `TextDisplayMediumAutoResize`) para diferentes densidades de tela.

Um dos maiores pontos de fricção na adoção de clientes de e-mail mobile é a configuração manual de servidores, portas e certificados. Para solucionar esse problema, implementei a **migração multiplataforma via QR code** utilizando `QrCodeScannerActivityContract` e `K9MigrationManager`. Os usuários podem exportar suas contas no Thunderbird Desktop e escanear o QR code diretamente no Android, configurando tudo instantaneamente.

Também tornei o pipeline de autenticação OAuth mais resiliente, tratando casos de encerramento prematuro de navegadores e falhas de redirecionamento quando não há navegador padrão disponível.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ privacidade & padrões", "Privacidade por design com compiler plugins em Kotlin e liderança técnica em RFCs.")

Em consonância com o compromisso central da Mozilla com a privacidade digital, projetei e implementei um **Compiler Plugin em Kotlin K2 (`pii-safe`)** customizado para eliminar vazamentos de Informações de Identificação Pessoal (PII) em tempo de compilação (`#11532`, `#11533`, `#11534`).

Em um cliente de e-mail multi-módulo de grande escala, registrar instâncias de modelos de dados (como credenciais de conta, endereços de e-mail e metadados de pastas) pode acidentalmente expor dados sensíveis do usuário em saídas de depuração ou logs de erros. A sanitização manual de strings é propensa a falhas, ruidosa e difícil de manter em escala.

Para solucionar essa questão de forma sistêmica, desenvolvi uma arquitetura de proteção em nível de compilador:

- **Subplugin Gradle & Anotações:** Desenvolvi as anotações `@LoggingPii.HasPii`, `@LoggingPii.Mask` e `@LoggingPii.Hide`, suportadas por um subplugin de integração Gradle dedicado (`PiiSafeGradleSubplugin`).
- **Validadores em Frontend IR (FIR):** Construí checkers de validação no novo compilador K2 (`HasPiiPropagationChecker`, `HasPiiRequiresAnnotatedPropertyChecker`) que apontam omissões como erros de compilação durante o build, impedindo que modelos com PII desprotegido cheguem a compilar.
- **Geração Sintética de `toString()` no Backend IR:** Implementei um gerador em IR (`ToStringOverridePiiSafeBodyGenerator`) que injeta uma implementação sintética de `toString()` protegida contra vazamento nas classes anotadas. Campos confidenciais são automaticamente mascarados ou ocultados sem qualquer uso de reflexão (reflection) ou overhead em runtime.

Além da engenharia de compiladores, liderei revisões técnicas de código e redigi **RFCs Arquiteturais** de impacto em todo o projeto:

- **RFC #11223 (Arquitetura Declarativa de Feature Flags):** Propus e implementei um catálogo tipado em tempo de compilação para feature flags no projeto multi-módulo, substituindo definições dispersas por um modelo escalável.
- **RFC #11180 (Imagens Inline em Mensagens Plain-Text):** Resolvi uma inconsistência histórica de interoperabilidade em que imagens embutidas em e-mails em texto puro causavam anomalias visuais em diferentes clientes, adequando o app estritamente aos padrões RFC MIME sem quebrar o suporte a identificadores `cid:`.
- **Especificação de Confiabilidade de Notificações:** Conduzi pesquisas aprofundadas sobre falhas de sincronização sob o modo Doze do Android e restrições de economia de bateria de fabricantes para planejar uma arquitetura resiliente de push.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ resultados", "Resultados e impacto.")

{{{ .components.organisms.projects.ProjectOutcomes ("Compose", "lista e leitor de mensagens integrados", "QR sync", "onboarding desktop para mobile", "compilador", "plugin K2 para segurança de PII") }}}

A arquitetura de ponte para o Compose provou que é possível modernizar uma base de código de 15 anos de forma incremental **sem regressões ou interrupção no fluxo de entregas**. A lista de mensagens agora desfruta do gerenciamento reativo do Compose mantendo total compatibilidade com os loaders de dados legados.

A integração do Thundermail e a migração por QR code reduziram sensivelmente a barreira de entrada, criando uma ponte fluida entre o desktop e o celular.

Ao mesmo tempo, o compiler plugin K2 de proteção de PII e as RFCs arquiteturais consolidaram a posição do Thunderbird como um cliente de e-mail seguro, moderno e estritamente aderente aos padrões da web aberta.

}}}
