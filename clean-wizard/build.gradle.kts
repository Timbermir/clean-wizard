plugins {
    alias(libs.plugins.cleanwizard.internal.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    ksp(projects.processors.dataClass)
}