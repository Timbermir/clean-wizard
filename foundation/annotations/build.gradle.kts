plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.cleanwizard.internal.publish)
}

publish {
    artifactId = "clean-wizard"
}