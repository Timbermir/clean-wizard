package corp.tbm.cleanwizard.foundation.codegen.universal.extensions

fun String.firstCharLowercase(): String {
    return replaceFirstChar { it.lowercase() }
}

fun String.firstCharUppercase(): String {
    return replaceFirstChar { it.uppercase() }
}

infix fun String?.takeIfNotEmptyOrReturnDefault(default: String): String {
    return toString().takeIf { it.isNotEmpty() } ?: default
}