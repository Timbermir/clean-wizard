package corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks

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
import corp.tbm.cleanwizard.foundation.codegen.extensions.asPackage
import corp.tbm.cleanwizard.foundation.codegen.extensions.firstCharUppercase
import corp.tbm.cleanwizard.foundation.codegen.extensions.withoutDTOSchemaSuffix
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions.dataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions.layerConfigs
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardNullResolutionStrategy
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig

inline val KSPropertyDeclaration.name
    get() = simpleName.asString()

fun KSPropertyDeclaration.getParameterName(packageName: String): String {
    val resolvedType = type.resolve()

    return when {

        resolvedType.isClassMappable ->
            resolvedType.toClassName().simpleName.withoutDTOSchemaSuffix + dataClassGenerationPattern.findLayerConfig(
                packageName
            ).classSuffix

        resolvedType.isListMappable ->
            resolvedType.arguments.first().type?.resolve()
                ?.toClassName()?.simpleName.withoutDTOSchemaSuffix + dataClassGenerationPattern.findLayerConfig(
                packageName
            ).classSuffix

        else -> name
    }
}

fun KSPropertyDeclaration.getQualifiedPackageNameBasedOnParameterName(
    packageName: String
): String {

    val relevantParts = packageName.asPackage.toMutableList()

    return dataClassGenerationPattern.getQualifiedPackageName(relevantParts, type.resolve())
}

@OptIn(KspExperimental::class)
fun KSPropertyDeclaration.determineParameterType(
    symbol: KSClassDeclaration,
    resolver: Resolver,
    packageName: String
): TypeName {

    val type = type.resolve()

    return when {

        annotations.filter { it.isEnum }.toList().isNotEmpty() -> {
            val filteredAnnotations =
                annotations.filter { it.isEnum }
                    .toList().first()

            val enumPackageName =
                "${dataClassGenerationPattern.generatePackageName(symbol, layerConfigs.domain)}.enums"

            val declarations = resolver.getDeclarationsFromPackage(
                enumPackageName
            ).toList()

            val enum =
                declarations.firstOrNull {
                    it.name == this.name.firstCharUppercase() || it.name == filteredAnnotations.arguments.first { valueArgument ->
                        valueArgument.simpleName == "enumName"
                    }.simpleName
                }

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

fun KSPropertyDeclaration.isNullable(layerConfig: CleanWizardLayerConfig? = null): Boolean {
    val isNullableType = type.resolve().isMarkedNullable
    val isStubGeneration =
        layerConfigs.data.nullResolutionStrategy == CleanWizardNullResolutionStrategy.STUB_GENERATION
    val isDataLayer = layerConfig is CleanWizardLayerConfig.Data

    return isNullableType || isStubGeneration && (layerConfig == null || isDataLayer)
}

fun KSPropertyDeclaration.elvis(
    block: String,
    layerConfig: CleanWizardLayerConfig? = null,
    predicate: Boolean = isNullable(layerConfig),
): String {
    return if (predicate) "?: $block" else ""
}

fun KSPropertyDeclaration.safeCall(
    block: String,
    layerConfig: CleanWizardLayerConfig? = null,
    predicate: Boolean = isNullable(layerConfig)
): String {
    return "${if (predicate) "?" else ""}.$block"
}