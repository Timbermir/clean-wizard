package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.ModelType
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.dtoRegex

inline val KSPropertyDeclaration.name
    get() = simpleName.asString()

fun KSPropertyDeclaration.getParameterName(packageName: String): String {
    val resolvedType = type.resolve()
    return when {

        resolvedType.isClassMappable ->
            resolvedType.toClassName().simpleName.replace(
                dtoRegex,
                ""
            ) + ModelType.entries.first {
                packageName.split(".").last() == it.suffix.lowercase()
            }.suffix

        resolvedType.isListMappable -> {
            resolvedType.arguments.first().type?.resolve()?.toClassName()?.simpleName?.replace(
                dtoRegex,
                ""
            ) + ModelType.entries.first {
                packageName.split(".").last() == it.suffix.lowercase()
            }.suffix
        }

        else -> name
    }
}

fun KSPropertyDeclaration.getQualifiedPackageNameBasedOnParameterName(packageName: String): String {

    val parts = packageName.split(".")
    val startIndex = parts.indexOfFirst { it == "corp" }

    if (startIndex == -1 || startIndex + 3 >= parts.size) {
        return packageName
    }

    val relevantParts = parts.drop(startIndex).toMutableList()
    val resolvedType = type.resolve()
    relevantParts[3] = when {
        resolvedType.isClassMappable ->
            (type.resolve().toClassName().simpleName.replace(dtoRegex, "") + ModelType.entries.first {
                packageName.split(".").last() == it.suffix.lowercase()
            }.suffix).removeSuffix(
                when {
                    packageName.contains(dtoRegex) -> "DTO"
                    packageName.contains("model") -> "Model"
                    else -> "UI"
                }
            ).replaceFirstChar {
                it.lowercase()
            }

        resolvedType.isListMappable -> (resolvedType.arguments.first().type?.resolve()
            ?.toClassName()?.simpleName?.replace(dtoRegex, "") + ModelType.entries.first {
            packageName.split(".").last() == it.suffix.lowercase()
        }.suffix).removeSuffix(
            when {
                packageName.contains(dtoRegex) -> "DTO"
                packageName.contains("model") -> "Model"
                else -> "UI"
            }
        ).replaceFirstChar {
            it.lowercase()
        }

        else -> (name.replace(dtoRegex, "") + ModelType.entries.first {
            packageName.split(".").last() == it.suffix.lowercase()
        }.suffix).removeSuffix(
            when {
                packageName.contains(dtoRegex) -> "DTO"
                packageName.contains("model") -> "Model"
                else -> "UI"
            }
        ).replaceFirstChar {
            it.lowercase()
        }
    }

    return relevantParts.joinToString(".")
}

fun KSPropertyDeclaration.determineParameterType(packageName: String): TypeName {

    return when {

        type.resolve().isClassMappable -> ClassName(
            getQualifiedPackageNameBasedOnParameterName(packageName),
            getParameterName(packageName).replaceFirstChar { it.uppercase() }
        )

        type.resolve().isListMappable ->
            List::class.asClassName()
                .parameterizedBy(
                    ClassName(
                        getQualifiedPackageNameBasedOnParameterName(packageName),
                        getParameterName(packageName).replaceFirstChar { it.uppercase() }
                    )
                )

        else -> type.toTypeName()
    }
}