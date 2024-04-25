package dev.tonholo.marktdown.domain.renderer

import dev.tonholo.marktdown.domain.content.MarktdownElement
import kotlin.reflect.KClass

object MarktdownRenderer {
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Default

    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Custom(val type: KClass<out MarktdownElement>)
}
