plugins {
    alias(libs.plugins.cleanwizard.internal.processor)
    alias(libs.plugins.vanniktech.maven.publish)
}

dependencies {
    implementation(projects.visitors.enums)
}