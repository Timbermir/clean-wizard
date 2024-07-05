package corp.tbm.cleanwizard.buildLogic.config

import com.google.gson.*
import com.google.gson.stream.JsonReader
import kotlinx.serialization.Serializable
import java.text.DateFormat

@Serializable
data class CleanWizardGsonSerializationConfig(
    val longSerializationPolicy: LongSerializationPolicy = LongSerializationPolicy.DEFAULT,
    val fieldNamingPolicy: FieldNamingPolicy = FieldNamingPolicy.IDENTITY,
    val serializeNulls: Boolean = false,
    val datePattern: String? = null,
    val dateStyle: Int = DateFormat.DEFAULT,
    val timeStyle: Int = DateFormat.DEFAULT,
    val complexMapKeySerialization: Boolean = false,
    val serializeSpecialFloatingPointValues: Boolean = false,
    val htmlSafe: Boolean = true,
    val generateNonExecutableJson: Boolean = false,
    val strictness: Strictness? = null,
    val useJdkUnsafe: Boolean = true,
    val objectToNumberStrategy: CleanWizardToNumberStrategy = ToNumberPolicy.DOUBLE.toCleanWizardToNumberStrategy(),
    val numberToNumberStrategy: CleanWizardToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER.toCleanWizardToNumberStrategy()
) {
    fun toGson(): Gson {
        return GsonBuilder().apply {
            setLongSerializationPolicy(longSerializationPolicy)
            setFieldNamingPolicy(fieldNamingPolicy)
            if (serializeNulls) serializeNulls()
            setDateFormat(dateStyle, timeStyle)
            datePattern?.let(::setDateFormat)
            if (complexMapKeySerialization) enableComplexMapKeySerialization()
            if (serializeSpecialFloatingPointValues) serializeSpecialFloatingPointValues()
            if (!htmlSafe)
                disableHtmlEscaping()
            if (generateNonExecutableJson)
                generateNonExecutableJson()
            strictness?.let(::setStrictness)
            if (!useJdkUnsafe) disableJdkUnsafe()
            setObjectToNumberStrategy(objectToNumberStrategy)
            setNumberToNumberStrategy(numberToNumberStrategy)
        }.create()
    }

    @Serializable
    abstract class CleanWizardToNumberStrategy : ToNumberStrategy {
        abstract override fun readNumber(`in`: JsonReader): Number

        @Serializable
        companion object
    }
}

fun ToNumberStrategy.toCleanWizardToNumberStrategy(): CleanWizardGsonSerializationConfig.CleanWizardToNumberStrategy {

    return object : CleanWizardGsonSerializationConfig.CleanWizardToNumberStrategy() {
        override fun readNumber(`in`: JsonReader): Number {
            return this@toCleanWizardToNumberStrategy.readNumber(`in`)
        }
    }
}