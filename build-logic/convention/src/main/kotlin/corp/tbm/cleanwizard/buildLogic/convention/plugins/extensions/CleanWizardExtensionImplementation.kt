package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.*
import corp.tbm.cleanwizard.buildLogic.config.api.*

internal open class CleanWizardExtensionImplementation(
    override var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    internal var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization,
    internal var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
) : CleanWizardExtension() {

    private var data: CleanWizardLayerConfig.Data = CleanWizardLayerConfig.Data()
    private var domain: CleanWizardLayerConfig.Domain = CleanWizardLayerConfig.Domain()
    private var presentation: CleanWizardLayerConfig.Presentation = CleanWizardLayerConfig.Presentation()

    internal val layerConfigs by lazy {
        CleanWizardLayerConfigWrapper(data, domain, presentation)
    }

    override fun jsonSerializer(block: CleanWizardJsonSerializerBuilder.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializerBuilderImplementation().apply(block).build()
    }

    override fun data(block: CleanWizardDataLayerConfigBuilder.() -> Unit) {
        data = CleanWizardDataLayerConfigBuilderImplementation(data).apply(block).build()
    }

    override fun domain(block: CleanWizardDomainLayerConfigBuilder.() -> Unit) {
        domain = CleanWizardDomainLayerConfigBuilderImplementation(domain).apply(block).build()
    }

    override fun presentation(block: CleanWizardPresentationLayerConfigBuilder.() -> Unit) {
        presentation =
            CleanWizardPresentationLayerConfigBuilderImplementation(presentation).apply(block).build()
    }

    override fun dependencyInjection(block: CleanWizardDependencyInjectionFrameworkBuilder.() -> Unit) {
        dependencyInjectionFramework =
            CleanWizardDependencyInjectionFrameworkBuilderImplementation().apply(block).build()
    }
}

private class CleanWizardJsonSerializerBuilderImplementation : CleanWizardJsonSerializerBuilder() {

    override var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization

    override fun kotlinXSerialization(block: CleanWizardJsonSerializer.KotlinXSerialization.() -> Unit) {
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

private class CleanWizardDataLayerConfigBuilderImplementation(data: CleanWizardLayerConfig.Data) :
    CleanWizardDataLayerConfigBuilder(data) {

    private val roomConfigBuilder = CleanWizardDataLayerRoomConfigBuilderImplementation()

    override fun room(block: CleanWizardDataLayerRoomConfigBuilder.() -> Unit) {
        roomConfigBuilder.apply(block)
    }

    fun build(): CleanWizardLayerConfig.Data {
        return layerConfig.copy(
            moduleName,
            classSuffix,
            packageName,
            schemaSuffix,
            interfaceMapperName,
            toDomainMapFunctionName,
            roomConfigBuilder.build()
        )
    }

    private inner class CleanWizardDataLayerRoomConfigBuilderImplementation :
        CleanWizardDataLayerRoomConfigBuilder() {

        private val roomTypeConvertersConfigBuilder =
            CleanWizardDataLayerRoomTypeConvertersConfigBuilderImplementation()

        override fun typeConverters(block: CleanWizardDataLayerRoomTypeConvertersConfigBuilder.() -> Unit) {
            roomTypeConvertersConfigBuilder.apply(block)
        }

        fun build(): CleanWizardRoomConfig {
            return this@CleanWizardDataLayerConfigBuilderImplementation.layerConfig.roomConfig.copy(
                roomTypeConvertersConfigBuilder.build()
            )
        }

        private inner class CleanWizardDataLayerRoomTypeConvertersConfigBuilderImplementation :
            CleanWizardDataLayerRoomTypeConvertersConfigBuilder() {

            fun build(): CleanWizardRoomTypeConvertersConfig {
                return this@CleanWizardDataLayerConfigBuilderImplementation.layerConfig.roomConfig.roomTypeConvertersConfig.copy(
                    classSuffix,
                    packageName,
                    generateSeparateConverterForEachDTO,
                    useProvidedTypeConverter
                )
            }
        }
    }
}

private class CleanWizardDomainLayerConfigBuilderImplementation(domain: CleanWizardLayerConfig.Domain) :
    CleanWizardDomainLayerConfigBuilder(
        domain
    ) {

    private val cleanWizardUseCaseConfigBuilder = CleanWizardUseCaseConfigBuilderImplementation()

    override fun useCase(block: CleanWizardUseCaseConfigBuilder.() -> Unit) {
        cleanWizardUseCaseConfigBuilder.apply(block)
    }

    fun build(): CleanWizardLayerConfig.Domain {
        return layerConfig.copy(
            moduleName,
            classSuffix,
            packageName,
            toDTOMapFunctionName,
            toUIMapFunctionName,
            cleanWizardUseCaseConfigBuilder.build()
        )
    }

    private inner class CleanWizardUseCaseConfigBuilderImplementation : CleanWizardUseCaseConfigBuilder() {

        fun build(): CleanWizardUseCaseConfig {
            return this@CleanWizardDomainLayerConfigBuilderImplementation.layerConfig.useCaseConfig.copy(
                classSuffix,
                packageName,
                useCaseFunctionType,
                createWrapper
            )
        }
    }
}

private class CleanWizardPresentationLayerConfigBuilderImplementation(presentation: CleanWizardLayerConfig.Presentation) :
    CleanWizardPresentationLayerConfigBuilder(presentation) {

    fun build(): CleanWizardLayerConfig.Presentation {
        return layerConfig.copy(
            moduleName,
            classSuffix,
            packageName,
            toDomainMapFunctionName
        )
    }
}