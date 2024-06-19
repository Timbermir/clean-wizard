plugins {
    alias(libs.plugins.cleanwizard.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    ksp(projects.processors.dataClass)
}