package corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks

import com.google.devtools.ksp.symbol.KSValueArgument

inline val KSValueArgument.simpleName
    get() = name?.asString()