package corp.tbm.cleanwizard.buildLogic.config

import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
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
    sealed class Koin(
        private val additionalDependencies: List<String> = listOf(),
        @SerialName("automatically-create-module") open val automaticallyCreateModule: Boolean
    ) :
        CleanWizardDependencyInjectionFramework(mutableListOf("io.insert-koin:koin-core").also {
            it.addAll(
                additionalDependencies
            )
        }) {

        @Serializable
        @SerialName("core")
        class Core(
            override val automaticallyCreateModule: Boolean,
            val useConstructorDSL: Boolean,
        ) : Koin(automaticallyCreateModule = automaticallyCreateModule)

        @Serializable
        @SerialName("annotations")
        class Annotations(
            override val automaticallyCreateModule: Boolean,
            val specifyUseCasePackageForComponentScan: Boolean,
        ) :
            Koin(
                listOf(
                    "io.insert-koin:koin-annotations",
                    "io.insert-koin:koin-ksp-compiler"
                ),
                automaticallyCreateModule
            )
    }

    @Serializable
    @SerialName("kodein")
    class Kodein(
        val useSimpleFunctions: Boolean,
        var binding: KodeinBinding
    ) :
        CleanWizardDependencyInjectionFramework(listOf("org.kodein.di:kodein-di")) {
        @Serializable
        @SerialName("kodein-binding")
        sealed class KodeinBinding(
            val longFunction: KodeinFunction,
            val shortFunction: KodeinFunction,
        ) {

            @Serializable
            data class KodeinFunction(
                val imports: List<CleanWizardImport.Kodein>,
                private val provisionLambda: String
            ) {

                fun getProvisionLambda(newContent: String): String {
                    return provisionLambda.replace("{content}", newContent)
                }
            }

            @Serializable
            @SerialName("provider")
            data object Provider :
                KodeinBinding(
                    KodeinFunction(
                        listOf(CleanWizardImport.Kodein(name = "provider"), bindImport, instanceImport),
                        getLongFunctionDefaultProvisionLambda("provider")
                    ),
                    KodeinFunction(
                        listOf(CleanWizardImport.Kodein(name = "bindProvider"), instanceImport),
                        getShortFunctionDefaultProvisionLambda("Provider")
                    )
                )

            @Serializable
            @SerialName("singleton")
            class Singleton(private val enableSync: Boolean = false) : KodeinBinding(
                KodeinFunction(
                    listOf(CleanWizardImport.Kodein(name = "singleton"), bindImport, instanceImport),
                    getLongFunctionDefaultProvisionLambda("singleton(sync = $enableSync)")
                ),
                KodeinFunction(
                    listOf(CleanWizardImport.Kodein(name = "bindSingleton"), instanceImport),
                    getShortFunctionDefaultProvisionLambda("Singleton(sync = $enableSync)")
                )
            )

            @Serializable
            @SerialName("eagerSingleton")
            data object EagerSingleton : KodeinBinding(
                KodeinFunction(
                    listOf(CleanWizardImport.Kodein(name = "eagerSingleton"), bindImport, instanceImport),
                    getLongFunctionDefaultProvisionLambda("eagerSingleton")
                ),
                KodeinFunction(
                    listOf(CleanWizardImport.Kodein(name = "bindEagerSingleton"), instanceImport),
                    getShortFunctionDefaultProvisionLambda("EagerSingleton")
                )
            )

            @Serializable
            @SerialName("multiton")
            class Multiton(private val enableSync: Boolean = false) : KodeinBinding(
                KodeinFunction(
                    listOf(
                        CleanWizardImport.Kodein(name = "multiton"),
                        bindImport
                    ),
                    "bind { multiton(sync = $enableSync) { {content} } }"
                ),
                KodeinFunction(
                    listOf(CleanWizardImport.Kodein(name = "bindMultiton")),
                    "bindMultiton(sync = $enableSync) { {content} }"
                )
            )

            @Serializable
            @SerialName("factory")
            data object Factory : KodeinBinding(
                KodeinFunction(
                    listOf(CleanWizardImport.Kodein(name = "factory"), bindImport),
                    "bind { factory { {content} } }"
                ),
                KodeinFunction(
                    listOf(CleanWizardImport.Kodein(name = "bindFactory")),
                    "bindFactory { {content} } "
                )
            )

            companion object {
                private val bindImport = CleanWizardImport.Kodein(name = "bind")
                private val instanceImport = CleanWizardImport.Kodein(name = "instance")

                private fun getLongFunctionDefaultProvisionLambda(functionName: String): String {
                    return "bind { $functionName { {content}(instance()) } }"
                }

                private fun getShortFunctionDefaultProvisionLambda(functionName: String): String {
                    return "bind$functionName { {content}(instance()) } "
                }
            }
        }
    }

    @Serializable
    @SerialName("dagger")
    data object Dagger : CleanWizardDependencyInjectionFramework(listOf("javax.inject:javax.inject"))
}