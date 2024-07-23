pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
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

rootProject.name = "clean-wizard-root"
includeBuild(".")
includeBuild("build-logic")
includeBuild("gradle-plugins") {
    dependencySubstitution {
        substitute(module("io.github.timbermir.clean-wizard:gradle-plugins-api")).using(project(":api"))
    }
}
include(
    "foundation:annotations",
    "foundation:codegen"
)
include("clean-wizard")
include(
    "processors:data-class",
    "processors:use-case"
)
include("workloads:single-module")
include(
    "workloads:multi-module:data",
    "workloads:multi-module:domain",
    "workloads:multi-module:presentation"
)