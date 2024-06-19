package corp.tbm.cleanwizard.processors.useCase

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardUseCaseFunctionType
import corp.tbm.cleanwizard.foundation.annotations.Repository
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharLowercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.firstCharUppercase
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.getAnnotatedSymbols
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.isListSubclass
import corp.tbm.cleanwizard.foundation.codegen.universal.extensions.ksp.ks.name
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dataClassGenerationPattern
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.dependencyInjectionFramework
import corp.tbm.cleanwizard.foundation.codegen.universal.processor.ProcessorOptions.layerConfigs
import java.io.OutputStreamWriter
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

    private lateinit var mResolver: Resolver

    override fun process(resolver: Resolver): List<KSAnnotated> {
        mResolver = resolver
        val symbols = resolver.getAnnotatedSymbols<KSClassDeclaration>(Repository::class.qualifiedName!!)

        val deferredSymbols = symbols.filterNot(::generateUseCase)

        return deferredSymbols
    }

    @OptIn(KspExperimental::class)
    private fun generateUseCase(symbol: KSClassDeclaration): Boolean {
//        if (mResolver.getDeclarationsFromPackage("corp.tbm.cleanwizard.workloads.multimodule.domain.models").toList()
//                .isEmpty()
//        ) {
//            return false
//        }

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
            if (dependencyInjectionFramework is CleanWizardDependencyInjectionFramework.Dagger)
                constructor.addAnnotation(Inject::class)

            val functionParameters = declaredFunction.parameters.map {
                ParameterSpec.builder(
                    it.name?.asString().toString(), when (it.type.resolve().isListSubclass) {
                        false -> {
                            it.type.resolve().toTypeName()
                        }

                        true -> {
                            List::class.asClassName()
                                .parameterizedBy(it.type.resolve().arguments.first().toTypeName())
                        }
                    }
                ).build()
            }

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

            if (layerConfigs.domain.useCaseConfig.useCaseFunctionType is CleanWizardUseCaseFunctionType.Operator)
                function.addModifiers(KModifier.OPERATOR)

            val classToBuild = TypeSpec.classBuilder(className).primaryConstructor(
                constructor.build()

            ).addProperty(
                PropertySpec.builder(repositoryName, symbol.toClassName())
                    .addModifiers(KModifier.PRIVATE)
                    .initializer(repositoryName)
                    .build()
            ).addFunction(
                function.build()
            )

            val fileSpec =
                FileSpec.builder(packageName, className)
                    .addType(classToBuild.build())
                    .build()

            try {
                val file = codeGenerator.createNewFile(
                    Dependencies.ALL_FILES,
                    packageName,
                    className
                )

                OutputStreamWriter(file).use { writer ->
                    fileSpec.writeTo(writer)
                }
            } catch (someFile: Exception) {

            }
        }
        return true
    }

//    private fun generateKoinModule() {
//        val generatedUseCases =
//            mResolver.getDeclarationsFromPackage("corp.tbm.cleanwizard.workloads.multimodule.domain.usecase").toList()
//                .map { it.closestClassDeclaration() }
//
//        if (generatedUseCases.isNotEmpty()) {
//            val packageName = generatedUseCases.firstOrNull()?.basePackagePath
//            val className = generatedUseCases.firstOrNull()?.basePackagePath?.packageLastSegment.toString()
//            val fileSpec =
//                FileSpec.scriptBuilder(className, packageName.toString()).addImport("org.koin.dsl", "module").addCode(
//                    """
//            val ${className}DomainModule = module {
//            ${
//                        generatedUseCases.map { it.name }.joinToString("factoryOf(") {
//                            "factoryOf(::$it)"
//                        }
//
//
//                    }
//            }
//
//        """.trimIndent()
//                ).build()
//            try {
//                val file = codeGenerator.createNewFile(
//                    Dependencies(
//                        false,
//                        *mResolver.getDeclarationsFromPackage("corp.tbm.cleanwizard.workloads.multimodule.domain.usecase")
//                            .map { it.containingFile!! }.toList().toTypedArray()
//                    ),
//                    packageName.toString(),
//                    className
//                )
//                OutputStreamWriter(file).use { writer ->
//
//                    fileSpec.writeTo(writer)
//                }
//            } catch (_: FileAlreadyExistsException) {
//            }
//        }
//    }
}

internal class UseCaseProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return UseCaseProcessor(environment.codeGenerator, environment.options, environment.logger)
    }
}