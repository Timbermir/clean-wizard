plugins {
    alias(libs.plugins.cleanwizard.internal.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(projects.foundation.annotations)
    implementation(projects.cleanWizard)
    implementation(libs.bundles.di)
    implementation(libs.room.common)
    ksp(projects.processors.dataClass)
    ksp(projects.processors.useCase)
    ksp(libs.koin.annotations.ksp)
}