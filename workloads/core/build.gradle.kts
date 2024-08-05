plugins {
    alias(libs.plugins.cleanwizard.internal.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    api(projects.foundation.annotations)
    ksp(projects.processors.dataClass)
}