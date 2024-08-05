package corp.tbm.cleanwizard.foundation.codegen.extensions

inline fun <T> Collection<T>.ifEmpty(action: () -> Unit) {
    if (isEmpty()) {
        action()
    }
}

inline fun <T> Collection<T>.ifNotEmpty(action: () -> Unit) {
    if (isNotEmpty()) {
        action()
    }
}