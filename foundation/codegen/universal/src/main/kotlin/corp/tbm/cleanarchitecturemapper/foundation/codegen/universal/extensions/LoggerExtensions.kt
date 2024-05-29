package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode

fun KSPLogger.log(message: String = "CAP", symbol: KSNode? = null) {
    warn(message, symbol)
}