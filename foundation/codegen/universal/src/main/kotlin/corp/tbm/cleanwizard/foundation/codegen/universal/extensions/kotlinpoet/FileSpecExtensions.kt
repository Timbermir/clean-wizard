package corp.tbm.cleanwizard.foundation.codegen.universal.extensions.kotlinpoet

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.FileSpec
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardImport
import java.io.OutputStreamWriter

fun FileSpec.writeNewFile(codeGenerator: CodeGenerator, dependencies: Dependencies = Dependencies.ALL_FILES) {
    val file = codeGenerator.createNewFile(dependencies, this.packageName, this.name)
    OutputStreamWriter(file).use { writer ->
        writeTo(writer)
    }
}

fun FileSpec.Builder.addImport(cleanWizardImport: CleanWizardImport) {
    addImport(cleanWizardImport.packageName, cleanWizardImport.name)
}