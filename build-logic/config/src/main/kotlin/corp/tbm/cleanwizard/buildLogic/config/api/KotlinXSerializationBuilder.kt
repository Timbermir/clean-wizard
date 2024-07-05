package corp.tbm.cleanwizard.buildLogic.config.api

import com.google.gson.*
import com.google.gson.annotations.Expose
import com.google.gson.internal.Excluder
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardIncubatingAPI
import corp.tbm.cleanwizard.buildLogic.config.toCleanWizardGsonSerializationConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.JsonNamingStrategy
import java.text.DateFormat

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

fun someFun() {
    GsonBuilder().excludeFieldsWithoutExposeAnnotation()
}
@CleanWizardConfigDsl
abstract class GsonSerializationBuilder {
    /**
     * Allows you to pass raw [Gson]. Values from it will be collected
     * via [Reflection](https://www.oracle.com/technical-resources/articles/java/javareflection.html) due to
     * inability to properly serialize it.
     *
     * If it is not assigned, properties will be used for building instead.
     *
     * However, it is **not recommended to use** because of reflection under the hood as it is not reliable.
     *
     * @see [toCleanWizardGsonSerializationConfig]
     * @see longSerializationPolicy
     * @see fieldNamingPolicy
     * @see serializeNulls
     * @see datePattern
     * @see timeStyle
     * @see complexMapKeySerialization
     * @see serializeSpecialFloatingPointValues
     * @see htmlSafe
     * @see generateNonExecutableJson
     * @see strictness
     * @see useJdkUnsafe
     * @see objectToNumberStrategy
     * @see numberToNumberStrategy
     */
    @CleanWizardIncubatingAPI
    var gson: Gson? = null

        var excluder: Excluder = Excluder.DEFAULT
    /**
     * Alternative to [GsonBuilder.setLongSerializationPolicy].
     */
    var longSerializationPolicy: LongSerializationPolicy = LongSerializationPolicy.DEFAULT

    /**
     * Alternative to [GsonBuilder.setFieldNamingPolicy]
     */
    var fieldNamingPolicy: FieldNamingPolicy = FieldNamingPolicy.IDENTITY

    /**
     * Alternative to [GsonBuilder.serializeNulls]
     */
    var serializeNulls: Boolean = false

    /**
     * Alternative to [GsonBuilder.setDateFormat]
     */
    var datePattern: String? = null

    /**
     * Alternative to [GsonBuilder.setDateFormat]
     */
    var dateStyle: Int = DateFormat.DEFAULT

    /**
     * Alternative to [GsonBuilder.setDateFormat]
     */
    var timeStyle: Int = DateFormat.DEFAULT

    /**
     * Alternative to [GsonBuilder.enableComplexMapKeySerialization]
     */
    var complexMapKeySerialization: Boolean = false

    /**
     * Alternative to [GsonBuilder.serializeSpecialFloatingPointValues]
     */
    var serializeSpecialFloatingPointValues: Boolean = false

    /**
     * Alternative to [GsonBuilder.disableHtmlEscaping]
     */
    var htmlSafe: Boolean = true

    /**
     * Alternative to [GsonBuilder.generateNonExecutableJson]
     */
    var generateNonExecutableJson: Boolean = false

    /**
     * Alternative to [GsonBuilder.setStrictness]
     */
    var strictness: Strictness? = null

    /**
     * Alternative to [GsonBuilder.useJdkUnsafe]
     */
    var useJdkUnsafe: Boolean = true

    /**
     * Alternative to [GsonBuilder.setObjectToNumberStrategy]
     */
    var objectToNumberStrategy: ToNumberStrategy = ToNumberPolicy.DOUBLE

    /**
     * Alternative to [GsonBuilder.setNumberToNumberStrategy]
     */
    var numberToNumberStrategy: ToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER
}