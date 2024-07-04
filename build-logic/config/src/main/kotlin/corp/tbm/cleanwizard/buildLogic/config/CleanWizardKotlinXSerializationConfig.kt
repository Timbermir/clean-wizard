package corp.tbm.cleanwizard.buildLogic.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@Serializable
data class CleanWizardKotlinXSerializationConfig(
    val encodeDefaults: Boolean = false,
    val ignoreUnknownKeys: Boolean = false,
    val isLenient: Boolean = false,
    val allowStructuredMapKeys: Boolean = false,
    val prettyPrint: Boolean = false,
    val explicitNulls: Boolean = true,
    val prettyPrintIndent: String = "    ",
    val coerceInputValues: Boolean = false,
    val useArrayPolymorphism: Boolean = false,
    val classDiscriminator: String = "type",
    val allowSpecialFloatingPointValues: Boolean = false,
    val useAlternativeNames: Boolean = true,
    val namingStrategy: CleanWizardKotlinXSerializationJsonNamingStrategy? = null,
    val decodeEnumsCaseInsensitive: Boolean = false,
    val allowTrailingComma: Boolean = false,
    val allowComments: Boolean = false,
    val classDiscriminatorMode: ClassDiscriminatorMode = ClassDiscriminatorMode.POLYMORPHIC,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
abstract class CleanWizardKotlinXSerializationJsonNamingStrategy : JsonNamingStrategy {
    @Serializable
    companion object
}

@OptIn(ExperimentalSerializationApi::class)
fun JsonNamingStrategy.toCleanWizardKotlinXSerializationJsonNamingStrategy(): CleanWizardKotlinXSerializationJsonNamingStrategy {

    return object : CleanWizardKotlinXSerializationJsonNamingStrategy() {

        override fun serialNameForJson(descriptor: SerialDescriptor, elementIndex: Int, serialName: String): String {
            return this@toCleanWizardKotlinXSerializationJsonNamingStrategy.serialNameForJson(
                descriptor,
                elementIndex,
                serialName
            )
        }

        override fun toString(): String {
            return this@toCleanWizardKotlinXSerializationJsonNamingStrategy.toString()
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun CleanWizardKotlinXSerializationConfig.toJson(): Json {
    val config = this
    return Json {
        encodeDefaults = config.encodeDefaults
        ignoreUnknownKeys = config.ignoreUnknownKeys
        isLenient = config.isLenient
        allowStructuredMapKeys = config.allowStructuredMapKeys
        prettyPrint = config.prettyPrint
        explicitNulls = config.explicitNulls
        prettyPrintIndent = config.prettyPrintIndent
        coerceInputValues = config.coerceInputValues
        useArrayPolymorphism = config.useArrayPolymorphism
        classDiscriminator = config.classDiscriminator
        allowSpecialFloatingPointValues = config.allowSpecialFloatingPointValues
        useAlternativeNames = config.useAlternativeNames
        namingStrategy = config.namingStrategy
        decodeEnumsCaseInsensitive = config.decodeEnumsCaseInsensitive
        allowTrailingComma = config.allowTrailingComma
        allowComments = config.allowComments
        classDiscriminatorMode = config.classDiscriminatorMode
    }
}