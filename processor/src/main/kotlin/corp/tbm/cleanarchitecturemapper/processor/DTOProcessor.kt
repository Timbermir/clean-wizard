package corp.tbm.cleanarchitecturemapper.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import corp.tbm.cleanarchitecturemapper.processor.annotations.DTO
import corp.tbm.cleanarchitecturemapper.processor.mapper.DTOMapper
import java.io.OutputStreamWriter

class Debill
class DtoProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(DTO::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        symbols.forEach { symbol ->
            val dtoClassName = symbol.simpleName.asString()
            val packageName = symbol.packageName.asString()

            val domainClassName = dtoClassName.replace("DTOSchema", "Model")
            val uiClassName = dtoClassName.removeSuffix("DTOSchema") + "UI"

            val dtoProperties = symbol.getAllProperties().map { property ->
                property.simpleName.asString() to property.type.resolve().declaration.simpleName.asString()
            }.toList()

            generateClass(packageName, domainClassName, dtoProperties)

            generateClass(packageName, dtoClassName.replace("DTOSchema", "DTO"), dtoProperties, classBuilder = {
                addSuperinterface(DTOMapper::class.parameterizedBy(defaultClassBuilder(domainClassName,dtoProperties)))
                    .addFunction(
                        FunSpec.builder("toDomain")
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(ClassName(packageName, domainClassName))
                            .addStatement(
                                "return %T(${dtoProperties.joinToString(", ") { it.first }})",
                                ClassName(packageName, domainClassName)
                            )
                            .build()
                    )
            })
            generateClass(packageName, uiClassName, dtoProperties, fileSpecBuilder = { properties ->
                generateTopLevelMappingFunction(
                    packageName,
                    "toUI",
                    uiClassName.replaceAfter("UI", "Model"),
                    uiClassName,
                    properties
                )
                this
            })
        }

        return emptyList()
    }

    private fun generateTopLevelMappingFunction(
        packageName: String,
        functionName: String,
        classToMapFrom: String,
        classToMapTo: String,
        classProperties: List<Pair<String, String>>
    ): FunSpec {
        return FunSpec.builder(functionName)
            .receiver(ClassName(packageName, classToMapFrom))
            .returns(ClassName(packageName, classToMapTo))
            .addStatement(
                "return %T(${classProperties.joinToString(", ") { it.first }})",
                ClassName(packageName, classToMapTo)
            )
            .build()
    }

    private fun generateClass(
        packageName: String,
        className: String,
        properties: List<Pair<String, String>>,
        classBuilder: TypeSpec.Builder.() -> TypeSpec.Builder = { this },
        fileSpecBuilder: FileSpec.Builder.(properties: List<Pair<String, String>>) -> FileSpec.Builder = { this }
    ) {

        val classToBuild = classBuilder(TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder().apply {
                    properties.forEach { (name, type) ->
                        addParameter(name, getType(type))
                    }
                }.build()
            )
            .addProperties(
                properties.map { (name, type) ->
                    PropertySpec.builder(name, getType(type))
                        .initializer(name)
                        .build()
                }
            )).build()

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

    private fun defaultClassBuilder(className: String, properties: List<Pair<String, String>>) = TypeSpec.classBuilder(className)
        .addModifiers(KModifier.DATA)
        .primaryConstructor(
            FunSpec.constructorBuilder().apply {
                properties.forEach { (name, type) ->
                    addParameter(name, getType(type))
                }
            }.build()
        )
        .addProperties(
            properties.map { (name, type) ->
                PropertySpec.builder(name, getType(type))
                    .initializer(name)
                    .build()
            }
        )

    private fun getType(type: String): TypeName {
        return when (type) {
            "Int" -> INT
            "String" -> STRING
            "Boolean" -> BOOLEAN
            "Double" -> DOUBLE
            else -> ClassName("", type)
        }
    }
}

class DtoProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DtoProcessor(environment.codeGenerator, environment.logger)
    }
}

interface Debil {

}