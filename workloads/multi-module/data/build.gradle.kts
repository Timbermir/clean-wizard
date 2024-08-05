plugins {
    alias(libs.plugins.cleanwizard.internal.kotlin)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.cleanwizard.multimodule)
}

dependencies {
    implementation(projects.workloads.core)
    implementation(projects.workloads.multiModule.domain)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.room.common)
    implementation(libs.kodein)
    ksp(projects.processors.dataClass)
    ksp(projects.processors.useCase)
}

`clean-wizard-codegen` {
    domainProjectPath = projects.workloads.multiModule.domain.dependencyProject.path
    presentationProjectPath = projects.workloads.multiModule.presentation.dependencyProject.path
}