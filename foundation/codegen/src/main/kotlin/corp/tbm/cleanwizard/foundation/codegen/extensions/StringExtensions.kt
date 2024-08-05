package corp.tbm.cleanwizard.foundation.codegen.extensions

import corp.tbm.cleanwizard.foundation.codegen.dtoSchemaRegex
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions.layerConfigs

fun String.firstCharLowercase(): String {
    return replaceFirstChar { it.lowercase() }
}

fun String.firstCharUppercase(): String {
    return replaceFirstChar { it.uppercase() }
}

infix fun String?.takeIfNotEmptyOrReturnDefault(default: String): String {
    return toString().takeIf { it.isNotEmpty() } ?: default
}

inline val List<String>.asPackage
    get() = joinToString(".")

inline val String.asPackage
    get() = split(".")

inline val String.packageLastSegment
    get() = asPackage.last()

inline val String?.withoutDTOSchemaSuffix
    get() = this?.replace(dtoSchemaRegex, "")?.replace(layerConfigs.data.schemaSuffix, "").toString()