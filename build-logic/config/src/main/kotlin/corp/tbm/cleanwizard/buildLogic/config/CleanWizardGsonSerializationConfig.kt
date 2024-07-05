package corp.tbm.cleanwizard.buildLogic.config

import com.google.gson.*
import com.google.gson.internal.Excluder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.serialization.Serializable
import java.text.DateFormat

@Serializable
data class CleanWizardGsonSerializationConfig(
    val excluder: Excluder = Excluder.DEFAULT,
    val longSerializationPolicy: LongSerializationPolicy = LongSerializationPolicy.DEFAULT,
    val fieldNamingPolicy: FieldNamingPolicy = FieldNamingPolicy.IDENTITY,
//    var instanceCreators: Map<Type, InstanceCreator<*>> = HashMap()
    val serializeNulls: Boolean = false,
    val datePattern: String? = null,
    val dateStyle: Int = DateFormat.DEFAULT,
    val timeStyle: Int = DateFormat.DEFAULT,
    val complexMapKeySerialization: Boolean = false,
    val serializeSpecialFloatingPointValues: Boolean = false,
    val htmlSafe: Boolean = true,
//    var formattingStyle: FormattingStyle = FormattingStyle.COMPACT,
    val generateNonExecutableJson: Boolean = false,
    val strictness: Strictness? = null,
    val useJdkUnsafe: Boolean = true,
    val objectToNumberStrategy: CleanWizardToNumberStrategy = ToNumberPolicy.DOUBLE.toCleanWizardToNumberStrategy(),
    val numberToNumberStrategy: CleanWizardToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER.toCleanWizardToNumberStrategy()
//    var reflectionFilters: ArrayDeque<ReflectionAccessFilter> = ArrayDeque<ReflectionAccessFilter>()
)

abstract class CleanWizardGsonExcluder : TypeAdapterFactory, Cloneable {

}

fun Excluder.toCleanWizardGsonExcluder(): CleanWizardGsonExcluder {
    return object : CleanWizardGsonExcluder() {
        override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T> {
            return this@toCleanWizardGsonExcluder.create(gson, type)
        }
    }
}

fun CleanWizardGsonSerializationConfig.toGson(): Gson {
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

fun ToNumberStrategy.toCleanWizardToNumberStrategy(): CleanWizardToNumberStrategy {

    return object : CleanWizardToNumberStrategy() {
        override fun readNumber(`in`: JsonReader): Number {
            return this@toCleanWizardToNumberStrategy.readNumber(`in`)
        }
    }
}

/**
 * Builds [CleanWizardGsonSerializationConfig] from [Gson] instance
 * using [Reflection](https://www.oracle.com/technical-resources/articles/java/javareflection.html)
 */
fun Gson.toCleanWizardGsonSerializationConfig(): CleanWizardGsonSerializationConfig {
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
