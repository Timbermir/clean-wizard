package corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSClassDeclaration
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.asPackage
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.DataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.layerConfigs

inline val KSClassDeclaration.name
    get() = simpleName.asString()

inline val KSClassDeclaration.packagePath
    get() = packageName.asString()

inline val KSClassDeclaration.basePackagePath: String
    get() {
        val splitPath = packagePath.asPackage.dropLastWhile { it.isEmpty() }

        return when (dataClassGenerationPattern) {
            DataClassGenerationPattern.LAYER ->
                splitPath.takeWhile {
                    it !in listOf(
                        layerConfigs.data.moduleName,
                        layerConfigs.domain.moduleName,
                        layerConfigs.presentation.moduleName
                    )
                }

            DataClassGenerationPattern.TYPE ->
                splitPath.dropLastWhile { it.isEmpty() }
        }.asPackage
    }