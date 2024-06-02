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
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.EnumType
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks.name
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.takeIfNotEmptyOrReturnDefault

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
        val enumAnnotation = property.annotations.first { it.shortName.asString().contains("Enum") }

        val findAnnotationArgument: (nameToCompare: String) -> Any? = { nameToCompareTo ->
            enumAnnotation.arguments.first { it.name?.asString() == nameToCompareTo }.value
        }

        val enumType =
            EnumType.entries.first {
                it.name == property.annotations.firstNotNullOf { annotation ->
                    annotation.shortName.asString().substringBefore("Enum").uppercase()
                }
            }

        enumType.enumName = findAnnotationArgument("enumName").toString() takeIfNotEmptyOrReturnDefault property.name

        enumType.enumEntries =
            findAnnotationArgument("enumEntries") as ArrayList<String>

        enumType.parameterName =
            findAnnotationArgument("parameterName").toString() takeIfNotEmptyOrReturnDefault property.name

        enumType.enumEntryValues = findAnnotationArgument("enumEntryValues") as ArrayList<Any>

        val enumClass = TypeSpec.enumBuilder(enumType.enumName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(enumType.parameterName, property.type.toTypeName())
                    .build()
            )
            .addProperty(
                PropertySpec.builder(enumType.parameterName, property.type.toTypeName())
                    .initializer(enumType.parameterName)
                    .build()
            )
            .apply {

                generateAppropriateEnumBuilder(
                    enumType,
                    Triple(
                        enumType.enumEntries,
                        enumType.parameterName,
                        enumType.enumEntryValues
                    ), this
                )
            }.build()
        logger.warn(property.packageName.asString())

        val fileSpec = FileSpec.builder(
            packageName,
            enumType.enumName
        ).apply {
            addType(enumClass)
        }.build()

        fileSpec.writeTo(codeGenerator, false)
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