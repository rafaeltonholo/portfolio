package dev.tonholo.portfolio.components.atoms.shiki

/**
 * Themes supported by Shiki syntax highlighter.
 *
 * @property themeName the exact identifier string Shiki expects.
 */
sealed interface ShikiTheme {
    val themeName: String

    data object OneDarkPro : ShikiTheme {
        override val themeName: String = "one-dark-pro"
    }

    data object OneLight : ShikiTheme {
        override val themeName: String = "one-light"
    }

    data object GitHubDark : ShikiTheme {
        override val themeName: String = "github-dark"
    }

    data object GitHubLight : ShikiTheme {
        override val themeName: String = "github-light"
    }

    data object Dracula : ShikiTheme {
        override val themeName: String = "dracula"
    }

    data object Nord : ShikiTheme {
        override val themeName: String = "nord"
    }

    data object TokyoNight : ShikiTheme {
        override val themeName: String = "tokyo-night"
    }

    data object CatppuccinMacchiato : ShikiTheme {
        override val themeName: String = "catppuccin-macchiato"
    }

    data object CatppuccinLatte : ShikiTheme {
        override val themeName: String = "catppuccin-latte"
    }
}
