package dev.tonholo.portfolio.resources.projects

import com.varabyte.kobweb.compose.css.FontWeight
import dev.tonholo.portfolio.core.ui.text.LinkAnnotation
import dev.tonholo.portfolio.core.ui.text.SpanStyle
import dev.tonholo.portfolio.core.ui.text.TextLinkStyles
import dev.tonholo.portfolio.core.ui.text.TitleStyle
import dev.tonholo.portfolio.core.ui.text.annotatedString
import dev.tonholo.portfolio.core.ui.text.bold
import dev.tonholo.portfolio.core.ui.text.paragraph
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
            level = TitleStyle.Level.H5,
        )
        title(text = "The problem", level = TitleStyle.Level.H6)
        paragraph {
            append(
                value = """The primary issue we faced was the extensive build times. On high-end machines 
                    |equipped with i9 processors and 64GB of memory, a clean build of the app could take 
                    |up to """
                    .trimMargin(),
            )
            bold(text = "30 minutes")
            append(value = ", and even incremental builds were around ")
            bold(text = "15 minutes.")
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
        title(text = "Challenges", level = TitleStyle.Level.H6)
        paragraph(
            text = """Modularizing such a large and aging codebase without disrupting ongoing feature 
                |development was a significant challenge. The app’s tightly coupled components made 
                |decoupling and refactoring difficult. We also had to ensure that the transition 
                |didn’t introduce new errors or affect the stability of the app. Moreover, integrating 
                |dynamic feature modules led to application crashes when required features weren’t 
                |available at runtime, mainly due to the use of reflection in dependency management."""
                .trimMargin()
        )
        title(text = "Actions and Implementation", level = TitleStyle.Level.H6)
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
                value = """ This manager allowed features requiring dynamic modules to request 
                    |them without using reflection. By leveraging Kotlin’s type safety using 
                    |interfaces, we eliminated many runtime errors related to missing features 
                    |or typos. This solution not only improved the application’s reliability 
                    |but also simplified the integration process for developers working on new 
                    |dynamic features."""
                    .trimMargin(),
            )
        }
        paragraph(
            text = """We also focused on optimizing dependencies by identifying and removing 
                |unnecessary ones and cleaning up dead code. This effort reduced module 
                |interdependencies and prevented unnecessary builds of modules that certain 
                |features didn’t rely on."""
                .trimMargin(),
        )

        title(text = "Outcomes and Impact", level = TitleStyle.Level.H6)
        paragraph {
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
        paragraph(
            text = """The centralized dynamic feature dependency manager improved the app’s 
                |stability by ensuring that dynamic features were correctly loaded at runtime. 
                |This enhancement reduced crashes and provided a smoother user experience when 
                |features were installed on demand."""
                .trimMargin(),
        )
        paragraph {
            append(value = "The new modular architecture also ")
            bold(text = "improved the app’s scalability and maintainability. ")
            append(
                value = """Additionally, it helped the Pinterest app for future development, 
                    |making it easier to integrate additional features and adapt to new 
                    |requirements."""
                    .trimMargin(),
            )
        }
        title(text = "Key Learnings", level = TitleStyle.Level.H6)
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
        paragraph(
            text = """O Pinterest é uma plataforma de descoberta visual para encontrar 
                            ideias como receitas, inspiração para sua casa e estilo, e muito mais.
                            """
                .trimIndent(),
        )
        paragraph(
            text = """Com bilhões de Pins no Pinterest, você sempre encontrará ideias 
                            para despertar a sua inspiração. Quando você descobrir Pins de seu agrado, 
                            salve-os em pastas para manter suas ideias organizadas e fáceis de encontrar. 
                            Você também pode criar Pins para compartilhar suas ideias com outras 
                            pessoas no Pinterest.
                            """.trimIndent(),
        )

    },
    description = annotatedString {
        title(
            text = "Modularizando o codebase do Pinterest",
            level = TitleStyle.Level.H5,
        )
        title(text = "O problema", level = TitleStyle.Level.H6)
        paragraph(
            text = """Enquanto trabalhava com o Pinterest, fui responsável pela modularização 
                            de sua codebase. Devido ao tamanho do Pinterest, a modularização apresenta alguns 
                            desafios. A codebase era imensa, com mais de nove anos, com features em constante 
                            desenvolvimento que não podem ser interrompidos durante esse processo.
                            """
                .trimIndent(),
        )
        paragraph(
            text = """Eles estavam com problemas com o tempo de compilação, que quase chegava a 30 
                            minutos para um clean build, usando um setup com i9 e 64 GB de memória, sendo 
                            extremamente longa, enquanto um incremental build levava cerca de 15 minutos, também 
                            muito longo. Diante desse desafio, eles optaram por modularizar o aplicativo para 
                            melhorar o tempo de build e ao mesmo tempo implementar features que pudessem ser 
                            instaláveis sob demanda, o que exigiu a utilização do Play Delivery Feature.
                            """.trimIndent(),
        )
        title(text = "Ações", level = TitleStyle.Level.H6)
        paragraph(
            text = """Manter uma comunicação aberta com os desenvolvedores das features nos 
                            ajudou a fazer uma transição tranquila de um aplicativo monolítico para um 
                            modularizado.
                            """.trimIndent(),
        )
        paragraph(
            text = """Começamos focando nas classes e nos recursos com o menor número de 
                            dependências antes de abordar os outros arquivos problemáticos, o que 
                            exigiria várias alterações.
                            """.trimIndent(),
        )
        paragraph(
            text = """Enquanto migrávamos o código para o novo feature module criado para 
                            esse fim, também identificamos todas as dependências desnecessárias e códigos 
                            mortos que poderiam ser removidos do recurso, evitando assim acionando builds 
                            desnecessárias para módulos dos quais a feature não deveria depender.
                            """.trimIndent(),
        )
        paragraph(
            text = """Também começamos enfrentar crashes no aplicativo ao usar features dinâmicas 
                            por ela não estar disponível no applicativo durante tempo de execução. 
                            Precisávamos usar Reflection para adicionar ao dagger graph do aplicativo essa 
                            feature dinâmica, o que causou falhas inesperadas devido a erros de digitação 
                            ou porque a feature ainda não tinha sido carregada. Para resolver esse problema, 
                            criamos um gerenciador centralizado de dependências de features dinâmicas. 
                            Esse gerenciador permitiu que cada feature que precisasse de uma feature dinâmica 
                            o solicite sem usar Reflection, contando com o type-safety do Kotlin por meio do uso 
                            de interfaces.""".trimIndent(),
        )
        title(text = "Resultados", level = TitleStyle.Level.H6)
        paragraph(
            text = """Por meio da modularização, conseguimos tempos de build mais rápidos, 
                            com uma redução de quase 30% no tempo de clean build. Criamos cerca de 30 módulos 
                            de features para diferentes tipos de features, o que melhorou não apenas o fluxo 
                            de trabalho do desenvolvedor, mas também a produtividade da equipe.
                            """.trimIndent(),
        )
        paragraph(
            text = """Ao usar o gerenciador centralizado de dependências de features 
                        dinâmicos, tornamos o aplicativo mais confiável. Isso ajudou a reduzir o risco 
                        do módulo Dagger da feature dinâmica não estar disponível durante tempo de execução 
                        da aplicação devido a erros de digitação ou se o recurso ainda não estivesse 
                        sido instalado, evitando crashes no aplicativo.
                        """.trimIndent(),
        )
        paragraph(
            text = """Além disso, esse projeto me ensinou a importância do planejamento 
                        estratégico e da comunicação em projetos de grande escala.
                        """.trimIndent(),
        )
    }
)
