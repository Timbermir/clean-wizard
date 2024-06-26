package corp.tbm.cleanwizard.foundation.codegen.extensions.ksp

import com.google.devtools.ksp.processing.Resolver

inline fun <reified T> Resolver.getAnnotatedSymbols(annotationName: String): List<T> {
    return getSymbolsWithAnnotation(annotationName).filterIsInstance<T>().toList()
}