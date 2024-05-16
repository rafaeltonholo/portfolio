package dev.tonholo.portfolio.resources.pages

import androidx.compose.runtime.Immutable
import dev.tonholo.portfolio.core.collections.ImmutableList
import dev.tonholo.portfolio.core.collections.immutableListOf
import kotlin.random.Random

data class AboutPage(
    val main: MainContent,
    val moreAboutMe: MoreAboutMe,
) : Page {
    companion object {
        val En = AboutPage(
            main = MainContent.En,
            moreAboutMe = MoreAboutMe.En,
        )
        val PtBr = AboutPage(
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
                "Lorem ipsum dolor sit amet consectetur. Egestas massa sapien facilisis purus donec ornare. Platea lorem nisl orci pellentesque eros nisl tellus sed. Vitae gravida tincidunt lacus turpis duis odio proin quam et. Vitae pharetra turpis augue quis ac. ",
                "Pharetra arcu vitae urna pellentesque id dui. Nunc id vel aliquet eget auctor dolor suspendisse. Ullamcorper condimentum turpis sem elit neque. Quis nulla quam id congue rhoncus lacinia malesuada. Sagittis pharetra vitae ullamcorper tempus quam neque et tincidunt. Felis ultrices sit morbi nulla eget donec bibendum nisl. ",
            ),
            viewMyResume = "View my resume",
        )
        val PtBr = MainContent(
            title = "Este sou eu...",
            description = immutableListOf(),
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
