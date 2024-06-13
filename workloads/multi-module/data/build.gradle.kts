import com.google.devtools.ksp.gradle.KspTask
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardProcessorConfig

plugins {
    alias(libs.plugins.cleanwizard.workload)
}

dependencies {
    implementation(projects.workloads.multiModule.domain)
}

tasks.register<Copy>("copyGeneratedDomainClasses") {

    from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule") {
        exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
        exclude("**/${cleanWizardProcessorConfig.presentationModuleName}/**")
    }

    include("**/*.kt")
    includeEmptyDirs = false

    into("${projects.workloads.multiModule.domain.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
    finalizedBy("copyGeneratedUIClasses")
}

tasks.register<Copy>("copyGeneratedUIClasses") {
    from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule") {
        exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
        exclude("**/${cleanWizardProcessorConfig.domainModuleName}/**")
    }
    include("**/*.kt")
    includeEmptyDirs = false

    into("${projects.workloads.multiModule.presentation.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
    finalizedBy("cleanDomainAndPresentationClassesInData")
}

tasks.register<Delete>("cleanDomainAndPresentationClassesInData") {

    val dataDir = file("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
    val fileTree = fileTree(dataDir) {
        exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
        include("**/*.kt")
    }

    delete(fileTree)
    delete("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/${cleanWizardProcessorConfig.domainModuleName}")
    delete("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/${cleanWizardProcessorConfig.presentationModuleName}")
}

tasks.withType<KspTask>().configureEach {
    finalizedBy("copyGeneratedDomainClasses")
}