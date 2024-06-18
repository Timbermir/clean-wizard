import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
//    alias(libs.plugins.cleanwizard.kotlin)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}
dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.google.gson)
    implementation(libs.squareup.moshi)
    implementation(libs.fasterxml.jackson.databind)
}

tasks.withType<KotlinCompile>().configureEach {
    this.compilerOptions {
        languageVersion =
            org.jetbrains.kotlin.gradle.dsl.KotlinVersion.values()
                .first { it.version == projectConfig.versions.kotlin.get() }
    }
}

sourceSets {

    val internal by creating {
        kotlin.srcDirs("src/internal/kotlin")
    }

    val main by getting {
        kotlin.srcDirs("src/main/kotlin", "src/internal/kotlin")
    }
}
dependencies {
    implementation(sourceSets.getByName("internal").output)
}