import com.google.devtools.ksp.gradle.KspTask
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.CleanWizardProcessorConfig

plugins {
    alias(libs.plugins.cleanarchitecturemapper.workload)
}
dependencies {
    implementation(projects.workloads.multiModule.domain)
}

tasks.register<Copy>("moveGeneratedFiles") {
    dependsOn("kspKotlin")

    from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/data/") {
        rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let {
            exclude("**/${it.dtoClassPackageName}/**")
            exclude("**/${it.uiClassPackageName}/**")
        }

        eachFile {
            path = path.replaceBefore(this.name, "")
        }

        include("**/*.kt")
        includeEmptyDirs = false
    }

    into("${projects.workloads.multiModule.domain.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/domain/")

    finalizedBy("copyGeneratedUIClassesToPresentation")
}

tasks.register<Copy>("copyGeneratedUIClassesToPresentation") {
    dependsOn("moveGeneratedFiles")
    from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/data/") {
        rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let {
            exclude("**/${it.dtoClassPackageName}/**")
            exclude("**/${it.domainClassPackageName}/**")
        }

        eachFile {
            path = path.replaceBefore(this.name, "")
        }

        include("**/*.kt")
        includeEmptyDirs = false
    }

    into("${projects.workloads.multiModule.presentation.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/presentation/")

    finalizedBy("cleanDomainAndPresentationClassesInData")
}

tasks.register<Delete>("cleanDomainAndPresentationClassesInData") {
    rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let { config ->
        val dataDir = file("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/data/")

        val fileTree = fileTree(dataDir) {
            exclude("**/${config.dtoClassPackageName}/**")
            include("**/*.kt")
        }
        delete(fileTree)
    }
    finalizedBy("plainCopy")
}
tasks.register<Copy>("plainCopy") {
    dependsOn("cleanDomainAndPresentationClassesInData")
    from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/data/") {

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        eachFile {
            path = path.replaceBefore(this.name, "")
            println(path)
        }

        include("**/*.kt")
        includeEmptyDirs = false
    }
    into("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/domain/")
}

tasks.withType<KspTask>().configureEach {
    finalizedBy("moveGeneratedFiles")
}

tasks.named("compileKotlin") {
    dependsOn("moveGeneratedFiles")
}