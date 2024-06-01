package corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.accessors.dm.LibrariesForPluginConfig
import org.gradle.accessors.dm.LibrariesForProjectConfig
import org.gradle.api.Project
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

internal inline fun <reified T> Project.retrieveExtension(name: String): T where T : AbstractExternalDependencyFactory {
    return (this as ExtensionAware).extensions.getByName(name) as T
}

internal inline val Project.libs: LibrariesForLibs
    inline get() = retrieveExtension("libs")

internal inline val Project.projectConfig: LibrariesForProjectConfig
    inline get() = retrieveExtension("projectConfig")

internal inline val Project.pluginConfig: LibrariesForPluginConfig
    inline get() = retrieveExtension("pluginConfig")

internal inline val Project.jvmTarget
    get() = JvmTarget.values()
        .first { it.target.contains(projectConfig.versions.jdk.get()) }


internal fun Project.applyPlugin(pluginDependency: Provider<PluginDependency>) {
    pluginManager.apply(pluginDependency.get().pluginId)
}