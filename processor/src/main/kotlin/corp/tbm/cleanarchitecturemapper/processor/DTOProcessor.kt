package corp.tbm.cleanarchitecturemapper.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
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
import corp.tbm.cleanarchitecturemapper.processor.validators.dtoRegex
import kotlinx.serialization.SerialName
import java.io.OutputStreamWriter
import java.util.*

class DtoProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : SymbolProcessor {

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(DTO::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        symbols.forEach { symbol ->

            val dtoProperties = symbol.getAllProperties().toList()

            generateClass(resolver, symbol, "Model", dtoProperties)

            generateClass(resolver, symbol, "DTO", dtoProperties, classBuilder = { packageName, className, properties ->
                if (!symbol.getAnnotationsByType(DTO::class).first().toDomainAsTopLevel) {
                    addSuperinterface(
                        DTOMapper::class.asClassName()
                            .parameterizedBy(
                                ClassName(
                                    packageName.replace("dto", "model"),
                                    className.replace("DTO", "Model")
                                )
                            )
                    )
                    addFunction(
                        FunSpec.builder("toDomain")
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(ClassName(packageName.replace("dto", "model"), className.replace("DTO", "Model")))
                            .addStatement(
                                "return %T(${
                                    properties.map { it.getParameterName(packageName) }.joinToString(", ") {
                                        if (it.endsWith("DTO")
                                        ) "$it.toDomain()" else it
                                    }
                                })",
                                ClassName(packageName.replace("dto", "model"), className.replace("DTO", "Model"))
                            )
                            .build()
                    )
                }
                this
            }, fileSpecBuilder = { packageName, className, properties ->
                val dtoAnnotation = symbol.getAnnotationsByType(DTO::class).first()
                properties.forEach {
                    if (it.isCustomClass && resolver.getClassDeclarationByName(it.type.resolve().declaration.qualifiedName!!)
                            ?.getAnnotationsByType(DTO::class)?.first()?.toDomainAsTopLevel == true
                    )
                        addImport(
                            it.getQualifiedPackageNameBasedOnParameterName(packageName),
                            ".toDomain"
                        )
                }
                if (dtoAnnotation.toDomainAsTopLevel) {
                    addFunction(
                        generateTopLevelMappingFunction(
                            packageName,
                            packageName.replace("dto", "model"),
                            "toDomain",
                            className,
                            className.replace("DTO", "Model"),
                            properties
                        )
                    )
                }
                this
            })

            generateClass(
                resolver,
                symbol,
                "UI",
                dtoProperties,
                fileSpecBuilder = { packageName, className, properties ->
                    properties.forEach {
                        if (it.isCustomClass)
                            addImport(
                                it.getQualifiedPackageNameBasedOnParameterName(packageName),
                                ".toUI"
                            )
                    }
                    addFunction(
                        generateTopLevelMappingFunction(
                            packageName.replace("ui", "model"),
                            packageName,
                            "toUI",
                            className.replace("UI", "Model"),
                            className,
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
                        .joinToString(", ") { if (it.endsWith("Model") || it.endsWith("DTO")) "$it.$functionName()" else it }
                }\n)",
                ClassName(classPackageToMapTo, classToMapTo)
            )
            .build()
    }

    @OptIn(KspExperimental::class)
    private fun generateClass(
        resolver: Resolver,
        symbol: KSClassDeclaration,
        neededSuffix: String,
        properties: List<KSPropertyDeclaration>,
        classBuilder: TypeSpec.Builder.(packageName: String, className: String, properties: List<KSPropertyDeclaration>) -> TypeSpec.Builder = { _, _, _ -> this },
        fileSpecBuilder: FileSpec.Builder.(packageName: String, className: String, properties: List<KSPropertyDeclaration>) -> FileSpec.Builder = { _, _, _ -> this }
    ) {
        var className = ""

        var packageName = ""

        if (resolver.getDeclarationsFromPackage("${
                symbol.packageName.asString().split(".").dropLast(1)
                    .joinToString(".") + "." + symbol.simpleName.asString().replace(dtoRegex, "").replaceFirstChar {
                    it.lowercase(
                        Locale.getDefault()
                    )
                }
            }.${neededSuffix.lowercase()}").toList()
                .none { it.simpleName.asString().endsWith(neededSuffix) }
        ) {
            className = symbol.simpleName.asString().replace(dtoRegex, "") + neededSuffix
            packageName = "${
                symbol.packageName.asString().split(".").dropLast(1)
                    .joinToString(".") + "." + symbol.simpleName.asString().replace(dtoRegex, "").replaceFirstChar {
                    it.lowercase(
                        Locale.getDefault()
                    )
                }
            }.${neededSuffix.lowercase()}"
        }

        val classToBuild = classBuilder(
            TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)

                .primaryConstructor(
                    FunSpec.constructorBuilder().apply {
                        properties.forEach { property ->
                            logger.warn(
                                ClassName.bestGuess(
                                    property.getParameterName(packageName)
                                        .replaceFirstChar { it.uppercase() }).simpleName
                            )
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
                        ).also {
                            it.initializer(
                                property.getParameterName(packageName),
                            )
                            if (symbol.isAnnotationPresent(DTO::class))
                                it.addAnnotation(
                                    AnnotationSpec.builder(SerialName::class)
                                        .addMember("%S", property.name)
                                        .build()
                                )
                        }
                            .build()
                    }), packageName, className, properties
        ).build()

        val fileSpec = fileSpecBuilder(
            FileSpec.builder(packageName, className)
                .addType(classToBuild), packageName, className, properties
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