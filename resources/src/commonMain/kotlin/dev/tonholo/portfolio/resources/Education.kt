package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LanguageTag
import dev.tonholo.portfolio.locale.Locales
import kotlinx.datetime.LocalDate

data class Education(
    val title: String,
    val school: String,
    val date: LocalDate,
    val description: Description,
) {
    companion object {
        val Educations: Map<LanguageTag, List<Education>> = mapOf(
            Locales.EN to listOf(
                Una,
                PucMinas,
                Cotemig,
            ),
            Locales.PT_BR to listOf(
                Una.copy(
                    school = "Centro universitário UNA",
                    title = "Pós graduação, Desenvolvimento de aplicativos móveis",
                    description = Description(
                        "- Desenolvimento de aplicativos para iOS, Android e Windows Phone.\n- Desenvolvimento de aplicações híbridas com foco em Cordova (ionic) e Xamarin.",
                    ),
                ),
                PucMinas.copy(
                    school = "PUC-MG",
                    title = "Tecnólogo, Jogos digitais",
                    description = Description(
                        "Desenvolvimento de jogos digitais utilizando Unity3D engine, DirectX e OpenGL."
                    ),
                ),
                Cotemig.copy(
                    school = "COTEMIG",
                    title = "Técnico em informática",
                    description = Description(
                        "",
                    ),
                ),
            )
        )
    }
}

private val Una = Education(
    school = "University center UNA",
    title = "Post-graduate, Development of Application for Mobile Devices",
    description = Description(
        "- Development of applications to iOS, Android, Windows Phone.\n- Development of hybrid applications focused on Cordova (ionic) and Xamarin.",
    ),
    date = LocalDate.parse("2015-03-20"),
)
private val PucMinas = Education(
    school = "PUC-MG",
    title = "Technologist, Digital games",
    description = Description(
        "Development of Digital Games using Unity3D engine, DirectX and OpenGL.",
    ),
    date = LocalDate.parse("2012-02-01"),
)
private val Cotemig = Education(
    school = "COTEMIG",
    title = "Computer Technician",
    description = Description(
        "",
    ),
    date = LocalDate.parse("2010-01-01"),
)
