package corp.tbm.cleanwizard.buildLogic.convention.implementation.serializers

import com.google.gson.*
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardGsonSerializationConfig
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardIncubatingAPI
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardGsonSerializationBuilder
import corp.tbm.cleanwizard.buildLogic.config.toCleanWizardToNumberStrategy

internal class CleanWizardGsonSerializationBuilderImplementation : CleanWizardGsonSerializationBuilder() {

    @OptIn(CleanWizardIncubatingAPI::class)
    internal fun build(): CleanWizardJsonSerializer.Gson {
        return CleanWizardJsonSerializer.Gson(
            gson?.toCleanWizardGsonSerializationConfig() ?: CleanWizardGsonSerializationConfig(
                longSerializationPolicy,
                fieldNamingPolicy,
                serializeNulls,
                datePattern,
                dateStyle,
                timeStyle,
                complexMapKeySerialization,
                serializeSpecialFloatingPointValues,
                htmlSafe,
                generateNonExecutableJson,
                strictness,
                useJdkUnsafe,
                objectToNumberStrategy.toCleanWizardToNumberStrategy(),
                numberToNumberStrategy.toCleanWizardToNumberStrategy()
            )
        )
    }
}

/**
 * Builds [CleanWizardGsonSerializationConfig] from [Gson] instance
 * using [Reflection](https://www.oracle.com/technical-resources/articles/java/javareflection.html)
 */
private fun Gson.toCleanWizardGsonSerializationConfig(): CleanWizardGsonSerializationConfig {
    try {
        val gsonClass: Class<*> = this::class.java

        fun getFieldValue(fieldName: String): Any? {
            return try {
                val field = gsonClass.getDeclaredField(fieldName)
                field.isAccessible = true
                field.get(this)
            } catch (e: NoSuchFieldException) {
                null
            }
        }

        val longSerializationPolicy = getFieldValue("longSerializationPolicy") as LongSerializationPolicy
        val fieldNamingPolicy = getFieldValue("fieldNamingPolicy") as FieldNamingPolicy? ?: FieldNamingPolicy.IDENTITY
        val serializeNulls = getFieldValue("serializeNulls") as Boolean
        val dateStyle = getFieldValue("dateStyle") as Int
        val timeStyle = getFieldValue("timeStyle") as Int
        val datePattern = getFieldValue("datePattern") as String?
        val complexMapKeySerialization = getFieldValue("complexMapKeySerialization") as Boolean
        val serializeSpecialFloatingPointValues =
            getFieldValue("serializeSpecialFloatingPointValues") as Boolean
        val htmlSafe = getFieldValue("htmlSafe") as Boolean? ?: true
        val generateNonExecutableJson = getFieldValue("generateNonExecutableJson") as Boolean
        val strictness = getFieldValue("strictness") as Strictness?
        val useJdkUnsafe = getFieldValue("useJdkUnsafe") as Boolean
        val objectToNumberStrategy =
            getFieldValue("objectToNumberStrategy") as ToNumberStrategy
        val numberToNumberStrategy =
            getFieldValue("numberToNumberStrategy") as ToNumberStrategy

        return CleanWizardGsonSerializationConfig(
            longSerializationPolicy,
            fieldNamingPolicy,
            serializeNulls,
            datePattern,
            dateStyle,
            timeStyle,
            complexMapKeySerialization,
            serializeSpecialFloatingPointValues,
            htmlSafe,
            generateNonExecutableJson,
            strictness,
            useJdkUnsafe,
            objectToNumberStrategy.toCleanWizardToNumberStrategy(),
            numberToNumberStrategy.toCleanWizardToNumberStrategy()
        )
    } catch (e: Exception) {
        e.printStackTrace()
        throw RuntimeException("Failed to extract configuration from Gson instance", e)
    }
}