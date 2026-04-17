@file:JsModule("firebase/analytics")

package dev.tonholo.portfolio.core.binding.firebase

external fun initializeAnalytics(
    app: FirebaseApp,
    options: AnalyticsSettings = definedExternally,
): Analytics

external fun logEvent(
    analytics: Analytics,
    eventName: String,
    eventParams: EventParams? = definedExternally,
    options: AnalyticsCallOptions? = definedExternally,
)

/**
 * An instance of Firebase Analytics.
 * @public
 */
external interface Analytics {
    /**
     * The {@link @firebase/app#FirebaseApp} this {@link Analytics} instance is associated with.
     */
    val app: FirebaseApp
}

external interface AnalyticsSettings {
    /**
     * Params to be passed in the initial `gtag` config call during Firebase
     * Analytics initialization.
     */
    var config: GtagConfigParams?
}

external interface GtagConfigParams : EventParams {
    /**
     * Whether or not a page view should be sent.
     * If set to true (default), a page view is automatically sent upon initialization
     * of analytics.
     * See [Page views](https://developers.google.com/analytics/devguides/collection/ga4/views)
     */
    @JsName("send_page_view")
    var sendPageView: Boolean?
    /**
     * The title of the page.
     * See [Page views](https://developers.google.com/analytics/devguides/collection/ga4/views)
     */
    @JsName("page_title")
    var pageTitle: String?
    /**
     * The URL of the page.
     * See [Page views](https://developers.google.com/analytics/devguides/collection/ga4/views)
     */
    @JsName("page_location")
    var pageLocation: String?
    /**
     * Defaults to `auto`.
     * See [Cookies and user identification](https://developers.google.com/analytics/devguides/collection/ga4/cookies-user-id)
     */
    @JsName("cookie_domain")
    var cookieDomain: String?
    /**
     * Defaults to 63072000 (two years, in seconds).
     * See [Cookies and user identification](https://developers.google.com/analytics/devguides/collection/ga4/cookies-user-id)
     */
    @JsName("cookie_expires")
    var cookieExpires: Number?
    /**
     * Defaults to `_ga`.
     * See [Cookies and user identification](https://developers.google.com/analytics/devguides/collection/ga4/cookies-user-id)
     */
    @JsName("cookie_prefix")
    var cookiePrefix: String?
    /**
     * If set to true, will update cookies on each page load.
     * Defaults to true.
     * See [Cookies and user identification](https://developers.google.com/analytics/devguides/collection/ga4/cookies-user-id)
     */
    @JsName("cookie_update")
    var cookieUpdate: Boolean?
    /**
     * Appends additional flags to the cookie when set.
     * See [Cookies and user identification](https://developers.google.com/analytics/devguides/collection/ga4/cookies-user-id)
     */
    @JsName("cookie_flags")
    var cookieFlags: String?
    /**
     * If set to false, disables all advertising features with `gtag.js`.
     * See [Disable advertising features](https://developers.google.com/analytics/devguides/collection/ga4/display-features)
     */
    @JsName("allow_google_signals")
    var allowGoogleSignals: Boolean?
    /**
     * If set to false, disables all advertising personalization with `gtag.js`.
     * See [Disable advertising features](https://developers.google.com/analytics/devguides/collection/ga4/display-features)
     */
    @JsName("allow_ad_personalization_signals")
    var allowAdPersonalizationSignals: Boolean?
}

/**
 * Additional options that can be passed to Analytics method
 * calls such as `logEvent`, etc.
 * @public
 */
external interface AnalyticsCallOptions {
    /**
     * If true, this config or event call applies globally to all
     * Google Analytics properties on the page.
     */
    val global: Boolean
}

external interface EventParams
