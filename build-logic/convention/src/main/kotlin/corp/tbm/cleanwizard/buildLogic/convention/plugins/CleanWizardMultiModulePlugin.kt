package corp.tbm.cleanwizard.buildLogic.convention.plugins

import com.google.devtools.ksp.gradle.KspTask
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardProcessorConfig
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class CleanWizardMultiModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
//            pluginManager.withPlugin(libs.plugins.google.devtools.ksp.get().pluginId) {
//                afterEvaluate {
//                    tasks.register<Copy>("copyGeneratedDomainClasses") {
//
//                        from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule") {
//                            exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
//                            exclude("**/${cleanWizardProcessorConfig.presentationModuleName}/**")
//                        }
//
//                        include("**/*.kt")
//                        includeEmptyDirs = false
//
//                        into("${projects.workloads.multiModule.domain.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
//                        finalizedBy("copyGeneratedUIClasses")
//                    }
//
//                    tasks.register<Copy>("copyGeneratedUIClasses") {
//                        from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule") {
//                            exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
//                            exclude("**/${cleanWizardProcessorConfig.domainModuleName}/**")
//                        }
//                        include("**/*.kt")
//                        includeEmptyDirs = false
//
//                        into("${projects.workloads.multiModule.presentation.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
//                        finalizedBy("cleanDomainAndPresentationClassesInData")
//                    }
//
//                    tasks.register<Delete>("cleanDomainAndPresentationClassesInData") {
//
//                        val dataDir = file("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
//                        val fileTree = fileTree(dataDir) {
//                            exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
//                            include("**/*.kt")
//                        }
//
//                        delete(fileTree)
//                        delete("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/${cleanWizardProcessorConfig.domainModuleName}")
//                        delete("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/${cleanWizardProcessorConfig.presentationModuleName}")
//                    }
//
//                    tasks.withType<KspTask>().configureEach {
//                        finalizedBy("copyGeneratedDomainClasses")
//                    }
//                }
//            }
        }
    }
}