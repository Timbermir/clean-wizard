plugins {
    alias(libs.plugins.cleanwizard.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(projects.foundation.annotations)
    implementation(projects.cleanWizard)
    ksp(projects.processors.dataClass)
    ksp(projects.processors.useCase)
}