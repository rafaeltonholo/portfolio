---
layout: .components.layouts.ProjectMarkdownLayout
routeOverride: /projects/pt-br/pinterest
slug: pinterest
locale: pt-BR
title: Pinterest - Modularizando o codebase do Pinterest
description: Adotando uma abordagem incremental e um gerenciador central de dependências para features dinâmicas, modularizamos uma base de código com mais de 9 anos de história e milhões de usuários.
---

{{{ .components.organisms.projects.ProjectSection ("/ contexto", "Pinterest - 9 anos de código e isso aparece.")

O Pinterest é um aplicativo líder de descoberta visual, inspirando milhões de pessoas com ideias que vão desde receitas até inspiração para casa e estilo.

Como seu aplicativo cresceu por mais de 9 anos, enfrentou problemas significantes em termos de escalabilidade, particularmente com tempos de build próximos de **30 minutos para um clean build e 15 minutos para builds incrementais** em máquinas de última geração.

Também era necessário implementar features on-demand, que fossem instaláveis quando necessário, requerendo o uso do **Google Play Dynamic Feature Delivery**.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ problema", "Os desafios de uma base de código grande e antiga.")

O principal problema que enfrentamos era o longo tempo de compilação. Mesmo em máquinas de ponta com processadores i9 e 64 GB de memória, um clean build do aplicativo podia levar cerca de 30 minutos, e mesmo os builds incrementais demoravam algo próximo de 15 minutos. Esses tempos prolongados afetavam a produtividade dos desenvolvedores e reduziam a velocidade do ciclo de release. Além disso, havia a necessidade de implementar features que seriam instaladas sob demanda, o que exigia a integração com o Play Feature Delivery da Google, adicionando outra camada de complexidade a uma base de código já complexa.

Modularizar uma base tão grande e antiga sem interromper o desenvolvimento contínuo de features foi um grande desafio. As várias features com dependências acopladas dificultaram o desacoplamento e a refatoração do código. Também tivemos que garantir que a transição não introduzisse novos erros nem afetasse a estabilidade do aplicativo. E a integração de módulos de features dinâmicas levava a crashes em tempo de execução quando as features requeridas não estavam disponíveis, principalmente devido ao uso de reflexão no gerenciamento de dependências.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ ações", "Ações e implementações.")

Começamos promovendo uma comunicação aberta com todas as partes interessadas, especialmente os Product Engineers, para garantir que todos estivessem alinhados e cientes das mudanças. Essa colaboração foi crucial para minimizar as interrupções no trabalho de desenvolvimento em andamento.

Adotando uma **abordagem incremental**, começamos identificando e extraindo classes e recursos com o mínimo de dependências em módulos separados. Essa estratégia nos permitiu modularizar partes do aplicativo sem causar efeitos significativos em toda a base de código. À medida que avançávamos, lidávamos com componentes mais complexos, refatorando cuidadosamente para desacoplar módulos com maior número de dependências.

Para resolver as falhas causadas pelo uso de features dinâmicas, desenvolvemos um **gerenciador central de dependências para features dinâmicas**. Esse gerenciador permitiu que as features solicitassem dependências dinâmicas sem usar Reflection. Ao contar com a segurança de tipo do Kotlin, usando interfaces, eliminamos muitos erros em tempo de execução relacionados ao uso de features ainda não disponíveis ou erros de digitação. Essa solução melhorou a confiabilidade do aplicativo e simplificou o processo de integração para desenvolvedores que trabalham com features que dependem de outras features dinâmicas.

Também focamos em otimizar as dependências, identificando e removendo aquelas desnecessárias e eliminando código morto. Esse esforço reduziu as interdependências dos módulos e evitou compilações desnecessárias de módulos dos quais determinadas features não deveriam depender.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ resultados", "Resultados e impacto.")

{{{ .components.organisms.projects.ProjectOutcomes ("30%", "de redução em tempo de compilação", "gerenciador", "central de dependências para features dinâmicas", "escalável", "melhoria em escalabilidade e manutenção") }}}

Os esforços de modularização levaram a cerca de **30% de redução no tempo de compilação**, melhorando significativamente a eficiência e produtividade. Os desenvolvedores agora trabalham de forma mais independente dentro de suas respectivas features, reduzindo a sobrecarga de lidar com a aplicação inteira durante o desenvolvimento.

O gerenciador central de dependências para features dinâmicas melhorou a estabilidade do aplicativo, garantindo que as features dinâmicas fossem carregadas corretamente em tempo de execução. Esse aprimoramento reduziu os crashes e proporcionou uma experiência de usuário mais suave quando as features eram instaladas sob demanda.

A nova arquitetura modular também **melhorou a escalabilidade e a manutenção do aplicativo**. Isso ajudou o Pinterest no desenvolvimento futuro de novas features, tornando mais fácil integrá-las e se adaptar a novos requisitos.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ aprendizados", "Principais aprendizados.")

Este projeto demonstrou o quão importante é **um planejamento estratégico e uma comunicação efetiva** no desenvolvimento de um aplicativo em larga escala. Ao manter todos os membros da equipe informados e envolvidos, conseguimos lidar com a complexidade da modularização em uma base de código viva e em constante desenvolvimento.

Também destaca o valor de uma **abordagem incremental** para lidar com problemas complexos. Dividir o processo de modularização em partes menores e gerenciáveis torna a tarefa menos complexa e permite progresso contínuo sem interrupções significativas.

Do ponto de vista técnico, esta experiência me ajudou a aprofundar meus conhecimentos em **Kotlin e Java, arquiteturas modulares, módulos de features dinâmicas e injeção de dependências usando Dagger**. Reforçou a necessidade de inovar em soluções de problemas e estar sempre aberto a repensar os métodos estabelecidos que não atendem mais às necessidades do projeto.

}}}
