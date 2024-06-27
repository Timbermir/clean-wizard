package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.*
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardExtension
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardJsonSerializerBuilder

internal open class CleanWizardExtensionImplementation(
    override var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    internal var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization,
    internal var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
    internal val layerConfigs: CleanWizardLayerConfigWrapper = CleanWizardLayerConfigWrapper(),
) : CleanWizardExtension() {

    override fun `json-serializer`(block: CleanWizardJsonSerializerBuilder.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializerBuilderImplementation().apply(block).build()
    }

    override fun data(block: CleanWizardLayerConfig.Data.() -> Unit) {
        layerConfigs.data.apply(block)
    }

    override fun domain(block: CleanWizardLayerConfig.Domain.() -> Unit) {
        layerConfigs.domain.apply(block)
    }

    override fun presentation(block: CleanWizardLayerConfig.Presentation.() -> Unit) {
        layerConfigs.presentation.apply(block)
    }

    override fun `dependency-injection`(block: CleanWizardDependencyInjectionFrameworkBuilder.() -> Unit) {
        dependencyInjectionFramework =
            CleanWizardDependencyInjectionFrameworkBuilderImplementation().apply(block).build()
    }
}

private class CleanWizardJsonSerializerBuilderImplementation : CleanWizardJsonSerializerBuilder() {

    override var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization

    override fun `kotlinx-serialization`(block: CleanWizardJsonSerializer.KotlinXSerialization.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization.apply(block)
    }

    override fun gson(block: CleanWizardJsonSerializer.Gson.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializer.Gson.apply(block)

    }

    override fun moshi(block: CleanWizardJsonSerializer.Moshi.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializer.Moshi.apply(block)
    }

    fun build(): CleanWizardJsonSerializer {
        return jsonSerializer
    }
}

private class CleanWizardDependencyInjectionFrameworkBuilderImplementation :
    CleanWizardDependencyInjectionFrameworkBuilder() {

    override var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework =
        CleanWizardDependencyInjectionFramework.None

    override fun koin(block: KoinBuilder.() -> Unit) {
        KoinBuilderImplementation().apply(block)
    }

    override fun kodein(block: CleanWizardDependencyInjectionFramework.Kodein.() -> Unit) {
        dependencyInjectionFramework = CleanWizardDependencyInjectionFramework.Kodein().apply(block)
    }

    override fun dagger() {
        dependencyInjectionFramework = CleanWizardDependencyInjectionFramework.Dagger
    }

    fun build(): CleanWizardDependencyInjectionFramework {
        return dependencyInjectionFramework
    }

    private inner class KoinBuilderImplementation : KoinBuilder() {

        override fun core(block: CleanWizardDependencyInjectionFramework.Koin.Core.() -> Unit) {
            this@CleanWizardDependencyInjectionFrameworkBuilderImplementation.dependencyInjectionFramework =
                CleanWizardDependencyInjectionFramework.Koin.Core().apply(block)
        }

        override fun annotations(block: CleanWizardDependencyInjectionFramework.Koin.Annotations.() -> Unit) {
            this@CleanWizardDependencyInjectionFrameworkBuilderImplementation.dependencyInjectionFramework =
                CleanWizardDependencyInjectionFramework.Koin.Annotations().apply(block)
        }
    }
}