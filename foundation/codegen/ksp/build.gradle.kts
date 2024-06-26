plugins {
    alias(libs.plugins.cleanwizard.internal.codegen.foundation)
}
dependencies {
    implementation(projects.foundation.codegen.universal)
}