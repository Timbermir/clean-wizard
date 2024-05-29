package corp.tbm.cleanarchitecturemapper.processor.extensions

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import corp.tbm.cleanarchitecturemapper.processor.enums.ModelType
import corp.tbm.cleanarchitecturemapper.processor.validators.dtoRegex
import java.util.*


inline val KSPropertyDeclaration.name
    get() = simpleName.asString()

fun KSPropertyDeclaration.getParameterName(packageName: String): String {
    return if (isCustomClass) name.replace(dtoRegex, "") + ModelType.entries.first {
        packageName.split(".").last() == it.suffix.lowercase()
    }.suffix else name
}

inline val KSPropertyDeclaration.isCustomClass: Boolean
    get() = type.resolve().declaration.packageName.asString() !in setOf("kotlin", "java.lang")

fun KSPropertyDeclaration.getQualifiedPackageNameBasedOnParameterName(packageName: String): String {

    val parts = packageName.split(".")
    val startIndex = parts.indexOfFirst { it == "corp" }

    if (startIndex == -1 || startIndex + 3 >= parts.size) {
        return packageName
    }

    val relevantParts = parts.drop(startIndex).toMutableList()

    relevantParts[3] = (name.replace(dtoRegex, "") + ModelType.entries.first {
        packageName.split(".").last() == it.suffix.lowercase()
    }.suffix).removeSuffix(
        when {
            packageName.contains(dtoRegex) -> "DTO"
            packageName.contains("model") -> "Model"
            else -> "UI"
        }
    ).replaceFirstChar {
        it.lowercase(Locale.getDefault())
    }
    return relevantParts.joinToString(".")
}