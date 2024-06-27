package corp.tbm.cleanwizard.buildLogic.convention.plugins

import com.google.devtools.ksp.gradle.KspTask
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.*
import corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions.CleanWizardCodegenExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

internal class CleanWizardMultiModulePlugin : Plugin<Project> {

    private var lastPackageSegmentWhereFirstSourceClassOccurs = ""

    override fun apply(target: Project) {
        with(target) {

            alias(libs.plugins.google.devtools.ksp)

            val codegenExtension =
                extensions.create("clean-wizard-codegen", CleanWizardCodegenExtension::class.java)

            dependencies {
                implementation(project(":foundation:annotations"))
                implementation(project(":clean-wizard"))
                ksp(project(":processors:data-class"))
            }

            gradle.projectsEvaluated {
                scanForMissingDependencies(this@with.extensions.getByType<CleanWizardCodegenExtension>())
            }

            configureGradleTasks(codegenExtension)

            sourceSets {
                getByName("main") {
                    kotlin {
                        srcDirs.forEach { dir ->
                            findBasePackageInDir(dir)
                        }
                    }
                }
            }
        }
    }

    private fun Project.configureGradleTasks(codegenExtension: CleanWizardCodegenExtension) {
        afterEvaluate {

            val presentationProject = project(codegenExtension.presentationProject)
            val domainProject = project(codegenExtension.domainProject)

            val copyGeneratedDomainClasses = tasks.register<Copy>("copyGeneratedDomainClasses") {
                println(delete(File(domainProject.kspMainBuildDirectory)))
                println(delete(File(presentationProject.kspMainBuildDirectory)))
                copyToModule(
                    this,
                    {
                        exclude("**/${cleanWizardExtension.layerConfigs.data.moduleName}/**")
                        exclude("**/${cleanWizardExtension.layerConfigs.presentation.moduleName}/**")
                    },
                    domainProject,
                )
            }

            val cleanDomainAndPresentationClasses = tasks.register<Delete>("cleanDomainAndPresentationClasses") {
                val basePackage = File(
                    kspMainBuildDirectory,
                    lastPackageSegmentWhereFirstSourceClassOccurs.split(".").joinToString("/")
                        .replace(cleanWizardExtension.layerConfigs.data.moduleName, "")
                ).path

                delete(File(basePackage, "/${cleanWizardExtension.layerConfigs.domain.moduleName}"))
                delete(File(basePackage, "/${cleanWizardExtension.layerConfigs.presentation.moduleName}"))
            }

            val copyGeneratedUIClasses = tasks.register<Copy>("copyGeneratedUIClasses") {
                copyToModule(
                    this,
                    {
                        exclude("**/${cleanWizardExtension.layerConfigs.data.moduleName}/**")
                        exclude("**/${cleanWizardExtension.layerConfigs.domain.moduleName}/**")
                    },
                    presentationProject,
                )
                finalizedBy(cleanDomainAndPresentationClasses)
            }

            presentationProject.tasks.withType<KotlinCompile>().configureEach {
                dependsOn(copyGeneratedUIClasses)
            }

            tasks.withType<KspTask>().configureEach {
                finalizedBy(
                    copyGeneratedDomainClasses,
                    copyGeneratedUIClasses
                )
            }
        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(this@configureGradleTasks.jvmTarget)
            }
        }
    }

    private fun Project.copyToModule(
        copy: Copy,
        fromCopySpec: CopySpec.() -> Unit,
        pathToCopyInto: Project,
    ) {
        copy.apply {

            from(kspMainBuildDirectory) {
                fromCopySpec()
            }

            include("**/*.kt")
            includeEmptyDirs = false

            into(pathToCopyInto.kspMainBuildDirectory)
        }
    }

    private fun findBasePackageInDir(dir: File) {

        val findSubdirectories: (File) -> List<File> = { baseDir ->
            baseDir.listFiles()?.filter { it.isDirectory } ?: emptyList()
        }

        val findClassFiles: (File) -> List<File> = { baseDir ->
            baseDir.listFiles()?.filter { it.name.endsWith(".kt") || it.name.endsWith(".java") }
                ?: emptyList()
        }

        val basePackagePath: String
        var currentDir = dir
        while (true) {
            val subdirs = findSubdirectories(currentDir)
            val classFiles = findClassFiles(currentDir)

            if (classFiles.isNotEmpty() || subdirs.isEmpty()) {
                basePackagePath = currentDir.absolutePath.replace(dir.absolutePath, "")
                break
            }

            if (subdirs.size == 1) {
                currentDir = subdirs[0]
            } else {
                basePackagePath = currentDir.absolutePath.replace(dir.absolutePath, "")
                break
            }
        }

        if (basePackagePath.isNotEmpty()) {
            lastPackageSegmentWhereFirstSourceClassOccurs =
                if (basePackagePath.startsWith(File.separator)) basePackagePath.substring(1) else basePackagePath
        }
    }

    private fun Project.scanForMissingDependencies(codegenExtension: CleanWizardCodegenExtension) {

        val dataDependencies = configurations.flatMap { configuration ->
            configuration.dependencies.map { dependency -> "${dependency.group}:${dependency.name}" }
        }.toSet()

        if (codegenExtension.domainProject.isEmpty())
            error("You have to specify path for your domain module")
        if (codegenExtension.presentationProject.isEmpty())
            error("You have to specify path for your presentation module")
        val domainProject = project(codegenExtension.domainProject)
        val domainDependencies = project(codegenExtension.domainProject).configurations.flatMap { configuration ->
            configuration.dependencies.map { dependency -> "${dependency.group}:${dependency.name}" }
        }.toSet()

        val missingDependencies =
            cleanWizardExtension.dependencyInjectionFramework.dependencies.filter { it !in domainDependencies }

        when {

            !dataDependencies.contains(cleanWizardExtension.jsonSerializer.dependency) -> {
                error("[${cleanWizardExtension.jsonSerializer::class.java.simpleName}] serializer is applied at the root, but no [${cleanWizardExtension.jsonSerializer.dependency}] dependency was found.")
            }

            missingDependencies.isNotEmpty() && cleanWizardExtension.dependencyInjectionFramework != CleanWizardDependencyInjectionFramework.None -> {
                error("${cleanWizardExtension.dependencyInjectionFramework::class.java.simpleName} dependency injection framework is applied at the root, but module with path `${domainProject.path}` doesn't have $missingDependencies dependencies.")
            }
        }
    }
}