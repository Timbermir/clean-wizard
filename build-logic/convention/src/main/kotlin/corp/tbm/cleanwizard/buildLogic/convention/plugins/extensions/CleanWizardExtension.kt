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
        dependencyInjectionFramework = CleanWizardDependencyInjectionFrameworkBuilder().apply(block).build()
    }
}

@CleanWizardConfigDsl
class CleanWizardDependencyInjectionFrameworkBuilder {

    private var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework =
        CleanWizardDependencyInjectionFramework.None

    fun koin(block: KoinBuilder.() -> Unit) {
        KoinBuilder().apply(block)
    }

    fun kodein(block: CleanWizardDependencyInjectionFramework.Kodein.() -> Unit) {
        dependencyInjectionFramework = CleanWizardDependencyInjectionFramework.Kodein().apply(block)
    }

    fun dagger() {
        dependencyInjectionFramework = CleanWizardDependencyInjectionFramework.Dagger
    }

    internal fun build(): CleanWizardDependencyInjectionFramework {
        return dependencyInjectionFramework
    }

    @CleanWizardConfigDsl
    inner class KoinBuilder {

        fun core(block: CleanWizardDependencyInjectionFramework.Koin.Core.() -> Unit) {
            this@CleanWizardDependencyInjectionFrameworkBuilder.dependencyInjectionFramework =
                CleanWizardDependencyInjectionFramework.Koin.Core().apply(block)
        }

        fun annotations(block: CleanWizardDependencyInjectionFramework.Koin.Annotations.() -> Unit) {
            this@CleanWizardDependencyInjectionFrameworkBuilder.dependencyInjectionFramework =
                CleanWizardDependencyInjectionFramework.Koin.Annotations().apply(block)
        }
    }
}