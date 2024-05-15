package dev.tonholo.portfolio.resources

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import cafe.adriel.lyricist.LyricistStrings
import dev.tonholo.marktdown.domain.content.MarktdownDocument
import dev.tonholo.portfolio.HelloWorld
import dev.tonholo.portfolio.KmpMigratrionPart1En
import dev.tonholo.portfolio.MyArticleEn
import dev.tonholo.portfolio.MyArticlePtBr
import dev.tonholo.portfolio.locale.Locales
import dev.tonholo.portfolio.resources.pages.AboutPage
import dev.tonholo.portfolio.resources.pages.ArticlePage
import dev.tonholo.portfolio.resources.pages.Home
import dev.tonholo.portfolio.resources.pages.Pages
import dev.tonholo.portfolio.resources.pages.ResumePage
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.todayIn

@Immutable
data class Strings(
    val navBar: NavBar,
    val footer: Footer,
    val pages: Pages,
    val viewProject: String,
    val present: String,
    val monthNames: MonthNames,
    val scrollToTop: String,
    val articles: List<MarktdownDocument>,
)

@Stable
val MonthNamesEn = MonthNames.ENGLISH_ABBREVIATED

@Stable
val MonthNamesPtBr = MonthNames(
    names = listOf(
        "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul",
        "Ago", "Set", "Out", "Nov", "Dez",
    ),
)

@Stable
@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = Strings(
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
        article = ArticlePage.En,
    ),
    viewProject = "View Project",
    present = "Present",
    monthNames = MonthNamesEn,
    scrollToTop = "Scroll to top ⬆",
    articles = listOf(
        HelloWorld,
        MyArticleEn,
        KmpMigratrionPart1En
    ),
)

@Stable
@LyricistStrings(languageTag = Locales.PT_BR)
val PtStrings = Strings(
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
        article = ArticlePage.PtBr,
    ),
    viewProject = "Ver projeto",
    present = "Presente",
    monthNames = MonthNamesPtBr,
    scrollToTop = "Ir para o início ⬆",
    articles = listOf(
        HelloWorld,
        MyArticlePtBr,
        KmpMigratrionPart1En,
    ),
)
