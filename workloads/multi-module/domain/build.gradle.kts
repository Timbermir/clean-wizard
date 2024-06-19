plugins {
    alias(libs.plugins.cleanwizard.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(projects.foundation.annotations)
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    ksp(projects.processors.useCase)
    ksp(libs.koin.annotations.ksp)
}