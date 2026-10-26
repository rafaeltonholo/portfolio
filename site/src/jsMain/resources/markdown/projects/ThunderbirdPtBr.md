---
layout: .components.layouts.ProjectMarkdownLayout
routeOverride: /projects/pt-br/thunderbird
slug: thunderbird
locale: pt-BR
title: Thunderbird para Android - Máquinas de estado em Compose, onboarding do Thundermail e arquitetura com foco em privacidade
description: "Modernizando um cliente de e-mail open source de 15 anos: integrando views legadas do Android a máquinas de estado em Jetpack Compose, entregando o onboarding desktop-para-mobile do Thundermail e liderando padrões de RFC e privacidade na Mozilla."
---

{{{ .components.organisms.projects.ProjectSection ("/ contexto", "Trazendo os padrões modernos da Mozilla para um cliente open source lendário.")

O Thunderbird para Android representa a nova geração de e-mail móvel open source, construído sobre a sólida fundação do K-9 Mail em conjunto com a Mozilla. Com mais de 15 anos de desenvolvimento contínuo e milhões de usuários ativos, o aplicativo apresentava um desafio de engenharia singular: **modernizar uma base de código legada de grande escala sem abrir mão da confiabilidade essencial do e-mail**.

À medida que o projeto se preparava para o lançamento oficial sob a chancela da Mozilla, demandas cruciais se destacaram:

**1. Modernização Arquitetural:** A lista e o leitor de mensagens estavam fortemente acoplados às Android Views tradicionais e a estados mutáveis complexos. A equipe necessitava de uma estratégia segura e incremental para migrar esses fluxos centrais para **Jetpack Compose** e **Unidirectional Data Flow (UDF)**, sem arriscar regressões com múltiplos provedores IMAP, POP3 e Exchange.

**2. Onboarding Integrado ao Ecossistema:** O lançamento do novo serviço de e-mail hospedado da Mozilla, o **Thundermail**, exigia uma experiência fluida de integração e migração de contas sem atrito a partir do Thunderbird Desktop via leitura de QR code.

**3. Liderança em Padrões e Privacidade:** Sendo um projeto da Mozilla, a privacidade do usuário e a conformidade com especificações técnicas são inegociáveis. O app precisava de proteção reforçada contra rastreamento de teclado e RFCs formais para padronizar feature flags, entrega de notificações e tratamento do padrão RFC MIME em e-mails.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ views legadas para compose", "Integrando Android Views a uma máquina de estados em Compose.")

O coração de qualquer cliente de e-mail reside na listagem e na leitura de mensagens. No K-9 Mail, `MessageListFragment` e `MessageContainerView` eram profundamente entrelaçados com Views legadas do Android, cursores diretos de banco de dados e eventos fragmentados. Uma reescrita total representaria alto risco e paralisaria a entrega de novas funcionalidades.

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

{{{ .components.organisms.projects.ProjectSection ("/ privacidade & padrões", "Privacidade por design e liderança técnica em RFCs.")

Em consonância com o compromisso central da Mozilla com a privacidade digital, implementei o **Modo Teclado Anônimo** em todo o aplicativo.

Teclados virtuais frequentemente capturam telemetria de digitação para treinar modelos preditivos. Ao impor o sinalizador `EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING` em campos de busca, composição e credenciais, o app impede que teclados de terceiros registrem conversas confidenciais, rascunhos ou senhas digitadas.

Além do desenvolvimento de features, redigi **RFCs Arquiteturais** de impacto em todo o projeto:

- **RFC #11223 (Arquitetura Declarativa de Feature Flags):** Propus e implementei um catálogo tipado em tempo de compilação para feature flags no projeto multi-módulo, substituindo definições dispersas por um modelo escalável.
- **RFC #11180 (Imagens Inline em Mensagens Plain-Text):** Resolvi uma inconsistência histórica de interoperabilidade em que imagens embutidas em e-mails em texto puro causavam anomalias visuais em diferentes clientes, adequando o app estritamente aos padrões RFC MIME sem quebrar o suporte a identificadores `cid:`.
- **Especificação de Confiabilidade de Notificações:** Conduzi pesquisas aprofundadas sobre falhas de sincronização sob o modo Doze do Android e restrições de economia de bateria de fabricantes para planejar uma arquitetura resiliente de push.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ resultados", "Resultados e impacto.")

{{{ .components.organisms.projects.ProjectOutcomes ("Compose", "lista e leitor de mensagens integrados", "QR sync", "onboarding desktop para mobile", "privado", "modo teclado anônimo sem rastreio") }}}

A arquitetura de ponte para o Compose provou que é possível modernizar uma base de código de 15 anos de forma incremental **sem regressões ou interrupção no fluxo de entregas**. A lista de mensagens agora desfruta do gerenciamento reativo do Compose mantendo total compatibilidade com os loaders de dados legados.

A integração do Thundermail e a migração por QR code reduziram sensivelmente a barreira de entrada, criando uma ponte fluida entre o desktop e o celular.

Ao mesmo tempo, as melhorias de privacidade e as RFCs arquiteturais consolidaram a posição do Thunderbird como um cliente de e-mail seguro, moderno e estritamente aderente aos padrões da web aberta.

}}}
