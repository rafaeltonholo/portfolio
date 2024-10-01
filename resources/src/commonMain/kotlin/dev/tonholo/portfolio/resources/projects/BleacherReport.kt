package dev.tonholo.portfolio.resources.projects

import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.ui.text.TitleStyle
import dev.tonholo.portfolio.core.ui.text.annotatedString
import dev.tonholo.portfolio.core.ui.text.bold
import dev.tonholo.portfolio.core.ui.text.paragraph
import dev.tonholo.portfolio.core.ui.text.subtitle
import dev.tonholo.portfolio.core.ui.text.title
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.Project
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus

internal val BleacherReport = Project(
    id = "bleacher-report",
    title = "Bleacher Report",
    client = "Bleacher Report",
    summary = annotatedString {
        paragraph(
            text = """Bleacher Report, a leading platform for sports and culture 
                |content, serves millions of fans with the latest news, scores, 
                |and community interactions. As the app expanded, two critical 
                |needs emerged:"""
                .trimMargin(),
        )
        paragraph(
            paddingValues = PaddingValues(start = 16.dp),
        ) {
            bold(text = "1. Enhancing Ad Integration: ")
            append(
                value = """The existing ad system was fragmented and unstable, 
                    |leading to crashes and limiting the ad team’s ability to 
                    |manage ad content dynamically. There was an urgent need 
                    |to create a more reliable and flexible ad integration 
                    |system to boost monetization without degrading user experience."""
                    .trimMargin(),
            )
        }
        paragraph(
            paddingValues = PaddingValues(start = 16.dp),
        ) {
            bold(text = "2. Adopting Modern UI Development: ")
            append(value = "With the upcoming development of a new app using ")
            bold(text = "Jetpack Compose")
            append(
                value = """, there was a emergent need to integrate Compose 
                    |into the current app. This would prepare the development team 
                    |for the transition and allow for the gradual adoption of modern 
                    |UI practices."""
                    .trimMargin(),
            )
        }
    },
    src = "/project/bleacher-report",
    playStoreSrc = "https://play.google.com/store/apps/details?id=com.bleacherreport.android.teamstream",
    role = """As a Senior Android Engineer, I orchestrated the revamp of the ad 
        |integration system, significantly improving the app's stability and 
        |monetization flexibility. I also pioneered the adoption of Jetpack Compose, 
        |paving the way for the team's transition to modern UI development."""
        .trimMargin(),
    timeline = LocalDate(2023, Month.AUGUST, 1) -
        LocalDate(2022, Month.MARCH, 1),
    stack = listOf(
        "Java", "Kotlin", "Hilt", "MVVM", "XML View System",
        "Compose for Android", "Google Ads", "ExoPlayer",
        "Room",
    ),
    description = annotatedString {
        title(
            text = "Enhancing Ad Integration",
            level = TitleStyle.Level.H4,
        )
        title(
            text = "Introduction",
            level = TitleStyle.Level.H5,
        )
        paragraph(
            text = """They wanted to improve how advertisements were integrated within 
                |its community features. As the app expanded, it became crucial to 
                |streamline ad placement to enhance user experience and maximize 
                |monetization opportunities."""
                .trimMargin(),
        )
        title(
            text = "The Problem",
            level = TitleStyle.Level.H5,
        )
        paragraph(
            text = """The existing ad system faced several issues. There were three different 
                |methods for injecting ads into the app, leading to inconsistencies and 
                |maintenance challenges. Ads were dynamically inserted during scrolling, 
                |which occasionally caused crashes due to concurrent modification exceptions. 
                |Additionally, any changes or additions to ad slots would require publishing 
                |a new version of the app, limiting the ad team’s flexibility to respond 
                |swiftly to market demands."""
                .trimMargin(),
        )
        title(
            text = "Actions Taken",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(
                value = """As a Senior Android Engineer, I led the initiative to overhaul 
                    |the ad integration process. We developed a """
                    .trimMargin(),
            )
            bold(text = "centralized Ad Manager ")
            append(
                value = """that consolidated all ad creation and injection mechanisms into 
                    |a single system. This unified approach served as the """
                    .trimMargin(),
            )
            bold(text = "single source of truth for ad operations")
            append(value = ", simplifying maintenance and reducing the potential for errors.")
        }
        paragraph {
            append(
                value = "To enhance flexibility, we created an Ad Schema that allowed the Ad Manager to ",
            )
            bold(text = "request the expected ads ")
            append(value = "for each screen directly ")
            bold(text = "from the backend")
            append(
                value = """. This change enabled the ad team to dynamically add or modify 
                    |ad slots without requiring app updates."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(value = "Additionally, we have restructured the ad injection process by ")
            bold(text = "pre-computing ")
            append(
                value = """and inserting ads into the content list before rendering it on the 
                    |screen. This proactive method prevented concurrent modification exceptions, """
                    .trimMargin(),
            )
            bold(text = "eliminating the crashes ")
            append(value = "users were experiencing.")
        }
        title(
            text = "Outcomes and Impact",
            level = TitleStyle.Level.H5,
        )
        paragraph(tag = "container-outcome") {
            subtitle(text = "Improved the app’s stability and user experience")
            append(value = "The new centralized ad management system significantly ")
            bold(text = "improved the app’s stability and user experience")
            append(
                value = """. Crashes caused by ad injection were eliminated, 
                    |resulting in a smoother browsing experience for users."""
                    .trimMargin(),
            )
        }
        paragraph(tag = "container-outcome") {
            subtitle(text = "Managing ad content dynamically, and quickly responding to market")
            append(value = "The ad team gained the ")
            bold(text = "ability to manage ad content dynamically")
            append(", adding or changing ad slots on demand ")
            bold(text = "without the need for new app releases")
            append(value = ". This flexibility allowed for ")
            bold(text = "quicker responses to market opportunities")
            append(value = " and ")
            bold(text = "increased potential for revenue generation")
            append(value = " via ads.")
        }
        title(
            text = "Key Learnings",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(
                value = """This project underscored the importance of simplifying complex systems 
                    |through a single source of truth. By creating a single point of control for 
                    |ad management, """
                    .trimMargin(),
            )
            bold("we reduced complexity and improved reliability")
            append(value = " of the app. It also highlighted the value of ")
            bold(text = "backend-driven configurations")
            append(value = ", which provide ")
            bold(text = "greater flexibility and agility in managing app content")
            append(" without the overhead of frequent app updates.")
        }
        title(
            text = "Championing Jetpack Compose Adoption",
            level = TitleStyle.Level.H4,
        )
        title(
            text = "Introduction",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(value = "Anticipating the shift toward modern ")
            bold(text = "UI development")
            append(value = " in Android, Bleacher Report planned to adopt ")
            bold(text = "Jetpack Compose")
            append(
                value = """—Google’s declarative UI toolkit—for its next-generation app. 
                    |Preparing the existing development team and codebase for this 
                    |transition was essential to ensure a smooth and efficient rollout."""
                    .trimMargin(),
            )
        }
        title(
            text = "The Challenge",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(value = "The current app was built using the traditional ")
            bold(text = "Android View system")
            append(
                value = """. Introducing Jetpack Compose presented challenges, as the  
                    |development team needed practical experience with the new toolkit 
                    |to contribute effectively to the upcoming app. Additionally, 
                    |ongoing """.trimMargin(),
            )
            bold(text = "feature development")
            append(value = " for the existing app had to continue without disruption.")
        }
        title(
            text = "Actions Taken",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(value = "Taking the initiative, I led the integration of ")
            bold(text = "Jetpack Compose")
            append(
                value = """ into the current application. We established the foundational 
                    |infrastructure required to support Compose, configuring project 
                    |dependencies and ensuring compatibility with existing components."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(
                value = """To demonstrate Compose’s capabilities and encourage adoption, 
                    |we developed a new feature for the """
                    .trimMargin(),
            )
            bold(text = "community redesign")
            append(
                value = """ using Jetpack Compose. This feature showcased how Compose 
                    |could coexist with legacy UI elements within the app."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(value = "I also facilitated ")
            bold(text = "training and knowledge sharing")
            append(
                value = """ among team members. Mentoring other developers and providing 
                    |resources helped the team build proficiency with Compose, preparing 
                    |them for the upcoming development of the new app."""
                    .trimMargin(),
            )
        }
        title(
            text = "Outcomes and Impact",
            level = TitleStyle.Level.H5,
        )
        paragraph(tag = "container-outcome") {
            subtitle(text = "Reducing the learning curve for the future application")
            append(
                value = """Integrating Jetpack Compose into the existing app acted as 
                    |a catalyst for the team’s adoption of the new technology. Developers 
                    |gained """.trimMargin(),
            )
            bold(text = "hands-on experience")
            append(
                value = """, reducing the learning curve for future projects. The new 
                    |feature enriched the app’s functionality and showcased the benefits 
                    |of """.trimMargin(),
            )
            bold(text = "modern UI development")
            append(value = " practices.")
        }
        paragraph(tag = "container-outcome") {
            subtitle(text = "Efficiency and effectiveness in the development process")
            append(
                value = """This proactive approach ensured that when development of 
                    |the new app began, the team was already familiar with Compose, 
                    |allowing for a more """
                    .trimMargin(),
            )
            bold(text = "efficient and effective")
            append(value = " development process.")
        }
        title(
            text = "Key Learnings",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(
                value = """This project emphasized the value of early adoption 
                    |of new technologies and """
                    .trimMargin(),
            )
            bold(text = "strategic planning")
            append(
                value = """. Integrating Jetpack Compose into the existing app 
                    |minimized risks associated with transitioning to a new UI 
                    |framework. It also highlighted the importance of """
                    .trimMargin(),
            )
            bold(text = "leadership")
            append(
                value = """ in guiding teams through technological changes and 
                    |fostering an environment of """
                    .trimMargin(),
            )
            bold(text = "continuous learning")
            append(value = " and ")
            bold(text = "innovation")
            append(".")
        }
        title(
            text = "Overall Reflections",
            level = TitleStyle.Level.H4,
        )
        paragraph {
            append(
                value = """Through these projects at Bleacher Report, I was able to 
                    |drive significant improvements in both the """
                    .trimMargin(),
            )
            bold(text = "technical infrastructure")
            append(value = " and the ")
            bold(text = "development team’s capabilities")
            append(value = ". By enhancing ad integration, we improved the  and ")
            bold(text = "app stability")
            append(value = ", and ")
            bold(text = "the ad monetization flexibility")
            append(
                value = """. By pioneering Jetpack Compose adoption, we prepared 
                    |the team for future technological shifts, ensuring that 
                    |Bleacher Report remains at the forefront of """
                    .trimMargin(),
            )
            bold(text = "modern Android development")
            append(value = ".")
        }
        paragraph {
            append(value = "These experiences reinforced the importance of ")
            bold(text = "proactive problem-solving, strategic planning")
            append(value = ", and effective ")
            bold(text = "leadership")
            append(value = ". They also highlighted the value of ")
            bold(text = "continuous learning")
            append(value = " and ")
            bold(text = "adaptability")
            append(value = " in the ever-evolving field of ")
            bold(text = "mobile app development")
            append(value = ".")
        }
    },
)

internal val BleacherReportPtBr = BleacherReport.copy(
    summary = annotatedString {
        paragraph(
            text = """Bleacher Report é uma plataforma líder em conteúdo esportivo 
                |e cultural, que serve a milhões de fãs as últimas notícias, placares, 
                |e interações da comunidade. Com a expansão do app, dois requisítos 
                |críticos sugiram:"""
                .trimMargin(),
        )
        paragraph(
            paddingValues = PaddingValues(start = 16.dp),
        ) {
            bold(text = "1. Optimização da Integração com Ads: ")
            append(
                value = """O sistema existente de ads era fragmentado e instável, 
                    |trazendo crashes e limitando o time de ads a gerenciar seu 
                    |conteúdo de forma dinâmica. Havia uma urgente necessidade de 
                    |uma integração mais flexível e estável que impulsionasse a 
                    |monetização de conteúdo sem prejudicar a experiência do usuário."""
                    .trimMargin(),
            )
        }
        paragraph(
            paddingValues = PaddingValues(start = 16.dp),
        ) {
            bold(text = "2. Adoção do desenvolvimento moderno de UIs: ")
            append(value = "Com o desenvolvimento previsto de um novo app usando ")
            bold(text = "Jetpack Compose")
            append(
                value = """, havia uma necessidade emergente de integração do Compose 
                    |no aplicativo atual. Isto prepararia o time de desenvolvimento para 
                    |uma transição mais tranquila e permitiria a adoção gradual de práticas 
                    |de desenvolvimento para UIs modernas."""
                    .trimMargin(),
            )
        }
    },
    role = """Engenheiro Sênior de Android, liderei a reformulação do sistema de 
        |integração de anúncios para melhorar a estabilidade do aplicativo e a 
        |flexibilidade de monetização e conduzi a adoção do Jetpack Compose para 
        |preparar a equipe para o desenvolvimento de interfaces modernas."""
        .trimMargin(),
    description = annotatedString {
        title(
            text = "Aprimorando a Integração de Anúncios",
            level = TitleStyle.Level.H4,
        )
        title(
            text = "Introdução",
            level = TitleStyle.Level.H5,
        )
        paragraph(
            text = """Buscava-se melhorar como os anúncios eram integrados nas 
                |funcionalidades de comunidade. À medida que o aplicativo se 
                |expandia, tornáva-se crucial otimizar a inserção de anúncios 
                |para aprimorar a experiência do usuário e maximizar as oportunidades 
                |de monetização."""
                .trimMargin(),
        )
        title(
            text = "O Problema",
            level = TitleStyle.Level.H5,
        )
        paragraph(
            text = """O gerenciamento de anúncios existente enfrentava vários problemas. 
                |Havia três diferentes métodos para inserir anúncios no aplicativo, levando 
                |a inconsistências e desafios para manutenção. Os anúncios eram inseridos 
                |dinamicamente enquanto o usuário rolava a tela, o que ocasionalmente causava 
                |falhas devido a exceções de modificação concorrente. Além disso, quaisquer 
                |mudanças ou adições de espaços publicitários exigiam a publicação de uma nova 
                |versão do aplicativo, limitando a flexibilidade da equipe de anúncios em responder 
                |rapidamente às demandas do mercado."""
                .trimMargin(),
        )
        title(
            text = "Ações Realizadas",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(
                value = """Como Engenheiro Sênior de Android, liderei a iniciativa para reformular 
                    |o processo de integração de anúncios na plataforma. Desenvolvemos um """
                    .trimMargin(),
            )
            bold(text = "gerenciador Centralizado de Anúncios")
            append(
                value = """ que consolidou todas as criações e inserções de anúncios em um único 
                    |sistema. Essa abordagem unificada serviu como uma """
                    .trimMargin(),
            )
            bold(text = "fonte única")
            append(
                value = """" para as operações de anúncios, simplificando a manutenção e 
                    |potencialmente reduzindo erros.""".trimMargin()
            )
        }
        paragraph {
            append(
                value = """Para aumentar a flexibilidade, criamos um Ad scheme que permite 
                    |ao Gerenciador de Ads """.trimMargin(),
            )
            bold(text = "solicitar os anúncios esperados")
            append(value = ", para cada tela, ")
            bold(text = "diretamente ao backend")
            append(
                value = """. Essa mudança possibilitou a equipe de anúncios a adicionar ou 
                    |modificar espaços publicitários dinamicamente, sem a necessidade de 
                    |atualizações do aplicativo."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(value = "Também reestruturamos o processo de inserção de anúncios ao ")
            bold(text = "pré-computar")
            append(
                value = """ e inserir os anúncios na lista de conteúdo antes de renderizá-la 
                    |na tela. Esse método proativo evitou exceções de modificação concorrente, 
                    |"""
                    .trimMargin(),
            )
            bold(text = "eliminando os crashes")
            append(value = " que os usuários estavam enfrentando.")
        }
        title(
            text = "Resultados e Impacto",
            level = TitleStyle.Level.H5,
        )
        paragraph(tag = "container-outcome") {
            subtitle(text = "Melhora da estabilidade do aplicativo e a experiência do usuário")
            append(value = "O novo sistema centralizado de gerenciamento de anúncios melhorou significativamente a ")
            bold(text = "estabilidade e a experiência do usuário do aplicativo")
            append(
                value = """. Os crashes causados pela inserção dinâmica de anúncios foram eliminadas, 
                    |resultando em uma experiência de navegação mais suave para os usuários."""
                    .trimMargin(),
            )
        }
        paragraph(tag = "container-outcome") {
            subtitle(text = "Gerenciando o conteúdo do anúncio de forma dinâmica e respondendo rapidamente ao mercado")
            append(value = "A equipe de ads agora pode ")
            bold(text = "gerenciar o conteúdo publicitário dinamicamente")
            append(", adicionando ou alterando espaços publicitários sob demanda ")
            bold(text = "sem a necessidade de novas versões do aplicativo")
            append(value = ". Essa flexibilidade permitiu ")
            bold(text = " respostas mais rápidas às oportunidades de mercado")
            append(value = " e ")
            bold(text = "o aumento potencial de geração de receita")
            append(value = " via ads.")
        }
        title(
            text = "Principais Aprendizados",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(
                value = """Este projeto destacou a importância de simplificar sistemas complexos 
                    |por meio da centralização. Ao criar um ponto único de controle para o 
                    |gerenciamento de anúncios, """
                    .trimMargin(),
            )
            bold("reduzimos a complexidade e melhoramos a confiabilidade")
            append(value = " do aplicativo. Também ressaltou o valor de ")
            bold(text = "configurações dinâmicas vindas do backend")
            append(value = ", que proporcionam ")
            bold(text = "uma maior flexibilidade e agilidade na gestão de conteúdo do aplicativo")
            append(" sem a sobrecarga de atualizações frequentes.")
        }
        title(
            text = "Promovendo a adoção ao Jetpack Compose",
            level = TitleStyle.Level.H4,
        )
        title(
            text = "Introdução",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(value = "Antecipando a mudança em direção ao desenvolvimento moderno de ")
            bold(text = "interfaces de usuário (UI)")
            append(value = " no Android, a Bleacher Report planejava adotar o ")
            bold(text = "Jetpack Compose")
            append(
                value = """, o novo kit de ferramentas declarativo do Google, para seu 
                    |próximo aplicativo. Preparar a equipe de desenvolvimento atual e a 
                    |base de código para essa transição era essencial para garantir um 
                    |lançamento tranquilo e eficiente."""
                    .trimMargin(),
            )
        }
        title(
            text = "O Desafio",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(value = "O aplicativo atual foi construído usando o tradicional ")
            bold(text = "sistema de Views do Android")
            append(
                value = """. A adoção do Jetpack Compose apresentaria desafios, já que a 
                    |equipe de desenvolvimento precisaria de experiência prática com a nova 
                    |ferramenta para contribuir efetivamente com o futuro aplicativo. Além disso, 
                    |o desenvolvimento contínuo de """.trimMargin(),
            )
            bold(text = "novas features")
            append(value = " para o aplicativo existente precisava prosseguir sem interrupções.")
        }
        title(
            text = "Ações Realizadas",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(value = "Tomando a iniciativa, conduzi a integração do ")
            bold(text = "Jetpack Compose")
            append(
                value = """ no aplicativo atual. Foi estabelecido a infraestrutura fundamental 
                    |necessária para suportar o Compose, configurando dependências do projeto e 
                    |garantindo compatibilidade com os componentes existentes."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(
                value = """Para demonstrar o Compose e encorajar a adoção, desenvolvemos uma 
                    |nova feature que focava no """
                    .trimMargin(),
            )
            bold(text = "redesign comunidade")
            append(
                value = """ usando Jetpack Compose. Com essa feature, conseguimos provar que o 
                    |Compose poderia coexistir com elementos de UI legados dentro do aplicativo."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(value = "Também promovi ")
            bold(text = "treinamento e troca de conhecimentos")
            append(
                value = """ entre os membros da equipe. Ao orientar outros desenvolvedores e 
                    |fornecer recursos, ajudei a equipe a construir novas telas, com proficiência, 
                    |usando Compose, preparando-os para o desenvolvimento do novo aplicativo."""
                    .trimMargin(),
            )
        }
        title(
            text = "Resultados e Impacto",
            level = TitleStyle.Level.H5,
        )
        paragraph(tag = "container-outcome") {
            subtitle(text = "Reduzindo a curva de aprendizado para o futuro aplicativo")
            append(
                value = """A integração do Jetpack Compose no aplicativo existente 
                    |funcionou como um motivador para a adoção da nova tecnologia 
                    |pela equipe. Os desenvolvedores puderam ter """.trimMargin(),
            )
            bold(text = "experiência prática")
            append(
                value = """, reduzindo a curva de aprendizado para projetos futuros. 
                    |O novo recurso enriqueceu a funcionalidade do aplicativo e mostrou 
                    |os benefícios das """.trimMargin(),
            )
            bold(text = "práticas modernas de dedesenvolvimento de UI")
            append(value = ".")
        }
        paragraph(tag = "container-outcome") {
            subtitle(text = "Eficiência e eficácia no processo de desenvolvimento")
            append(
                value = """Essa abordagem proativa garantiu que, quando o desenvolvimento do 
                    |novo aplicativo começasse, a equipe já estivesse familiarizada com o 
                    |Compose, permitindo um processo de desenvolvimento mais """
                    .trimMargin(),
            )
            bold(text = "eficiente e eficaz")
            append(value = ".")
        }
        title(
            text = "Principais Aprendizados",
            level = TitleStyle.Level.H5,
        )
        paragraph {
            append(
                value = "Este projeto enfatizou o valor da adoção antecipada de novas tecnologias e do ",
            )
            bold(text = "planejamento estratégico")
            append(
                value = """. Integrar o Jetpack Compose no aplicativo existente minimizou os riscos 
                    |associados à transição para um novo framework de UI. Também destacou a importância 
                    |de uma """
                    .trimMargin(),
            )
            bold(text = "liderança")
            append(
                value = " em guiar equipes através de mudanças tecnológicas e fomentar um ambiente de ",
            )
            bold(text = "aprendizado contínuo")
            append(value = " e ")
            bold(text = "inovação")
            append(".")
        }
        title(
            text = "Reflexões Finais",
            level = TitleStyle.Level.H4,
        )
        paragraph {
            append(
                value = """Através desses projetos na Bleacher Report, fui capaz de promover 
                    |melhorias significativas tanto na """
                    .trimMargin(),
            )
            bold(text = "infraestrutura técnica")
            append(value = " e nas ")
            bold(text = "capacidades técnicas da equipe de desenvolvimento")
            append(value = ". Ao aprimorar a integração de anúncios, melhoramos a ")
            bold(text = "estabilidade do aplicativo")
            append(value = " e a ")
            bold(text = "flexibilidade de monetização")
            append(
                value = """dos ads. Ao liderar a adoção do Jetpack Compose, pudemos preparar 
                    |a equipe para futuras mudanças de frameworks, garantindo que a Bleacher 
                    |Report permaneça na frente do  """
                    .trimMargin(),
            )
            bold(text = "desenvolvimento moderno de Android")
            append(value = ".")
        }
        paragraph {
            append(value = "Essas experiências reforçaram a importância da ")
            bold(text = "resolução proativa de problemas")
            append(value = ", do ")
            bold(text = "planejamento estratégico")
            append(value = ", e uma ")
            bold(text = "liderança")
            append(value = "eficaz. Também destacaram o valor de um ")
            bold(text = "aprendizado contínuo")
            append(value = " e da  ")
            bold(text = "adaptabilidade")
            append(value = " em um campo de constante evolução que é o ")
            bold(text = "desenvolvimento de aplicativos móveis")
            append(value = ".")
        }
    },
)
