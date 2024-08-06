plugins {
    alias(libs.plugins.cleanwizard.internal.processor)
}

dependencies {
    implementation(libs.room.common)
}

publish {
    artifactId = "data-class-compiler"
}