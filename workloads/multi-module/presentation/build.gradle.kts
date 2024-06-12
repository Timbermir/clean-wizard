plugins {
    alias(libs.plugins.cleanwizard.workload)
}
dependencies {
    implementation(projects.workloads.multiModule.data)
    implementation(projects.workloads.multiModule.domain)
}