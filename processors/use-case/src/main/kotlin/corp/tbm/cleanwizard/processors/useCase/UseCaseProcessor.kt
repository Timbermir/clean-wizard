@file:OptIn(KspExperimental::class)

package corp.tbm.cleanwizard.processors.useCase

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardUseCaseFunctionType
import corp.tbm.cleanwizard.foundation.annotations.Repository
import corp.tbm.cleanwizard.foundation.annotations.UseCase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharLowercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharUppercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.kotlinpoet.writeNewFile
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.getAnnotatedSymbols
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.basePackagePath
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.isListSubclass
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.name
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.packageLastSegment
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dependencyInjectionFramework
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.layerConfigs
import kotlinx.collections.immutable.toImmutableSet
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import javax.inject.Inject

const val PARAMETER_SEPARATOR = ", \n    "
const val PARAMETER_PREFIX = "\n    "

class UseCaseProcessor(
    private val codeGenerator: CodeGenerator,
    processorOptions: Map<String, String>,
    private val logger: KSPLogger
) : SymbolProcessor {

    init {
        ProcessorOptions.generateConfigs(processorOptions)
    }

    private var hasKoinAnnotationsModuleBeenGenerated = false

    private lateinit var mResolver: Resolver

    override fun process(resolver: Resolver): List<KSAnnotated> {
        mResolver = resolver
        val symbols = resolver.getAnnotatedSymbols<KSClassDeclaration>(Repository::class.qualifiedName!!)

        symbols.onEach(::generateUseCase)

        val diFramework = dependencyInjectionFramework

        if (diFramework is CleanWizardDependencyInjectionFramework.Koin) {

            when (diFramework) {
                is CleanWizardDependencyInjectionFramework.Koin.Annotations -> {
                    generateKoinAnnotationsModule(symbols.firstOrNull(), diFramework)
                }

                is CleanWizardDependencyInjectionFramework.Koin.Core -> {
                    generateKoinModule(
                        mResolver.getAnnotatedSymbols<KSClassDeclaration>(UseCase::class.qualifiedName!!),
                        diFramework
                    )
                }
            }
        }

        return emptyList()
    }

    private fun generateUseCase(symbol: KSClassDeclaration): Boolean {
        symbol.getDeclaredFunctions().forEach { declaredFunction ->
            if (declaredFunction.parameters.any { it.type.resolve().isError })
                return false

            val className =
                "${declaredFunction.name.firstCharUppercase()}${layerConfigs.domain.useCaseConfig.classSuffix.firstCharUppercase()}"
            val packageName =
                "${
                    dataClassGenerationPattern.generatePackageName(
                        symbol,
                        layerConfigs.domain
                    )
                }.${layerConfigs.domain.useCaseConfig.packageName}"

            val repositoryName = symbol.name.firstCharLowercase()

            val constructor = FunSpec.constructorBuilder()
                .addParameter(repositoryName, symbol.toClassName())

            val functionParameters = declaredFunction.parameters.map { parameter ->
                val resolvedType = parameter.type.resolve()

                ParameterSpec.builder(
                    parameter.name?.asString().toString(), when (resolvedType.isListSubclass) {
                        false -> {
                            resolvedType.toTypeName()
                        }

                        true -> {
                            List::class.asClassName()
                                .parameterizedBy(resolvedType.arguments.first().toTypeName())
                        }
                    }
                ).also {
                    if (resolvedType.isMarkedNullable)
                        it.defaultValue("%S", null)
                }.build()
            }

            val functionModifiers = declaredFunction.modifiers.mapNotNull { it.toKModifier() }.toMutableSet().also {
                if (layerConfigs.domain.useCaseConfig.useCaseFunctionType is CleanWizardUseCaseFunctionType.Operator)
                    it.add(KModifier.OPERATOR)
            }.toImmutableSet()

            val function =
                FunSpec.builder(
                    when (val functionType = layerConfigs.domain.useCaseConfig.useCaseFunctionType) {

                        is CleanWizardUseCaseFunctionType.Operator -> {
                            "invoke"
                        }

                        is CleanWizardUseCaseFunctionType.InheritRepositoryFunctionName -> {
                            declaredFunction.name
                        }

                        is CleanWizardUseCaseFunctionType.CustomFunctionName -> {
                            functionType.functionName
                        }
                    }
                )
                    .addModifiers(functionModifiers)
                    .returns(declaredFunction.returnType?.toTypeName()!!)
                    .addParameters(functionParameters)
                    .addStatement(
                        "return ${repositoryName}.${declaredFunction.name}(${
                            functionParameters.joinToString(
                                separator = PARAMETER_SEPARATOR,
                                prefix = PARAMETER_PREFIX
                            ) { it.name }
                        }\n)"
                    )

            if (dependencyInjectionFramework is CleanWizardDependencyInjectionFramework.Dagger)
                constructor.addAnnotation(Inject::class)

            val classToBuild = TypeSpec.classBuilder(className).primaryConstructor(
                constructor.build()

            ).addProperty(
                PropertySpec.builder(repositoryName, symbol.toClassName())
                    .addModifiers(KModifier.PRIVATE)
                    .initializer(repositoryName)
                    .build()
            ).addFunction(
                function.build()
            ).addAnnotation(UseCase::class)

            if (dependencyInjectionFramework is CleanWizardDependencyInjectionFramework.Koin.Annotations) {
                classToBuild.addAnnotation(Factory::class)
            }

            FileSpec.builder(packageName, className)
                .addType(classToBuild.build())
                .build().writeNewFile(codeGenerator)
        }
        return true
    }

    private fun generateKoinAnnotationsModule(
        symbol: KSClassDeclaration?,
        koinAnnotationsConfig: CleanWizardDependencyInjectionFramework.Koin.Annotations
    ) {
        symbol?.let {

            val packageName = dataClassGenerationPattern.generatePackageName(
                symbol,
                layerConfigs.domain
            )

            val className =
                "${symbol.basePackagePath.packageLastSegment.firstCharUppercase()}${layerConfigs.domain.moduleName.firstCharUppercase()}Module"

            val classBuilder =
                TypeSpec.classBuilder(className).addAnnotation(Module::class)
                    .addAnnotation(
                        AnnotationSpec.builder(ComponentScan::class).also { annotationSpecBuilder ->
                            if (koinAnnotationsConfig.specifyUseCasePackageForComponentScan)
                                annotationSpecBuilder.addMember(
                                    "value = %S",
                                    "$packageName.${layerConfigs.domain.useCaseConfig.packageName}"
                                )
                        }
                            .build()
                    )

            FileSpec.builder(packageName, className)
                .addType(classBuilder.build())
                .build().writeNewFile(codeGenerator)
        }
    }

    private fun generateKoinModule(
        generatedUseCases: List<KSClassDeclaration>,
        koinConfig: CleanWizardDependencyInjectionFramework.Koin.Core
    ) {
        val symbolName = generatedUseCases.firstOrNull()

        symbolName?.let { symbol ->

            val packageName = dataClassGenerationPattern.generatePackageName(
                symbol,
                layerConfigs.domain
            )

            val className =
                "${symbol.basePackagePath.packageLastSegment.firstCharUppercase()}${layerConfigs.domain.moduleName.firstCharUppercase()}Module"
            val fileSpec = FileSpec.builder(packageName, className).addImport("org.koin.dsl", "module")
                .addProperty(
                    PropertySpec.builder(className, org.koin.core.module.Module::class.asTypeName())
                        .initializer(
                            """module {
               ${
                                generatedUseCases.joinToString(
                                    prefix = "\n",
                                    postfix = "",
                                    separator = "\n",
                                ) { useCaseName ->
                                    val prefix = "factory"
                                    if (koinConfig.useConstructorDSL) {
                                        "${prefix}Of(::$useCaseName)"
                                    } else {
                                        "$prefix { $useCaseName(get()) }"
                                    }
                                }
                            }
}   """.trimIndent()
                        ).build()
                )
            if (koinConfig.useConstructorDSL)
                fileSpec.addImport("org.koin.core.module.dsl", "factoryOf")

            generatedUseCases.forEach {
                fileSpec.addImport(it.toClassName(), "")
            }

            fileSpec.build().writeNewFile(codeGenerator)
        }
    }
}

internal class UseCaseProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return UseCaseProcessor(environment.codeGenerator, environment.options, environment.logger)
    }
}