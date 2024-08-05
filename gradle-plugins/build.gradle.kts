plugins {
    alias(libs.plugins.kotlin.dsl) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.vanniktech.maven.publish) apply false
}