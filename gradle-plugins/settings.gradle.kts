enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {

        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }

        create("projectConfig") {
            from(files("../gradle/project-config.versions.toml"))
        }

        create("pluginConfig") {
            from(files("gradle/plugin-config.versions.toml"))
        }
    }
}

rootProject.name = "gradle-plugins"
includeBuild("../build-logic")
includeBuild("api") {
    dependencySubstitution {
        substitute(module("io.github.timbermir.clean-wizard:gradle-plugins-api")).using(project(":"))
    }
}
include("implementation")