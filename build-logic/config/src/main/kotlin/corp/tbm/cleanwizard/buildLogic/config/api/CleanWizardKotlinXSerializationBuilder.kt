package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.json.JsonNamingStrategy

@CleanWizardConfigDsl
abstract class CleanWizardKotlinXSerializationBuilder {

    abstract fun json(block: CleanWizardKotlinXSerializationConfigBuilder.() -> Unit)

    @CleanWizardConfigDsl
    abstract class CleanWizardKotlinXSerializationConfigBuilder {
        /**
         * Similar to [JsonBuilder.encodeDefaults]
         * @see [JsonBuilder.encodeDefaults]
         */
        abstract var encodeDefaults: Boolean

        /**
         * Similar to [JsonBuilder.ignoreUnknownKeys]
         * @see [JsonBuilder.ignoreUnknownKeys]
         */
        abstract var ignoreUnknownKeys: Boolean

        /**
         * Similar to [JsonBuilder.isLenient]
         * @see [JsonBuilder.isLenient]
         */
        abstract var isLenient: Boolean

        /**
         * Similar to [JsonBuilder.allowStructuredMapKeys]
         * @see [JsonBuilder.allowStructuredMapKeys]
         */
        abstract var allowStructuredMapKeys: Boolean

        /**
         * Similar to [JsonBuilder.prettyPrint]
         * @see [JsonBuilder.prettyPrint]
         */
        abstract var prettyPrint: Boolean

        /**
         * Similar to [JsonBuilder.explicitNulls]
         * @see [JsonBuilder.explicitNulls]
         */
        abstract var explicitNulls: Boolean

        /**
         * Similar to [JsonBuilder.prettyPrintIndent]
         * @see [JsonBuilder.prettyPrintIndent]
         */
        abstract var prettyPrintIndent: String

        /**
         * Similar to [JsonBuilder.coerceInputValues]
         * @see [JsonBuilder.coerceInputValues]
         */
        abstract var coerceInputValues: Boolean

        /**
         * Similar to [JsonBuilder.useArrayPolymorphism]
         * @see [JsonBuilder.useArrayPolymorphism]
         */
        abstract var useArrayPolymorphism: Boolean

        /**
         * Similar to [JsonBuilder.classDiscriminator]
         * @see [JsonBuilder.classDiscriminator]
         */
        abstract var classDiscriminator: String

        /**
         * Similar to [JsonBuilder.allowSpecialFloatingPointValues]
         * @see [JsonBuilder.allowSpecialFloatingPointValues]
         */
        abstract var allowSpecialFloatingPointValues: Boolean

        /**
         * Similar to [JsonBuilder.useAlternativeNames]
         * @see [JsonBuilder.useAlternativeNames]
         */
        abstract var useAlternativeNames: Boolean

        @OptIn(ExperimentalSerializationApi::class)
        /**
         * Similar to [JsonBuilder.namingStrategy]
         * @see [JsonBuilder.namingStrategy]
         */
        abstract var namingStrategy: JsonNamingStrategy?

        /**
         * Similar to [JsonBuilder.decodeEnumsCaseInsensitive]
         * @see [JsonBuilder.decodeEnumsCaseInsensitive]
         */
        abstract var decodeEnumsCaseInsensitive: Boolean

        /**
         * Similar to [JsonBuilder.allowTrailingComma]
         * @see [JsonBuilder.allowTrailingComma]
         */
        abstract var allowTrailingComma: Boolean

        /**
         * Similar to [JsonBuilder.allowComments]
         * @see [JsonBuilder.allowComments]
         */
        abstract var allowComments: Boolean

        /**
         * Similar to [JsonBuilder.classDiscriminatorMode]
         * @see [JsonBuilder.classDiscriminatorMode]
         */
        abstract var classDiscriminatorMode: ClassDiscriminatorMode
    }
}