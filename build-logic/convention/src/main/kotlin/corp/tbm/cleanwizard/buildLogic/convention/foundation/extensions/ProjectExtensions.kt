package corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.accessors.dm.LibrariesForProjectConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.plugin.use.PluginDependency
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun Project.alias(pluginDependency: Provider<PluginDependency>) {
    pluginManager.apply(pluginDependency.get().pluginId)
}

internal fun Project.sourceSets(configure: NamedDomainObjectContainer<KotlinSourceSet>.() -> Unit) {
    configure<KotlinJvmProjectExtension> {
        configure(sourceSets)
    }
}

inline fun <reified T> Project.retrieveExtension(name: String): T {
    return extensions.getByName(name) as T
}

inline val Project.libs: LibrariesForLibs
    inline get() = retrieveExtension("libs")

inline val Project.projectConfig: LibrariesForProjectConfig
    inline get() = retrieveExtension("projectConfig")

internal inline val Project.jvmTarget
    get() = JvmTarget.values()
        .first { it.target.contains(projectConfig.versions.jdk.get()) }