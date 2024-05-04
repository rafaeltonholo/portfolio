package dev.tonholo.marktdown.processor

interface Logger {
    fun trace(message: String, vararg args: Any?)
    fun warn(message: String, vararg args: Any?)
    fun error(message: String, vararg args: Any?)
    fun debug(message: String?, vararg args: Any?)
    fun info(message: String?, vararg args: Any?)
    fun lifecycle(message: String?, vararg args: Any?)
    fun quiet(message: String?, vararg args: Any?)
}
