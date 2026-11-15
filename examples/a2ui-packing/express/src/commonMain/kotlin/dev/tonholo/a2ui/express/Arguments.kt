package dev.tonholo.a2ui.express

/**
 * Maps a call's positional and keyword arguments to the signature's parameter names, in signature order.
 * Arguments skipped with `_` are left out, the way the A2UI Express spec describes.
 */
internal fun bindArguments(signature: ComponentSignature, call: Expression.Call): Map<String, Expression> {
    val parameters = signature.parameters
    if (call.positional.size > parameters.size) {
        throw ExpressException(call.line, "${call.name} takes at most ${parameters.size} arguments")
    }
    val bound = call.positional.withIndex().associate { (index, value) -> parameters[index].name to value }.toMutableMap()
    call.keywords.forEach { (keyword, value) ->
        if (parameters.none { it.name == keyword }) throw ExpressException(call.line, "${call.name} has no parameter $keyword")
        if (keyword in bound) throw ExpressException(call.line, "${call.name} gets $keyword twice")
        bound[keyword] = value
    }
    val provided = bound.filterValues { it !is Expression.Skip }
    parameters.firstOrNull { it.required && it.name !in provided }?.let {
        throw ExpressException(call.line, "${call.name} is missing ${it.name}")
    }
    return parameters.mapNotNull { parameter -> provided[parameter.name]?.let { parameter.name to it } }.toMap()
}
