plugins {
    alias(libs.plugins.cleanwizard.codegen.foundation)
}
dependencies {
    implementation(projects.foundation.codegen.universal)
}