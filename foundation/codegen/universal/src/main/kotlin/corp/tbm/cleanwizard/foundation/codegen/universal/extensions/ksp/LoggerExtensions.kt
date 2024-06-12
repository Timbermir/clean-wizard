package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode

fun KSPLogger.log(message: Any? = "CAP", symbol: KSNode? = null) {
    warn(message.toString(), symbol)
}