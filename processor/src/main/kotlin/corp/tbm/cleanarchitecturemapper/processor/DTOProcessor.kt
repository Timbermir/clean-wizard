package corp.tbm.cleanarchitecturemapper.processor

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toKModifier
import corp.tbm.cleanarchitecturemapper.foundation.annotations.DTO
import corp.tbm.cleanarchitecturemapper.foundation.codegen.kotlinpoet.allowedDataClassPropertiesModifiers
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.DTOMapper
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.dtoRegex
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.getAnnotatedSymbols
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.ks.*
import corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.extensions.ksp.log
import corp.tbm.cleanarchitecturemapper.visitors.enums.EnumGenerateVisitor
import kotlinx.serialization.SerialName
import java.io.OutputStreamWriter
import java.util.*

const val PARAMETER_SEPARATOR = ", \n"
const val PARAMETER_PREFIX = "\n"

class DTOProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : SymbolProcessor {

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
                                        properties.map { it.getParameterName(packageName) }
                                            .joinToString(separator = PARAMETER_SEPARATOR, prefix = PARAMETER_PREFIX) {
                                                if (it.endsWith("DTO")
                                                ) "$it.toDomain()" else it
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
                            addFunction(
                                generateTopLevelMappingFunctions(
                                    "toDomain",
                                    properties,
                                    ClassName(
                                        packageName,
                                        className
                                    ),
                                    ClassName(
                                        packageName.replace("dto", "model"),
                                        className.replace("DTO", "Model")
                                    ),
                                    packageName
                                )
                            )
                        }
                    }
                    properties.forEach { property ->
                        when {

                            dtoAnnotation.toDomainAsTopLevel -> {

                                when {
                                    property.type.resolve().isMappable -> {
                                        addImport(
                                            property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                            ".toDomain"
                                        )
                                    }

                                    property.type.resolve().isListMappable -> {
                                        addFunction(
                                            generateTopLevelMappingFunctions(
                                                "toDomain", properties, List::class.asClassName()
                                                    .parameterizedBy(
                                                        ClassName(
                                                            property.getQualifiedPackageNameBasedOnParameterName(
                                                                packageName
                                                            ),
                                                            property.getParameterName(packageName)
                                                                .replaceFirstChar { it.uppercase() })
                                                    ),
                                                List::class.asClassName()
                                                    .parameterizedBy(
                                                        ClassName(
                                                            property.getQualifiedPackageNameBasedOnParameterName(
                                                                packageName.replace("dto", "model")
                                                            ),
                                                            property.getParameterName(packageName)
                                                                .replace("DTO", "Model")
                                                                .replaceFirstChar { it.uppercase() })
                                                    ),
                                                packageName.replace("dto", "model"),
                                                "return map { ${
                                                    property.getParameterName(
                                                        packageName.replace(
                                                            "model",
                                                            "dto"
                                                        )
                                                    )
                                                } -> ${
                                                    property.getParameterName(packageName)
                                                }.toDomain() }",
                                                List::class.asClassName()
                                                    .parameterizedBy(
                                                        ClassName(
                                                            property.getQualifiedPackageNameBasedOnParameterName(
                                                                packageName
                                                            ),
                                                            property.getParameterName(packageName)
                                                                .replaceFirstChar { it.uppercase() })
                                                    )
                                            )
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
                    addFunction(
                        generateTopLevelMappingFunctions(
                            "toUI", properties, ClassName(
                                packageName.replace("ui", "model"),
                                className.replace("UI", "Model")
                            ),
                            ClassName(packageName, className),
                            packageName.replace("ui", "model")
                        )
                    )
                    properties.forEach { property ->
                        when {

                            property.type.resolve().isMappable -> {
                                addImport(
                                    property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                    ".toUI"
                                )
                            }

                            property.type.resolve().isListMappable -> {
                                addImport(
                                    property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                    ".toUI"
                                )
                                addFunction(
                                    generateTopLevelMappingFunctions(
                                        "toUI", properties, List::class.asClassName()
                                            .parameterizedBy(
                                                ClassName(
                                                    property.getQualifiedPackageNameBasedOnParameterName(packageName)
                                                        .replace("ui", "model"),
                                                    property.getParameterName(packageName).replace("UI", "Model")
                                                        .replaceFirstChar { it.uppercase() })
                                            ),
                                        List::class.asClassName()
                                            .parameterizedBy(
                                                ClassName(
                                                    property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                                    property.getParameterName(packageName)
                                                        .replaceFirstChar { it.uppercase() })
                                            ),
                                        packageName.replace("ui", "model"),
                                        "return map { ${
                                            property.getParameterName(
                                                packageName.replace(
                                                    "ui",
                                                    "model"
                                                )
                                            )
                                        } -> ${
                                            property.getParameterName(packageName.replace("ui", "model"))
                                        }.toUI() }",
                                        List::class.asClassName()
                                            .parameterizedBy(
                                                ClassName(
                                                    property.getQualifiedPackageNameBasedOnParameterName(packageName),
                                                    property.getParameterName(packageName)
                                                        .replaceFirstChar { it.uppercase() })
                                            )
                                    )
                                )
                            }
                        }
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
            properties.map { it.getParameterName(receiverPackageName) }
                .joinToString(
                    separator = "$PARAMETER_SEPARATOR    ",
                    prefix = "$PARAMETER_PREFIX    "
                ) { if (it.endsWith("Model") || it.endsWith("DTO")) "$it.$functionName()" else it }
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
                .none { it.simpleName.asString().contains(neededSuffix) }
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

        if (neededSuffix == "Model")
            properties.forEach { property ->
                if (property.annotations.filter { it.shortName.asString().contains("Enum") }.toList().isNotEmpty()) {
                    property.accept(enumGenerateVisitor, "$packageName.enums")
                }
            }

        val classToBuild = classBuilder(
            TypeSpec.classBuilder(className)
                .addModifiers(KModifier.DATA)

                .primaryConstructor(
                    FunSpec.constructorBuilder().apply {
                        properties.forEach { property ->
                            logger.log(property.type.resolve().toClassName().simpleName)
                            logger.log(property.closestClassDeclaration())
                            addParameter(
                                property.getParameterName(packageName),
                                property.determineParameterType(packageName)
                            )
                        }
                    }.build()
                )
                .addProperties(
                    properties.map { property ->

                        PropertySpec.builder(
                            property.getParameterName(packageName),
                            property.determineParameterType(packageName)
                        ).also {
                            it.mutable(property.isMutable)
                            it.addModifiers(property.modifiers.toList().map { it.toKModifier() }
                                .filter { it?.name in allowedDataClassPropertiesModifiers.map { it.name } }
                                .filterNotNull())
                            it.initializer(
                                property.getParameterName(packageName)
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