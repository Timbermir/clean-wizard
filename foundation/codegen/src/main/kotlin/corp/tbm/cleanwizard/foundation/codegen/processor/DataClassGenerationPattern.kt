package corp.tbm.cleanwizard.foundation.codegen.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.foundation.codegen.extensions.asPackage
import corp.tbm.cleanwizard.foundation.codegen.extensions.firstCharLowercase
import corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks.basePackagePath
import corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks.isClassMappable
import corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks.isListMappable
import corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks.name
import corp.tbm.cleanwizard.foundation.codegen.extensions.packageLastSegment
import corp.tbm.cleanwizard.foundation.codegen.extensions.withoutDTOSchemaSuffix
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions.layerConfigs

enum class DataClassGenerationPattern {
    LAYER {

        override fun generatePackageName(
            symbol: KSClassDeclaration,
            layerConfig: CleanWizardLayerConfig
        ): String {
            return "${symbol.basePackagePath}.${layerConfig.moduleName}"
        }

        override fun generateUseCasePackageName(symbol: KSClassDeclaration): String {
            return "${
                generatePackageName(
                    symbol,
                    layerConfigs.domain
                )
            }.${layerConfigs.domain.useCaseConfig.packageName}"
        }

        override fun findLayerConfig(packageName: String): CleanWizardLayerConfig {
            return layerConfigs.first { modelType -> packageName.packageLastSegment == modelType.moduleName || packageName.packageLastSegment == modelType.packageName }
        }

        override fun getQualifiedPackageName(packageName: MutableList<String>, type: KSType): String {
            return packageName.asPackage
        }

        override fun classNameReplacement(
            packageName: String,
            className: String,
            layerConfig: CleanWizardLayerConfig
        ): ClassName {
            if (layerConfig is CleanWizardLayerConfig.Domain)
                throw IllegalArgumentException("Not allowed to do so")
            return ClassName(
                packageName.replace(layerConfig.moduleName, layerConfigs.domain.moduleName),
                className.replace(layerConfig.classSuffix, layerConfigs.domain.classSuffix)
            )
        }

        override fun packageNameReplacement(packageName: String, layerConfig: CleanWizardLayerConfig): String {
            if (layerConfig is CleanWizardLayerConfig.Domain)
                throw IllegalArgumentException("Not allowed to do so")
            return packageName.replace(layerConfig.moduleName, layerConfigs.domain.moduleName)
        }
    },
    TYPE {

        override fun generatePackageName(
            symbol: KSClassDeclaration,
            layerConfig: CleanWizardLayerConfig
        ): String {
            return "${symbol.basePackagePath}.${
                symbol.name.withoutDTOSchemaSuffix.firstCharLowercase()
            }.${layerConfig.packageName}"
        }

        override fun generateUseCasePackageName(symbol: KSClassDeclaration): String {
            return "${
                symbol.basePackagePath.asPackage.dropLast(1).asPackage
            }.${layerConfigs.domain.moduleName}.${layerConfigs.domain.useCaseConfig.packageName}"
        }

        override fun findLayerConfig(packageName: String): CleanWizardLayerConfig {
            return layerConfigs.first { layerConfig -> packageName.packageLastSegment == layerConfig.packageName }
        }

        override fun getQualifiedPackageName(packageName: MutableList<String>, type: KSType): String {
            packageName[packageName.lastIndex - 1] =
                when {
                    type.isClassMappable -> {
                        type.toClassName().simpleName
                    }

                    type.isListMappable ->
                        type.arguments.first().type?.resolve()
                            ?.toClassName()?.simpleName

                    else -> name
                }?.withoutDTOSchemaSuffix?.firstCharLowercase().toString()
            return packageName.asPackage
        }

        override fun classNameReplacement(
            packageName: String,
            className: String,
            layerConfig: CleanWizardLayerConfig,
        ): ClassName {
            if (layerConfig is CleanWizardLayerConfig.Domain)
                throw IllegalArgumentException("Not allowed to do so")
            return ClassName(
                packageName.replace(layerConfig.packageName, layerConfigs.domain.packageName),
                className.replace(layerConfig.classSuffix, layerConfigs.domain.classSuffix)
            )
        }

        override fun packageNameReplacement(packageName: String, layerConfig: CleanWizardLayerConfig): String {
            if (layerConfig is CleanWizardLayerConfig.Domain)
                throw IllegalArgumentException("Not allowed to do so")
            return packageName.replace(layerConfig.packageName, layerConfigs.domain.packageName)
        }
    };

    abstract fun generatePackageName(symbol: KSClassDeclaration, layerConfig: CleanWizardLayerConfig): String
    abstract fun generateUseCasePackageName(symbol: KSClassDeclaration): String
    abstract fun findLayerConfig(packageName: String): CleanWizardLayerConfig
    abstract fun getQualifiedPackageName(packageName: MutableList<String>, type: KSType): String
    abstract fun classNameReplacement(
        packageName: String,
        className: String,
        layerConfig: CleanWizardLayerConfig
    ): ClassName

    abstract fun packageNameReplacement(packageName: String, layerConfig: CleanWizardLayerConfig): String
}