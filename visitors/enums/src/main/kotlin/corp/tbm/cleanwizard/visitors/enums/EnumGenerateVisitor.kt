package corp.tbm.cleanarchitecturemapper.visitors.enums

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo
import corp.tbm.cleanwizard.foundation.codegen.universal.EnumType
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharUppercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.name
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.takeIfNotEmptyOrReturnDefault

class EnumGenerateVisitor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : KSDefaultVisitor<String, Unit>() {

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: String) {
        generateEnumClassForProperty(property, data)
    }

    @Suppress("UNCHECKED_CAST")
    private fun generateEnumClassForProperty(
        property: KSPropertyDeclaration,
        packageName: String,
    ) {
        val enumAnnotation = property.annotations.first { it.shortName.asString().endsWith("Enum") }

        val findAnnotationArgument: (nameToCompare: String) -> Any? = { nameToCompareTo ->
            enumAnnotation.arguments.first { it.name?.asString() == nameToCompareTo }.value
        }

        val enumType =
            EnumType.entries.first { enumType ->
                enumType.name == property.annotations.first { annotation ->
                    annotation.name.endsWith("Enum")
                }.name.substringBefore("Enum").uppercase()
            }

        enumType.enumName =
            findAnnotationArgument("enumName").toString() takeIfNotEmptyOrReturnDefault property.name.firstCharUppercase()

        enumType.enumEntries =
            findAnnotationArgument("enumEntries") as ArrayList<String>

        enumType.parameterName =
            findAnnotationArgument("parameterName").toString() takeIfNotEmptyOrReturnDefault property.name

        enumType.enumEntryValues = findAnnotationArgument("enumEntryValues") as ArrayList<Any>
        val enumClass = with(enumType) {
            TypeSpec.enumBuilder(enumName)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(parameterName, enumEntryValues.first()::class)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(parameterName, enumEntryValues.first()::class)
                        .initializer(parameterName)
                        .build()
                )
                .apply {

                    generateAppropriateEnumBuilder(
                        enumType,
                        Triple(
                            enumEntries.map { it.uppercase() },
                            parameterName,
                            enumEntryValues
                        ), this
                    )
                }.build()
        }

        val fileSpec = FileSpec.builder(
            packageName,
            enumType.enumName
        ).apply {
            addType(enumClass)
        }.build()

        fileSpec.writeTo(codeGenerator, true)
    }

    private fun generateAppropriateEnumBuilder(
        enumType: EnumType,
        arguments: Triple<List<String>, String, List<Any>>,
        enumClassBuilder: TypeSpec.Builder
    ) {
        arguments.first.zip(arguments.third).forEach { (enumEntry, enumValue) ->
            enumClassBuilder.addEnumConstant(
                enumEntry, TypeSpec.anonymousClassBuilder()
                    .addSuperclassConstructorParameter(
                        when (enumType) {
                            EnumType.STRING -> "%S"
                            else -> "%L"
                        }, "${arguments.second} = $enumValue${enumType.parameterValueSuffix.toString().trim()}"
                    ).build()
            )
        }
    }

    override fun defaultHandler(node: KSNode, data: String) {
    }
}