package corp.tbm.cleanarchitecturemapper.processor.foundation.extensions

import com.google.devtools.ksp.symbol.KSClassDeclaration

inline val KSClassDeclaration.name
    get() = simpleName.asString()

inline val KSClassDeclaration.packagePath
    get() = packageName.asString()