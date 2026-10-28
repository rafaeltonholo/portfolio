package dev.tonholo.marktdown.processor.extensions

import com.squareup.kotlinpoet.asClassName
import dev.tonholo.marktdown.domain.content.MarktdownElement
import kotlin.reflect.KClass


val KClass<out MarktdownElement>.fnName: String
    get() = asClassName().simpleNames.joinToString("")
