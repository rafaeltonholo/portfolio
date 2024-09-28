package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LanguageTag
import dev.tonholo.portfolio.locale.Locales
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus

data class Project(
    val id: String,
    val title: String,
    val description: String,
    val src: String?,
    val role: String,
    val timeline: DatePeriod? = null,
    val playStoreSrc: String? = null,
    val headlineImage: String? = null,
) {
    companion object {
        internal val SvgToCompose = Project(
            id = "svg-to-compose",
            title = "SVG to Compose",
            description = buildString {
                append("A command-line tool to convert SVG ")
                append("or an Android Vector Drawable (AVG) to Jetpack Compose Icons.")
            },
            src = "https://github.com/rafaeltonholo/svg-to-compose",
            role = "Maintainer",
        )
        internal val ComposeDestinations = Project(
            id = "compose-destinations",
            title = "Compose Destinations",
            description = buildString {
                append("Annotation processing library for type-safe Jetpack Compose")
                append(" navigation with no boilerplate.")
            },
            src = "https://github.com/raamcosta/compose-destinations",
            role = "Maintainer",
        )
        internal val Kobweb = Project(
            id = "kobweb",
            title = "Kobweb",
            description = buildString {
                append("A modern framework for full stack web apps in Kotlin, built upon Compose HTML")
            },
            src = "https://github.com/raamcosta/compose-destinations",
            role = "Maintainer",
        )
        internal val IvyWallet = Project(
            id = "ivy-wallet",
            title = "ivy-wallet",
            description = buildString {
                append("Ivy Wallet is a free and open source money management android app.")
                append(" It's written using 100% Kotlin and Jetpack Compose.")
                append(" It's designed to help you keep track of your personal finances with ease.")
            },
            src = "https://github.com/Ivy-Apps/ivy-wallet",
            playStoreSrc = "https://play.google.com/store/apps/details?id=com.ivy.wallet",
            role = "Maintainer",
        )
        internal val Pinterest = Project(
            id = "pinterest",
            title = "Pinterest",
            description = buildString {
                append("On Pinterest, find inspiration for whatever you’re into. Unlock billions of ideas and turn your dreams into reality: Seeing yourself in your dream wardrobe? \uD83D\uDC57 Finding new recipes for weeknight dinner? \uD83C\uDF7D\uFE0F Shopping the perfect pieces for your home? \uD83D\uDECD\uFE0F Creating the life you love?")
            },
            src = "/project/pinterest",
            playStoreSrc = "https://play.google.com/store/apps/details?id=com.pinterest",
            role = "Senior Android Platform Engineer",
            timeline = LocalDate(2022, Month.MARCH, 1) -
                LocalDate(2021, Month.JULY, 1),
        )
        internal val BleacherReport = Project(
            id = "bleacher-report",
            title = "Bleacher Report",
            description = buildString {
                append("Stay up to date with the latest sports scores and news on Bleacher Report.")
            },
            src = "/project/bleacher-report",
            playStoreSrc = "https://play.google.com/store/apps/details?id=com.bleacherreport.android.teamstream",
            role = "Senior Android Engineer",
            timeline = LocalDate(2023, Month.AUGUST, 1) -
                LocalDate(2022, Month.MARCH, 1),
        )
        internal val Questrade = Project(
            id = "questrade",
            title = "Questrade",
            description = buildString {
                append("Stay up to date with the latest sports scores and news on Bleacher Report.")
            },
            src = "/project/questrade",
            playStoreSrc = "https://play.google.com/store/apps/details?id=com.questrade.questmobile",
            role = "Principal Software Engineer",
            timeline = LocalDate(2017, Month.JULY, 1) -
                LocalDate(2021, Month.JULY, 1),
        )
        internal val WPLSesameStreet = Project(
            id = "watch-play-learn",
            title = "Watch, Play, Learn",
            description = buildString {
                append("The \"Watch, Play, Learn\" initiative by Sesame Workshop offers early learning videos designed")
                append(" to support children affected by crises such as conflict and displacement.")
                append(" This global resource provides 140 five-minute animated segments featuring beloved")
                append(" characters like Elmo and Cookie Monster.")
            },
//            src = "https://sesameworkshop.org/our-work/what-we-do/support-for-families-affected-by-crisis/watch-play-learn/",
            src = "/project/watch-play-learn",
            role = "Senior Android Engineer",
            timeline = LocalDate(2024, Month.NOVEMBER, 15) -
                LocalDate(2024, Month.MAY, 15)
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
            Project.Pinterest,
            Project.BleacherReport,
            Project.Questrade,
            Project.WPLSesameStreet,
        ),
        ProjectType.OpenSource to listOf(
            Project.SvgToCompose.copy(
                description = buildString {
                    append("Uma ferramenta command-line para converter SVG ")
                    append("ou um Android Vector Drawable (AVG) para Jetpack Compose icones.")
                },
            ),
            Project.ComposeDestinations.copy(
                description = buildString {
                    append("Biblioteca KSP para navegação Jetpack Compose")
                    append(" com typesafe e sem boilerplate.")
                },
            ),
            Project.Kobweb.copy(
                description = buildString {
                    append("Um framework moderno para aplicativos fullstack para Web em Kotlin,")
                    append(" desenvolvida com base no Compose HTML")
                },
            ),
            Project.IvyWallet.copy(
                description = buildString {
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
