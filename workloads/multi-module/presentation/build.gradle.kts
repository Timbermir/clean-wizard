plugins {
    alias(libs.plugins.cleanwizard.kotlin)
}
dependencies {
    implementation(projects.workloads.multiModule.data)
    implementation(projects.workloads.multiModule.domain)
    implementation(libs.kodein)
}