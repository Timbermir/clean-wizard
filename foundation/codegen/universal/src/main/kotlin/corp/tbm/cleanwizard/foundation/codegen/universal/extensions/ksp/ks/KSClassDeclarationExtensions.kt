package corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSClassDeclaration
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.domainOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dtoOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.uiOptions

inline val KSClassDeclaration.name
    get() = simpleName.asString()

inline val KSClassDeclaration.packagePath
    get() = packageName.asString()

inline val KSClassDeclaration.basePackagePath
    get() = packagePath.split(".").takeIf {
        it.last() in listOf(
            dtoOptions.moduleName,
            domainOptions.moduleName,
            uiOptions.moduleName
        )
    }?.joinToString(".") ?: packagePath.split(".").take(6).joinToString(".")