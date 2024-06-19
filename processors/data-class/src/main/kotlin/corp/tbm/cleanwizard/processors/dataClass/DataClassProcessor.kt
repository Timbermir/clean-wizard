package corp.tbm.cleanwizard.processors.dataClass

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.writeTo
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.foundation.codegen.kotlinpoet.allowedDataClassPropertiesModifiers
import corp.tbm.cleanwizard.foundation.codegen.universal.dtoRegex
import corp.tbm.cleanwizard.foundation.codegen.universal.exceptions.references.PropertyAlreadyMarkedWithEnumException
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharLowercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.getAnnotatedSymbols
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.*
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.DataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.jsonSerializer
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.layerConfigs
import corp.tbm.cleanwizard.visitors.enums.EnumGenerateVisitor
import java.io.OutputStreamWriter
import kotlin.reflect.KClass

const val PARAMETER_SEPARATOR = ", \n    "
const val PARAMETER_PREFIX = "\n    "

class DataClassProcessor(
    private val codeGenerator: CodeGenerator,
    processorOptions: Map<String, String>,
    private val logger: KSPLogger
) : SymbolProcessor {

    init {
        ProcessorOptions.generateConfigs(processorOptions)
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
                        separator = PARAMETER_SEPARATOR,
                        prefix = PARAMETER_PREFIX
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

    private val generateDomainClassName: ClassName.() -> ClassName = {
        if (dataClassGenerationPattern == DataClassGenerationPattern.LAYER)
            ClassName(
                "${packageName}.${layerConfigs.domain.packageName}",
                simpleName
            ) else ClassName(packageName, simpleName)
    }

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        processingRound++

        if (resolver.getModuleName().asString() == "clean-wizard")
            try {
                val typeVariable = TypeVariableName(layerConfigs.domain.classSuffix)
                val interfaceBuilder = TypeSpec.interfaceBuilder(
                    ClassName(
                        "corp.tbm.cleanwizard.${layerConfigs.data.interfaceMapperName}",
                        layerConfigs.data.interfaceMapperName
                    )
                ).addTypeVariable(typeVariable)
                    .addFunction(
                        FunSpec.builder(layerConfigs.data.toDomainMapFunctionName).addModifiers(KModifier.ABSTRACT)
                            .returns(
                                typeVariable
                            ).build()
                    )
                val fileSpec = FileSpec.builder(
                    "corp.tbm.cleanwizard",
                    layerConfigs.data.interfaceMapperName
                ).apply {
                    addType(
                        interfaceBuilder.build()
                    ).addFunction(
                        FunSpec.builder(layerConfigs.data.toDomainMapFunctionName).addTypeVariable(typeVariable)
                            .receiver(
                                List::class.asClassName()
                                    .parameterizedBy(
                                        ClassName(
                                            "corp.tbm.cleanwizard",
                                            layerConfigs.data.interfaceMapperName
                                        ).plusParameter(typeVariable)
                                    )
                            )
                            .returns(List::class.asClassName().plusParameter(typeVariable))
                            .addStatement(
                                "return map { dto -> dto.${layerConfigs.data.toDomainMapFunctionName}() }"
                            ).build()
                    )
                        .build()
                }.build()

                fileSpec.writeTo(codeGenerator, true)
            } catch (_: FileAlreadyExistsException) {
            }

        val symbols = resolver.getAnnotatedSymbols<KSClassDeclaration>(DTO::class.qualifiedName!!)

        symbols.forEach { symbol ->

            if (symbol.getDeclaredProperties()
                    .any { property -> property.annotations.filter { it.name.endsWith("Enum") }.toList().isNotEmpty() }
            ) {
                if (processingRound == 1) {
                    symbol.getDeclaredProperties().forEach { property ->
                        val enumPackageName =
                            "${dataClassGenerationPattern.generatePackageName(symbol, layerConfigs.domain)}.enums"

                        val propertyAnnotations = property.annotations.filter { it.name.endsWith("Enum") }.toList()

                        if (propertyAnnotations.isNotEmpty()) {

                            if (propertyAnnotations.size >= 2) {
                                throw PropertyAlreadyMarkedWithEnumException(
                                    "Property [${property.name}] in \n[${property.parentDeclaration?.fullyQualifiedName}] has the following $propertyAnnotations enums annotations. Only 1 is allowed"
                                )
                            }

                            property.accept(enumGenerateVisitor, enumPackageName)
                        }
                    }
                    return symbols.filter { it.validate() }
                }
            }

            generateClass(resolver, symbol, layerConfigs.domain)

            generateClass(
                resolver,
                symbol,
                layerConfigs.data,
                classBuilder = { packageName, className, properties ->

                    val domainClassName =
                        generateDomainClassName(
                            dataClassGenerationPattern.classNameReplacement(
                                packageName,
                                className,
                                layerConfigs.data
                            )
                        )

                    if (!symbol.getAnnotationsByType(DTO::class).first().toDomainAsTopLevel) {
                        addSuperinterface(
                            ClassName("corp.tbm.cleanwizard", layerConfigs.data.interfaceMapperName)
                                .parameterizedBy(
                                    domainClassName
                                )
                        )
                        addFunction(
                            FunSpec.builder(layerConfigs.data.toDomainMapFunctionName)
                                .addModifiers(KModifier.OVERRIDE)
                                .returns(
                                    domainClassName
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
                                                    "$parameter.${layerConfigs.data.toDomainMapFunctionName}()" else parameter
                                            }
                                    }\n)",
                                    domainClassName
                                ).build()
                        )
                    }
                    this
                },
                fileSpecBuilder = { packageName, className, properties ->
                    val dtoAnnotation = symbol.getAnnotationsByType(DTO::class).first()
                    val domainClassName =
                        generateDomainClassName(
                            dataClassGenerationPattern.classNameReplacement(
                                packageName,
                                className,
                                layerConfigs.data
                            )
                        )
                    when {

                        !dtoAnnotation.toDomainAsTopLevel -> {
                            addImport(
                                "corp.tbm.cleanwizard",
                                ".${layerConfigs.data.toDomainMapFunctionName}"
                            )
                        }

                        dtoAnnotation.toDomainAsTopLevel -> {
                            val mappingFunctionName = layerConfigs.data.toDomainMapFunctionName
                            addFunction(
                                generateTopLevelMappingFunctions(
                                    mappingFunctionName,
                                    properties,
                                    ClassName(
                                        packageName,
                                        className
                                    ),
                                    domainClassName,
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
                                            ".${layerConfigs.data.toDomainMapFunctionName}"
                                        )
                                    }

                                    property.type.resolve().isListMappable -> {
                                        addImport(
                                            property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                            ".${layerConfigs.data.toDomainMapFunctionName}"
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (dtoAnnotation.backwardsMappingConfig == BackwardsMappingConfig.DOMAIN_TO_DATA ||
                        dtoAnnotation.backwardsMappingConfig == BackwardsMappingConfig.FULL_MAPPING
                    ) {
                        val backWardMappingFunctionName = layerConfigs.domain.toDTOMapFunctionName
                        properties.forEach { property ->
                            if (property.type.resolve().isMappable) {
                                addImport(
                                    property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                    ".$backWardMappingFunctionName"
                                )
                            }
                        }


                        addFunction(
                            generateTopLevelMappingFunctions(
                                backWardMappingFunctionName,
                                properties,
                                domainClassName,
                                ClassName(
                                    packageName,
                                    className
                                ),
                                statementFormat = statementListFormatMapping(
                                    backWardMappingFunctionName,
                                    packageName.replace(layerConfigs.data.packageName, layerConfigs.domain.packageName),
                                    properties
                                )
                            )
                        )
                    }
                    this
                }, propertyBuilder = { _, _, property ->
                    if (!property.hasAnnotation(jsonSerializer.annotation)) {
                        addAnnotation(
                            AnnotationSpec.builder(
                                jsonSerializer.annotation
                            ).addMember("${jsonSerializer.nameProperty} = %S", property.name).build()
                        )
                    } else {
                        val existingAnnotation = property.annotations
                            .find { it.shortName.asString() == jsonSerializer.annotation.simpleName }
                        existingAnnotation?.let { ann ->
                            addAnnotation(ann.toAnnotationSpec())
                        }
                    }
                    this
                })

            generateClass(
                resolver,
                symbol,
                layerConfigs.presentation,
                fileSpecBuilder = { packageName, className, properties ->
                    val domainClassName =
                        generateDomainClassName(
                            dataClassGenerationPattern.classNameReplacement(
                                packageName,
                                className,
                                layerConfigs.presentation
                            )
                        )
                    addFunction(
                        generateTopLevelMappingFunctions(
                            layerConfigs.domain.toUIMapFunctionName, properties, domainClassName,
                            ClassName(packageName, className),
                            statementFormat = statementListFormatMapping(
                                layerConfigs.domain.toUIMapFunctionName,
                                dataClassGenerationPattern.packageNameReplacement(
                                    packageName,
                                    layerConfigs.presentation
                                ),
                                properties
                            )
                        )
                    )
                    properties.forEach { property ->
                        if (property.type.resolve().isMappable)
                            addImport(
                                property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                ".${layerConfigs.domain.toUIMapFunctionName}"
                            )
                    }
                    if (symbol.getAnnotationsByType(DTO::class)
                            .first().backwardsMappingConfig == BackwardsMappingConfig.FULL_MAPPING
                    ) {
                        val backWardMappingFunctionName = layerConfigs.presentation.toDomainMapFunctionName
                        properties.forEach { property ->
                            if (property.type.resolve().isMappable) {
                                addImport(
                                    property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                    ".${backWardMappingFunctionName}"
                                )
                            }
                        }

                        addFunction(
                            generateTopLevelMappingFunctions(
                                backWardMappingFunctionName,
                                properties,
                                ClassName(
                                    packageName,
                                    className
                                ),
                                domainClassName,
                                statementFormat = statementListFormatMapping(
                                    backWardMappingFunctionName,
                                    packageName,
                                    properties
                                )
                            )
                        )
                    }
                    this
                })
        }
        return emptyList()
    }

    private fun generateTopLevelMappingFunctions(
        functionName: String,
        properties: List<KSPropertyDeclaration>,
        receiver: TypeName,
        returns: TypeName,
        statementFormat: String = "return %T(${
            properties.map { it.name }
                .joinToString(
                    separator = PARAMETER_SEPARATOR,
                    prefix = PARAMETER_PREFIX
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

    private fun generateClass(
        resolver: Resolver,
        symbol: KSClassDeclaration,
        layerConfig: CleanWizardLayerConfig,
        classBuilder: TypeSpec.Builder.(packageName: String, className: String, properties: List<KSPropertyDeclaration>) -> TypeSpec.Builder = { _, _, _ -> this },
        fileSpecBuilder: FileSpec.Builder.(packageName: String, className: String, properties: List<KSPropertyDeclaration>) -> FileSpec.Builder = { _, _, _ -> this },
        propertyBuilder: PropertySpec.Builder.(packageName: String, className: String, property: KSPropertyDeclaration) -> PropertySpec.Builder = { _, _, _ -> this }
    ) {

        val properties = symbol.getDeclaredProperties().toList()

        val className = symbol.name.replace(dtoRegex, "") + layerConfig.classSuffix

        val packageName = "${
            dataClassGenerationPattern.generatePackageName(
                symbol,
                layerConfig
            )
        }${if (dataClassGenerationPattern == DataClassGenerationPattern.LAYER && layerConfig is CleanWizardLayerConfig.Domain) ".${layerConfig.packageName}" else ""}"

        val classToBuild = classBuilder(
            TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)
                .primaryConstructor(
                    FunSpec.constructorBuilder().apply {
                        properties.forEach { property ->
                            addParameter(
                                ParameterSpec.builder(
                                    property.name,
                                    property.determineParameterType(symbol, resolver, packageName)
                                ).also {
                                    if (property.type.resolve().isMarkedNullable)
                                        it.defaultValue("%S", null)
                                }.build()
                            )
                        }
                    }.build()
                ).addProperties(
                    properties.map { property ->

                        PropertySpec.builder(
                            property.name,
                            property.determineParameterType(symbol, resolver, packageName)
                        ).also {
                            it.mutable(property.isMutable)
                            it.addModifiers(property.modifiers.toList().map { modifier -> modifier.toKModifier() }
                                .filter { modifier ->
                                    modifier?.name in allowedDataClassPropertiesModifiers.map { allowedModifier ->
                                        allowedModifier.name
                                    }
                                }
                                .filterNotNull())
                            it.initializer(property.name)
                            propertyBuilder(it, packageName, className, property)

                        }.build()
                    }), packageName, className, properties
        ).build()

        val fileSpec = fileSpecBuilder(
            FileSpec.builder(packageName, className)
                .addType(classToBuild), packageName, className, properties
        ).build()

        try {
            val file = codeGenerator.createNewFile(
                Dependencies(true, symbol.containingFile!!),
                packageName,
                className
            )

            OutputStreamWriter(file).use { writer ->
                fileSpec.writeTo(writer)
            }
        } catch (_: FileAlreadyExistsException) {
        }
    }

    private fun KSPropertyDeclaration.hasAnnotation(
        annotation: KClass<out Annotation>
    ): Boolean {
        return annotations.any { ann ->
            val resolvedName = ann.annotationType.resolve().declaration.qualifiedName?.asString()
            resolvedName == annotation.qualifiedName
        }
    }

    private fun KSAnnotation.toAnnotationSpec(): AnnotationSpec {
        val builder = AnnotationSpec.builder(
            ClassName.bestGuess(this.annotationType.resolve().declaration.qualifiedName!!.asString())
        )
        this.arguments.forEach { arg ->
            if (arg.name?.asString() != "alternate" || (arg.value as? List<*>)?.isEmpty() == false) {
                builder.addMember(
                    "${arg.name?.asString()} = ${if (arg.value is String) "%S" else "%L"}",
                    arg.value.toString()
                )
            }
        }
        return builder.build()
    }
}

internal class DataClassProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DataClassProcessor(environment.codeGenerator, environment.options, environment.logger)
    }
}