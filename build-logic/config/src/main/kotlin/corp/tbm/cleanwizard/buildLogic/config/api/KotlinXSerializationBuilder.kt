package corp.tbm.cleanwizard.buildLogic.config.api

import com.google.gson.GsonBuilder
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

@CleanWizardConfigDsl
abstract class GsonSerializationBuilder

@CleanWizardConfigDsl
abstract class GsonSerializationConfigBuilder {

//    var excluder: Excluder = Excluder.DEFAULT
//    var longSerializationPolicy = LongSerializationPolicy.DEFAULT
//    var fieldNamingPolicy: FieldNamingStrategy = FieldNamingPolicy.IDENTITY
//    var instanceCreators: Map<Type, InstanceCreator<*>> = HashMap()
//    var factories: List<TypeAdapterFactory> = ArrayList()
//    var hierarchyFactories: List<TypeAdapterFactory> = ArrayList()
//    var serializeNulls = false
//    var datePattern: String? = null
//    var dateStyle = DateFormat.DEFAULT
//    var timeStyle = DateFormat.DEFAULT
//    var complexMapKeySerialization = false
//    var serializeSpecialFloatingPointValues = false
//    var escapeHtmlChars = true
//    var formattingStyle: FormattingStyle = FormattingStyle.COMPACT
//    var generateNonExecutableJson = false
//    var strictness: Strictness? = null
//    var useJdkUnsafe = true
//    var objectToNumberStrategy: ToNumberStrategy = ToNumberPolicy.DOUBLE
//    var numberToNumberStrategy: ToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER
//    var reflectionFilters = ArrayDeque<ReflectionAccessFilter>()
}