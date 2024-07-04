package corp.tbm.cleanwizard.buildLogic.convention.implementation

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardKotlinXSerializationConfig
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardJsonSerializerBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.GsonSerializationConfigBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.KotlinXSerializationBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.KotlinXSerializationConfigBuilder
import corp.tbm.cleanwizard.buildLogic.config.toCleanWizardKotlinXSerializationJsonNamingStrategy
import kotlinx.serialization.ExperimentalSerializationApi

internal class CleanWizardJsonSerializerBuilderImplementation : CleanWizardJsonSerializerBuilder() {

    override var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization()

    private val kotlinXSerializationBuilder = KotlinXSerializationBuilderImplementation()

    private val gsonSerializationBuilder = GsonSerializationBuilderImplementation()

    override fun kotlinXSerialization(block: KotlinXSerializationBuilder.() -> Unit) {
        jsonSerializer = kotlinXSerializationBuilder.apply(block).build()
    }

    override fun gson(block: GsonSerializationConfigBuilder.() -> Unit) {
        jsonSerializer = gsonSerializationBuilder.apply(block).build()
    }

    override fun moshi(block: CleanWizardJsonSerializer.Moshi.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializer.Moshi.apply(block)
    }

    internal fun build(): CleanWizardJsonSerializer {
        return jsonSerializer
    }
}

private class KotlinXSerializationBuilderImplementation : KotlinXSerializationBuilder() {

    private val kotlinXSerializationConfigBuilder = KotlinXSerializationConfigBuilderImplementation()

    override fun json(block: KotlinXSerializationConfigBuilder.() -> Unit) {
        kotlinXSerializationConfigBuilder.apply(block)
    }

    fun build(): CleanWizardJsonSerializer.KotlinXSerialization {
        return CleanWizardJsonSerializer.KotlinXSerialization(
            kotlinXSerializationConfigBuilder.build()
        )
    }
}

private class KotlinXSerializationConfigBuilderImplementation : KotlinXSerializationConfigBuilder() {

    @OptIn(ExperimentalSerializationApi::class)
    fun build(): CleanWizardKotlinXSerializationConfig {
        return CleanWizardKotlinXSerializationConfig(
            encodeDefaults,
            ignoreUnknownKeys,
            isLenient,
            allowStructuredMapKeys,
            prettyPrint,
            explicitNulls,
            prettyPrintIndent,
            coerceInputValues,
            useArrayPolymorphism,
            classDiscriminator,
            allowSpecialFloatingPointValues,
            useAlternativeNames,
            namingStrategy?.toCleanWizardKotlinXSerializationJsonNamingStrategy(),
            decodeEnumsCaseInsensitive,
            allowTrailingComma,
            allowComments,
            classDiscriminatorMode
        )
    }
}

private class GsonSerializationBuilderImplementation : GsonSerializationConfigBuilder() {
}