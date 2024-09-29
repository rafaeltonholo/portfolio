package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LanguageTag
import dev.tonholo.portfolio.core.ui.text.AnnotatedString
import dev.tonholo.portfolio.core.ui.text.TitleStyle
import dev.tonholo.portfolio.core.ui.text.annotatedString
import dev.tonholo.portfolio.core.ui.text.paragraph
import dev.tonholo.portfolio.core.ui.text.title
import dev.tonholo.portfolio.locale.Locales
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus

data class Project(
    val id: String,
    val title: String,
    val summary: CharSequence,
    val src: String?,
    val role: String,
    val description: AnnotatedString,
    val stack: List<String>,
    val timeline: DatePeriod? = null,
    val playStoreSrc: String? = null,
    val headlineImage: String? = null,
) {
    companion object {
        internal val SvgToCompose = Project(
            id = "svg-to-compose",
            title = "SVG to Compose",
            summary = buildString {
                append("A command-line tool to convert SVG ")
                append("or an Android Vector Drawable (AVG) to Jetpack Compose Icons.")
            },
            src = "https://github.com/rafaeltonholo/svg-to-compose",
            role = "Maintainer",
            stack = listOf(),
            description = annotatedString { },
        )
        internal val ComposeDestinations = Project(
            id = "compose-destinations",
            title = "Compose Destinations",
            summary = buildString {
                append("Annotation processing library for type-safe Jetpack Compose")
                append(" navigation with no boilerplate.")
            },
            src = "https://github.com/raamcosta/compose-destinations",
            role = "Maintainer",
            stack = listOf(),
            description = annotatedString { },
        )
        internal val Kobweb = Project(
            id = "kobweb",
            title = "Kobweb",
            summary = buildString {
                append("A modern framework for full stack web apps in Kotlin, built upon Compose HTML")
            },
            src = "https://github.com/raamcosta/compose-destinations",
            role = "Maintainer",
            stack = listOf(),
            description = annotatedString { },
        )
        internal val IvyWallet = Project(
            id = "ivy-wallet",
            title = "ivy-wallet",
            summary = buildString {
                append("Ivy Wallet is a free and open source money management android app.")
                append(" It's written using 100% Kotlin and Jetpack Compose.")
                append(" It's designed to help you keep track of your personal finances with ease.")
            },
            src = "https://github.com/Ivy-Apps/ivy-wallet",
            playStoreSrc = "https://play.google.com/store/apps/details?id=com.ivy.wallet",
            role = "Maintainer",
            stack = listOf(),
            description = annotatedString { },
        )
        internal val Pinterest = Project(
            id = "pinterest",
            title = "Pinterest",
            summary = annotatedString {
                paragraph(
                    text = """Pinterest is a visual discovery engine for finding ideas like recipes, 
                        home and style inspiration, and more.
                        """
                        .trimIndent(),
                )
                paragraph(
                    text = """With billions of Pins on Pinterest, you'll always find ideas to spark inspiration. 
                        When you discover Pins you love, save them to boards to keep your ideas organized 
                        and easy to find. You can also create Pins to share your ideas with other people 
                        on Pinterest.
                        """.trimIndent(),
                )
            },
            src = "/project/pinterest",
            playStoreSrc = "https://play.google.com/store/apps/details?id=com.pinterest",
            role = "Senior Android Platform Engineer",
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
                paragraph(
                    text = """While working with Pinterest, I was responsible for modularizing their codebase. 
                        Due to the size of Pinterest, modularization poses a couple of challenges. 
                        The codebase is extensive, over nine years old, with features under constant development 
                        that cannot halt during such a process.
                        """
                        .trimIndent()
                )
                paragraph(
                    text = """They encountered issues with build time, which nearly reached 30 minutes for a 
                        clean build on an i9 machine with 64GB of memory—a notably long duration. An incremental 
                        build took around 15 minutes. Faced with this challenge, they opted to modularize the 
                        application to enhance build efficiency and implement features installable on demand, 
                        necessitating the utilization of the Play Delivery Feature.
                        """
                        .trimIndent()
                )
                title(text = "Actions", level = TitleStyle.Level.H6)
                paragraph(
                    text = """The open communication with the developers helped us to smoothly transition from 
                        a monolithic application to a modularized one.
                        """
                        .trimIndent(),
                )
                paragraph(
                    text = """We began by targeting the classes and resources with the fewest dependencies 
                        before addressing the other problematic ones, which would necessitate numerous 
                        changes.
                        """
                        .trimIndent(),
                )
                paragraph(
                    text = """While we migrated the code to the new feature module created for that purpose, 
                        we also identified any unnecessary dependencies and dead code that could be removed 
                        from the feature, thus avoiding unnecessary builds for modules the feature shouldn't 
                        rely on.
                        """
                        .trimIndent(),
                )
                paragraph(
                    text = """We were experiencing application crashes when using dynamic feature modules  
                        because the required feature was not available at runtime. We had to use Reflection 
                        to add the dynamic feature module's dagger graph to the application's dagger graph, 
                        which caused unexpected crashes due to typos or because the feature was not yet loaded. 
                        To solve this problem, we created a centralized dynamic feature dependency manager. 
                        This manager allows the feature that needs the dynamic feature module to request it 
                        without using reflection, relying on Kotlin's type safety via interfaces.
                        """.trimIndent(),
                )
                title(text = "The outcome", level = TitleStyle.Level.H6)
                paragraph(
                    text = """Through modularization, we achieved faster build times with a nearly 30% 
                    reduction in clean build time. We created almost 30 feature modules for different kinds 
                     of features, which improved not only the developer's workflow but also the team's 
                     productivity.""".trimIndent(),
                )
                paragraph(
                    text = """By using a centralized Dynamic Feature dependency manager, we made the application 
                        more reliable. This helped to mitigate the risk of the Dagger Module not being available 
                        in the runtime due to misspelling or if the feature was not installed yet avoiding 
                        application crashes.""".trimIndent(),
                )
                paragraph(
                    text = """Moreover, this project taught me the importance of strategic planning and 
                        communication in large-scale projects.""".trimIndent(),
                )
            },
        )
        internal val BleacherReport = Project(
            id = "bleacher-report",
            title = "Bleacher Report",
            summary = buildString {
                append("Stay up to date with the latest sports scores and news on Bleacher Report.")
            },
            src = "/project/bleacher-report",
            playStoreSrc = "https://play.google.com/store/apps/details?id=com.bleacherreport.android.teamstream",
            role = "Senior Android Engineer",
            timeline = LocalDate(2023, Month.AUGUST, 1) -
                LocalDate(2022, Month.MARCH, 1),
            stack = listOf(
                "Java", "Kotlin", "Hilt", "MVVM", "XML View System",
                "Compose for Android", "Google Ads", "ExoPlayer",
                "Room",
            ),
            description = annotatedString { },
        )
        internal val Questrade = Project(
            id = "questrade",
            title = "Questrade",
            summary = buildString {
                append("Stay up to date with the latest sports scores and news on Bleacher Report.")
            },
            src = "/project/questrade",
            playStoreSrc = "https://play.google.com/store/apps/details?id=com.questrade.questmobile",
            role = "Principal Software Engineer",
            timeline = LocalDate(2017, Month.JULY, 1) -
                LocalDate(2021, Month.JULY, 1),
            stack = listOf(),
            description = annotatedString { },
        )
        internal val WPLSesameStreet = Project(
            id = "watch-play-learn",
            title = "Watch, Play, Learn",
            summary = buildString {
                append("The \"Watch, Play, Learn\" initiative by Sesame Workshop offers early learning videos designed")
                append(" to support children affected by crises such as conflict and displacement.")
                append(" This global resource provides 140 five-minute animated segments featuring beloved")
                append(" characters like Elmo and Cookie Monster.")
            },
//            src = "https://sesameworkshop.org/our-work/what-we-do/support-for-families-affected-by-crisis/watch-play-learn/",
            src = "/project/watch-play-learn",
            role = "Senior Android Engineer",
            timeline = LocalDate(2024, Month.NOVEMBER, 15) -
                LocalDate(2024, Month.MAY, 15),
            stack = listOf(
                "Java", "Kotlin", "Apache Cordova", "ReactNative",
                "RSA Cryptography", "OAuth", "IdentityServer",
            ),
            description = annotatedString { },
        )
    }
}

enum class ProjectType {
    OpenSource,
    Commercial
}

object Projects {
    internal val En = mapOf(
        ProjectType.Commercial to listOf(
            Project.Pinterest,
            Project.BleacherReport,
            Project.Questrade,
            Project.WPLSesameStreet,
        ),
        ProjectType.OpenSource to listOf(
            Project.SvgToCompose,
            Project.ComposeDestinations,
            Project.Kobweb,
            Project.IvyWallet,
        ),
    )

    internal val PtBr = mapOf(
        ProjectType.Commercial to listOf(
            Project.Pinterest.copy(
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
            ),
            Project.BleacherReport,
            Project.Questrade,
            Project.WPLSesameStreet,
        ),
        ProjectType.OpenSource to listOf(
            Project.SvgToCompose.copy(
                summary = buildString {
                    append("Uma ferramenta command-line para converter SVG ")
                    append("ou um Android Vector Drawable (AVG) para Jetpack Compose icones.")
                },
            ),
            Project.ComposeDestinations.copy(
                summary = buildString {
                    append("Biblioteca KSP para navegação Jetpack Compose")
                    append(" com typesafe e sem boilerplate.")
                },
            ),
            Project.Kobweb.copy(
                summary = buildString {
                    append("Um framework moderno para aplicativos fullstack para Web em Kotlin,")
                    append(" desenvolvida com base no Compose HTML")
                },
            ),
            Project.IvyWallet.copy(
                summary = buildString {
                    append("Ivy Wallet é um aplicativo Android gerenciador de finanças grátis e open source.")
                    append(" É escrito usando 100% Kotlin e Jetpack Compose.")
                    append(" Seu objetivo é ajudar o usuário a manter os registros pessoais de suas finanças")
                    append(" de maneira simples.")
                },
            ),
        ),
    )

    fun findProject(languageTag: LanguageTag, id: String): Project? {
        val projects = when (languageTag) {
            Locales.EN -> En
            Locales.PT_BR -> PtBr
            else -> return null
        }
        return projects
            .flatMap { it.value }
            .find { it.id == id }
    }
}
