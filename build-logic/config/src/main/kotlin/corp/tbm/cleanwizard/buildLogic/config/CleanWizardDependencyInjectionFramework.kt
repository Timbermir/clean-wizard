package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDsl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@CleanWizardConfigDsl
sealed class CleanWizardDependencyInjectionFramework(val dependencies: List<String>) {

    @Serializable
    @SerialName("none")
    data object None : CleanWizardDependencyInjectionFramework(listOf())

    /**
     * Have to do it this weird way because if you do `listOf("io.insert-koin:koin-core + additionalDeps`,
     * compiler throws a warning that `additionalDependencies is not used as a property and val should be removed`
     * but `@Serializable` throws a compiler error that for the class with properties to be Serializable,
     * properties must be either `var` or `val`.
     */
    @Serializable
    @SerialName("koin")
    sealed class Koin(private val additionalDependencies: List<String> = listOf()) :
        CleanWizardDependencyInjectionFramework(mutableListOf("io.insert-koin:koin-core").also {
            it.addAll(
                additionalDependencies
            )
        }) {

        @Serializable
        @SerialName("core")
        class Core(
            var automaticallyCreateModule: Boolean = true,
            var useConstructorDSL: Boolean = true,
        ) : Koin()

        @Serializable
        @SerialName("annotations")
        class Annotations(
            var automaticallyCreateModule: Boolean = false,
            var specifyUseCasePackageForComponentScan: Boolean = false
        ) :
            Koin(
                listOf(
                    "io.insert-koin:koin-annotations",
                    "io.insert-koin:koin-ksp-compiler"
                )
            )
    }

    @Serializable
    @SerialName("kodein")
    class Kodein(
        var useFactory: Boolean = true,
        var useSimpleFunctions: Boolean = true,
//        var binding: KodeinBinding = KodeinBinding(),
    ) :
        CleanWizardDependencyInjectionFramework(listOf("org.kodein.di:kodein-di"))

    @Serializable
    @SerialName("dagger")
    data object Dagger : CleanWizardDependencyInjectionFramework(listOf("javax.inject:javax.inject"))
}

@Serializable
sealed class KodeinBinding(
    val defaultFunctionImport: KodeinFunction,
    val shortFunctionImport: KodeinFunction,
) {
    data object Provider :
        KodeinBinding(
            KodeinFunction(
                CleanWizardImport.Kodein(name = "provider"), { content ->
                    defaultFunctionProvisionLambda("provider", content)
                }, listOf(bindImport, instanceImport)
            ),
            KodeinFunction(
                CleanWizardImport.Kodein(name = "bindProvider"),
                { content ->
                    "{ $content(instance()) }"
                },
                listOf(instanceImport)
            )
        )

    data object Singleton : KodeinBinding(
        KodeinFunction(
            CleanWizardImport.Kodein(name = "singleton"), { content ->
                defaultFunctionProvisionLambda("singleton", content)
            }, listOf(bindImport, instanceImport)
        ),
        KodeinFunction(
            CleanWizardImport.Kodein(name = "bindSingleton"),
            { content ->
                "{ $content(instance()) }"
            }, listOf(instanceImport)
        )
    )

    data object EagerSingleton : KodeinBinding(
        KodeinFunction(
            CleanWizardImport.Kodein(name = "eagerSingleton"), { content ->
                defaultFunctionProvisionLambda("eagerSingleton", content)
            }, listOf(bindImport, instanceImport)
        ),
        KodeinFunction(
            CleanWizardImport.Kodein(name = "bindEagerSingleton"),
            { content ->
                "{ $content(instance()) }"
            }, listOf(instanceImport)
        )
    )

    class Factory(private val definedType: String) : KodeinBinding(
        KodeinFunction(CleanWizardImport.Kodein(name = "factory"), { content ->
            "{ factory { $content } }"
        }, listOf(bindImport)),
        KodeinFunction(CleanWizardImport.Kodein(name = "bindFactory"), { content ->

        })
    )

    companion object {
        private val bindImport = CleanWizardImport.Kodein(name = "bind")
        private val instanceImport = CleanWizardImport.Kodein(name = "instance")
        private val defaultFunctionProvisionLambda: (functionName: String, content: String) -> String =
            { functionName, content ->
                "{ $functionName { $content(instance()) } }"
            }
    }
}

fun someFun() {
    KodeinBinding.Provider.shortFunctionImport.provisionLambda
}

@Serializable
data class KodeinFunction(
    val import: CleanWizardImport.Kodein,
    val provisionLambda: (content: String) -> String,
    val additionalImports: List<CleanWizardImport>,
) {
}

@Serializable
sealed class CleanWizardImport(
    @SerialName("packageName") open val packageName: String,
    @SerialName("name") open val name: String
) {

    @Serializable
    data class Kodein(
        @SerialName("kodeinPackageName") override val packageName: String = "org.kodein.di",
        @SerialName("kodeinName") override val name: String
    ) : CleanWizardImport(packageName, name)
}