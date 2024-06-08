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
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks.name
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.EnumType

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
        val enumClass = TypeSpec.enumBuilder(property.name)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(property.name, property.type.toTypeName())
                    .build()
            )
            .addProperty(
                PropertySpec.builder(property.name, property.type.toTypeName())
                    .initializer(property.name)
                    .build()
            )
            .apply {
                val annotationArguments =
                    property.annotations.find { it.shortName.asString().contains("Enum") }?.arguments
                generateAppropriateEnumBuilder(
                    EnumType.entries.first {
                        it.name == property.annotations.firstNotNullOf { annotation ->
                            annotation.shortName.asString().substringBefore("Enum").uppercase()
                        }
                    }.also {
                        logger.warn(it.toString())
                    },
                    Triple(
                        annotationArguments?.first()?.value as ArrayList<String>,
                        annotationArguments[1].value.toString(),
                        annotationArguments[2].value as ArrayList<Any>
                    ), this
                )
            }.build()
        logger.warn(property.packageName.asString())

        val fileSpec = FileSpec.builder(
            packageName,
            property.name
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
                        }, enumValue
                    ).build()
            )
        }
    }

    override fun defaultHandler(node: KSNode, data: String) {
    }
}