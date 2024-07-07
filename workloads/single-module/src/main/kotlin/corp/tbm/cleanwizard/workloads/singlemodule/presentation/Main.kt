package corp.tbm.cleanwizard.workloads.singlemodule.presentation

import java.io.File

fun main() {
    findBasePackageInDir(File("corp..tbm.cleanwizard.workloads.singlemodule.data"))
    println(camelToSnakeCase("IWantToEat"))

}

fun camelToSnakeCase(name: String): String {
    return name.split("(?=[A-Z])".toRegex())
        .joinToString("_") { it.lowercase() }
}

private var lastPackageSegmentWhereFirstSourceClassOccurs = ""

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