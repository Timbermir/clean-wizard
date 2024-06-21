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
    @SerialName("dagger")
    data object Dagger : CleanWizardDependencyInjectionFramework(listOf("javax.inject:javax.inject"))
}