package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.JsonNamingStrategy

@CleanWizardConfigDsl
abstract class KotlinXSerializationBuilder {

    abstract fun json(block: KotlinXSerializationConfigBuilder.() -> Unit)
}

@CleanWizardConfigDsl
abstract class KotlinXSerializationConfigBuilder {
    var encodeDefaults: Boolean = false
    var ignoreUnknownKeys: Boolean = false
    var isLenient: Boolean = false
    var allowStructuredMapKeys: Boolean = false
    var prettyPrint: Boolean = false
    var explicitNulls: Boolean = true
    var prettyPrintIndent: String = "    "
    var coerceInputValues: Boolean = false
    var useArrayPolymorphism: Boolean = false
    var classDiscriminator: String = "type"
    var allowSpecialFloatingPointValues: Boolean = false
    var useAlternativeNames: Boolean = true

    @OptIn(ExperimentalSerializationApi::class)
    var namingStrategy: JsonNamingStrategy? = null
    var decodeEnumsCaseInsensitive: Boolean = false
    var allowTrailingComma: Boolean = false
    var allowComments: Boolean = false
    var classDiscriminatorMode: ClassDiscriminatorMode = ClassDiscriminatorMode.POLYMORPHIC
}