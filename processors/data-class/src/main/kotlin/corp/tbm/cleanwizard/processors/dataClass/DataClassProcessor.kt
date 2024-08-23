package corp.tbm.cleanwizard.processors.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import com.google.gson.reflect.TypeToken
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.ksp.toKModifier
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.foundation.codegen.exceptions.references.PropertyAlreadyMarkedWithEnumException
import corp.tbm.cleanwizard.foundation.codegen.extensions.*
import corp.tbm.cleanwizard.foundation.codegen.extensions.kotlinpoet.writeNewFile
import corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.getAnnotatedSymbols
import corp.tbm.cleanwizard.foundation.codegen.extensions.ksp.ks.*
import corp.tbm.cleanwizard.foundation.codegen.processor.DataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.processor.Logger
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions.dataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions.jsonSerializer
import corp.tbm.cleanwizard.foundation.codegen.processor.ProcessorOptions.layerConfigs
import corp.tbm.cleanwizard.gradle.api.config.layerConfigs.CleanWizardLayerConfig
import corp.tbm.cleanwizard.gradle.api.config.serializer.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.processors.dataClass.visitors.EnumGenerateVisitor
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import java.text.DateFormat

const val PARAMETER_SEPARATOR = ", \n    "
const val PARAMETER_PREFIX = "\n    "

@OptIn(KspExperimental::class)
private class DataClassProcessor(
    private val codeGenerator: CodeGenerator,
    processorOptions: Map<String, String>,
    private val logger: KSPLogger
) : SymbolProcessor {

    init {
        ProcessorOptions.generateConfigs(processorOptions)
        Logger.getInstance(logger)
    }

    private var processingRound = 0

    private val enumGenerateVisitor by lazy {
        EnumGenerateVisitor(codeGenerator)
    }

    private fun mapAndFormatStatementList(
        symbol: KSClassDeclaration,
        resolver: Resolver,
        layerConfig: CleanWizardLayerConfig,
        returns: ClassName,
        functionName: String,
        packageName: String,
        properties: List<KSPropertyDeclaration>,
        fileSpec: FileSpec.Builder? = null
    ): String {
        fun FileSpec.Builder?.addKotlinxCollectionsImmutableBlockWithImport(block: String): String {
            return block.also {
                this?.addImport(KOTLINX_COLLECTIONS_IMMUTABLE_PACKAGE_NAME, block.removeSuffix("()"))
            }
        }

        val typeNameCheck: (propertyName: String) -> String = { propertyName ->
            when (propertyName) {
                "Byte", "Short", "Int", "Long" -> "-1"
                "Float" -> "-1f"
                "Double" -> "-1.0"
                "Boolean" -> "false"
                "String" -> "\"\""
                "List" -> "emptyList()"
                "ImmutableList", "PersistentList" -> fileSpec.addKotlinxCollectionsImmutableBlockWithImport("persistentListOf()")
                "MutableList" -> "mutableListOf()"
                "ArrayList" -> "arrayListOf()"
                "Set" -> "emptySet()"
                "ImmutableSet", "PersistentSet" -> fileSpec.addKotlinxCollectionsImmutableBlockWithImport("persistentSetOf()")
                "HashSet" -> "hashSetOf()"
                "MutableSet" -> "mutableSetOf()"
                "Map" -> "emptyMap()"
                "ImmutableMap", "PersistentMap" -> fileSpec.addKotlinxCollectionsImmutableBlockWithImport("persistentMapOf()")
                "HashMap" -> "hashMapOf()"
                "MutableMap" -> "mutableMapOf()"
                else -> "null"
            }
        }

        return "return %T(${
            properties.map { it }
                .joinToString(
                    separator = PARAMETER_SEPARATOR,
                    prefix = PARAMETER_PREFIX
                )
                { currentProperty ->
                    val currentPropertyTypeDeclarationName = currentProperty.type.resolve().declaration.name
                    val filteredProperties = properties.filter { it.name == currentProperty.name }
                    when {
                        filteredProperties.any { it.type.resolve().isClassMappable } -> {
                            val classType = currentProperty.determineParameterTypeNoList(
                                symbol,
                                resolver,
                                dataClassGenerationPattern.classNameReplacement(
                                    packageName,
                                    returns.simpleName,
                                    layerConfigs.data
                                ).packageName
                            )
                            val classFullyQualifiedName =
                                "${classType.packageName}.${layerConfigs.domain.packageName}.${classType.simpleName}"

                            fileSpec?.addImport(
                                classFullyQualifiedName,
                                ""
                            )
                            "${currentProperty.safeCall("$functionName()")} ${
                                currentProperty.elvis(
                                    "${
                                        classType.simpleName
                                    }(${
                                        resolver.getClassDeclarationByName(classType.simpleName)
                                            ?.getDeclaredProperties()?.joinToString {
                                                typeNameCheck(it.type.resolve().declaration.name)
                                            }
                                    })"
                                )
                            }"
                        }

                        filteredProperties.any { it.type.resolve().isListMappable } ->
                            currentProperty.safeCall(
                                "map { ${
                                    currentProperty.getParameterName(
                                        packageName
                                    ).firstCharLowercase()
                                } -> ${
                                    currentProperty.getParameterName(packageName).firstCharLowercase()
                                }.$functionName() } ${
                                    currentProperty.elvis(
                                        typeNameCheck(currentPropertyTypeDeclarationName), layerConfig
                                    )
                                }"
                            )

                        else -> "${currentProperty.name} ${
                            currentProperty.elvis(
                                typeNameCheck(currentPropertyTypeDeclarationName), layerConfig
                            )
                        }"
                    }
                }
        }\n)"

    }

    private fun mapAndFormatStatementList(
        mappingFunctionName: String,
        packageName: String,
        properties: List<KSPropertyDeclaration>,
    ): String {

        return "return %T(${
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

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getAnnotatedSymbols<KSClassDeclaration>(DTO::class.qualifiedName!!)
        return reprocess(resolver, symbols)
    }

    private fun reprocess(resolver: Resolver, symbols: List<KSClassDeclaration>): List<KSAnnotated> {
        processingRound++
        val deferredSymbols by lazy {
            mutableSetOf<KSAnnotated>()
        }
        val classesWithEnums =
            symbols.filter {
                it.getDeclaredProperties().filter { property ->
                    property.annotations.filter { annotation -> annotation.isEnum }.toList().isNotEmpty()
                }.toList()
                    .isNotEmpty()
            }

        when (processingRound) {
            1 -> {

                val pathToModule = layerConfigs.data.interfaceMapperConfig.pathToModuleToGenerateInterfaceMapper

                if (pathToModule.isEmpty() || (pathToModule.isNotEmpty() && resolver.getModuleName()
                        .asString() == pathToModule)
                ) {
                    val typeVariable = TypeVariableName(layerConfigs.domain.classSuffix)
                    val interfaceBuilder = TypeSpec.interfaceBuilder(
                        ClassName(
                            "corp.tbm.cleanwizard.${layerConfigs.data.interfaceMapperConfig.className}",
                            layerConfigs.data.interfaceMapperConfig.className
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
                        layerConfigs.data.interfaceMapperConfig.className
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
                                                layerConfigs.data.interfaceMapperConfig.className
                                            ).plusParameter(typeVariable)
                                        )
                                )
                                .returns(List::class.asClassName().plusParameter(typeVariable))
                                .addStatement(
                                    "return map { ${layerConfigs.data.classSuffix.lowercase()} -> ${layerConfigs.data.classSuffix.lowercase()}.${layerConfigs.data.toDomainMapFunctionName}() }"
                                ).build()
                        )
                            .build()
                    }.build()

                    fileSpec.writeNewFile(codeGenerator)
                }
                return reprocess(resolver, symbols)
            }

            2 -> {
                deferredSymbols.addAll(symbols)

                classesWithEnums.ifEmpty {
                    return reprocess(resolver, symbols)
                }

                classesWithEnums.forEach { symbol ->
                    val properties = symbol.getDeclaredProperties().toList()
                    if (properties
                            .any { property ->
                                property.annotations.find { it.name.endsWith("Enum") } != null
                            }
                    ) {
                        properties.forEach { property ->
                            val enumPackageName =
                                "${dataClassGenerationPattern.generatePackageName(symbol, layerConfigs.domain)}.enums"

                            val propertyAnnotations = property.annotations.filter { it.name.endsWith("Enum") }.toList()

                            propertyAnnotations.ifNotEmpty {
                                if (propertyAnnotations.size >= 2) {
                                    throw PropertyAlreadyMarkedWithEnumException(
                                        "Property [${property.name}] in \n[${property.parentDeclaration?.fullyQualifiedName}] has the following $propertyAnnotations enums annotations. Only 1 is allowed"
                                    )
                                }
                                property.accept(enumGenerateVisitor, enumPackageName)
                            }
                        }
                    }
                }
            }

            3 -> {
                symbols.forEach { symbol ->
                    startGeneratingClasses(symbol, resolver)
                    deferredSymbols.clear()
                }
            }
        }
        return deferredSymbols.toList()
    }

    private fun startGeneratingClasses(
        symbol: KSClassDeclaration,
        resolver: Resolver
    ) {
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
                        ClassName("corp.tbm.cleanwizard", layerConfigs.data.interfaceMapperConfig.className)
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
                addAnnotationsForDTO(symbol)

                if (symbol.isAnnotationPresent(Entity::class) && symbol.getDeclaredProperties()
                        .any { it.type.resolve().isClassMappable }
                ) {
                    generateTypeConvertersAndFile(symbol, packageName, className, resolver)
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
                                statementFormat = mapAndFormatStatementList(
                                    symbol,
                                    resolver,
                                    layerConfigs.data,
                                    domainClassName,
                                    mappingFunctionName,
                                    packageName,
                                    properties,
                                    this
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
                            statementFormat = mapAndFormatStatementList(
                                symbol,
                                resolver,
                                layerConfigs.data,
                                domainClassName,
                                backWardMappingFunctionName,
                                packageName.replace(layerConfigs.data.packageName, layerConfigs.domain.packageName),
                                properties,
                                this
                            )
                        )
                    )
                }
                this
            }, propertyBuilder = { property ->
                if (jsonSerializer !is CleanWizardJsonSerializer.None) {
                    val jsonAnnotationSpec =
                        property.annotations.find { it.shortName.asString() == jsonSerializer.annotation.simpleName }
                            ?.toAnnotationSpec()
                            ?: AnnotationSpec.builder(jsonSerializer.annotation)
                                .addMember("${jsonSerializer.nameProperty} = %S", property.name)
                                .build()
                    addAnnotation(jsonAnnotationSpec)
                }

                property.annotations.find { it.shortName.asString() == PrimaryKey::class.simpleName }
                    ?.let { primaryKeyAnnotation ->
                        addAnnotation(primaryKeyAnnotation.toAnnotationSpec())
                    }
            }
        )

        if (layerConfigs.presentation.shouldGenerate) {
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
                            statementFormat = mapAndFormatStatementList(
                                layerConfigs.domain.toUIMapFunctionName,
                                dataClassGenerationPattern.packageNameReplacement(
                                    packageName,
                                    layerConfigs.presentation
                                ),
                                properties,
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
                                statementFormat = mapAndFormatStatementList(
                                    backWardMappingFunctionName,
                                    packageName,
                                    properties,
                                )
                            )
                        )
                    }
                    this
                })
        }
    }

    @OptIn(KspExperimental::class)
    private fun TypeSpec.Builder.addAnnotationsForDTO(symbol: KSClassDeclaration): TypeSpec.Builder {
        if (symbol.isAnnotationPresent(DTO::class)) {
            if (jsonSerializer is CleanWizardJsonSerializer.KotlinXSerialization && symbol.isAnnotationPresent(
                    Serializable::class
                )
            ) {
                addAnnotation(AnnotationSpec.builder(Serializable::class).build())
            }
            symbol.annotations.filter { it.shortName.asString() == "Entity" }.forEach { entityAnnotation ->
                addAnnotation(entityAnnotation.toAnnotationSpec())
            }
        }
        return this
    }

    private fun generateTypeConvertersAndFile(
        symbol: KSClassDeclaration,
        packageName: String,
        className: String,
        resolver: Resolver
    ) {
        val converterClassName = "${className}${layerConfigs.data.roomConfig.roomTypeConvertersConfig.classSuffix}"
        val converterPackageName = "$packageName.${layerConfigs.data.roomConfig.roomTypeConvertersConfig.packageName}"
        val converterClassBuilder = TypeSpec.objectBuilder(converterClassName)

        generateTypeConverters(symbol, resolver, packageName, converterClassBuilder)
        generateConverterFile(converterPackageName, converterClassName, converterClassBuilder).writeNewFile(
            codeGenerator
        )
    }

    private fun generateConverterFile(
        converterPackageName: String,
        converterClassName: String,
        converterClassBuilder: TypeSpec.Builder,
    ): FileSpec {
        val fileSpecBuilder = FileSpec.builder(converterPackageName, converterClassName)

        if (jsonSerializer !is CleanWizardJsonSerializer.None)
            when {
                jsonSerializer is CleanWizardJsonSerializer.KotlinXSerialization -> {
                    fileSpecBuilder
                        .addImport(ClassDiscriminatorMode::class, "")
                        .addImport(Json::class, "")
                        .addImport("kotlinx.serialization", "encodeToString")
                }

                jsonSerializer is CleanWizardJsonSerializer.Gson -> {
                    fileSpecBuilder.addImport(TypeToken::class, "")
                    fileSpecBuilder.addImport(GsonBuilder::class, "")
                }

                jsonSerializer is CleanWizardJsonSerializer.Moshi -> {

                }
            }
        return fileSpecBuilder.addType(converterClassBuilder.build()).build()
    }

    private fun generateTypeConverters(
        symbol: KSClassDeclaration,
        resolver: Resolver,
        packageName: String,
        converterClassBuilder: TypeSpec.Builder
    ) {
        var gsonAdded = false
        var kotlinxAdded = false

        symbol.getDeclaredProperties().forEach { property ->
            if (property.type.resolve().isMappable) {
                val propertyName = property.simpleName.asString()
                val propertyType = property.determineParameterType(symbol, resolver, packageName)

                when (jsonSerializer) {
                    is CleanWizardJsonSerializer.KotlinXSerialization -> {
                        if (!kotlinxAdded) {
                            kotlinxAdded = true
                            val serializerConfig =
                                (jsonSerializer as CleanWizardJsonSerializer.KotlinXSerialization).serializerConfig

                            val jsonConfigString = buildString {
                                append("Json { \n")
                                append("encodeDefaults = ${serializerConfig.encodeDefaults}\n")
                                append("ignoreUnknownKeys = ${serializerConfig.ignoreUnknownKeys}\n")
                                append("isLenient = ${serializerConfig.isLenient}\n")
                                append("allowStructuredMapKeys = ${serializerConfig.allowStructuredMapKeys}\n")
                                append("prettyPrint = ${serializerConfig.prettyPrint}\n")
                                append("explicitNulls = ${serializerConfig.explicitNulls}\n")
                                append("prettyPrintIndent = \"${serializerConfig.prettyPrintIndent}\"\n")
                                append("coerceInputValues = ${serializerConfig.coerceInputValues}\n")
                                append("useArrayPolymorphism = ${serializerConfig.useArrayPolymorphism}\n")
                                append("classDiscriminator = \"${serializerConfig.classDiscriminator}\"\n")
                                append("allowSpecialFloatingPointValues = ${serializerConfig.allowSpecialFloatingPointValues}\n")
                                append("useAlternativeNames = ${serializerConfig.useAlternativeNames}\n")
//                              TODO append("namingStrategy = ${serializerConfig.namingStrategy}\n")
                                append("decodeEnumsCaseInsensitive = ${serializerConfig.decodeEnumsCaseInsensitive}\n")
                                append("allowTrailingComma = ${serializerConfig.allowTrailingComma}\n")
                                append("allowComments = ${serializerConfig.allowComments}\n")
                                append("classDiscriminatorMode = ClassDiscriminatorMode.${serializerConfig.classDiscriminatorMode}\n")
                                append("}")
                            }

                            converterClassBuilder.addProperty(
                                PropertySpec.builder("serializerConfig", Json::class)
                                    .initializer(jsonConfigString)
                                    .build()
                            )
                        }
                        converterClassBuilder.addFunction(
                            FunSpec.builder("from${propertyName.firstCharUppercase()}")
                                .returns(String::class)
                                .addAnnotation(TypeConverter::class)
                                .addParameter(propertyName, propertyType)
                                .addStatement(
                                    "return serializerConfig.encodeToString($propertyName)"
                                )
                                .build()
                        )
                        converterClassBuilder.addFunction(
                            FunSpec.builder("to${propertyName.firstCharUppercase()}")
                                .returns(propertyType)
                                .addAnnotation(TypeConverter::class)
                                .addParameter("json", String::class)
                                .addStatement("return serializerConfig.decodeFromString(json)")
                                .build()
                        )
                    }

                    is CleanWizardJsonSerializer.Gson -> {
                        if (!gsonAdded) {
                            gsonAdded = true
                            val gsonConfig = (jsonSerializer as CleanWizardJsonSerializer.Gson).serializerConfig
                            val gsonBuilderCode = buildString {
                                fun String.appendWithIndent() {
                                    append("  .${this}\n")
                                }

                                append("GsonBuilder()\n")
                                if (gsonConfig.longSerializationPolicy != LongSerializationPolicy.DEFAULT) "setLongSerializationPolicy(com.google.gson.LongSerializationPolicy.${gsonConfig.longSerializationPolicy.name})".appendWithIndent()
                                if (gsonConfig.fieldNamingPolicy != FieldNamingPolicy.IDENTITY) "setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.${gsonConfig.fieldNamingPolicy.name})".appendWithIndent()
                                if (gsonConfig.serializeNulls) "serializeNulls()".appendWithIndent()
                                gsonConfig.datePattern?.let { "setDateFormat(\"$it\")".appendWithIndent() }
                                if (gsonConfig.dateStyle != DateFormat.DEFAULT && gsonConfig.timeStyle != DateFormat.DEFAULT)
                                    "setDateFormat(${gsonConfig.dateStyle}, ${gsonConfig.timeStyle})".appendWithIndent()
                                if (gsonConfig.complexMapKeySerialization) "enableComplexMapKeySerialization()".appendWithIndent()
                                if (gsonConfig.serializeSpecialFloatingPointValues) "serializeSpecialFloatingPointValues()".appendWithIndent()
                                if (!gsonConfig.htmlSafe) "disableHtmlEscaping()".appendWithIndent()
                                if (gsonConfig.generateNonExecutableJson) "generateNonExecutableJson()".appendWithIndent()
                                gsonConfig.strictness?.let {
                                    "setStrictness(com.google.gson.Strictness.${it.name})".appendWithIndent()
                                }
//                                TODO append("    setObjectToNumberStrategy(${gsonConfig.objectToNumberStrategy}())\n")
//                                TODO append("    setNumberToNumberStrategy(${gsonConfig.numberToNumberStrategy}())\n")
                                if (!gsonConfig.useJdkUnsafe) "disableJdkUnsafe()".appendWithIndent()
                            }

                            converterClassBuilder.addProperty(
                                PropertySpec.builder("gson", Gson::class)
                                    .initializer(gsonBuilderCode + "  " + if (gsonBuilderCode.substringAfterLast("\n") == ".") "create()" else ".create()")
                                    .build()
                            )
                        }
                        converterClassBuilder.addFunction(
                            FunSpec.builder("from${propertyName.firstCharUppercase()}")
                                .returns(String::class)
                                .addAnnotation(TypeConverter::class)
                                .addParameter(propertyName, propertyType)
                                .addStatement("return gson.toJson($propertyName)")
                                .build()
                        )
                        converterClassBuilder.addFunction(
                            FunSpec.builder("to${propertyName.firstCharUppercase()}")
                                .returns(propertyType)
                                .addAnnotation(TypeConverter::class)
                                .addParameter("json", String::class)
                                .addStatement(
                                    """val type = object : TypeToken<$propertyType>() {}.type""".trimIndent()
                                )
                                .addStatement("return gson.fromJson(json, type)")
                                .build()
                        )
                    }

                    is CleanWizardJsonSerializer.Moshi -> {
                        val adapterClassName =
                            ClassName("com.squareup.moshi", "JsonAdapter").parameterizedBy(propertyType)
                        val moshiType = ClassName("com.squareup.moshi", "Moshi")

                        converterClassBuilder.addProperty(
                            PropertySpec.builder("${propertyName}Adapter", adapterClassName)
                                .initializer(
                                    "%T.Builder().add(%T()).build().adapter(%T::class.java)",
                                    moshiType,
                                    ClassName("com.squareup.moshi.kotlin.reflect", "KotlinJsonAdapterFactory"),
                                    propertyType
                                )
                                .build()
                        )

                        converterClassBuilder.addFunction(
                            FunSpec.builder("from${propertyName.firstCharUppercase()}")
                                .returns(String::class)
                                .addAnnotation(TypeConverter::class)
                                .addParameter(propertyName, propertyType)
                                .addStatement("return ${propertyName}Adapter.toJson($propertyName)")
                                .build()
                        )
                        converterClassBuilder.addFunction(
                            FunSpec.builder("to${propertyName.firstCharUppercase()}")
                                .returns(propertyType)
                                .addAnnotation(TypeConverter::class)
                                .addParameter("json", String::class)
                                .addStatement("return ${propertyName}Adapter.fromJson(json) ?: throw IllegalArgumentException(\"Cannot parse json\")")
                                .build()
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    private fun generateTopLevelMappingFunctions(
        functionName: String,
        properties: List<KSPropertyDeclaration>,
        receiver: TypeName,
        returns: TypeName,
        statementFormat: String = "return %T(${
            properties.map { it }
                .joinToString(
                    separator = PARAMETER_SEPARATOR,
                    prefix = PARAMETER_PREFIX
                )
                { property ->
                    if (properties.filter { it.name == property.name }
                            .any { it.type.resolve().isMappable })
                        "${property.name}${if (property.isNullable()) "?" else ""}.$functionName()" else property.name
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
        propertyBuilder: PropertySpec.Builder.(property: KSPropertyDeclaration) -> Unit = {},
        fileSpecBuilder: FileSpec.Builder.(packageName: String, className: String, properties: List<KSPropertyDeclaration>) -> FileSpec.Builder = { _, _, _ -> this }
    ) {

        val properties = symbol.getDeclaredProperties().toList()

        val className = symbol.name.withoutDTOSchemaSuffix + layerConfig.classSuffix

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
                                        .copy(property.isNullable(layerConfig))
                                ).also {
                                    if (property.isNullable(layerConfig))
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
                                .copy(property.isNullable(layerConfig))
                        ).also {
                            it.mutable(property.isMutable)
                            it.addModifiers(property.modifiers.toList().map { modifier -> modifier.toKModifier() }
                                .filter { modifier ->
                                    modifier?.name in listOf(
                                        KModifier.PUBLIC,
                                        KModifier.OVERRIDE,
                                        KModifier.FINAL
                                    ).map { allowedModifier ->
                                        allowedModifier.name
                                    }
                                }
                                .filterNotNull())
                            it.initializer(property.name)
                            propertyBuilder(it, property)
                        }.build()
                    }), packageName, className, properties
        ).build()

        fileSpecBuilder(
            FileSpec.builder(packageName, className)
                .addType(classToBuild), packageName, className, properties
        ).build().writeNewFile(codeGenerator, Dependencies(true, symbol.containingFile!!))
    }

    private fun KSAnnotation.toAnnotationSpec(): AnnotationSpec {
        val builder = AnnotationSpec.builder(
            ClassName.bestGuess(this.annotationType.resolve().declaration.qualifiedName!!.asString())
        )
        this.arguments
            .filterNot { arg ->
                when (val value = arg.value) {
                    is String -> value.isEmpty()
                    is List<*> -> value.isEmpty()
                    is Boolean -> !value
                    null -> true
                    else -> false
                }
            }
            .forEach { arg ->
                builder.addMember(
                    "${
                        arg.name?.asString()
                    } = ${if (arg.value is String) "%S" else "%L"}",
                    arg.value.toString()
                )
            }
        return builder.build()
    }

    private companion object {
        private const val KOTLINX_COLLECTIONS_IMMUTABLE_PACKAGE_NAME = "kotlinx.collections.immutable"
    }
}

internal class DataClassProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return DataClassProcessor(environment.codeGenerator, environment.options, environment.logger)
    }
}