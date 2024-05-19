package dev.tonholo.portfolio.resources

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import cafe.adriel.lyricist.LyricistStrings
import dev.tonholo.marktdown.domain.content.MarktdownDocument
import dev.tonholo.portfolio.HelloWorldEn
import dev.tonholo.portfolio.HelloWorldPtBr
import dev.tonholo.portfolio.KmpMigratrionPart1En
import dev.tonholo.portfolio.core.collections.immutableListOf
import dev.tonholo.portfolio.locale.Locales
import dev.tonholo.portfolio.resources.pages.Pages
import kotlinx.datetime.format.MonthNames

@Immutable
data class Strings(
    val navBar: NavBar,
    val footer: Footer,
    val pages: Pages,
    val viewProject: String,
    val present: String,
    val monthNames: MonthNames,
    val scrollToTop: String,
    val articles: Map<String,MarktdownDocument>,
    val siteName: String,
)

@Stable
val MonthNamesEn = MonthNames.ENGLISH_ABBREVIATED

@Stable
val MonthNamesPtBr = MonthNames(
    names = immutableListOf(
        "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul",
        "Ago", "Set", "Out", "Nov", "Dez",
    ),
)

@Stable
@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = Strings(
    navBar = NavBar.En,
    footer = Footer.En,
    pages = Pages.En,
    viewProject = "View Project",
    present = "Present",
    monthNames = MonthNamesEn,
    scrollToTop = "Scroll to top ⬆",
    articles = mapOf(
        "hello-world" to HelloWorldEn,
        "kmp-part-1" to KmpMigratrionPart1En
    ),
    siteName = "Rafael Tonholo Blog",
)

@Stable
@LyricistStrings(languageTag = Locales.PT_BR)
val PtStrings = Strings(
    navBar = NavBar.PtBr,
    footer = Footer.PtBr,
    pages = Pages.PtBr,
    viewProject = "Ver projeto",
    present = "Presente",
    monthNames = MonthNamesPtBr,
    scrollToTop = "Ir para o início ⬆",
    articles = mapOf(
        "hello-world" to HelloWorldPtBr,
        "kmp-part-1" to KmpMigratrionPart1En,
    ),
    siteName = "Rafael Tonholo Blog",
)
