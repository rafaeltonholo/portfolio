package dev.tonholo.portfolio.analytics

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLScriptElement

internal object UmamiInjector {
    private const val SCRIPT_ID = "umami-analytics"
    private val LOCAL_HOSTS = setOf("localhost", "127.0.0.1", "")

    fun shouldInject(hostname: String): Boolean =
        hostname !in LOCAL_HOSTS && BuildAnalytics.UMAMI_WEBSITE_ID.isNotBlank()

    fun inject() {
        if (!shouldInject(window.location.hostname)) return
        if (document.getElementById(SCRIPT_ID) != null) return

        val script = document.createElement("script") as HTMLScriptElement
        script.id = SCRIPT_ID
        script.async = true
        script.defer = true
        script.src = BuildAnalytics.UMAMI_SCRIPT_URL
        script.setAttribute("data-website-id", BuildAnalytics.UMAMI_WEBSITE_ID)
        document.head?.appendChild(script)
    }
}
