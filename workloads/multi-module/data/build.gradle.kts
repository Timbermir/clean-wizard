import com.google.devtools.ksp.gradle.KspTaskJvm
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardProcessorConfig
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.kspMainBuildDirectory
var defaultPackagePath = ""
plugins {
    alias(libs.plugins.cleanwizard.workload)
}

dependencies {
    implementation(projects.workloads.multiModule.domain)
}

tasks.withType<KspTaskJvm>().configureEach {
    finalizedBy("findLastPackageSegmentWhereFirstSourceClassOccurs")
}

tasks.register("findLastPackageSegmentWhereFirstSourceClassOccurs") {
    dependsOn("kspKotlin")
    mustRunAfter("kspKotlin")
    sourceSets.main {
        allSource.srcDirs.forEach { dir ->
            findBasePackageInDir(dir)
        }
    }
    finalizedBy("copyGeneratedDomainClasses")
}

tasks.register<Copy>("copyGeneratedDomainClasses") {

    mustRunAfter("kspKotlin","findLastPackageSegmentWhereFirstSourceClassOccurs")

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
    mustRunAfter("copyGeneratedDomainClasses")
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
    mustRunAfter("copyGeneratedUIClasses")

    doFirst {

        val dataDir = file(kspMainBuildDirectory)
        val fileTree = fileTree(dataDir) {
            exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
            include("**/*.kt")
        }

        val basePackage =
            "$kspMainBuildDirectory/${
                defaultPackagePath.split(".").joinToString("/")
                    .replace(cleanWizardProcessorConfig.dataModuleName, "")
            }"

        delete(fileTree)
        delete("$basePackage/${cleanWizardProcessorConfig.domainModuleName}")
        delete("$basePackage/${cleanWizardProcessorConfig.presentationModuleName}")

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
        defaultPackagePath =
            if (basePackagePath.startsWith(File.separator)) basePackagePath.substring(1) else basePackagePath
        cleanWizardProcessorConfig.basePackagePath =
            if (basePackagePath.startsWith(File.separator)) basePackagePath.substring(1) else basePackagePath
    }
}