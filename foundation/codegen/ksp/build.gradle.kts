plugins {
    alias(libs.plugins.cleanarchitecturemapper.codegen.foundation)
}
dependencies {
    implementation(projects.foundation.codegen.universal)
}