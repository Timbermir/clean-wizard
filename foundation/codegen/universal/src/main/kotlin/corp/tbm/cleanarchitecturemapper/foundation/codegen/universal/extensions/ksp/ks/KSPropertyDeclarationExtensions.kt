package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.ModelType
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.dtoRegex
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.firstCharLowercase
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.firstCharUppercase
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.processor.ProcessorOptions

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
                packageName.split(".").last() == it.packageName.lowercase()
            }.suffix

        resolvedType.isListMappable -> {
            resolvedType.arguments.first().type?.resolve()?.toClassName()?.simpleName?.replace(
                dtoRegex,
                ""
            ) + ModelType.entries.first {
                packageName.split(".").last() == it.packageName.lowercase()
            }.suffix
        }

        else -> name
    }
}

fun KSPropertyDeclaration.getQualifiedPackageNameBasedOnParameterName(
    packageName: String
): String {

    val relevantParts = packageName.split(".").toMutableList()

    val resolvedType = type.resolve()

    relevantParts[relevantParts.lastIndex - 1] =
        when {
            resolvedType.isClassMappable ->
                type.resolve().toClassName().simpleName

            resolvedType.isListMappable ->
                resolvedType.arguments.first().type?.resolve()
                    ?.toClassName()?.simpleName

            else -> name
        }?.replace(dtoRegex, "")?.firstCharLowercase().toString()

    return relevantParts.joinToString(".")
}

@OptIn(KspExperimental::class)
fun KSPropertyDeclaration.determineParameterType(
    symbol: KSClassDeclaration,
    resolver: Resolver,
    packageName: String
): TypeName {

    val type = type.resolve()

    return when {

        annotations.filter { it.name.endsWith("Enum") }.toList().isNotEmpty() -> {

            val enumPackageName = "${
                symbol.basePackagePath
            }.${
                symbol.name.replace(dtoRegex, "").firstCharLowercase()
            }.${ProcessorOptions.domainOptions.packageName}.enums"

            val declarations = resolver.getDeclarationsFromPackage(
                enumPackageName
            ).toList()

            val enum = declarations.firstOrNull()

            ClassName(enum?.packageName?.asString().toString(), enum.name)
        }

        type.isClassMappable -> ClassName(
            getQualifiedPackageNameBasedOnParameterName(packageName),
            getParameterName(packageName).firstCharUppercase()
        )

        type.isListMappable ->
            List::class.asClassName()
                .parameterizedBy(
                    ClassName(
                        getQualifiedPackageNameBasedOnParameterName(packageName),
                        getParameterName(packageName).firstCharUppercase()
                    )
                )

        else -> type.toTypeName()
    }
}