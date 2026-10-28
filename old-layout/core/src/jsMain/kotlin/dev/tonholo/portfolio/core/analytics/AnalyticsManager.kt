package dev.tonholo.portfolio.core.analytics

import androidx.compose.runtime.staticCompositionLocalOf
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.binding.firebase.Analytics
import dev.tonholo.portfolio.core.binding.firebase.AnalyticsSettings
import dev.tonholo.portfolio.core.binding.firebase.FirebaseApp
import dev.tonholo.portfolio.core.binding.firebase.FirebaseOptions
import dev.tonholo.portfolio.core.binding.firebase.GtagConfigParams
import dev.tonholo.portfolio.core.binding.firebase.initializeAnalytics
import dev.tonholo.portfolio.core.binding.firebase.initializeApp
import dev.tonholo.portfolio.core.binding.firebase.logEvent
import portfolio.config.BuildConfig

class AnalyticsManager private constructor() {
    private lateinit var firebaseApp: FirebaseApp
    private lateinit var analytics: Analytics

    fun initialize() {
        firebaseApp = initializeApp(
            options = FirebaseOptions {
                apiKey = BuildConfig.FIREBASE_API_KEY
                authDomain = BuildConfig.FIREBASE_AUTH_DOMAIN
                projectId = BuildConfig.FIREBASE_PROJECT_ID
                storageBucket = BuildConfig.FIREBASE_STORAGE_BUCKET
                messagingSenderId = BuildConfig.FIREBASE_MESSAGING_SENDER_ID
                appId = BuildConfig.FIREBASE_APP_ID
                measurementId = BuildConfig.FIREBASE_MEASUREMENT_ID
            }
        )

        analytics = initializeAnalytics(
            app = firebaseApp,
            options = AnalyticsSettings {
                config = GtagConfigParams {
                    sendPageView = false
                }
            }
        )
    }

    fun track(event: AnalyticEvent) {
        logEvent(
            analytics = analytics,
            eventName = event.name,
            eventParams = event.toParams(),
        )
    }

    companion object {
        private lateinit var instance: AnalyticsManager
        operator fun invoke(): AnalyticsManager {
            if (!::instance.isInitialized) {
                instance = AnalyticsManager()
                instance.initialize()
            }
            return instance
        }
    }
}

val LocalAnalyticsManager = staticCompositionLocalOf<AnalyticsManager> { error("No AnalyticsManager provided") }
