package dev.tonholo.portfolio.resources.pages

import androidx.compose.runtime.Immutable
import dev.tonholo.portfolio.core.collections.ImmutableList
import dev.tonholo.portfolio.core.collections.immutableListOf
import kotlin.random.Random

data class AboutPage(
    override val title: String,
    val main: MainContent,
    val moreAboutMe: MoreAboutMe,
) : Page {
    companion object {
        val En = AboutPage(
            title = "About me | Rafael Tonholo",
            main = MainContent.En,
            moreAboutMe = MoreAboutMe.En,
        )
        val PtBr = AboutPage(
            title = "Sobre mim | Rafael Tonholo",
            main = MainContent.PtBr,
            moreAboutMe = MoreAboutMe.PtBr,
        )
    }
}

@Immutable
data class MainContent(
    val title: String,
    val description: ImmutableList<String>,
    val viewMyResume: String,
) : Section {
    companion object {
        val En = MainContent(
            title = "This is me...",
            description = immutableListOf(
                """
                    Ever since I was little, I've always had a passion for technology, spending 
                    hours and hours trying to customize my computer and make it my own. My first 
                    contact with programming was at the age of 12 when I decided to create a private 
                    online MU server to play with my cousins. Since then I've been fascinated with 
                    creating programs and games for computers.
                """.trimIndent(),
                """
                    In high school, I had the opportunity to study at a technical school, where I 
                    immersed myself even more in the world of programming and fell in love with 
                    Android development. Since 2012, I've been working in software development, 
                    playing roles such as fullstack, frontend and backend engineer, but it was in 
                    the mobile area that I found my true passion, focusing my efforts on Android 
                    app development since 2015.
                """.trimIndent(),
            ),
            viewMyResume = "View my resume",
        )
        val PtBr = MainContent(
            title = "Este sou eu...",
            description = immutableListOf(
                """
                    Desde pequeno, sempre fui apaixonado por tecnologia, passando horas e horas tentando 
                    personalizar meu computador deixando do meu jeito. Meu primeiro contato com programação 
                    foi aos 12 anos onde resolvi criar um servidor privado de MU online para jogar com meus primos. 
                    Desde então me senti interessado em criar programas e jogos para computadores.
                """.trimIndent(),
                """
                    No meu ensino médio, tive a oportunidade de estudar em uma escola técnica, onde mergulhei 
                    ainda mais no mundo da programação e me encantei pelo desenvolvimento Android. Desde 2012, 
                    venho atuando na área de desenvolvimento de software, desempenhando papéis como fullstack, 
                    frontend e backend engineer, mas foi na área mobile que encontrei minha verdadeira paixão, 
                    concentrando meus esforços no desenvolvimento de aplicativos para Android desde 2015.
                """.trimIndent(),
            ),
            viewMyResume = "Acesse o meu currículo",
        )
    }
}

@Immutable
data class MoreAboutMe(
    val title: String,
    val highlights: ImmutableList<Highlight>,
) : Section {
    companion object {
        val En = MoreAboutMe(
            title = "More about me",
            highlights = immutableListOf(
                Highlight(
                    title = "Experience",
                    description = "Loren ipsum dolor sit amet consectetur. Egestas massa sapien facilisis purus donec ornare. Platea lorem nisl orci pellentesque eros nisl tellus sed. Vitae gravida tincidunt lacus turpis duis odio proin quam et. Vitae pharetra turpis augue quis ac. ",
                    image = "https://picsum.photos/seed/${Random.nextInt(until = 100)}/541/342",
                ),
                Highlight(
                    title = "Education",
                    description = "Loren ipsum dolor sit amet consectetur. Egestas massa sapien facilisis purus donec ornare. Platea lorem nisl orci pellentesque eros nisl tellus sed. Vitae gravida tincidunt lacus turpis duis odio proin quam et. Vitae pharetra turpis augue quis ac. ",
                    image = "https://picsum.photos/seed/${Random.nextInt(until = 100)}/541/342",
                ),
                Highlight(
                    title = "Skills",
                    description = "Loren ipsum dolor sit amet consectetur. Egestas massa sapien facilisis purus donec ornare. Platea lorem nisl orci pellentesque eros nisl tellus sed. Vitae gravida tincidunt lacus turpis duis odio proin quam et. Vitae pharetra turpis augue quis ac. ",
                    image = "https://picsum.photos/seed/${Random.nextInt(until = 100)}/541/342",
                ),
                Highlight(
                    title = "Interests",
                    description = "Loren ipsum dolor sit amet consectetur. Egestas massa sapien facilisis purus donec ornare. Platea lorem nisl orci pellentesque eros nisl tellus sed. Vitae gravida tincidunt lacus turpis duis odio proin quam et. Vitae pharetra turpis augue quis ac. ",
                    image = "https://picsum.photos/seed/${Random.nextInt(until = 100)}/541/342",
                ),
                Highlight(
                    title = "Languages",
                    description = "Loren ipsum dolor sit amet consectetur. Egestas massa sapien facilisis purus donec ornare. Platea lorem nisl orci pellentesque eros nisl tellus sed. Vitae gravida tincidunt lacus turpis duis odio proin quam et. Vitae pharetra turpis augue quis ac. ",
                    image = "https://picsum.photos/seed/${Random.nextInt(until = 100)}/541/342",
                ),
                Highlight(
                    title = "Games",
                    description = "Loren ipsum dolor sit amet consectetur. Egestas massa sapien facilisis purus donec ornare. Platea lorem nisl orci pellentesque eros nisl tellus sed. Vitae gravida tincidunt lacus turpis duis odio proin quam et. Vitae pharetra turpis augue quis ac. ",
                    image = "https://picsum.photos/seed/${Random.nextInt(until = 100)}/541/342",
                ),
            ),
        )
        val PtBr = MoreAboutMe(
            title = "Mais sobre mim",
            highlights = immutableListOf(),
        )
    }

    data class Highlight(
        val title: String,
        val description: String,
        val image: String,
    )
}
