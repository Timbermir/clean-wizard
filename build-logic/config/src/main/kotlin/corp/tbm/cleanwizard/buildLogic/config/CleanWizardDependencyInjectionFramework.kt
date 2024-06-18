package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class CleanWizardDependencyInjectionFramework(val dependencies: List<String>) {

    @Serializable
    @SerialName("none")
    data object None : CleanWizardDependencyInjectionFramework(listOf())

    @Serializable
    @SerialName("koin")
    data class Koin(val useConstructorDSL: Boolean = true) :
        CleanWizardDependencyInjectionFramework(listOf("io.insert-koin:koin-core"))

    @Serializable
    @SerialName("koin-annotations")
    data object KoinAnnotations :
        CleanWizardDependencyInjectionFramework(listOf("io.insert-koin:koin-core", "io-insert-koin:koin-annotations"))

    @Serializable
    @SerialName("dagger")
    data object Dagger : CleanWizardDependencyInjectionFramework(listOf("javax.inject:javax.inject"))
}