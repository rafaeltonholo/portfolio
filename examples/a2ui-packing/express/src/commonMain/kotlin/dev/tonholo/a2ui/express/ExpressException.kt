package dev.tonholo.a2ui.express

/**
 * Thrown when a model response can't be compiled into A2UI messages.
 *
 * @property line the line inside the `<a2ui>` block where the problem starts, counting the first line after
 * the opening tag as line 1, or `null` when the problem isn't tied to a line.
 * @property reason what went wrong, without the line prefix. Short enough to send back to the model as a
 * correction hint.
 */
class ExpressException(val line: Int?, val reason: String) :
    Exception(if (line == null) reason else "Line $line: $reason")
