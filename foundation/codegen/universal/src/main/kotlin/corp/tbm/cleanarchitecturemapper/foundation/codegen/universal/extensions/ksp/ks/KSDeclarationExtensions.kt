package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSDeclaration

inline val KSDeclaration?.name
    get() = this?.simpleName?.asString().toString()

inline val KSDeclaration?.fullyQualifiedName
    get() = this?.qualifiedName?.asString().toString()