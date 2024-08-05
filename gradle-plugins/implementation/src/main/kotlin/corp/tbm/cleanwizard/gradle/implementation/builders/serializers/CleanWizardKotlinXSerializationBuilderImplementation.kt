package corp.tbm.cleanwizard.gradle.implementation.builders.serializers

import corp.tbm.cleanwizard.gradle.api.builders.jsonSerializers.CleanWizardKotlinXSerializationBuilder
import corp.tbm.cleanwizard.gradle.api.config.serializer.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.gradle.api.config.serializer.CleanWizardKotlinXSerializationConfig
import corp.tbm.cleanwizard.gradle.api.config.serializer.toCleanWizardKotlinXSerializationJsonNamingStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.JsonNamingStrategy

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
        override var encodeDefaults: Boolean = false

        override var ignoreUnknownKeys: Boolean = false

        override var isLenient: Boolean = false

        override var allowStructuredMapKeys: Boolean = false

        override var prettyPrint: Boolean = false

        override var explicitNulls: Boolean = true

        override var prettyPrintIndent: String = "    "

        override var coerceInputValues: Boolean = false

        override var useArrayPolymorphism: Boolean = false

        override var classDiscriminator: String = "type"

        override var allowSpecialFloatingPointValues: Boolean = false

        override var useAlternativeNames: Boolean = true

        @OptIn(ExperimentalSerializationApi::class)
        override var namingStrategy: JsonNamingStrategy? = null

        override var decodeEnumsCaseInsensitive: Boolean = false

        override var allowTrailingComma: Boolean = false

        override var allowComments: Boolean = false

        override var classDiscriminatorMode: ClassDiscriminatorMode = ClassDiscriminatorMode.POLYMORPHIC

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