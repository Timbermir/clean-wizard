plugins {
    alias(libs.plugins.cleanwizard.internal.processor)
}

dependencies {
    implementation(libs.bundles.di)
}

publish {
    artifactId = "use-case-compiler"
}