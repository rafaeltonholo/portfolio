package dev.tonholo.marktdown.gradle.plugin.internal

interface CanDisableGeneration {
    /**
     * Whether generation is disabled.
     */
    var disabledGeneration: Boolean

    /**
     * Disables generation.
     */
    fun disableGeneration()
}
