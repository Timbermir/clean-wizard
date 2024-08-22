plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.cleanwizard.internal.publish)
}

dependencies {
    compileOnly(libs.cleanwizard.gradle.plugins.api)
}

publish {
    artifactId = "clean-wizard"
}