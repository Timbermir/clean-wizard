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

    @Serializable
    @SerialName("koin")
    class Koin(
        var automaticallyCreateModule: Boolean = true,
        var useConstructorDSL: Boolean = true,
    ) : CleanWizardDependencyInjectionFramework(listOf("io.insert-koin:koin-core"))

    @Serializable
    @SerialName("koin-annotations")
    class KoinAnnotations(var automaticallyCreateModule: Boolean = false) :
        CleanWizardDependencyInjectionFramework(listOf("io.insert-koin:koin-core", "io.insert-koin:koin-annotations"))


    @Serializable
    @SerialName("dagger")
    data object Dagger : CleanWizardDependencyInjectionFramework(listOf("javax.inject:javax.inject"))
}