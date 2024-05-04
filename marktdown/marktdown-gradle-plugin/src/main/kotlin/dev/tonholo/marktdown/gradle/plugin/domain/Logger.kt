package dev.tonholo.marktdown.gradle.plugin.domain

import dev.tonholo.marktdown.processor.Logger
import org.gradle.api.logging.Logger as GLogger

class LoggerImpl internal constructor(private val logger: GLogger) : Logger {
    override fun trace(message: String, vararg args: Any?) = logger.trace(message, *args)
    override fun warn(message: String, vararg args: Any?) = logger.warn(message, *args)
    override fun error(message: String, vararg args: Any?) = logger.error(message, *args)
    override fun debug(message: String?, vararg args: Any?) = logger.debug(message, *args)
    override fun info(message: String?, vararg args: Any?) = logger.info(message, *args)
    override fun lifecycle(message: String?, vararg args: Any?) = logger.lifecycle(message, *args)
    override fun quiet(message: String?, vararg args: Any?) = logger.quiet(message, *args)
}

fun GLogger.asLogger(): Logger = LoggerImpl(this)
