plugins {
    alias(libs.plugins.cleanwizard.codegen.visitor)
    alias(libs.plugins.vanniktech.maven.publish)
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.javax.inject)
    implementation(libs.koin.annotations)
}