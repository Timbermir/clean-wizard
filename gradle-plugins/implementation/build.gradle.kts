plugins {
    alias(libs.plugins.cleanwizard.internal.kotlin)
    alias(libs.plugins.cleanwizard.internal.publish)
    alias(libs.plugins.kotlin.dsl)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.gradle.signing)
}

dependencies {
    compileClasspath(libs.kotlin.gradle.plugin)
    implementation(libs.cleanwizard.gradle.plugins.api)
    implementation(libs.kotlinx.serialization.json)
    compileOnly(libs.google.gson)
    compileOnly(libs.google.devtools.ksp)
    compileOnly(files(ksp.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(projectConfig.javaClass.superclass.protectionDomain.codeSource.location))
}

group = projectConfig.versions.group.get()
version = projectConfig.versions.version.get()

gradlePlugin {

    website.set("https://github.com/Timbermir/clean-wizard")
    vcsUrl.set("https://github.com/Timbermir/clean-wizard.git")

    plugins {
        libs.plugins.cleanwizard.apply {

            val pluginConfiguration = pluginConfig.versions.cleanwizard

            create(libs.plugins.cleanwizard.core.pluginId) {
                id = core.pluginId
                implementationClass = pluginConfiguration.root.implementation.get()
                displayName = "Clean Wizard Root Project Plugin"
                description =
                    "This plugin is required for Clean Wizard Kotlin Symbol Processor advanced configuration. " +
                            "Using the following plugin you are able to configure how classes are generated," +
                            " for instance, using this plugin, you are able to change the DTO classes prefix from DTO to Dto."
                tags.set(
                    listOf(
                        "kotlin",
                        "ksp",
                        "kotlin-symbol-processing",
                        "android",
                        "clean-architecture",
                        "domain-model",
                        "ui-model",
                        "dto-entity-mapper",
                    )
                )
            }

            create(libs.plugins.cleanwizard.multimodule.pluginId) {
                id = multimodule.pluginId
                implementationClass = pluginConfiguration.multimodule.implementation.get()
                displayName = "Clean Wizard Multi Module Plugin"
                description =
                    "This plugin is required for Clean Wizard Kotlin Symbol Processor multi-module class generation. " +
                            "If your project is single-module, no need to apply it" +
                            ". It allows you" +
                            "to generate classes in various modules, so if you have data, domain and presentation modules and want to generate classes" +
                            " for each module, use this plugin. "
                tags.set(
                    listOf(
                        "kotlin",
                        "ksp",
                        "kotlin-symbol-processing",
                        "android",
                        "clean-architecture",
                        "domain-model",
                        "ui-model",
                        "dto-entity-mapper",
                    )
                )
            }
        }
    }
}

internal val Provider<PluginDependency>.pluginId
    get() = get().pluginId