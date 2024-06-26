plugins {
    alias(libs.plugins.cleanwizard.internal.kotlin)
}
dependencies {
    implementation(projects.workloads.multiModule.data)
    implementation(projects.workloads.multiModule.domain)
    implementation(libs.kodein)
}