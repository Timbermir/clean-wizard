package corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions

import org.gradle.kotlin.dsl.DependencyHandlerScope

internal fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    "implementation"(dependencyNotation)
}

internal fun DependencyHandlerScope.testImplementation(dependencyNotation: Any) {
    "testImplementation"(dependencyNotation)
}

internal fun DependencyHandlerScope.api(dependencyNotation: Any) {
    "api"(dependencyNotation)
}

internal fun DependencyHandlerScope.ksp(dependencyNotation: Any) {
    "ksp"(dependencyNotation)
}