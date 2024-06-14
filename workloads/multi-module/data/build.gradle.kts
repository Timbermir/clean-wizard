import com.google.devtools.ksp.gradle.KspTaskJvm
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.cleanWizardProcessorConfig
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.kspMainBuildDirectory

plugins {
    alias(libs.plugins.cleanwizard.workload)
}

dependencies {
    implementation(projects.workloads.multiModule.domain)
}

private var lastPackageSegmentWhereFirstSourceClassOccurs = ""

tasks.withType<KspTaskJvm>().configureEach {
    finalizedBy("findLastPackageSegmentWhereFirstSourceClassOccurs")
}

tasks.register("findLastPackageSegmentWhereFirstSourceClassOccurs") {
    dependsOn("kspKotlin")

    sourceSets.main {
        allSource.srcDirs.forEach { dir ->
            findBasePackageInDir(dir)
        }
    }

    finalizedBy("copyGeneratedDomainClasses")
}

tasks.register<Copy>("copyGeneratedDomainClasses") {

    copyToModule("findLastPackageSegmentWhereFirstSourceClassOccurs", {
        exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
        exclude("**/${cleanWizardProcessorConfig.presentationModuleName}/**")
    }, projects.workloads.multiModule.domain.dependencyProject, "copyGeneratedUIClasses")
}

tasks.register<Copy>("copyGeneratedUIClasses") {
    copyToModule("copyGeneratedDomainClasses", {
        exclude("**/${cleanWizardProcessorConfig.dataModuleName}/**")
        exclude("**/${cleanWizardProcessorConfig.domainModuleName}/**")
    }, projects.workloads.multiModule.presentation.dependencyProject, "cleanDomainAndPresentationClassesInData")
}

private fun Copy.copyToModule(
    taskToDependOn: String,
    fromCopySpec: CopySpec.() -> Unit,
    pathToCopyInto: Project,
    taskToFinalizeBy: String
) {
    dependsOn(taskToDependOn)

    from(kspMainBuildDirectory) {
        fromCopySpec()
    }

    include("**/*.kt")
    includeEmptyDirs = false

    into(pathToCopyInto.kspMainBuildDirectory)

    finalizedBy(taskToFinalizeBy)
}

tasks.register<Delete>("cleanDomainAndPresentationClassesInData") {
    dependsOn("copyGeneratedUIClasses")

    val basePackage = File(
        kspMainBuildDirectory, lastPackageSegmentWhereFirstSourceClassOccurs.split(".").joinToString("/")
            .replace(cleanWizardProcessorConfig.dataModuleName, "")
    ).path

    delete(File(basePackage, "/${cleanWizardProcessorConfig.domainModuleName}"))
    delete(File(basePackage, "/${cleanWizardProcessorConfig.presentationModuleName}"))
}

private fun findBasePackageInDir(dir: File) {

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
        lastPackageSegmentWhereFirstSourceClassOccurs =
            if (basePackagePath.startsWith(File.separator)) basePackagePath.substring(1) else basePackagePath
    }
}