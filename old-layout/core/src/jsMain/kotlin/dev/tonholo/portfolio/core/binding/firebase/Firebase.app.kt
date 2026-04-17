@file:JsModule("firebase/app")

package dev.tonholo.portfolio.core.binding.firebase

external fun initializeApp(
    options: FirebaseOptions,
    config: FirebaseAppSettings? = definedExternally,
): FirebaseApp

/**
 * A {@link @firebase/app#FirebaseApp} holds the initialization information for a collection of
 * services.
 *
 * @public
 */
external interface FirebaseApp {
    /**
     * The (read-only) name for this app.
     *
     * The default app's name is `"[DEFAULT]"`.
     *
     * @example
     * ```javascript
     * // The default app's name is "[DEFAULT]"
     * const app = initializeApp(defaultAppConfig);
     * console.log(app.name);  // "[DEFAULT]"
     * ```
     *
     * @example
     * ```javascript
     * // A named app's name is what you provide to initializeApp()
     * const otherApp = initializeApp(otherAppConfig, "other");
     * console.log(otherApp.name);  // "other"
     * ```
     */
    var name: String
    /**
     * The (read-only) configuration options for this app. These are the original
     * parameters given in {@link (initializeApp:1) | initializeApp()}.
     *
     * @example
     * ```javascript
     * const app = initializeApp(config);
     * console.log(app.options.databaseURL === config.databaseURL);  // true
     * ```
     */
    var options: FirebaseOptions
    /**
     * The settable config flag for GDPR opt-in/opt-out
     */
    var automaticDataCollectionEnabled: Boolean
}

/**
 * @public
 *
 * Firebase configuration object. Contains a set of parameters required by
 * services in order to successfully communicate with Firebase server APIs
 * and to associate client data with your Firebase project and
 * Firebase application. Typically this object is populated by the Firebase
 * console at project setup. See also:
 * {@link https://firebase.google.com/docs/web/setup#config-object | Learn about the Firebase config object}.
 */
external interface FirebaseOptions {
    /**
     * An encrypted string used when calling certain APIs that don't need to
     * access private user data
     * (example value: `AIzaSyDOCAbC123dEf456GhI789jKl012-MnO`).
     */
    var apiKey: String?
    /**
     * Auth domain for the project ID.
     */
    var authDomain: String?
    /**
     * Default Realtime Database URL.
     */
    var databaseURL: String?
    /**
     * The unique identifier for the project across all of Firebase and
     * Google Cloud.
     */
    var projectId: String?
    /**
     * The default Cloud Storage bucket name.
     */
    var storageBucket: String?
    /**
     * Unique numerical value used to identify each sender that can send
     * Firebase Cloud Messaging messages to client apps.
     */
    var messagingSenderId: String?
    /**
     * Unique identifier for the app.
     */
    var appId: String?
    /**
     * An ID automatically created when you enable Analytics in your
     * Firebase project and register a web app. In versions 7.20.0
     * and higher, this parameter is optional.
     */
    var measurementId: String?
}

external interface FirebaseAppSettings {
    /**
     * custom name for the Firebase App.
     * The default value is `"[DEFAULT]"`.
     */
    var name: String?
    /**
     * The settable config flag for GDPR opt-in/opt-out
     */
    var automaticDataCollectionEnabled: Boolean?
}
