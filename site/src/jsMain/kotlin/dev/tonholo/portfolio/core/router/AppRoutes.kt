package dev.tonholo.portfolio.core.router

import com.varabyte.kobweb.navigation.Route

sealed interface AppRoutes {
    val route: String

    data object Home : AppRoutes {
        const val ROUTE = "/"
        override val route: String = ROUTE
    }

    data object Articles : AppRoutes {
        const val ROUTE = "articles"
        override val route: String = ROUTE
    }

    data object About : AppRoutes {
        const val ROUTE = "about"
        override val route: String = ROUTE
    }

    data object Resume : AppRoutes {
        const val ROUTE = "resume"
        override val route: String = ROUTE
    }
}

val Route.Companion.Home get() = AppRoutes.Home.route
val Route.Companion.Articles get() = AppRoutes.Articles.route
val Route.Companion.About get() = AppRoutes.About.route
val Route.Companion.Resume get() = AppRoutes.Resume.route
