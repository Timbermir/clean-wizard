package corp.tbm.cleanwizard.foundation.codegen.universal.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import corp.tbm.cleanwizard.foundation.codegen.universal.ModelType
import corp.tbm.cleanwizard.foundation.codegen.universal.dtoRegex
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharLowercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.basePackagePath
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.isClassMappable
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.isListMappable
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.name
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.packageLastSegment
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.domainOptions

enum class DataClassGenerationPattern {
    LAYER {

        override fun generatePackageName(
            symbol: KSClassDeclaration,
            classGenerationConfig: ClassGenerationConfig
        ): String {
            return "${symbol.basePackagePath}.${classGenerationConfig.moduleName}"
        }

        override fun findRightModelType(packageName: String): ModelType {
            return ModelType.entries.first { modelType -> packageName.packageLastSegment == modelType.moduleName || packageName.packageLastSegment == modelType.packageName }
        }

        override fun getQualifiedPackageName(packageName: MutableList<String>, type: KSType): String {
            return packageName.joinToString(".")
        }

        override fun classNameReplacement(
            packageName: String,
            className: String,
            modelType: ModelType,
        ): ClassName {
            if (modelType == ModelType.MODEL)
                throw IllegalArgumentException("Not allowed to do so")
            return ClassName(
                packageName.replace(modelType.moduleName, domainOptions.moduleName),
                className.replace(modelType.suffix, domainOptions.suffix)
            )
        }

        override fun packageNameReplacement(packageName: String, modelType: ModelType): String {
            if (modelType == ModelType.MODEL)
                throw IllegalArgumentException("Not allowed to do so")
            return packageName.replace(modelType.moduleName, domainOptions.moduleName)
        }
    },
    TYPE {

        override fun generatePackageName(
            symbol: KSClassDeclaration,
            classGenerationConfig: ClassGenerationConfig
        ): String {
            return "${symbol.basePackagePath}.${
                symbol.name.replace(dtoRegex, "").firstCharLowercase()
            }.${classGenerationConfig.packageName}"
        }

        override fun findRightModelType(packageName: String): ModelType {
            return ModelType.entries.first { modelType -> packageName.packageLastSegment == modelType.packageName }
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
                }?.replace(dtoRegex, "")?.firstCharLowercase().toString()
            return packageName.joinToString(".")
        }

        override fun classNameReplacement(
            packageName: String,
            className: String,
            modelType: ModelType,
        ): ClassName {
            if (modelType == ModelType.MODEL)
                throw IllegalArgumentException("Not allowed to do so")
            return ClassName(
                packageName.replace(modelType.packageName, domainOptions.packageName),
                className.replace(modelType.suffix, domainOptions.suffix)
            )
        }

        override fun packageNameReplacement(packageName: String, modelType: ModelType): String {
            if (modelType == ModelType.MODEL)
                throw IllegalArgumentException("Not allowed to do so")
            return packageName.replace(modelType.packageName, domainOptions.packageName)
        }
    };

    abstract fun generatePackageName(symbol: KSClassDeclaration, classGenerationConfig: ClassGenerationConfig): String
    abstract fun findRightModelType(packageName: String): ModelType
    abstract fun getQualifiedPackageName(packageName: MutableList<String>, type: KSType): String
    abstract fun classNameReplacement(
        packageName: String,
        className: String,
        modelType: ModelType
    ): ClassName

    abstract fun packageNameReplacement(packageName: String, modelType: ModelType): String
}