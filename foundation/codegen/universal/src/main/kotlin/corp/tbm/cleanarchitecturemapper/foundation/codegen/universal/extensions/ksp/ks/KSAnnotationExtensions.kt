package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSAnnotation

inline val KSAnnotation.name
    get() = shortName.asString()