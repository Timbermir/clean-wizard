plugins {
    `kotlin-dsl`
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    compileOnly(libs.kotlin.gradle.plugin)
    implementation(projects.config)
    compileOnly(libs.google.devtools.ksp)
    compileOnly(files(ksp.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(projectConfig.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(pluginConfig.javaClass.superclass.protectionDomain.codeSource.location))
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
        kotlin.srcDirs(projects.config.dependencyProject.sourceSets.getByName("internal").allSource)
    }
}
gradlePlugin {
    plugins {
        libs.plugins.cleanwizard.apply {

            val pluginConfigVersions = pluginConfig.versions

            register(core.pluginId) {
                id = core.pluginId
                implementationClass = pluginConfigVersions.cleanwizard.implementation.get()
            }

            register(multimodule.pluginId) {
                id = multimodule.pluginId
                implementationClass = pluginConfigVersions.cleanwizard.multimodule.implementation.get()
            }

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