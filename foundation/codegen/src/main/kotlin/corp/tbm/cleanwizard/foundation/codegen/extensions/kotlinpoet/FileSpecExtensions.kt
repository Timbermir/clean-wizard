package corp.tbm.cleanwizard.foundation.codegen.universal.extensions.kotlinpoet

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardImport

fun FileSpec.writeNewFile(codeGenerator: CodeGenerator, dependencies: Dependencies = Dependencies.ALL_FILES) {
    writeTo(codeGenerator, dependencies)
}

fun FileSpec.Builder.addImport(cleanWizardImport: CleanWizardImport) {
    addImport(cleanWizardImport.packageName, cleanWizardImport.name)
}