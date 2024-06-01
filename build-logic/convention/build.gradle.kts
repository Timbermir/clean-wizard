plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.google.devtools.ksp)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(projectConfig.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(pluginConfig.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        libs.plugins.cleanarchitecturemapper.apply {

            val pluginConfigVersions = pluginConfig.versions

            register(kotlin.pluginId) {
                id = kotlin.pluginId
                implementationClass = pluginConfigVersions.foundation.kotlin.implementation.get()
            }

            register(codegen.foundation.pluginId) {
                id = codegen.foundation.pluginId
                implementationClass = pluginConfigVersions.foundation.codegen.implementation.get()
            }

            register(codegen.visitor.pluginId) {
                id = codegen.visitor.pluginId
                implementationClass = pluginConfigVersions.visitor.implementation.get()
            }

            register(workload.pluginId) {
                id = workload.pluginId
                implementationClass = pluginConfigVersions.workload.implementation.get()
            }
        }
    }
}

internal val Provider<PluginDependency>.pluginId
    get() = get().pluginId