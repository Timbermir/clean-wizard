plugins {
    alias(libs.plugins.cleanwizard.internal.codegen)
    alias(libs.plugins.cleanwizard.internal.publish)
}

dependencies {
    api(libs.cleanwizard.gradle.plugins.api)
}