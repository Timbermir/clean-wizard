plugins {
    alias(libs.plugins.cleanarchitecturemapper.workload)
}
dependencies {
    implementation(projects.workloads.multiModule.data)
    implementation(projects.workloads.multiModule.domain)
}