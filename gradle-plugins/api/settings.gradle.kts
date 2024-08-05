enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {

        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }

        create("projectConfig") {
            from(files("../../gradle/project-config.versions.toml"))
        }
    }
}

rootProject.name = "api"
includeBuild("../../build-logic")