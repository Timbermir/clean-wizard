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
        var encodeDefaults: Boolean = false

        /**
         * Similar to [JsonBuilder.ignoreUnknownKeys]
         * @see [JsonBuilder.ignoreUnknownKeys]
         */
        var ignoreUnknownKeys: Boolean = false

        /**
         * Similar to [JsonBuilder.isLenient]
         * @see [JsonBuilder.isLenient]
         */
        var isLenient: Boolean = false

        /**
         * Similar to [JsonBuilder.allowStructuredMapKeys]
         * @see [JsonBuilder.allowStructuredMapKeys]
         */
        var allowStructuredMapKeys: Boolean = false

        /**
         * Similar to [JsonBuilder.prettyPrint]
         * @see [JsonBuilder.prettyPrint]
         */
        var prettyPrint: Boolean = false

        /**
         * Similar to [JsonBuilder.explicitNulls]
         * @see [JsonBuilder.explicitNulls]
         */
        var explicitNulls: Boolean = true

        /**
         * Similar to [JsonBuilder.prettyPrintIndent]
         * @see [JsonBuilder.prettyPrintIndent]
         */
        var prettyPrintIndent: String = "    "

        /**
         * Similar to [JsonBuilder.coerceInputValues]
         * @see [JsonBuilder.coerceInputValues]
         */
        var coerceInputValues: Boolean = false

        /**
         * Similar to [JsonBuilder.useArrayPolymorphism]
         * @see [JsonBuilder.useArrayPolymorphism]
         */
        var useArrayPolymorphism: Boolean = false

        /**
         * Similar to [JsonBuilder.classDiscriminator]
         * @see [JsonBuilder.classDiscriminator]
         */
        var classDiscriminator: String = "type"

        /**
         * Similar to [JsonBuilder.allowSpecialFloatingPointValues]
         * @see [JsonBuilder.allowSpecialFloatingPointValues]
         */
        var allowSpecialFloatingPointValues: Boolean = false

        /**
         * Similar to [JsonBuilder.useAlternativeNames]
         * @see [JsonBuilder.useAlternativeNames]
         */
        var useAlternativeNames: Boolean = true

        @OptIn(ExperimentalSerializationApi::class)
                /**
                 * Similar to [JsonBuilder.namingStrategy]
                 * @see [JsonBuilder.namingStrategy]
                 */
        var namingStrategy: JsonNamingStrategy? = null

        /**
         * Similar to [JsonBuilder.decodeEnumsCaseInsensitive]
         * @see [JsonBuilder.decodeEnumsCaseInsensitive]
         */
        var decodeEnumsCaseInsensitive: Boolean = false

        /**
         * Similar to [JsonBuilder.allowTrailingComma]
         * @see [JsonBuilder.allowTrailingComma]
         */
        var allowTrailingComma: Boolean = false

        /**
         * Similar to [JsonBuilder.allowComments]
         * @see [JsonBuilder.allowComments]
         */
        var allowComments: Boolean = false

        /**
         * Similar to [JsonBuilder.classDiscriminatorMode]
         * @see [JsonBuilder.classDiscriminatorMode]
         */
        var classDiscriminatorMode: ClassDiscriminatorMode = ClassDiscriminatorMode.POLYMORPHIC
    }
}