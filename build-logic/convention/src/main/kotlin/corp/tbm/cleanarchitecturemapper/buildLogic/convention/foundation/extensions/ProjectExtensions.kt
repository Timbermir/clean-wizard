package corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.extensions

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.accessors.dm.LibrariesForPluginConfig
import org.gradle.accessors.dm.LibrariesForProjectConfig
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

internal inline fun <reified T> Project.retrieveExtension(name: String): T {
    return extensions.getByName(name) as T
}

internal inline val Project.libs: LibrariesForLibs
    inline get() = retrieveExtension("libs")

internal inline val Project.projectConfig: LibrariesForProjectConfig
    inline get() = retrieveExtension("projectConfig")

internal inline val Project.pluginConfig: LibrariesForPluginConfig
    inline get() = retrieveExtension("pluginConfig")

internal inline val Project.ksp: KspExtension
    get() = retrieveExtension("ksp")

internal inline fun Project.ksp(configuration: KspExtension.() -> Unit) {
    configuration(this.ksp)
}

internal inline val Project.jvmTarget
    get() = JvmTarget.values()
        .first { it.target.contains(projectConfig.versions.jdk.get()) }

internal fun Project.applyPlugin(pluginDependency: Provider<PluginDependency>) {
    pluginManager.apply(pluginDependency.get().pluginId)
}