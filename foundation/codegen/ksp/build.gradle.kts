plugins {
    id(libs.plugins.cleanarchitecturemapper.codegen.foundation.get().pluginId)
}
dependencies {
    implementation(projects.foundation.codegen.universal)
}