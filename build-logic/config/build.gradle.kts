import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.dsl)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.google.gson)
    implementation(libs.squareup.moshi)
}

sourceSets {
    val internal by creating {
        kotlin.srcDirs("src/internal/kotlin")
    }

    val main by getting {
        kotlin.srcDirs("src/main/kotlin", "src/internal/kotlin")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    this.compilerOptions {
        languageVersion =
            org.jetbrains.kotlin.gradle.dsl.KotlinVersion.values()
                .first { it.version == projectConfig.versions.kotlin.get() }
    }
}