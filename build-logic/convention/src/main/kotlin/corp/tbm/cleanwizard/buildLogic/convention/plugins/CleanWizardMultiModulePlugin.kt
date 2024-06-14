package corp.tbm.cleanwizard.buildLogic.convention.plugins

import com.google.devtools.ksp.gradle.KspTaskJvm
import corp.tbm.cleanwizard.buildLogic.convention.foundation.CleanWizardCodegenExtension
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

class CleanWizardMultiModulePlugin : Plugin<Project> {

    private var lastPackageSegmentWhereFirstSourceClassOccurs = ""
    override fun apply(target: Project) {
        with(target) {

            alias(libs.plugins.cleanwizard.kotlin)
            alias(libs.plugins.google.devtools.ksp)

            val codegenExtension =
                extensions.create("clean-wizard-codegen", CleanWizardCodegenExtension::class.java)

            dependencies {
                implementation(libs.bundles.kotlinx)
                implementation(project(":foundation:annotations"))
                implementation(project(":clean-wizard"))
                ksp(project(":processor"))
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
        tasks.named("compileKotlin") {
            dependsOn(project(":workloads:multi-module:data").tasks.named("copyGeneratedUIClasses"))
        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(this@configureGradleTasks.jvmTarget)
            }
        }

        tasks.withType<KspTaskJvm>().configureEach {
            finalizedBy("copyGeneratedDomainClasses")
        }

        tasks.register<Copy>("copyGeneratedDomainClasses") {

            copyToModule(
                this,
                "kspKotlin",
                {
                    exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
                    exclude("**/${cleanWizardProcessorConfig.presentationModuleName}/**")
                },
                project(codegenExtension.domainProject),
                "copyGeneratedUIClasses"
            )
        }

        tasks.register<Copy>("copyGeneratedUIClasses") {
            copyToModule(
                this,
                "copyGeneratedDomainClasses",
                {
                    exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
                    exclude("**/${cleanWizardProcessorConfig.domainModuleName}/**")
                },
                project(codegenExtension.presentationProject),
                "cleanDomainAndPresentationClassesInData"
            )
        }

        tasks.register<Delete>("cleanDomainAndPresentationClassesInData") {
            dependsOn("copyGeneratedUIClasses")

            val basePackage = File(
                kspMainBuildDirectory,
                lastPackageSegmentWhereFirstSourceClassOccurs.split(".").joinToString("/")
                    .replace(cleanWizardProcessorConfig.dataModuleName, "")
            ).path

            delete(File(basePackage, "/${cleanWizardProcessorConfig.domainModuleName}"))
            delete(File(basePackage, "/${cleanWizardProcessorConfig.presentationModuleName}"))
        }
    }

    private fun Project.copyToModule(
        copy: Copy,
        taskToDependOn: String,
        fromCopySpec: CopySpec.() -> Unit,
        pathToCopyInto: Project,
        taskToFinalizeBy: String
    ) {
        copy.apply {
            dependsOn(taskToDependOn)

            from(kspMainBuildDirectory) {
                fromCopySpec()
            }

            include("**/*.kt")
            includeEmptyDirs = false

            into(pathToCopyInto.kspMainBuildDirectory)
            finalizedBy(taskToFinalizeBy)
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
}