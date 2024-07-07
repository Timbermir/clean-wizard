plugins {
    alias(libs.plugins.cleanwizard.internal.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(projects.foundation.annotations)
    implementation(projects.cleanWizard)
    implementation(libs.bundles.di)
    implementation(libs.room.common)
    ksp(projects.processors.dataClass)
    ksp(projects.processors.useCase)
    ksp(libs.koin.annotations.ksp)

    implementation("org.testng:testng:7.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.kotest:kotest-assertions-core:4.6.1")
}