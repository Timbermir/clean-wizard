import com.google.devtools.ksp.gradle.KspTask
import corp.tbm.cleanwizard.buildLogic.convention.foundation.CleanWizardProcessorConfig

plugins {
    alias(libs.plugins.cleanwizard.workload)
}
dependencies {
    implementation(projects.workloads.multiModule.domain)
}

tasks.register<Copy>("moveGeneratedFiles") {
    dependsOn("kspKotlin")

    rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let { cleanWizardConfig ->
        from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule") {
            exclude("**/${cleanWizardConfig.dataModuleName}/**")
            exclude("**/${cleanWizardConfig.presentationModuleName}/**")
        }

        include("**/*.kt")
        includeEmptyDirs = false

        into("${projects.workloads.multiModule.domain.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
    }
    finalizedBy("copyGeneratedUIClassesToPresentation")
}

tasks.register<Copy>("copyGeneratedUIClassesToPresentation") {
    dependsOn("moveGeneratedFiles")
    rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let { cleanWizardProcessorConfig ->
        from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule") {
            exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
            exclude("**/${cleanWizardProcessorConfig.domainModuleName}/**")

            include("**/*.kt")
            includeEmptyDirs = false

        }

        into("${projects.workloads.multiModule.presentation.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
    }
    finalizedBy("cleanDomainAndPresentationClassesInData")
}

tasks.register<Delete>("cleanDomainAndPresentationClassesInData") {
    rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let { config ->
        val dataDir = file("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule")
        val fileTree = fileTree(dataDir) {
            this.exclude()
            exclude("**/${config.dataModuleName}/**")
            include("**/*.kt")
        }
        delete(fileTree)
    }
    delete("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/domain")
    delete("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/presentation")
}

tasks.withType<KspTask>().configureEach {
    finalizedBy("moveGeneratedFiles")
}

tasks.named("compileKotlin") {
    dependsOn("moveGeneratedFiles")
}