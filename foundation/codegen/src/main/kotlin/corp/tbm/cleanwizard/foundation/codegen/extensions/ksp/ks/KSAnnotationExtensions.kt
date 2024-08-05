package corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSAnnotation
import corp.tbm.cleanwizard.foundation.codegen.EnumType

inline val KSAnnotation.name
    get() = shortName.asString()

inline val KSAnnotation.isEnum
    get() = annotationType.resolve().declaration.fullyQualifiedName in EnumType.entries.map { it.annotation.qualifiedName }