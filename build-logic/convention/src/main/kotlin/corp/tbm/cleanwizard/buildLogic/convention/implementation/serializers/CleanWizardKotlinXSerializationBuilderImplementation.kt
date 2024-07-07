package corp.tbm.cleanwizard.buildLogic.convention.implementation.serializers

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardKotlinXSerializationConfig
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardKotlinXSerializationBuilder
import corp.tbm.cleanwizard.buildLogic.config.toCleanWizardKotlinXSerializationJsonNamingStrategy
import kotlinx.serialization.ExperimentalSerializationApi

internal class CleanWizardKotlinXSerializationBuilderImplementation : CleanWizardKotlinXSerializationBuilder() {

    private val kotlinXSerializationConfigBuilder = CleanWizardKotlinXSerializationConfigBuilderImplementation()

    override fun json(block: CleanWizardKotlinXSerializationConfigBuilder.() -> Unit) {
        kotlinXSerializationConfigBuilder.apply(block)
    }

    internal fun build(): CleanWizardJsonSerializer.KotlinXSerialization {
        return CleanWizardJsonSerializer.KotlinXSerialization(
            kotlinXSerializationConfigBuilder.build()
        )
    }

    private class CleanWizardKotlinXSerializationConfigBuilderImplementation :
        CleanWizardKotlinXSerializationConfigBuilder() {

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
}