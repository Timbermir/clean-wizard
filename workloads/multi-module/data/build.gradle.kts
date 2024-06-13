import com.google.devtools.ksp.gradle.KspTask
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardProcessorConfig
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.kspMainBuildDirectory

plugins {
    alias(libs.plugins.cleanwizard.workload)
}

dependencies {
    implementation(projects.workloads.multiModule.domain)
}

tasks.withType<KspTask>().configureEach {
    finalizedBy("copyGeneratedDomainClasses")
}

tasks.register<Copy>("copyGeneratedDomainClasses") {

    from(kspMainBuildDirectory) {
        exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
        exclude("**/${cleanWizardProcessorConfig.presentationModuleName}/**")
    }

    include("**/*.kt")
    includeEmptyDirs = false

    into(projects.workloads.multiModule.domain.dependencyProject.kspMainBuildDirectory)
    finalizedBy("copyGeneratedUIClasses")
}

tasks.register<Copy>("copyGeneratedUIClasses") {
    from(kspMainBuildDirectory) {
        exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
        exclude("**/${cleanWizardProcessorConfig.domainModuleName}/**")
    }
    include("**/*.kt")
    includeEmptyDirs = false

    into(projects.workloads.multiModule.presentation.dependencyProject.kspMainBuildDirectory)
    finalizedBy("cleanDomainAndPresentationClassesInData")
}

tasks.register<Delete>("cleanDomainAndPresentationClassesInData") {

    val dataDir = file(kspMainBuildDirectory)
    val fileTree = fileTree(dataDir) {
        exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
        include("**/*.kt")
    }

    delete(fileTree)
    delete("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/${cleanWizardProcessorConfig.domainModuleName}")
    delete("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/${cleanWizardProcessorConfig.presentationModuleName}")
    finalizedBy("findBasePackage")
}

tasks.register("findBasePackage") {
    doLast {
        sourceSets["main"].allSource.srcDirs.forEach { dir ->
            println("Searching in source directory: $dir")
            findBasePackageInDir(dir)
        }
    }
}

fun findBasePackageInDir(dir: File) {

    val findSubdirectories: (File) -> List<File> = { baseDir ->
        baseDir.listFiles()?.filter { it.isDirectory } ?: emptyList()
    }

    val findClassFiles: (File) -> List<File> = { baseDir ->
        baseDir.listFiles()?.filter { it.name.endsWith(".kt") || it.name.endsWith(".java") } ?: emptyList()
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
        cleanWizardProcessorConfig.basePackagePath =
            if (basePackagePath.startsWith(File.separator)) basePackagePath.substring(1) else basePackagePath
    }
}