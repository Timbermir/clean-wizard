package corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSType
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions

inline val KSType.isListSubclass
    get() = declaration.qualifiedName?.asString()?.endsWith("List") == true

inline val KSType.isClassMappable
    get() = declaration.qualifiedName?.asString()
        ?.contains("DTOSchema") == true || declaration.qualifiedName?.asString()
        ?.contains(ProcessorOptions.layerConfigs.presentation.classSuffix) == true

inline val KSType.isListMappable
    get() = isListSubclass && arguments.first().type?.resolve()?.isClassMappable == true

inline val KSType.isMappable
    get() = isClassMappable || isListMappable