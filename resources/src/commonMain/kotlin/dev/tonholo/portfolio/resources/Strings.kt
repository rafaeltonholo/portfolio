package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LyricistStrings
import dev.tonholo.marktdown.domain.content.MarktdownDocument
import dev.tonholo.portfolio.MyArticleEn
import dev.tonholo.portfolio.MyArticlePtBr
import dev.tonholo.portfolio.locale.Locales
import dev.tonholo.portfolio.resources.pages.AboutPage
import dev.tonholo.portfolio.resources.pages.Home
import dev.tonholo.portfolio.resources.pages.Page
import dev.tonholo.portfolio.resources.pages.Pages
import dev.tonholo.portfolio.resources.pages.ResumePage
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.todayIn

data class Screen(
    val home: HomePage,
    val articles: List<MarktdownDocument>,
)

data class HomePage(
    val info: InfoSection,
    val skills: SkillSection,
//    val historySection: HistorySection,
) : Page {
    data class InfoSection(
        val jobTitle: String,
        val about: List<String>,
    )

    data class SkillSection(
        val languages: Languages,
        val programingLanguages: ProgrammingLanguages
    ) {
        data class Languages(
            val title: String,
            val en: String,
            val pt: String,
        )

        data class ProgrammingLanguages(
            val title: String,
            val ctaShowMore: String,
            val ctaShowLess: String,
        )
    }

    data class HistorySection(
        val work: WorkHistory,
        val educational: EducationalHistory,
    ) {
        interface History {
            val title: String
            val experiences: List<Experience>
        }

        data class WorkHistory(
            override val title: String,
            val presentTag: String,
            val technologiesUsed: String,
            override val experiences: List<Experience>,
        ) : History

        data class EducationalHistory(
            override val title: String,
            override val experiences: List<Experience>,
        ) : History

        data class Experience(
            val key: Long = Clock.System.now().epochSeconds,
            val name: String,
            val title: String,
            val description: Description,
            val starting: LocalDate? = null,
            val ending: LocalDate? = null,
            val technologiesUsed: List<String> = emptyList(),
        )
    }
}

data class Strings(
    val screens: Screen,
    val navBar: NavBar,
    val footer: Footer,
    val pages: Pages,
    val viewProject: String,
    val present: String,
    val monthNames: MonthNames,
    val scrollToTop: String,
)

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = Strings(
    screens = Screen(
        home = HomePage(
            info = HomePage.InfoSection(
                jobTitle = "A Android Mage. Kotlin/Compose 💜",
                about = listOf(
                    "Experienced Android engineer with 10+ years in software development, 5+ years specializing in Kotlin for native Android apps.",
                    "Deep grasp of Android best practices, keen on user-centric design, adept at translating complex ideas into scalable solutions.",
                    "Skilled at fostering growth in collaborative, fast-paced settings.",
                ),
            ),
            skills = HomePage.SkillSection(
                languages = HomePage.SkillSection.Languages(
                    title = "Languages",
                    en = "English",
                    pt = "Portuguese",
                ),
                programingLanguages = HomePage.SkillSection.ProgrammingLanguages(
                    title = "Skills",
                    ctaShowMore = "Show more",
                    ctaShowLess = "Show less",
                ),
            ),
        ),
        articles = listOf(
            MyArticleEn,
        ),
    ),
    navBar = NavBar.En,
    footer = Footer(
        copyright = "© ${
            Clock.System.todayIn(TimeZone.currentSystemDefault()).year
        } All Rights reserved.",
        designedBy = "Designed by Amanda Bicalho",
        builtWith = "Built with Kotlin and Compose 💜",
    ),
    pages = Pages(
        home = Home.En,
        about = AboutPage.En,
        resume = ResumePage.En,
    ),
    viewProject = "View Project",
    present = "Present",
    monthNames = MonthNames.ENGLISH_ABBREVIATED,
    scrollToTop = "Scroll to top ⬆",
)

@LyricistStrings(languageTag = Locales.PT_BR)
val PtStrings = Strings(
    screens = Screen(
        home = HomePage(
            info = HomePage.InfoSection(
                jobTitle = "Um Mago do Android. Kotlin/Compose 💜",
                about = listOf(
                    "Engenheiro Android experiente com mais de 10 anos em desenvolvimento de software, e mais de 5 anos desenvolvendo em Kotlin para aplicações Android nativas.",
                    "Adepto das melhores práticas do Android, sempre busco um desenvolvimento centrado à experiência de usuário traduzindo ideias complexas em soluções escaláveis.",
                    "Busco promover o crescimento do time em ambientes colaborativos e de ritmo acelerado.",
                ),
            ),
            skills = HomePage.SkillSection(
                languages = HomePage.SkillSection.Languages(
                    title = "Languages",
                    en = "Inglês",
                    pt = "Português",
                ),
                programingLanguages = HomePage.SkillSection.ProgrammingLanguages(
                    title = "Habilidades",
                    ctaShowMore = "Mostrar mais",
                    ctaShowLess = "Mostrar menos",
                ),
            ),
        ),
        articles = listOf(
            MyArticlePtBr,
        ),
    ),
    navBar = NavBar.PtBr,
    footer = Footer(
        copyright = "© ${
            Clock.System.todayIn(TimeZone.currentSystemDefault()).year
        } Todos os direitos reservados.",
        designedBy = "Design feito por Amanda Bicalho",
        builtWith = "Feito com Kotlin e Compose 💜",
    ),
    pages = Pages(
        home = Home.PtBr,
        about = AboutPage.PtBr,
        resume = ResumePage.PtBr,
    ),
    viewProject = "Ver projeto",
    present = "Presente",
    monthNames = MonthNames(
        names = listOf(
            "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul",
            "Ago", "Set", "Out", "Nov", "Dez",
        ),
    ),
    scrollToTop = "Ir para o início ⬆",
)
