pluginManagement {
    includeBuild("gradle-plugins")
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("projectConfig") {
            from(files("gradle/project-config.versions.toml"))
        }
    }
}

rootProject.name = "clean-wizard"
includeBuild(".")
includeBuild("build-logic")
include(
    "foundation:annotations",
    "foundation:codegen"
)
include(
    "processors:data-class",
    "processors:use-case"
)
include("workloads:core")
include("workloads:single-module")
include(
    "workloads:multi-module:data",
    "workloads:multi-module:domain",
    "workloads:multi-module:presentation"
)