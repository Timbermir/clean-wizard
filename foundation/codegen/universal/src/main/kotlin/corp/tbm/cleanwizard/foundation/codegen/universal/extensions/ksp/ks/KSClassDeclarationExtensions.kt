package corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSClassDeclaration
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.asPackage
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.DataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.domainOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dtoOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.uiOptions

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
                        dtoOptions.moduleName,
                        domainOptions.moduleName,
                        uiOptions.moduleName
                    )
                }

            DataClassGenerationPattern.TYPE ->
                splitPath.dropLastWhile { it.isEmpty() }
        }.asPackage
    }