package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSValueArgument

inline val KSValueArgument.simpleName
    get() = name?.asString()