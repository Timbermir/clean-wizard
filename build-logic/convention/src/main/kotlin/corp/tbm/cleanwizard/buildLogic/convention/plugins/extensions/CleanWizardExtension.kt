package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.*
import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDsl

@CleanWizardConfigDsl
open class CleanWizardExtension(
    var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    internal var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization,
    internal var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
    internal var layerConfigs: CleanWizardLayerConfigWrapper = CleanWizardLayerConfigWrapper(),
) {

    fun `json-serializer`(block: CleanWizardJsonSerializerBuilder.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializerBuilder().apply(block).build()
    }

    fun data(block: CleanWizardLayerConfig.Data.() -> Unit) {
        layerConfigs.data.apply(block)
    }

    fun domain(block: CleanWizardLayerConfig.Domain.() -> Unit) {
        layerConfigs.domain.apply(block)
    }

    fun presentation(block: CleanWizardLayerConfig.Presentation.() -> Unit) {
        layerConfigs.presentation.apply(block)
    }

    fun `dependency-injection`(
        block: CleanWizardDependencyInjectionFrameworkBuilder.() -> Unit
    ) {
        dependencyInjectionFramework = CleanWizardDependencyInjectionFrameworkBuilder().apply(block).build()
    }
}

@CleanWizardConfigDsl
class CleanWizardJsonSerializerBuilder {
    private var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization

    fun `kotlinx-serialization`(block: CleanWizardJsonSerializer.KotlinXSerialization.() -> Unit = {}) {
        jsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization.apply(block)
    }

    fun gson(block: CleanWizardJsonSerializer.Gson.() -> Unit = {}) {
        jsonSerializer = CleanWizardJsonSerializer.Gson.apply(block)

    }

    fun moshi(block: CleanWizardJsonSerializer.Moshi.() -> Unit = {}) {
        jsonSerializer = CleanWizardJsonSerializer.Moshi.apply(block)
    }

    internal fun build(): CleanWizardJsonSerializer {
        return jsonSerializer
    }
}

@CleanWizardConfigDsl
class CleanWizardDependencyInjectionFrameworkBuilder {

    private var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework =
        CleanWizardDependencyInjectionFramework.None

    fun koin(block: KoinBuilder.() -> Unit) {
        KoinBuilder().apply(block)
    }

    fun kodein(block: CleanWizardDependencyInjectionFramework.Kodein.() -> Unit = {}) {
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

        fun core(block: CleanWizardDependencyInjectionFramework.Koin.Core.() -> Unit = {}) {
            this@CleanWizardDependencyInjectionFrameworkBuilder.dependencyInjectionFramework =
                CleanWizardDependencyInjectionFramework.Koin.Core().apply(block)
        }

        fun annotations(block: CleanWizardDependencyInjectionFramework.Koin.Annotations.() -> Unit = {}) {
            this@CleanWizardDependencyInjectionFrameworkBuilder.dependencyInjectionFramework =
                CleanWizardDependencyInjectionFramework.Koin.Annotations().apply(block)
        }
    }
}