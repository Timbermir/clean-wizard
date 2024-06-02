package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSType

inline val KSType.isListSubclass
    get() = declaration.qualifiedName?.asString()?.endsWith("List") == true

inline val KSType.isClassMappable
    get() = declaration.qualifiedName?.asString()
        ?.contains("DTOSchema") == true || declaration.qualifiedName?.asString()
        ?.contains("UI") == true

inline val KSType.isListMappable
    get() = isListSubclass && arguments.first().type?.resolve()?.isClassMappable == true

inline val KSType.isMappable
    get() = isClassMappable || isListMappable