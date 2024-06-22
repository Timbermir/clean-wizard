plugins {
    alias(libs.plugins.cleanwizard.codegen.visitor)
    alias(libs.plugins.vanniktech.maven.publish)
}

dependencies {
    implementation(libs.bundles.di)
}