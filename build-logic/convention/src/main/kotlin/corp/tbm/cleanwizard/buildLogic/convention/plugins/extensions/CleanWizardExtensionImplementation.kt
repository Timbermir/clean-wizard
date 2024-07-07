package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.*
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardInternalAPI
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardExtension
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardJsonSerializerBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardDataLayerConfigBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardDataLayerRoomConfigBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardDomainLayerConfigBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardPresentationLayerConfigBuilder

@OptIn(CleanWizardInternalAPI::class)
internal open class CleanWizardExtensionImplementation(
    override var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    internal var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization,
    internal var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
    internal val layerConfigs: CleanWizardLayerConfigWrapper = CleanWizardLayerConfigWrapper(),
) : CleanWizardExtension() {

    override fun `json-serializer`(block: CleanWizardJsonSerializerBuilder.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializerBuilderImplementation().apply(block).build()
    }

    override fun data(block: CleanWizardDataLayerConfigBuilder.() -> Unit) {
        CleanWizardDataLayerConfigBuilderImplementation(layerConfigs.data).apply(block)
    }

    override fun domain(block: CleanWizardDomainLayerConfigBuilder.() -> Unit) {
        CleanWizardDomainLayerConfigBuilderImplementation(layerConfigs.domain).apply(block)
    }

    override fun presentation(block: CleanWizardPresentationLayerConfigBuilder.() -> Unit) {
        CleanWizardPresentationLayerConfigBuilderImplementation(layerConfigs.presentation).apply(block)
    }

    override fun `dependency-injection`(block: CleanWizardDependencyInjectionFrameworkBuilder.() -> Unit) {
        dependencyInjectionFramework =
            CleanWizardDependencyInjectionFrameworkBuilderImplementation().apply(block).build()
    }
}

@CleanWizardInternalAPI
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

@CleanWizardInternalAPI
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

@CleanWizardInternalAPI
private class CleanWizardDataLayerConfigBuilderImplementation(private val cleanWizardLayerConfig: CleanWizardLayerConfig.Data) :
    CleanWizardDataLayerConfigBuilder(cleanWizardLayerConfig) {

    private val roomConfigBuilder = CleanWizardDataLayerRoomConfigBuilderImplementation()

    override var schemaSuffix: String
        get() = cleanWizardLayerConfig.schemaSuffix
        set(value) {
            cleanWizardLayerConfig.schemaSuffix = value
        }

    override var interfaceMapperName: String
        get() = cleanWizardLayerConfig.interfaceMapperName
        set(value) {
            cleanWizardLayerConfig.interfaceMapperName = value
        }

    override var toDomainMapFunctionName: String
        get() = cleanWizardLayerConfig.toDomainMapFunctionName
        set(value) {
            cleanWizardLayerConfig.toDomainMapFunctionName = value
        }

    override fun room(block: CleanWizardDataLayerRoomConfigBuilder.() -> Unit) {
        roomConfigBuilder.apply(block)
    }

    private inner class CleanWizardDataLayerRoomConfigBuilderImplementation :
        CleanWizardDataLayerRoomConfigBuilder() {

        override fun typeConverters(block: CleanWizardRoomTypeConvertersConfig.() -> Unit) {
            this@CleanWizardDataLayerConfigBuilderImplementation.cleanWizardLayerConfig.roomConfig.roomTypeConvertersConfig.apply(
                block
            )
        }
    }
}

@CleanWizardInternalAPI
private class CleanWizardDomainLayerConfigBuilderImplementation(private val domainLayerConfig: CleanWizardLayerConfig.Domain) :
    CleanWizardDomainLayerConfigBuilder(
        domainLayerConfig
    ) {
    override var toDTOMapFunctionName: String
        get() = domainLayerConfig.toDTOMapFunctionName
        set(value) {
            domainLayerConfig.toDTOMapFunctionName = value
        }
    override var toUIMapFunctionName: String
        get() = domainLayerConfig.toUIMapFunctionName
        set(value) {
            domainLayerConfig.toUIMapFunctionName = value
        }

    override fun useCase(block: CleanWizardUseCaseConfig.() -> Unit) {
        domainLayerConfig.useCaseConfig.apply(block)
    }
}

@CleanWizardInternalAPI
private class CleanWizardPresentationLayerConfigBuilderImplementation(private val presentationLayerConfig: CleanWizardLayerConfig.Presentation) :
    CleanWizardPresentationLayerConfigBuilder(presentationLayerConfig) {
    override var toDomainMapFunctionName: String
        get() = presentationLayerConfig.toDomainMapFunctionName
        set(value) {
            presentationLayerConfig.toDomainMapFunctionName = value
        }
}