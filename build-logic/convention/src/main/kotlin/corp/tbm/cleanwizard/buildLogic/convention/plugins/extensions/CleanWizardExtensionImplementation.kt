package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.*
import corp.tbm.cleanwizard.buildLogic.config.api.ICleanWizardDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.ICleanWizardExtension
import corp.tbm.cleanwizard.buildLogic.config.api.ICleanWizardJsonSerializerBuilder

internal open class CleanWizardExtension(
    override var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    internal var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization,
    internal var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
    internal val layerConfigs: CleanWizardLayerConfigWrapper = CleanWizardLayerConfigWrapper(),
) : ICleanWizardExtension() {

    override fun `json-serializer`(block: ICleanWizardJsonSerializerBuilder.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializerBuilder().apply(block).build()
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

    override fun `dependency-injection`(block: ICleanWizardDependencyInjectionFrameworkBuilder.() -> Unit) {
        dependencyInjectionFramework = CleanWizardDependencyInjectionFrameworkBuilder().apply(block).build()
    }
}

private class CleanWizardJsonSerializerBuilder : ICleanWizardJsonSerializerBuilder() {

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

private class CleanWizardDependencyInjectionFrameworkBuilder : ICleanWizardDependencyInjectionFrameworkBuilder() {

    override var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework =
        CleanWizardDependencyInjectionFramework.None

    override fun koin(block: IKoinBuilder.() -> Unit) {
        KoinBuilder().apply(block)
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

    private inner class KoinBuilder : IKoinBuilder() {

        override fun core(block: CleanWizardDependencyInjectionFramework.Koin.Core.() -> Unit) {
            this@CleanWizardDependencyInjectionFrameworkBuilder.dependencyInjectionFramework =
                CleanWizardDependencyInjectionFramework.Koin.Core().apply(block)
        }

        override fun annotations(block: CleanWizardDependencyInjectionFramework.Koin.Annotations.() -> Unit) {
            this@CleanWizardDependencyInjectionFrameworkBuilder.dependencyInjectionFramework =
                CleanWizardDependencyInjectionFramework.Koin.Annotations().apply(block)
        }
    }
}