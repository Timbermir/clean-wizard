plugins {
    alias(libs.plugins.cleanwizard.internal.kotlin)
    alias(libs.plugins.cleanwizard.multimodule)
}

dependencies {
    implementation(projects.workloads.multiModule.domain)
    implementation(libs.bundles.kotlinx)
    implementation(libs.kodein)
    implementation(libs.room.common)
}

`clean-wizard-codegen` {
    domainProjectPath = projects.workloads.multiModule.domain.dependencyProject.path
    presentationProjectPath = projects.workloads.multiModule.presentation.dependencyProject.path
}