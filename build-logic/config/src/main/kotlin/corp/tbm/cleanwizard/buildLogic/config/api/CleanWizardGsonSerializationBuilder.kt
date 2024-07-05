package corp.tbm.cleanwizard.buildLogic.config.api

import com.google.gson.*
import com.google.gson.internal.Excluder
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardIncubatingAPI
import corp.tbm.cleanwizard.buildLogic.config.toCleanWizardGsonSerializationConfig
import java.text.DateFormat

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