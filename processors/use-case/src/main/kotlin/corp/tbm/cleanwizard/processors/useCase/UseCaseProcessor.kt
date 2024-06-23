package corp.tbm.cleanwizard.processors.useCase

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
import corp.tbm.cleanwizard.buildLogic.config.KodeinBinding
import corp.tbm.cleanwizard.buildLogic.config.KodeinFunction
import corp.tbm.cleanwizard.foundation.annotations.Repository
import corp.tbm.cleanwizard.foundation.annotations.UseCase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.asPackage
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharLowercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharUppercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.kotlinpoet.addImport
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.kotlinpoet.writeNewFile
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.getAnnotatedSymbols
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.basePackagePath
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.isListSubclass
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.name
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.packagePath
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.packageLastSegment
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.Logger
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dependencyInjectionFramework
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.layerConfigs
import kotlinx.collections.immutable.toImmutableSet
import org.kodein.di.DI
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import javax.inject.Inject

const val PARAMETER_SEPARATOR = ", \n    "
const val PARAMETER_PREFIX = "\n    "

class UseCaseProcessor(
    private val codeGenerator: CodeGenerator,
    processorOptions: Map<String, String>,
    logger: KSPLogger
) : SymbolProcessor {

    init {
        ProcessorOptions.generateConfigs(processorOptions)
        Logger.getInstance(logger)
    }

    private lateinit var mResolver: Resolver

    override fun process(resolver: Resolver): List<KSAnnotated> {
        mResolver = resolver
        val symbols = resolver.getAnnotatedSymbols<KSClassDeclaration>(Repository::class.qualifiedName!!)

        val diFramework = dependencyInjectionFramework

        if (diFramework is CleanWizardDependencyInjectionFramework.Koin) {

            when (diFramework) {
                is CleanWizardDependencyInjectionFramework.Koin.Annotations -> {
                    generateKoinAnnotationsModule(symbols.firstOrNull(), diFramework)
                }

                is CleanWizardDependencyInjectionFramework.Koin.Core -> {
                    generateKoinCoreModule(
                        mResolver.getAnnotatedSymbols<KSClassDeclaration>(UseCase::class.qualifiedName!!),
                        diFramework
                    )
                }
            }
        }

        if (diFramework is CleanWizardDependencyInjectionFramework.Kodein) {
            generateKodeinModule(
                mResolver.getAnnotatedSymbols<KSClassDeclaration>(UseCase::class.qualifiedName!!),
                diFramework
            )
        }

        return symbols.filter { !generateUseCase(it) }
    }

    private fun generateUseCase(symbol: KSClassDeclaration): Boolean {
        symbol.getDeclaredFunctions().forEach { declaredFunction ->
            if (declaredFunction.parameters.any { it.type.resolve().isError }) {
                return false
            }

            val className =
                "${declaredFunction.name.firstCharUppercase()}${layerConfigs.domain.useCaseConfig.classSuffix.firstCharUppercase()}"

            val packageName = dataClassGenerationPattern.generateUseCasePackageName(symbol)

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

                        is CleanWizardUseCaseFunctionType.Operator ->
                            "invoke"

                        is CleanWizardUseCaseFunctionType.InheritRepositoryFunctionName ->
                            declaredFunction.name

                        is CleanWizardUseCaseFunctionType.CustomFunctionName ->
                            functionType.functionName
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

            FileSpec.builder(
                packageName,
                className
            )
                .addType(classToBuild.build())
                .build().writeNewFile(codeGenerator)
        }
        return true
    }

    private fun generateKoinCoreModule(
        generatedUseCases: List<KSClassDeclaration>,
        koinConfig: CleanWizardDependencyInjectionFramework.Koin.Core
    ) {
        val symbolName = generatedUseCases.firstOrNull()

        symbolName?.let { symbol ->

            val packageName =
                symbol.packagePath.asPackage.dropLastWhile { it != layerConfigs.domain.moduleName }.asPackage

            val className =
                "${symbol.basePackagePath.packageLastSegment.firstCharUppercase()}${layerConfigs.domain.moduleName.firstCharUppercase()}Module"

            val fileSpec = FileSpec.builder(packageName, className).addImport("org.koin.dsl", "module")
                .addProperty(
                    PropertySpec.builder(
                        className.firstCharLowercase(),
                        org.koin.core.module.Module::class.asTypeName()
                    ).initializer(
                        """module {
                            ${
                            generatedUseCases.joinToString(
                                prefix = "\n",
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
}""".trimIndent()
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

    private fun generateKoinAnnotationsModule(
        symbol: KSClassDeclaration?,
        koinAnnotationsConfig: CleanWizardDependencyInjectionFramework.Koin.Annotations
    ) {
        symbol?.let {

            val packageName =
                symbol.packagePath.asPackage.dropLastWhile { it != layerConfigs.domain.moduleName }.asPackage

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

    private fun generateKodeinModule(
        generatedUseCases: List<KSClassDeclaration>,
        kodeinConfig: CleanWizardDependencyInjectionFramework.Kodein
    ) {
        val symbolName = generatedUseCases.firstOrNull()

        symbolName?.let { symbol ->

            val packageName =
                symbol.packagePath.asPackage.dropLastWhile { it != layerConfigs.domain.moduleName }.asPackage

            val className =
                "${symbol.basePackagePath.packageLastSegment.firstCharUppercase()}${layerConfigs.domain.moduleName.firstCharUppercase()}Module"

            val fileSpec = FileSpec.builder(packageName, className).addImport("org.kodein.di", "DI")
            val propertySpec = PropertySpec.builder(
                className.firstCharLowercase(),
                DI.Module::class.asTypeName()
            ).initializer(
                """DI.Module("$className") {
                            ${
                    generatedUseCases.map { it.name }.joinToString(
                        prefix = "\n",
                        separator = "\n",
                    ) { useCaseName ->

                        val repositoryName =
                            symbol.primaryConstructor?.parameters?.first()?.name?.asString().toString()

                        val binding = kodeinConfig.binding

                        fun FileSpec.Builder.applyKodeinFunction(
                            function: KodeinFunction,
                        ): String {

                            function.imports.forEach {
                                addImport(it)
                            }

                            return function.getProvisionLambda(
                                when (binding is KodeinBinding.Factory || binding is KodeinBinding.Multiton) {
                                    true -> {
                                        val repositoryType =
                                            symbol.primaryConstructor?.parameters?.first()?.type?.resolve()
                                                ?.toClassName()!!
                                        addImport(repositoryType, "")
                                        "$repositoryName: ${repositoryName.firstCharUppercase()} -> $useCaseName($repositoryName)"
                                    }

                                    false -> {
                                        useCaseName
                                    }
                                }
                            )
                        }
                        fileSpec.applyKodeinFunction(if (kodeinConfig.useSimpleFunctions) binding.shortFunction else binding.longFunction)
                    }
                }
}""".trimIndent()
            )

            fileSpec.addProperty(propertySpec.build())

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