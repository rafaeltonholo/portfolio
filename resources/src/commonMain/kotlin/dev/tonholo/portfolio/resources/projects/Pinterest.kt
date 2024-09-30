package dev.tonholo.portfolio.resources.projects

import com.varabyte.kobweb.compose.css.FontWeight
import dev.tonholo.portfolio.core.ui.text.LinkAnnotation
import dev.tonholo.portfolio.core.ui.text.SpanStyle
import dev.tonholo.portfolio.core.ui.text.TextLinkStyles
import dev.tonholo.portfolio.core.ui.text.TitleStyle
import dev.tonholo.portfolio.core.ui.text.annotatedString
import dev.tonholo.portfolio.core.ui.text.bold
import dev.tonholo.portfolio.core.ui.text.paragraph
import dev.tonholo.portfolio.core.ui.text.subtitle
import dev.tonholo.portfolio.core.ui.text.title
import dev.tonholo.portfolio.core.ui.text.withLink
import dev.tonholo.portfolio.resources.Project
import dev.tonholo.portfolio.resources.ProjectHeadline
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus

internal val Pinterest = Project(
    id = "pinterest",
    title = "Pinterest",
    client = "Pinterest",
    summary = annotatedString {
        paragraph {
            bold(text = "Pinterest ")
            append(
                value = """
                    |is a leading visual discovery engine, inspiring millions with 
                    |ideas ranging from recipes to home and style inspiration.
                    """.trimMargin(),
            )
        }
        paragraph {
            append(
                value = """
                    |As the application grew over nine years, it faced significant 
                    |scalability challenges, particularly with build times reaching up to 
                    """.trimMargin(),
            )
            bold(text = "30 minutes for a clean build and 15 minutes for incremental builds ")
            append("on high-end machines.")
        }
        paragraph {
            append(
                value = """
                    |Additionally, the need to implement on-demand 
                    |installable features required leveraging the 
                    """.trimMargin(),
            )
            bold(text = "Google Play Dynamic Feature Delivery ")
            append(" system.")
        }
    },
    src = "/project/pinterest",
    playStoreSrc = "https://play.google.com/store/apps/details?id=com.pinterest",
    role = "Senior Android Platform Engineer, I led the modularization initiative " +
        "with a focus on improving build efficiency and enabling dynamic feature delivery.",
    timeline = LocalDate(2022, Month.MARCH, 1) -
        LocalDate(2021, Month.JULY, 1),
    stack = listOf(
        "Java", "Kotlin", "Play Feature Delivery",
        "Dagger2", "Model-View-Presenter", "XML View System",
        "Compose for Android"
    ),
    description = annotatedString {
        title(
            text = "Modularizing Pinterest's codebase",
            level = TitleStyle.Level.H4,
        )
        title(text = "The problem", level = TitleStyle.Level.H5)
        paragraph {
            append(
                value = """The primary issue we faced was the extensive build times. On high-end machines 
                    |equipped with i9 processors and 64GB of memory, a clean build of the app could take 
                    |up to """
                    .trimMargin(),
            )
            bold(text = "30 minutes")
            append(value = ", and even incremental builds were around ")
            bold(text = "15 minutes. ")
            append(
                value = """These prolonged build times impacted the developer's productivity and 
                    |slowed down the release cycle. Additionally, there was a need to implement 
                    |features that could be installed on demand, necessitating the integration of """
                    .trimMargin(),
            )
            withLink(
                link = LinkAnnotation.Url(
                    url = "https://developer.android.com/guide/playcore/feature-delivery",
                    styles = TextLinkStyles(
                        SpanStyle(fontWeight = FontWeight.Bold),
                    ),
                ),
            ) {
                append("Google’s Play Feature Delivery ")
            }
            append("system, which added another layer of complexity to an already cumbersome codebase.")
        }
        title(text = "Challenges", level = TitleStyle.Level.H5)
        paragraph(
            text = """Modularizing such a large and aging codebase without disrupting ongoing feature 
                |development was a significant challenge. The app’s tightly coupled components made 
                |decoupling and refactoring difficult. We also had to ensure that the transition 
                |didn’t introduce new errors or affect the stability of the app. Moreover, integrating 
                |dynamic feature modules led to application crashes when required features weren’t 
                |available at runtime, mainly due to the use of reflection in dependency management."""
                .trimMargin()
        )
        title(text = "Actions and Implementation", level = TitleStyle.Level.H5)
        paragraph(
            text = """We began by fostering open communication with all stakeholders, especially the 
                |product engineers, to ensure everyone was aligned and aware of the changes. 
                |This collaboration was crucial to minimize disruptions to ongoing development work."""
                .trimMargin(),
        )
        paragraph {
            append(value = "Adopting an ")
            bold(text = "incremental approach ")
            append(
                value = """we started by identifying and extracting classes and resources with the 
                    |fewest dependencies into separate modules. This strategy allowed us to modularize 
                    |parts of the app without causing significant ripple effects throughout the codebase. 
                    |As we progressed, we tackled more complex components, carefully refactoring code to 
                    |decouple tightly coupled modules."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(
                value = """To address the application crashes caused by dynamic feature modules, 
                    |we developed a """
                    .trimMargin(),
            )
            bold(text = "centralized dynamic feature dependency manager. ")
            append(
                value = """This manager allowed features requiring dynamic modules to request 
                    |them without using reflection. By leveraging Kotlin’s type safety using 
                    |interfaces, we eliminated many runtime errors related to not yet ready to 
                    |use dynamic features or typos. This solution not only improved the 
                    |application’s reliability but also simplified the integration process for 
                    |developers working on features that requires the usage of other dynamic 
                    |features."""
                    .trimMargin(),
            )
        }
        paragraph(
            text = """We also focused on optimizing dependencies by identifying and removing 
                |unnecessary ones and cleaning up dead code. This effort reduced module 
                |interdependencies and prevented unnecessary builds of modules that certain 
                |features shouldn’t rely on."""
                .trimMargin(),
        )

        title(text = "Outcomes and Impact", level = TitleStyle.Level.H5)
        subtitle(text = "30% reduction in build times")
        paragraph(tag = "container-outcome") {
            append(value = "The modularization efforts led to a ")
            bold(text = "30% reduction in build times")
            append(
                value = """, significantly enhancing developer efficiency and productivity. 
                    |Developers could now work more independently within their respective 
                    |modules, reducing the overhead of dealing with the entire application 
                    |during development."""
                    .trimMargin(),
            )
        }
        paragraph(tag = "container-outcome") {
            subtitle(text = "Centralized dynamic feature dependency manager")
            append(
                value = """The centralized dynamic feature dependency manager improved the app’s 
                    |stability by ensuring that dynamic features were correctly loaded at runtime. 
                    |This enhancement reduced crashes and provided a smoother user experience when 
                    |features were installed on demand."""
                    .trimMargin()
            )
        }
        paragraph(tag = "container-outcome") {
            subtitle(text = "Improved the app’s scalability and maintainability")
            append(value = "The new modular architecture also ")
            bold(text = "improved the app’s scalability and maintainability. ")
            append(
                value = """Additionally, it helped the Pinterest app for future development, 
                    |making it easier to integrate additional features and adapt to new 
                    |requirements."""
                    .trimMargin(),
            )
        }
        title(text = "Key Learnings", level = TitleStyle.Level.H5)
        paragraph {
            append(value = "This project demonstrated the importance of ")
            bold(text = "strategic planning and effective communication ")
            append(
                value = """in large-scale development initiatives. By keeping all team members 
                    |informed and involved, we were able to navigate the complexities of 
                    |modularizing a live and evolving codebase."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(value = "It also highlighted the value of an ")
            bold(text = "incremental approach ")
            append(
                value = """to tackling complex problems. Breaking down the modularization 
                    |process into manageable chunks made the task less overwhelming and 
                    |allowed for continuous progress without significant disruptions."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(value = "From a technical perspective, the experience deepened my expertise in ")
            bold(
                text = "Kotlin and Java, modular architectures, dynamic feature modules, " +
                    "and dependency injection with Dagger. ",
            )
            append(
                value = """It reinforced the necessity of innovative problem-solving and the 
                    |willingness to rethink established methods when they no longer serve the 
                    |project’s needs."""
                    .trimMargin(),
            )
        }
    },
    customHeadline = ProjectHeadline(
        headline = "Pinterest",
    )
)

internal val PinterestPtBr = Pinterest.copy(
    summary = annotatedString {
        paragraph {
            append("O ")
            bold(text = "Pinterest ")
            append(
                value = """
                    |é um aplicativo líder de descoberta visual, inspirando milhões de 
                    |pessoas com ideias que vão desde receitas até inspiração para casa e estilo. 
                    """.trimMargin(),
            )
        }
        paragraph {
            append(
                value = """
                    |Como seu aplicativo cresceu por mais de 9 anos, ele enfrentou problemas
                    |significantes em termos de escalabilidade, particularmente com tempos de
                    |build próximos de  
                    """.trimMargin(),
            )
            bold(text = "30 minutos para um clean build e 15 minutos para builds incrementais ")
            append("em máquinas de última on high-end machines.")
        }
        paragraph {
            append(
                value = """
                    |Também era necessário a implementação de features on-demand, 
                    |que fossem instaláveis quando necessário, requerendo o uso do 
                    """.trimMargin(),
            )
            bold(text = "Google Play Dynamic Feature Delivery.")
        }
    },
    role = """Sênior Android Platform Engineer, liderei a modularização com foco 
        |na melhoria da eficiência da build e na viabilização do fornecimento 
        |on-demand de features."""
        .trimMargin(),
    description = annotatedString {
        title(
            text = "Modularizando o codebase do Pinterest",
            level = TitleStyle.Level.H4,
        )
        title(text = "O problema", level = TitleStyle.Level.H5)
        paragraph {
            append(
                value = """O principal problema que enfrentamos era o longo tempo de compilação. 
                    |Mesmo em máquinas de ponta com processadores i9 e 64 GB de memória, um clean 
                    |build do aplicativo podia levar cerca de """
                    .trimMargin(),
            )
            bold(text = "30 minutos")
            append(value = ", e mesmo os builds incrementais demoravam algo próximo de ")
            bold(text = "15 minutos. ")
            append(
                value = """Esses tempos de compilação prolongados afetavam a produtividade dos 
                    |desenvolvedores e também reduziam a velocidade do ciclo de release. Além disso,  
                    |havia a necessidade de implementar features que seriam instaladas sob demanda,
                    |o que nos exigia a integração com o sistema de """
                    .trimMargin(),
            )
            withLink(
                link = LinkAnnotation.Url(
                    url = "https://developer.android.com/guide/playcore/feature-delivery",
                    styles = TextLinkStyles(
                        SpanStyle(fontWeight = FontWeight.Bold),
                    ),
                ),
            ) {
                append("Play Feature Delivery da Google")
            }
            append(", que adicionava outra camada de complexidade para uma base de código além de complexa.")
        }
        title(text = "Desafios", level = TitleStyle.Level.H5)
        paragraph(
            text = """Modularizar uma base de código tão grande e antiga sem interromper o desenvolvimento 
                |contínuo de features foi um grande desafio. As várias features com diversas dependencies   
                |acopladas à outras features no aplicativo dificultaram o desacoplamento e a refatoração do 
                |código. Também tivemos que garantir que a transição não introduzisse novos erros nem afetasse 
                |a estabilidade do aplicativo. Além disso, a integração de módulos de features dinâmicas levou 
                |a crashes no aplicativo quando as features requeridas não estavam disponíveis em tempo de 
                |execução, principalmente devido ao uso de reflexão no gerenciamento de dependências."""
                .trimMargin()
        )
        title(text = "Ações e implementações", level = TitleStyle.Level.H5)
        paragraph(
            text = """Começamos promovendo uma comunicação aberta com todas as partes interessadas, 
                |especialmente os Product Engineers, para garantir que todos estivessem alinhados e 
                |cientes das mudanças. Essa colaboração foi crucial para minimizar as interrupções no 
                |trabalho de desenvolvimento em andamento."""
                .trimMargin(),
        )
        paragraph {
            append(value = "Adotando uma ")
            bold(text = "abordagem incremental ")
            append(
                value = """, começamos identificando e extraindo classes e recursos com o mínimo de 
                    |dependências em módulos separados. Essa estratégia nos permitiu modularizar partes 
                    |do aplicativo sem causar efeitos significativos em toda a base de código. À medida 
                    |que avançávamos, lidávamos com componentes mais complexos, refatorando cuidadosamente 
                    |o código para desacoplar módulos com maior número de dependências."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(
                value = """Para resolver as falhas de aplicativos causadas pelo uso de features dinâmicas, 
                    |desenvolvemos um """
                    .trimMargin(),
            )
            bold(text = "gerenciador central de dependências para features dinâmicas. ")
            append(
                value = """Esse gerenciador permitiu que as features que necessitassem de features 
                    |dinâmicas as solicitassem sem usar Reflection. Ao contar com a segurança de tipo do 
                    |Kotlin, usando interfaces, eliminamos muitos erros em tempo de execução relacionados 
                    |ao uso de features ainda não disponíveis ou erros de digitação. Essa solução não 
                    |apenas melhorou a confiabilidade do aplicativo, mas também simplificou o processo de 
                    |integração para os desenvolvedores que trabalham com features que necessitem de features
                    |dinâmicas."""
                    .trimMargin(),
            )
        }
        paragraph(
            text = """Também focamos em otimizar as dependências, identificando e removendo aquelas 
                |desnecessárias e removendo códigos desnecessários. Esse esforço reduziu as interdependências 
                |dos módulos e evitou compilações desnecessárias de módulos dos quais determinadas features 
                |não deveriam dependiam."""
                .trimMargin(),
        )

        title(text = "Resultados e Impacto", level = TitleStyle.Level.H5)
        paragraph(tag = "container-outcome") {
            subtitle(text = "Até 30% de redução em tempo de compilação")
            append(value = "Os esforços da modularização levaram à cerca de ")
            bold(text = "30% redução no tempo de compilação")
            append(
                value = """, melhorando significativamente a eficiência de desenvolvimento 
                    |e produtividade. Os desenvolvedores agora podem trabalhar de forma mais 
                    |independente dentro de suas respectivas features, reduzindo a sobrecarga 
                    |de lidar com a aplicação inteira durante o desenvolvimento."""
                    .trimMargin(),
            )
        }
        paragraph(tag = "container-outcome") {
            subtitle(text = "Gerenciador central de dependências para features dinâmicas")
            append(
                value = """O gerenciador central de dependências para features dinâmicas melhorou a 
                    |estabilidade do aplicativo, garantindo que as features dinâmicos fossem 
                    |carregadas corretamente em tempo de execução. Esse aprimoramento reduziu os 
                    |crashes do aplicativo e proporcionou uma experiência de usuário mais suave quando 
                    |estas fossem instaladas sob demanda."""
                    .trimMargin(),
            )
        }
        paragraph(tag = "container-outcome") {
            subtitle(text = "Melhoria em escalabilidade e manutenção do aplicativo")
            append(value = "A nova arquitetura modular também ")
            bold(text = "melhorou a estabilidade do applicativo e sua manutenção. ")
            append(
                value = """Isto também ajudou o aplicativo do Pinterest para desenvolvimento 
                    |futuro de novas features, sendo mais fácil de integrar novas features e 
                    |se adaptar a novos requisitos."""
                    .trimMargin(),
            )
        }
        title(text = "Principais aprendizados", level = TitleStyle.Level.H5)
        paragraph {
            append(value = "Este projeto demonstrou o quão importante é ")
            bold(text = "um planejamento estrategico e uma comunicação efetiva ")
            append(
                value = """no desenvolvimento de um aplicativo em larga escala. Ao manter 
                    |todos os membros da equipe informados e envolvidos, conseguimos 
                    |lidar com a complexidade da modularização em uma base de código viva e 
                    |em constante desenvolvimento."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(value = "Também destaca o valor de uma ")
            bold(text = "abordagem incremental ")
            append(
                value = """para lidar com problemas complexos. Ao dividir o processo de 
                    |modularização em menores partes gerenciáveis torna-se a tarefa menos  
                    |complexa e nos permite um progresso contínuo sem interrupções significativas."""
                    .trimMargin(),
            )
        }
        paragraph {
            append(
                value = """Do ponto de vista técnico, esta experiência me ajudou a 
                    |aprofundar meus conhecimentos em """
                    .trimMargin(),
            )
            bold(
                text = "Kotlin e Java, arquiteturas modulares, módulos de features dinâmicas, " +
                    "e injeção de dependências usando Dagger. ",
            )
            append(
                value = """Ainda reforçou a necessidade de inovar em soluções de problemas e 
                    |estar sempre aberto a repensar os métodos estabelecidos que não atendem 
                    |mais às necessidades do projeto."""
                    .trimMargin(),
            )
        }
    }
)
