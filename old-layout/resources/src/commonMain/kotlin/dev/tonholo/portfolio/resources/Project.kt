package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.core.ui.text.AnnotatedString
import dev.tonholo.portfolio.core.ui.text.annotatedString
import dev.tonholo.portfolio.core.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.locale.Locales
import dev.tonholo.portfolio.resources.projects.BleacherReport
import dev.tonholo.portfolio.resources.projects.BleacherReportPtBr
import dev.tonholo.portfolio.resources.projects.Pinterest
import dev.tonholo.portfolio.resources.projects.PinterestPtBr
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus

data class Project(
    val id: String,
    val title: String,
    val client: String,
    val summary: CharSequence,
    val src: String?,
    val role: String,
    val description: AnnotatedString,
    val stack: List<String>,
    val timeline: DatePeriod? = null,
    val playStoreSrc: String? = null,
    val customHeadline: ProjectHeadline? = null,
) {
    companion object {
        internal val SvgToCompose = Project(
            id = "svg-to-compose",
            title = "SVG to Compose",
            client = "SVG to Compose",
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
            client = "Compose Destinations",
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
            client = "Kobweb",
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
            client = "ivy-wallet",
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
        internal val Questrade = Project(
            id = "questrade",
            title = "Questrade",
            client = "Questrade",
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
            client = "Sesame Street",
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

data class ProjectHeadline(
    val headline: String,
    val logoImageSrc: String? = null,
    val backgroundImageSrc: String? = null,
    val backgroundColor: Color? = null,
    val textColor: Color? = null,
    val customTextStyle: TextStyle? = null,
)

enum class ProjectType {
    OpenSource,
    Commercial
}

object Projects {
    internal val En = mapOf(
        ProjectType.Commercial to listOf(
            Pinterest,
            BleacherReport,
//            Project.Questrade,
//            Project.WPLSesameStreet,
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
            PinterestPtBr,
            BleacherReportPtBr,
//            Project.Questrade,
//            Project.WPLSesameStreet,
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
