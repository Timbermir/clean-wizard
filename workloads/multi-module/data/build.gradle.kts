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
    val oldPackageName = "corp/tbm/cleanwizard/workloads/multimodule/`data`"
    val newDomainPackageName = "corp/tbm/cleanwizard/workloads/multimodule/domain"

    rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let { cleanWizardConfig ->
        from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/data/") {
            exclude("**/${cleanWizardConfig.dtoClassPackageName}/**")
            exclude("**/${cleanWizardConfig.uiClassPackageName}/**")
        }

        eachFile {
            path = if (path.contains("enums")) path.replaceBefore("enums", "") else path.replaceBefore(this.name, "")
        }

        include("**/*.kt")
        includeEmptyDirs = false

        this.filter { line ->
            when {
                line.startsWith("package") -> {
                    val newPackageLine = line.replace(
                        oldPackageName.replace('/', '.'),
                        newDomainPackageName.replace('/', '.')
                    ).split(".").dropLastWhile { it != cleanWizardConfig.domainModuleName }
                        .joinToString(".")

                    when (line.endsWith("enums")) {
                        true ->
                            "$newPackageLine.enums"

                        false ->
                            newPackageLine
                    }
                }

                line.startsWith("import") && line.contains(cleanWizardConfig.dtoClassSuffix) || line.contains(
                    cleanWizardConfig.dtoClassPackageName
                ) -> {
                    ""
                }

                else -> line
            }
        }

        into("${projects.workloads.multiModule.domain.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/domain/")
    }
    finalizedBy("copyGeneratedUIClassesToPresentation")
}

fun changePackageName(line: String) {

}

tasks.register<Copy>("copyGeneratedUIClassesToPresentation") {
    dependsOn("moveGeneratedFiles")
    rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let { cleanWizardProcessorConfig ->
        from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/data/") {
            exclude("**/${cleanWizardProcessorConfig.dtoClassPackageName}/**")
            exclude("**/${cleanWizardProcessorConfig.domainClassPackageName}/**")

            eachFile {
                path = path.replaceBefore(this.name, "")
            }

            include("**/*.kt")
            includeEmptyDirs = false

            val oldPackageName = "corp/tbm/cleanwizard/workloads/multimodule/`data`"
            val newDomainPackageName = "corp/tbm/cleanwizard/workloads/multimodule/presentation"
            this.filter { line ->
                when {
                    line.startsWith("package") -> {
                        line.replace(
                            oldPackageName.replace('/', '.'),
                            newDomainPackageName.replace('/', '.')
                        ).split(".").dropLastWhile { it != cleanWizardProcessorConfig.presentationModuleName }
                            .joinToString(".")
                    }

                    line.startsWith("import") && line.contains(cleanWizardProcessorConfig.dtoClassSuffix) || line.contains(
                        cleanWizardProcessorConfig.dtoClassPackageName
                    ) -> {
                        ""
                    }

                    else -> line
                }
            }
        }

        into("${projects.workloads.multiModule.presentation.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/presentation/")
    }
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
        fileTree(dataDir) {
            this.files.forEach {
            }
        }
    }
}

//tasks.register<Copy>("plainCopy") {
//    dependsOn("cleanDomainAndPresentationClassesInData")
//    from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/data/") {
//
//        duplicatesStrategy = DuplicatesStrategy.INCLUDE
//        eachFile {
//            path = path.replaceBefore(this.name, "")
//        }
//
//        include("**/*.kt")
//        includeEmptyDirs = false
//    }
//    into("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/domain/")
//}

tasks.withType<KspTask>().configureEach {
    finalizedBy("moveGeneratedFiles")
}

tasks.named("compileKotlin") {
    dependsOn("moveGeneratedFiles")
}