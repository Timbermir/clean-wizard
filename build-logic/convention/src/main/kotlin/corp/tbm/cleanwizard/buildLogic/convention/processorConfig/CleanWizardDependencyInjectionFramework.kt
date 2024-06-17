package corp.tbm.cleanwizard.buildLogic.convention.processorConfig

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

enum class CleanWizardDependencyInjectionFramework(val dependencies: List<String>) {
    NONE(listOf()),
    KOIN(listOf("io.insert-koin:koin-core")),
    KOIN_ANNOTATIONS(listOf("io.insert-koin:koin-core", "io-insert-koin:koin-annotations")),
    DAGGER(listOf("javax.inject:javax.inject"));
}

@Serializable
sealed class DIFramework(@SerialName("deps") val dependencies: List<String>) {

    @Serializable
    @SerialName("None")
    @JsonClassDiscriminator("type")
    data object None : DIFramework(listOf())

    @Serializable
    @SerialName("Koin")
    @JsonClassDiscriminator("type")
    data class Koin(val useConstructorDSL: Boolean = true) : DIFramework(listOf("io.insert-koin:koin-core"))

    @Serializable
    @SerialName("KoinAnnotations")
    @JsonClassDiscriminator("type")
    class KoinAnnotations : DIFramework(listOf("io.insert-koin:koin-core", "io-insert-koin:koin-annotations"))

    @Serializable
    @SerialName("Dagger")
    @JsonClassDiscriminator("type")
    data object Dagger : DIFramework(listOf("javax.inject:javax.inject"))
}