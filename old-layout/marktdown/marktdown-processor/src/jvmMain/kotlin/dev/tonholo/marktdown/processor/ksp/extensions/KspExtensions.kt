package dev.tonholo.marktdown.processor.ksp.extensions

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference

// see https://gist.github.com/NikolaDespotoski/5bbb254cf988a0f529a3385fc55114a7#file-kspextensions-kt

/**
 * Reified function to check if receiver [KSType] is assignable from [T] class
 */
inline fun <reified T> KSType.isAssignableFrom(resolver: Resolver): Boolean {
    val classDeclaration = requireNotNull(resolver.getClassDeclarationByName<T>()) {
        "Unable to resolve ${KSClassDeclaration::class.simpleName} for type ${T::class.simpleName}"
    }
    return isAssignableFrom(classDeclaration.asStarProjectedType())
}


/**
 * Checks if [KSTypeReference] (which has to be [KSClassDeclaration])
 * belongs in [packageName]
 * @param packageName package name
 * @return true if [KSClassDeclaration] belongs to the [packageName]
 */
fun Resolver.isFromPackage(type: KSTypeReference, packageName: String): Boolean {
    val returnTypeClassDeclaration = type.resolve().declaration as KSClassDeclaration
    val kotlinCollections = getKSNameFromString(packageName)
    return kotlinCollections == returnTypeClassDeclaration.packageName
}

/**
 * Checks if [KSTypeReference] (which has to be [KSClassDeclaration]
 * belongs in [java.util] package
 */
fun Resolver.isJvmCollection(type: KSTypeReference) =
    isFromPackage(type = type, packageName = "java.util")

/**
 * Checks if [KSTypeReference] (which has to be [KSClassDeclaration]
 * belongs in [kotlin.collections] package
 */
fun Resolver.isKtCollection(type: KSTypeReference) =
    isFromPackage(type = type, packageName = "kotlin.collections")


/**
 * Returns the parameter type of the return type of [KSClassDeclaration]
 * e.g: In
 *  fun getAllDevices() : List<Device>
 *
 *  will return the Device class as [KSType]
 * @return returns parametrized type as [KSType]
 */
fun KSFunctionDeclaration.returnTypeParametrized(resolver: Resolver): KSType {
    val returnType = requireNotNull(returnType) {
        "Function has no return type"
    }
    check(returnType.resolve().declaration.qualifiedName == resolver.builtIns.unitType) {
        "Function ${simpleName.asString()} has kotlin.Unit type. It can't be parametrized."
    }
    return returnType.element!!.typeArguments.first().type!!.resolve()
}

/**
 * @returns [KSAnnotation] for typed T or null if not found
 */
inline fun <reified T> KSDeclaration.findAnnotation(resolver: Resolver): KSAnnotation? {
    val annotationKsName = resolver.getKSNameFromString(T::class.simpleName!!)
    return annotations.firstOrNull { it.shortName.asString() == annotationKsName.asString() }
}

inline fun <reified T> KSDeclaration.findAnnotation(): KSAnnotation? {
    return annotations.firstOrNull {
        it.shortName.getShortName() == T::class.simpleName
    }
}

fun KSAnnotation.argumentWithName(name: String): KSType? = arguments
    .firstOrNull { it.name?.asString() == name }
    ?.value as? KSType
