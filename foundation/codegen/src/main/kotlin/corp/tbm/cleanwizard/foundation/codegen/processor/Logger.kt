package corp.tbm.cleanwizard.foundation.codegen.universal.processor

import com.google.devtools.ksp.processing.KSPLogger

object Logger {

    private var kspLogger: KSPLogger? = null

    val logger: KSPLogger by lazy {
        kspLogger!!
    }

    fun getInstance(logger: KSPLogger) {
        kspLogger = logger
    }
}