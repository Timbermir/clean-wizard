package corp.tbm.cleanwizard.buildLogic.convention.processorConfig

enum class CleanWizardDependencyInjectionFramework(val dependencies: List<String>) {
    NONE(listOf()),
    KOIN(listOf("io.insert-koin:koin-core")),
    KOIN_ANNOTATIONS(listOf("io.insert-koin:koin-core", "io-insert-koin:koin-annotations")),
    DAGGER(listOf("javax.inject:javax.inject"));
}