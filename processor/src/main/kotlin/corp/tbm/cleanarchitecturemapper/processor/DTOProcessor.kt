package corp.tbm.cleanarchitecturemapper.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toKModifier
import corp.tbm.cleanarchitecturemapper.foundation.annotations.DTO
import corp.tbm.cleanarchitecturemapper.foundation.codegen.kotlinpoet.allowedDataClassPropertiesModifiers
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.DTOMapper
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.dtoRegex
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.firstCharLowercase
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.getAnnotatedSymbols
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks.*
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.log
import corp.tbm.cleanarchitecturemapper.visitors.enums.EnumGenerateVisitor
import kotlinx.serialization.SerialName
import java.io.OutputStreamWriter

const val PARAMETER_SEPARATOR = ", \n"
const val PARAMETER_PREFIX = "\n"

class DTOProcessor(private val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {

    private val statementListFormatMapping: (functionName: String, packageName: String, properties: List<KSPropertyDeclaration>) -> String =
        { mappingFunctionName, packageName, properties ->
            "return %T(${
                properties.map { it }
                    .joinToString(
                        separator = "$PARAMETER_SEPARATOR    ",
                        prefix = "$PARAMETER_PREFIX    "
                    )
                    { currentProperty ->
                        val filteredProperties = properties.filter { it.name == currentProperty.name }
                        when {

                            filteredProperties.any { it.type.resolve().isClassMappable } -> {
                                "${currentProperty.name}.$mappingFunctionName()"
                            }

                            filteredProperties.any { it.type.resolve().isListMappable } -> {
                                "${currentProperty.name}.map { ${
                                    currentProperty.getParameterName(
                                        packageName
                                    ).firstCharLowercase()
                                } -> ${
                                    currentProperty.getParameterName(packageName).firstCharLowercase()
                                }.$mappingFunctionName() }"
                            }

                            else -> currentProperty.name
                        }
                    }
            }\n)"
        }

    private val enumGenerateVisitor = EnumGenerateVisitor(codeGenerator, logger)

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val symbols = resolver.getAnnotatedSymbols<KSClassDeclaration>(DTO::class.qualifiedName!!)

        symbols.forEach { symbol ->

            val dtoProperties = symbol.getAllProperties().toList()
            generateClass(resolver, symbol, "Model", dtoProperties)

            generateClass(
                resolver,
                symbol,
                "DTO",
                dtoProperties,
                classBuilder = { packageName, className, properties ->
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
                                .returns(
                                    ClassName(
                                        packageName.replace("dto", "model"),
                                        className.replace("DTO", "Model")
                                    )
                                )
                                .addStatement(
                                    "return %T(${
                                        properties.map { it.name }
                                            .joinToString(
                                                separator = PARAMETER_SEPARATOR,
                                                prefix = PARAMETER_PREFIX
                                            ) { parameter ->
                                                if (properties.filter { it.name == parameter }
                                                        .any { it.type.resolve().isMappable })
                                                    "$parameter.toDomain()" else parameter
                                            }
                                    }\n)",
                                    ClassName(packageName.replace("dto", "model"), className.replace("DTO", "Model"))
                                )
                                .build()
                        )
                    }
                    this
                },
                fileSpecBuilder = { packageName, className, properties ->
                    val dtoAnnotation = symbol.getAnnotationsByType(DTO::class).first()
                    when {
                        !dtoAnnotation.toDomainAsTopLevel -> {
                            addImport(
                                "corp.tbm.cleanarchitecturemapper.foundation.codegen.universal",
                                ".toDomain"
                            )
                        }

                        dtoAnnotation.toDomainAsTopLevel -> {
                            val mappingFunctionName = "toDomain"
                            addFunction(
                                generateTopLevelMappingFunctions(
                                    mappingFunctionName,
                                    properties,
                                    ClassName(
                                        packageName,
                                        className
                                    ),
                                    ClassName(
                                        packageName.replace("dto", "model"),
                                        className.replace("DTO", "Model")
                                    ),
                                    packageName,
                                    statementFormat = statementListFormatMapping(
                                        mappingFunctionName,
                                        packageName,
                                        properties
                                    )
                                )
                            )
                        }
                    }
                    properties.forEach { property ->
                        when {

                            dtoAnnotation.toDomainAsTopLevel -> {
                                when {
                                    property.type.resolve().isClassMappable -> {
                                        addImport(
                                            property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                            ".toDomain"
                                        )
                                    }

                                    property.type.resolve().isListMappable -> {
                                        addImport(
                                            property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                            ".toDomain"
                                        )
                                    }
                                }
                            }
                        }
                    }
                    this
                })

            generateClass(
                resolver,
                symbol,
                "UI",
                dtoProperties,
                fileSpecBuilder = { packageName, className, properties ->
                    val mappingFunctionName = "toUI"
                    addFunction(
                        generateTopLevelMappingFunctions(
                            mappingFunctionName, properties, ClassName(
                                packageName.replace("ui", "model"),
                                className.replace("UI", "Model")
                            ),
                            ClassName(packageName, className),
                            packageName.replace("ui", "model"),
                            statementFormat = statementListFormatMapping(mappingFunctionName, packageName, properties)
                        )
                    )
                    properties.forEach { property ->

                        if (property.type.resolve().isMappable)
                            addImport(
                                property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                ".toUI"
                            )
                    }
                    this
                })
        }

        return symbols.filter { !it.validate() }
    }

    private fun generateTopLevelMappingFunctions(
        functionName: String,
        properties: List<KSPropertyDeclaration>,
        receiver: TypeName,
        returns: TypeName,
        receiverPackageName: String,
        statementFormat: String = "return %T(${
            properties.map { it.name }
                .joinToString(
                    separator = "$PARAMETER_SEPARATOR    ",
                    prefix = "$PARAMETER_PREFIX    "
                )
                { propertyName ->
                    if (properties.filter { it.name == propertyName }
                            .any { it.type.resolve().isMappable })
                        "$propertyName.$functionName()" else propertyName
                }
        }\n)",
        statementArgs: Any = returns,
    ): FunSpec {
        return FunSpec.builder(functionName)
            .receiver(receiver)
            .returns(returns)
            .addStatement(
                statementFormat, statementArgs
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

        val className = symbol.simpleName.asString().replace(dtoRegex, "") + neededSuffix
        val packageName = "${
            symbol.packageName.asString().split(".").dropLast(1)
                .joinToString(".") + "." + symbol.simpleName.asString().replace(dtoRegex, "").firstCharLowercase()
        }.${neededSuffix.lowercase()}"
        properties.forEach {
            logger.log(it.annotations.toList().toString())
        }

        if (neededSuffix == "Model")
            properties.forEach { property ->
                if (property.annotations.filter { it.shortName.asString().contains("Enum") }.toList()
                        .isNotEmpty()
                ) {
                    property.accept(enumGenerateVisitor, "$packageName.enums")
                }
            }

        val classToBuild = classBuilder(
            TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)

                .primaryConstructor(
                    FunSpec.constructorBuilder().apply {
                        properties.forEach { property ->
                            addParameter(
                                property.name,
                                property.determineParameterType(packageName)
                            )
                        }
                    }.build()
                )
                .addProperties(
                    properties.map { property ->

                        PropertySpec.builder(
                            property.name,
                            property.determineParameterType(packageName)
                        ).also {
                            it.mutable(property.isMutable)
                            it.addModifiers(property.modifiers.toList().map { it.toKModifier() }
                                .filter { it?.name in allowedDataClassPropertiesModifiers.map { it.name } }
                                .filterNotNull())
                            it.initializer(
                                property.name
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

internal class DTOProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DTOProcessor(environment.codeGenerator, environment.logger)
    }
}