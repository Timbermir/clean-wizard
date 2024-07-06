plugins {
    alias(libs.plugins.cleanwizard.kotlin)
    alias(libs.plugins.google.devtools.ksp)
}

dependencies {
    implementation(projects.cleanWizard)
    implementation(projects.foundation.annotations)
    implementation(libs.room.common)
    implementation("org.testng:testng:7.1.0")
    ksp(projects.processors.dataClass)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.kotest:kotest-assertions-core:4.6.1")
}