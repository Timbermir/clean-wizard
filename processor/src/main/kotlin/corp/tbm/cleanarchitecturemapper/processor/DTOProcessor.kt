package corp.tbm.cleanarchitecturemapper.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toTypeName
import corp.tbm.cleanarchitecturemapper.processor.annotations.DTO
import corp.tbm.cleanarchitecturemapper.processor.extensions.getParameterName
import corp.tbm.cleanarchitecturemapper.processor.extensions.getQualifiedPackageNameBasedOnParameterName
import corp.tbm.cleanarchitecturemapper.processor.extensions.isCustomClass
import corp.tbm.cleanarchitecturemapper.processor.extensions.name
import corp.tbm.cleanarchitecturemapper.processor.mapper.DTOMapper
import kotlinx.serialization.SerialName
import java.io.OutputStreamWriter
import java.util.*

class DtoProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(DTO::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        symbols.forEach { symbol ->

            val baseName = symbol.simpleName.asString().removeSuffix("DTOSchema")
            val basePackage = symbol.packageName.asString().split(".").dropLast(1)
                .joinToString(".") + "." + baseName.replaceFirstChar {
                it.lowercase(
                    Locale.getDefault()
                )
            }

            fun generatePackage(suffix: String) = "${basePackage}.$suffix"

            fun generateClassName(suffix: String) = "${baseName}$suffix"

            val dtoPackage = generatePackage("dto")
            val modelPackage = generatePackage("model")
            val uiPackage = generatePackage("ui")

            val dtoClassName = generateClassName("DTO")
            val modelClassName = generateClassName("Model")
            val uiClassName = generateClassName("UI")

            val dtoProperties = symbol.getAllProperties().toList()

            generateClass(modelPackage, modelClassName, dtoProperties)

            generateClass(dtoPackage, dtoClassName, dtoProperties, classBuilder = {
                addSuperinterface(
                    DTOMapper::class.asClassName()
                        .parameterizedBy(ClassName(modelPackage, modelClassName))
                )
                addFunction(
                    FunSpec.builder("toDomain")
                        .addModifiers(KModifier.OVERRIDE)
                        .returns(ClassName(modelPackage, modelClassName))
                        .addStatement(
                            "return %T(${
                                dtoProperties.map { it.getParameterName(dtoPackage) }.joinToString(", ") {
                                    if (it.endsWith("DTO")
                                    ) "$it.toDomain()" else it
                                }
                            })",
                            ClassName(modelPackage, modelClassName)
                        )
                        .build()
                )
            })


            generateClass(uiPackage, uiClassName, dtoProperties, fileSpecBuilder = { properties ->
                properties.forEach {
                    if (it.isCustomClass)
                        addImport(
                            it.getQualifiedPackageNameBasedOnParameterName(uiPackage),
                            ".toUI"
                        )
                }
                addFunction(
                    generateTopLevelMappingFunction(
                        modelPackage,
                        uiPackage,
                        "toUI",
                        modelClassName,
                        uiClassName,
                        properties
                    )
                )
            })
        }

        return emptyList()
    }

    private fun generateTopLevelMappingFunction(
        classPackageToMapFrom: String,
        classPackageToMapTo: String,
        functionName: String,
        classToMapFrom: String,
        classToMapTo: String,
        classProperties: List<KSPropertyDeclaration>
    ): FunSpec {
        return FunSpec.builder(functionName)
            .receiver(ClassName(classPackageToMapFrom, classToMapFrom))
            .returns(ClassName(classPackageToMapTo, classToMapTo))
            .addStatement(
                "return %T(${
                    classProperties.map { it.getParameterName(classPackageToMapFrom) }
                        .joinToString(", ") { if (it.endsWith("Model")) "$it.toUI()" else it }
                }\n)",
                ClassName(classPackageToMapTo, classToMapTo)
            )
            .build()
    }

    private fun generateClass(
        packageName: String,
        className: String,
        properties: List<KSPropertyDeclaration>,
        classBuilder: TypeSpec.Builder.() -> TypeSpec.Builder = { this },
        fileSpecBuilder: FileSpec.Builder.(properties: List<KSPropertyDeclaration>) -> FileSpec.Builder = { this }
    ) {
        val classToBuild = classBuilder(
            TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder().apply {
                        properties.forEach { property ->
                            addParameter(
                                property.getParameterName(packageName),
                                if (property.isCustomClass) ClassName(
                                    property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                    property.getParameterName(packageName).replaceFirstChar { it.uppercase() }
                                )
                                else property.type.toTypeName()
                            )
                        }
                    }.build()
                )
                .addProperties(
                    properties.map { property ->

                        PropertySpec.builder(
                            property.getParameterName(packageName),
                            if (property.isCustomClass) ClassName(
                                property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                property.getParameterName(packageName).replaceFirstChar { it.uppercase() }
                            )
                            else property.type.toTypeName()
                        )
                            .initializer(
                                property.getParameterName(packageName),
                            )

                            .addAnnotation(
                                AnnotationSpec.builder(SerialName::class)
                                    .addMember("%S", property.name)
                                    .build()
                            )
                            .build()
                    })
        ).build()

        val fileSpec = fileSpecBuilder(
            FileSpec.builder(packageName, className)
                .addType(classToBuild), properties
        ).build()

        val file = codeGenerator.createNewFile(
            Dependencies.ALL_FILES,
            packageName,
            className
        )

        OutputStreamWriter(file).use { writer ->
            fileSpec.writeTo(writer)
        }
    }
}

class DtoProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DtoProcessor(environment.codeGenerator, environment.logger)
    }
}