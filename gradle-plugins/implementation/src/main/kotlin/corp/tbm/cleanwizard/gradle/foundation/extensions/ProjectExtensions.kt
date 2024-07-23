package corp.tbm.cleanwizard.gradle.foundation.extensions

import com.google.devtools.ksp.gradle.KspExtension
import corp.tbm.cleanwizard.gradle.implementation.extensions.CleanWizardExtensionImplementation
import org.gradle.accessors.dm.LibrariesForProjectConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

internal fun Project.sourceSets(configure: NamedDomainObjectContainer<KotlinSourceSet>.() -> Unit) {
    configure<KotlinJvmProjectExtension> {
        configure(sourceSets)
    }
}

internal inline fun <reified T> Project.retrieveExtension(name: String): T {
    return extensions.getByName(name) as T
}

internal inline val Project.projectConfig: LibrariesForProjectConfig
    inline get() = retrieveExtension("projectConfig")

internal inline val Project.ksp: KspExtension
    get() = retrieveExtension("ksp")

internal inline fun Project.ksp(configuration: KspExtension.() -> Unit) {
    configuration(this.ksp)
}

internal inline val Project.kspMainBuildDirectory: String
    get() = File(layout.buildDirectory.asFile.get().path, "generated/ksp/main/kotlin").path

internal inline val Project.kspTestBuildDirectory: String
    get() = File(layout.buildDirectory.asFile.get().path, "generated/ksp/test/kotlin").path

internal inline val Project.cleanWizardExtension: CleanWizardExtensionImplementation
    get() = rootProject.extensions.getByType(CleanWizardExtensionImplementation::class.java)

internal inline val Project.jvmTarget
    get() = JvmTarget.values()
        .first { it.target.contains(projectConfig.versions.jdk.get()) }