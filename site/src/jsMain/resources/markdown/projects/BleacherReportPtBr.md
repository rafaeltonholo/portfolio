---
layout: .components.layouts.ProjectMarkdownLayout
routeOverride: /projects/pt-br/bleacher-report
slug: bleacher-report
locale: pt-BR
title: Bleacher Report - Ads centralizados e adoção do Jetpack Compose
description: "Duas iniciativas na Bleacher Report: um gerenciador Centralizado de Anúncios que eliminou os crashes de inserção de ads e moveu a configuração de slots para o backend, e a introdução do Jetpack Compose para preparar o time para o próximo aplicativo."
---

{{{ .components.organisms.projects.ProjectSection ("/ contexto", "Dois requisitos críticos para uma plataforma esportiva em escala.")

Bleacher Report é uma plataforma líder em conteúdo esportivo e cultural, que serve a milhões de fãs as últimas notícias, placares e interações da comunidade. Com a expansão do app, dois requisítos críticos sugiram:

**1. Optimização da Integração com Ads:** O sistema existente de ads era fragmentado e instável, trazendo crashes e limitando o time de ads a gerenciar seu conteúdo de forma dinâmica. Havia uma urgente necessidade de uma integração mais flexível e estável que impulsionasse a monetização de conteúdo sem prejudicar a experiência do usuário.

**2. Adoção do desenvolvimento moderno de UIs:** Com o desenvolvimento previsto de um novo app usando **Jetpack Compose**, havia uma necessidade emergente de integração do Compose no aplicativo atual. Isto prepararia o time de desenvolvimento para uma transição mais tranquila e permitiria a adoção gradual de práticas de desenvolvimento para UIs modernas.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ integração de ads", "Centralizando um stack de anúncios fragmentado.")

Buscava-se melhorar como os anúncios eram integrados nas funcionalidades de comunidade. À medida que o aplicativo se expandia, tornava-se crucial otimizar a inserção de anúncios para aprimorar a experiência do usuário e maximizar as oportunidades de monetização.

O gerenciamento de anúncios existente enfrentava vários problemas. Havia três diferentes métodos para inserir anúncios no aplicativo, levando a inconsistências e desafios para manutenção. Os anúncios eram inseridos dinamicamente enquanto o usuário rolava a tela, o que ocasionalmente causava falhas devido a exceções de modificação concorrente. Além disso, quaisquer mudanças ou adições de espaços publicitários exigiam a publicação de uma nova versão do aplicativo, limitando a flexibilidade da equipe de anúncios em responder rapidamente às demandas do mercado.

Como Engenheiro Sênior de Android, liderei a iniciativa para reformular o processo de integração de anúncios na plataforma. Desenvolvemos um **gerenciador Centralizado de Anúncios** que consolidou todas as criações e inserções de anúncios em um único sistema. Essa abordagem unificada serviu como uma **fonte única** para as operações de anúncios, simplificando a manutenção e potencialmente reduzindo erros.

Para aumentar a flexibilidade, criamos um Ad scheme que permite ao Gerenciador de Ads **solicitar os anúncios esperados**, para cada tela, **diretamente ao backend**. Essa mudança possibilitou a equipe de anúncios a adicionar ou modificar espaços publicitários dinamicamente, sem a necessidade de atualizações do aplicativo.

Também reestruturamos o processo de inserção de anúncios ao **pré-computar** e inserir os anúncios na lista de conteúdo antes de renderizá-la na tela. Esse método proativo evitou exceções de modificação concorrente, **eliminando os crashes** que os usuários estavam enfrentando.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ adoção do compose", "Promovendo a adoção do Jetpack Compose antes do próximo app.")

Antecipando a mudança em direção ao desenvolvimento moderno de **interfaces de usuário (UI)** no Android, a Bleacher Report planejava adotar o **Jetpack Compose**, o novo kit de ferramentas declarativo do Google, para seu próximo aplicativo. Preparar a equipe de desenvolvimento atual e a base de código para essa transição era essencial para garantir um lançamento tranquilo e eficiente.

O aplicativo atual foi construído usando o tradicional **sistema de Views do Android**. A adoção do Jetpack Compose apresentaria desafios, já que a equipe de desenvolvimento precisaria de experiência prática com a nova ferramenta para contribuir efetivamente com o futuro aplicativo. Além disso, o desenvolvimento contínuo de **novas features** para o aplicativo existente precisava prosseguir sem interrupções.

Tomando a iniciativa, conduzi a integração do **Jetpack Compose** no aplicativo atual. Foi estabelecida a infraestrutura fundamental necessária para suportar o Compose, configurando dependências do projeto e garantindo compatibilidade com os componentes existentes.

Para demonstrar o Compose e encorajar a adoção, desenvolvemos uma nova feature que focava no **redesign comunidade** usando Jetpack Compose. Com essa feature, conseguimos provar que o Compose poderia coexistir com elementos de UI legados dentro do aplicativo.

Também promovi **treinamento e troca de conhecimentos** entre os membros da equipe. Ao orientar outros desenvolvedores e fornecer recursos, ajudei a equipe a construir novas telas, com proficiência, usando Compose, preparando-os para o desenvolvimento do novo aplicativo.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ resultados", "Resultados e impacto.")

{{{ .components.organisms.projects.ProjectOutcomes ("estável", "crashes de inserção de anúncios eliminados", "dinâmico", "slots de ads configuráveis pelo backend", "pronto", "time preparado para o próximo app em Compose") }}}

O novo sistema centralizado de gerenciamento de anúncios melhorou significativamente a **estabilidade e a experiência do usuário do aplicativo**. Os crashes causados pela inserção dinâmica de anúncios foram eliminados, resultando em uma experiência de navegação mais suave para os usuários.

A equipe de ads agora pode **gerenciar o conteúdo publicitário dinamicamente**, adicionando ou alterando espaços publicitários sob demanda **sem a necessidade de novas versões do aplicativo**. Essa flexibilidade permitiu **respostas mais rápidas às oportunidades de mercado** e **o aumento potencial de geração de receita** via ads.

A integração do Jetpack Compose no aplicativo existente funcionou como um motivador para a adoção da nova tecnologia pela equipe. Os desenvolvedores puderam ter **experiência prática**, reduzindo a curva de aprendizado para projetos futuros. O novo recurso enriqueceu a funcionalidade do aplicativo e mostrou os benefícios das **práticas modernas de desenvolvimento de UI**. Quando o desenvolvimento do novo aplicativo começou, a equipe já estava familiarizada com o Compose, permitindo um processo de desenvolvimento mais **eficiente e eficaz**.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ aprendizados", "Principais aprendizados.")

Este projeto destacou a importância de simplificar sistemas complexos por meio da centralização. Ao criar um ponto único de controle para o gerenciamento de anúncios, **reduzimos a complexidade e melhoramos a confiabilidade** do aplicativo. Também ressaltou o valor de **configurações dinâmicas vindas do backend**, que proporcionam **uma maior flexibilidade e agilidade na gestão de conteúdo do aplicativo** sem a sobrecarga de atualizações frequentes.

No lado do Compose, este projeto enfatizou o valor da adoção antecipada de novas tecnologias e do **planejamento estratégico**. Integrar o Jetpack Compose no aplicativo existente minimizou os riscos associados à transição para um novo framework de UI. Também destacou a importância de uma **liderança** em guiar equipes através de mudanças tecnológicas e fomentar um ambiente de **aprendizado contínuo** e **inovação**.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ reflexões", "Reflexões finais.")

Através desses projetos na Bleacher Report, fui capaz de promover melhorias significativas tanto na **infraestrutura técnica** e nas **capacidades técnicas da equipe de desenvolvimento**. Ao aprimorar a integração de anúncios, melhoramos a **estabilidade do aplicativo** e a **flexibilidade de monetização** dos ads. Ao liderar a adoção do Jetpack Compose, pudemos preparar a equipe para futuras mudanças de frameworks, garantindo que a Bleacher Report permaneça na frente do **desenvolvimento moderno de Android**.

Essas experiências reforçaram a importância da **resolução proativa de problemas**, do **planejamento estratégico**, e uma **liderança** eficaz. Também destacaram o valor de um **aprendizado contínuo** e da **adaptabilidade** em um campo de constante evolução que é o **desenvolvimento de aplicativos móveis**.

}}}
