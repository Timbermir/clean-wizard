package corp.tbm.cleanwizard.buildLogic.config

import com.google.gson.*
import kotlinx.serialization.Serializable
import java.text.DateFormat

@Serializable
data class CleanWizardGsonSerializationConfig(
    val longSerializationPolicy: LongSerializationPolicy = LongSerializationPolicy.DEFAULT,
    val fieldNamingPolicy: FieldNamingPolicy = FieldNamingPolicy.IDENTITY,
//    var instanceCreators: Map<Type, InstanceCreator<*>> = HashMap()
    val factories: List<TypeAdapterFactory> = ArrayList(),
    val hierarchyFactories: List<TypeAdapterFactory> = ArrayList(),
    val serializeNulls: Boolean = false,
    val datePattern: String? = null,
    val dateStyle: Int = DateFormat.DEFAULT,
    val timeStyle: Int = DateFormat.DEFAULT,
    val complexMapKeySerialization: Boolean = false,
    val serializeSpecialFloatingPointValues: Boolean = false,
    val escapeHtmlChars: Boolean = true,
//    var formattingStyle: FormattingStyle = FormattingStyle.COMPACT,
    val generateNonExecutableJson: Boolean = false,
    val strictness: Strictness? = null,
    val useJdkUnsafe: Boolean = true,
    val objectToNumberStrategy: ToNumberStrategy = ToNumberPolicy.DOUBLE,
    val numberToNumberStrategy: ToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER,
//    var reflectionFilters: ArrayDeque<ReflectionAccessFilter> = ArrayDeque<ReflectionAccessFilter>()
)

fun CleanWizardGsonSerializationConfig.toGson(): Gson {
    val builder = GsonBuilder()
    builder.apply {
        setLongSerializationPolicy(longSerializationPolicy)
        setFieldNamingPolicy(fieldNamingPolicy)
        factories.forEach(::registerTypeAdapterFactory)
        hierarchyFactories.forEach(::registerTypeAdapterFactory)
        if (serializeNulls)
            serializeNulls()
        setDateFormat(dateStyle, timeStyle)
        datePattern?.let(::setDateFormat)
        if (complexMapKeySerialization) enableComplexMapKeySerialization()
        if (serializeSpecialFloatingPointValues) serializeSpecialFloatingPointValues()
        if (!escapeHtmlChars)
            disableHtmlEscaping()
        if (generateNonExecutableJson)
            generateNonExecutableJson()
        setStrictness(strictness)
        if (!useJdkUnsafe)
            disableJdkUnsafe()
        setObjectToNumberStrategy(objectToNumberStrategy)
        this.create().newBuilder().setNumberToNumberStrategy(numberToNumberStrategy)
    }.create()

    return builder.create()
}

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
        val fieldNamingPolicy = getFieldValue("fieldNamingPolicy") as FieldNamingPolicy
        val factories = getFieldValue("factories") as List<TypeAdapterFactory>? ?: listOf()
        val hierarchyFactories = getFieldValue("hierarchyFactories") as List<TypeAdapterFactory>? ?: listOf()
        val serializeNulls = getFieldValue("serializeNulls") as Boolean? ?: false
        val dateStyle = getFieldValue("dateStyle") as Int? ?: DateFormat.DEFAULT
        val timeStyle = getFieldValue("timeStyle") as Int? ?: DateFormat.DEFAULT
        val datePattern = getFieldValue("datePattern") as String?
        val complexMapKeySerialization = getFieldValue("complexMapKeySerialization") as Boolean? ?: false
        val serializeSpecialFloatingPointValues =
            getFieldValue("serializeSpecialFloatingPointValues") as Boolean? ?: false
        val escapeHtmlChars = getFieldValue("escapeHtmlChars") as Boolean? ?: true
        val generateNonExecutableJson = getFieldValue("generateNonExecutableJson") as Boolean? ?: false
        val strictness = getFieldValue("strictness") as Strictness?
        val useJdkUnsafe = getFieldValue("useJdkUnsafe") as Boolean? ?: true
        val objectToNumberStrategy =
            getFieldValue("objectToNumberStrategy") as ToNumberStrategy? ?: ToNumberPolicy.DOUBLE
        val numberToNumberStrategy =
            getFieldValue("numberToNumberStrategy") as ToNumberStrategy? ?: ToNumberPolicy.LAZILY_PARSED_NUMBER

        return CleanWizardGsonSerializationConfig(
            longSerializationPolicy,
            fieldNamingPolicy,
            factories,
            hierarchyFactories,
            serializeNulls,
            datePattern,
            dateStyle,
            timeStyle,
            complexMapKeySerialization,
            serializeSpecialFloatingPointValues,
            escapeHtmlChars,
            generateNonExecutableJson,
            strictness,
            useJdkUnsafe,
            objectToNumberStrategy,
            numberToNumberStrategy
        )
    } catch (e: Exception) {
        e.printStackTrace()
        throw RuntimeException("Failed to extract configuration from Gson instance", e)
    }
}
