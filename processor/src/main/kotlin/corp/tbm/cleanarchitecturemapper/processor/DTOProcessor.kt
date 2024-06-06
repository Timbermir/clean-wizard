package corp.tbm.cleanarchitecturemapper.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toKModifier
import corp.tbm.cleanarchitecturemapper.foundation.annotations.DTO
import corp.tbm.cleanarchitecturemapper.foundation.codegen.kotlinpoet.allowedDataClassPropertiesModifiers
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.DTOMapper
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.dtoRegex
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.exceptions.references.PropertyAlreadyMarkedWithEnumException
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.firstCharLowercase
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.getAnnotatedSymbols
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks.*
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.log
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.processor.ClassGenerationConfig
import corp.tbm.cleanarchitecturemapper.visitors.enums.EnumGenerateVisitor
import kotlinx.serialization.SerialName
import java.io.OutputStreamWriter

const val PARAMETER_SEPARATOR = ", \n"
const val PARAMETER_PREFIX = "\n"
const val DTO_CLASS_PREFIX_KEY = "DTO_CLASS_PREFIX"
const val DOMAIN_CLASS_PREFIX_KEY = "DOMAIN_CLASS_PREFIX"
const val UI_CLASS_PREFIX_KEY = "UI_CLASS_PREFIX"
const val DTO_CLASS_PACKAGE_NAME_KEY = "DTO_CLASS_PACKAGE_NAME"
const val DOMAIN_CLASS_PACKAGE_NAME_KEY = "DOMAIN_CLASS_PACKAGE_NAME"
const val UI_CLASS_PACKAGE_NAME_KEY = "UI_CLASS_PACKAGE_NAME"
const val DEFAULT_JSON_SERIALIZER_KEY = "DEFAULT_JSON_SERIALIZER"

class DTOProcessor(
    private val codeGenerator: CodeGenerator,
    private val processorOptions: Map<String, String>,
    val logger: KSPLogger
) : SymbolProcessor {

    init {
        ClassGenerationConfig.setProcessorOptions(processorOptions)
        ProcessorOptions.dtoOptions.prefix =
            processorOptions[DTO_CLASS_PREFIX_KEY] ?: "DTO"
        ProcessorOptions.dtoOptions.packageName =
            processorOptions[DTO_CLASS_PACKAGE_NAME_KEY] ?: "dto"
        ProcessorOptions.domainOptions.prefix =
            processorOptions[DOMAIN_CLASS_PREFIX_KEY] ?: "Model"
        ProcessorOptions.domainOptions.packageName =
            processorOptions[DOMAIN_CLASS_PACKAGE_NAME_KEY] ?: "model"
        ProcessorOptions.uiOptions.prefix =
            processorOptions[UI_CLASS_PREFIX_KEY] ?: "UI"
        ProcessorOptions.uiOptions.packageName =
            processorOptions[UI_CLASS_PACKAGE_NAME_KEY] ?: "ui"
    }

    private var processingRound = 0

    private val enumGenerateVisitor by lazy {
        EnumGenerateVisitor(codeGenerator, logger)
    }

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

                            filteredProperties.any { it.type.resolve().isClassMappable } ->
                                "${currentProperty.name}.$mappingFunctionName()"

                            filteredProperties.any { it.type.resolve().isListMappable } ->
                                "${currentProperty.name}.map { ${
                                    currentProperty.getParameterName(
                                        packageName
                                    ).firstCharLowercase()
                                } -> ${
                                    currentProperty.getParameterName(packageName).firstCharLowercase()
                                }.$mappingFunctionName() }"

                            else -> currentProperty.name
                        }
                    }
            }\n)"
        }

    val dtoOptions =
        GeneratedClassOptions(
            processorOptions[DTO_CLASS_PREFIX_KEY] ?: "DTO",
            processorOptions[DTO_CLASS_PACKAGE_NAME_KEY] ?: "dto"
        )

    val domainOptions = GeneratedClassOptions(
        processorOptions[DOMAIN_CLASS_PREFIX_KEY] ?: "Model",
        processorOptions[DOMAIN_CLASS_PACKAGE_NAME_KEY] ?: "model"
    )

    val uiOptions = GeneratedClassOptions(
        processorOptions[UI_CLASS_PREFIX_KEY] ?: "UI",
        processorOptions[UI_CLASS_PACKAGE_NAME_KEY] ?: "ui"
    )

    val defaultSerializer = processorOptions[DEFAULT_JSON_SERIALIZER_KEY] ?: "kotlinx-serialization"

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        processingRound++

        val symbols = resolver.getAnnotatedSymbols<KSClassDeclaration>(DTO::class.qualifiedName!!)
        logger.log(processingRound)

        symbols.forEach { symbol ->

            if (symbol.getDeclaredProperties()
                    .any { property -> property.annotations.filter { it.name.endsWith("Enum") }.toList().isNotEmpty() }
            ) {
                if (processingRound == 1) {
                    symbol.getDeclaredProperties().forEach { property ->
                        val packageName = "${
                            symbol.packageName.asString().split(".").dropLast(1)
                                .joinToString(".") + "." + symbol.simpleName.asString().replace(dtoRegex, "")
                                .firstCharLowercase()
                        }.model"

                        val propertyAnnotations = property.annotations.filter { it.name.endsWith("Enum") }.toList()

                        if (propertyAnnotations.isNotEmpty()) {

                            if (propertyAnnotations.size >= 2) {
                                throw PropertyAlreadyMarkedWithEnumException(
                                    "Property [${property.name}] in \n[${property.parentDeclaration?.fullyQualifiedName}] has the following $propertyAnnotations enums annotations. Only 1 is allowed"
                                )
                            }

                            property.accept(enumGenerateVisitor, "$packageName.enums")
                        }
                    }
                    return symbols.filter { it.validate() }
                }
            }

            generateClass(resolver, symbol, domainOptions.prefix)

            generateClass(
                resolver,
                symbol,
                dtoOptions.prefix,
                classBuilder = { packageName, className, properties ->
                    if (!symbol.getAnnotationsByType(DTO::class).first().toDomainAsTopLevel) {
                        addSuperinterface(
                            DTOMapper::class.asClassName()
                                .parameterizedBy(
                                    ClassName(
                                        packageName.replace(dtoOptions.packageName, domainOptions.packageName),
                                        className.replace(dtoOptions.prefix, domainOptions.prefix)
                                    )
                                )
                        )
                        addFunction(
                            FunSpec.builder("toDomain")
                                .addModifiers(KModifier.OVERRIDE)
                                .returns(
                                    ClassName(
                                        packageName.replace(dtoOptions.packageName, domainOptions.packageName),
                                        className.replace(dtoOptions.prefix, domainOptions.prefix)
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
                                    ClassName(
                                        packageName.replace(dtoOptions.packageName, domainOptions.packageName),
                                        className.replace(dtoOptions.prefix, domainOptions.prefix)
                                    )
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
                                        packageName.replace(dtoOptions.packageName, domainOptions.packageName),
                                        className.replace(dtoOptions.prefix, domainOptions.prefix)
                                    ),
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
                uiOptions.prefix,
                fileSpecBuilder = { packageName, className, properties ->
                    val mappingFunctionName = "toUI"
                    addFunction(
                        generateTopLevelMappingFunctions(
                            mappingFunctionName, properties, ClassName(
                                packageName.replace(uiOptions.packageName, domainOptions.packageName),
                                className.replace(uiOptions.prefix, domainOptions.prefix)
                            ),
                            ClassName(packageName, className),
                            statementFormat = statementListFormatMapping(
                                mappingFunctionName,
                                packageName,
                                properties
                            )
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
        classBuilder: TypeSpec.Builder.(packageName: String, className: String, properties: List<KSPropertyDeclaration>) -> TypeSpec.Builder = { _, _, _ -> this },
        fileSpecBuilder: FileSpec.Builder.(packageName: String, className: String, properties: List<KSPropertyDeclaration>) -> FileSpec.Builder = { _, _, _ -> this }
    ) {

        val properties = symbol.getDeclaredProperties().toList()

        val className = symbol.simpleName.asString().replace(dtoRegex, "") + neededSuffix

        val packageName = "${
            symbol.packageName.asString().split(".").dropLast(1)
                .joinToString(".") + "." + symbol.simpleName.asString().replace(dtoRegex, "").firstCharLowercase()
        }.${neededSuffix.lowercase()}"

        val classToBuild = classBuilder(
            TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)

                .primaryConstructor(
                    FunSpec.constructorBuilder().apply {
                        properties.forEach { property ->
                            addParameter(
                                property.name,
                                property.determineParameterType(symbol, resolver, packageName, logger)
                            )
                        }
                    }.build()
                )
                .addProperties(
                    properties.map { property ->

                        PropertySpec.builder(
                            property.name,
                            property.determineParameterType(symbol, resolver, packageName, logger)
                        ).also {
                            it.mutable(property.isMutable)
                            it.addModifiers(property.modifiers.toList().map { modifier -> modifier.toKModifier() }
                                .filter { modifier ->
                                    modifier?.name in allowedDataClassPropertiesModifiers.map { allowedModifier ->
                                        allowedModifier.name
                                    }
                                }
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
            Dependencies(true, symbol.containingFile!!),
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
        return DTOProcessor(environment.codeGenerator, environment.options, environment.logger)
    }
}