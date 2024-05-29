pluginManagement {
    repositories {
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
        create("gradleProjectConfig") {
            from(files("gradle/gradle-project-config.versions.toml"))
        }
    }
}

rootProject.name = "clean-architecture-mapper"
include(
    "foundation:annotations",
    "foundation:codegen:ksp",
    "foundation:codegen:kotlinpoet",
    "foundation:codegen:universal"
)
include("visitors:enums")
include("processor")
include("workload")