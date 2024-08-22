plugins {
    alias(libs.plugins.kotlin.dsl)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.gradle.signing)
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(projectConfig.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {

    plugins {
        libs.plugins.cleanwizard.apply {

            val pluginConfigVersions = pluginConfig.versions.cleanwizard

            register(internal.kotlin.pluginId) {
                id = internal.kotlin.pluginId
                implementationClass = pluginConfigVersions.kotlin.implementation.get()
            }

            register(internal.codegen.pluginId) {
                id = internal.codegen.pluginId
                implementationClass = pluginConfigVersions.codegen.implementation.get()
            }

            register(internal.processor.pluginId) {
                id = internal.processor.pluginId
                implementationClass = pluginConfigVersions.processor.implementation.get()
            }

            register(internal.publish.pluginId) {
                id = internal.publish.pluginId
                implementationClass = pluginConfigVersions.publish.implementation.get()
            }
        }
    }
}

internal val Provider<PluginDependency>.pluginId
    get() = get().pluginId