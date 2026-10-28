package dev.tonholo.portfolio.resources

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class Footer(
    val copyright: String,
    val designedBy: String,
    val builtWith: String,
) {
    companion object {
        val En = Footer(
            copyright = "© ${
                Clock.System.todayIn(TimeZone.currentSystemDefault()).year
            } All Rights reserved.",
            designedBy = "Designed by Amanda Bicalho",
            builtWith = "Built with Kotlin and Compose 💜",
        )
        val PtBr = Footer(
            copyright = "© ${
                Clock.System.todayIn(TimeZone.currentSystemDefault()).year
            } Todos os direitos reservados.",
            designedBy = "Design feito por Amanda Bicalho",
            builtWith = "Feito com Kotlin e Compose 💜",
        )
    }
}
