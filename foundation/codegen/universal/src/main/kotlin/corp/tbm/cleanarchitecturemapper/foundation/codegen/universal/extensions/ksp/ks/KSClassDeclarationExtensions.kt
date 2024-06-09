package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSClassDeclaration

inline val KSClassDeclaration.name
    get() = simpleName.asString()

inline val KSClassDeclaration.packagePath
    get() = packageName.asString()

inline val KSClassDeclaration.basePackagePath
    get() = packagePath.split(".").dropLastWhile { it.isEmpty() }.take(5).joinToString(".")