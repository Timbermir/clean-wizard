package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.*
import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDsl

@CleanWizardConfigDsl
open class CleanWizardExtension(
    var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization,
    internal var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
    internal var layerConfigs: CleanWizardLayerConfigWrapper = CleanWizardLayerConfigWrapper(),
) {

    fun data(block: CleanWizardLayerConfig.Data.() -> Unit) {
        layerConfigs.data.apply(block)
    }

    fun domain(block: CleanWizardLayerConfig.Domain.() -> Unit) {
        layerConfigs.domain.apply(block)
    }

    fun presentation(block: CleanWizardLayerConfig.Presentation.() -> Unit) {
        layerConfigs.presentation.apply(block)
    }

    fun dependencyInjection(
        block: CleanWizardDependencyInjectionFrameworkBuilder.() -> Unit
    ) {
        dependencyInjectionFramework = CleanWizardDependencyInjectionFrameworkBuilder.apply(block).build()
    }
}

@CleanWizardConfigDsl
object CleanWizardDependencyInjectionFrameworkBuilder {

    private var framework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None

    fun koin(block: CleanWizardDependencyInjectionFramework.Koin.() -> Unit) {
        framework = CleanWizardDependencyInjectionFramework.Koin().apply(block)
    }

    fun koinAnnotations(block: CleanWizardDependencyInjectionFramework.KoinAnnotations.() -> Unit) {
        framework = CleanWizardDependencyInjectionFramework.KoinAnnotations().apply(block)
    }

    fun dagger() {
        framework = CleanWizardDependencyInjectionFramework.Dagger
    }

    internal fun build(): CleanWizardDependencyInjectionFramework {
        return framework
    }
}