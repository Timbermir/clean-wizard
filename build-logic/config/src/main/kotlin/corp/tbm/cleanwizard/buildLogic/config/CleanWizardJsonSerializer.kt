package corp.tbm.cleanwizard.buildLogic.config

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.lang.reflect.Type
import kotlin.reflect.KClass

@Serializable
@CleanWizardConfigDsl
sealed class CleanWizardJsonSerializer(
    val dependency: String,
    val annotation: KClass<out Annotation>,
    val nameProperty: String = "value",
    @SerialName("json-serializer-delimiter") open val delimiter: String = "",
    @SerialName("serializer-config") open val serializerConfig: SerializerConfig
) {

    @Serializable
    @SerialName("kotlinx-serialization")
    data class KotlinXSerialization(
        override val delimiter: String = "",
        override val serializerConfig: SerializerConfig = KotlinXSerializerConfig(Json.Default)
    ) :
        CleanWizardJsonSerializer(
            "org.jetbrains.kotlinx:kotlinx-serialization-json",
            SerialName::class,
            serializerConfig = serializerConfig
        )

    @Serializable
    @SerialName("gson")
    data class Gson(
        override val delimiter: String = "",
        override val serializerConfig: SerializerConfig = KotlinXSerializerConfig(Json.Default)
    ) :
        CleanWizardJsonSerializer(
            "com.google.code.gson:gson",
            SerializedName::class,
            serializerConfig = serializerConfig
        )

    @Serializable
    @SerialName("moshi")
    data class Moshi(
        override val delimiter: String = "",
        override val serializerConfig: SerializerConfig = KotlinXSerializerConfig(Json.Default)
    ) :
        CleanWizardJsonSerializer(
            "com.squareup.moshi:moshi",
            com.squareup.moshi.Json::class,
            "name",
            serializerConfig = serializerConfig
        )
}

@Serializable
abstract class SerializerConfig

inline fun SerializerConfig.toJson(encoding: () -> String): String {
    return encoding()
}

inline fun <reified T : Any> SerializerConfig.fromJson(decoding: () -> T?): T? {
    return decoding()
}

object JsonSerializer : KSerializer<Json> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Json") {
        element<Boolean>("prettyPrint")
        element<Boolean>("isLenient")
        element<Boolean>("ignoreUnknownKeys")
        element<Boolean>("useArrayPolymorphism")
        element<Boolean>("allowSpecialFloatingPointValues")
        element<String>("classDiscriminator")
        // Note: SerializersModule serialization can be complex, so this is a simple placeholder
    }

    override fun serialize(encoder: Encoder, value: Json) {
        val compositeEncoder = encoder.beginStructure(descriptor)
        compositeEncoder.encodeBooleanElement(descriptor, 0, value.configuration.prettyPrint)
        compositeEncoder.encodeBooleanElement(descriptor, 1, value.configuration.isLenient)
        compositeEncoder.encodeBooleanElement(descriptor, 2, value.configuration.ignoreUnknownKeys)
        compositeEncoder.encodeBooleanElement(descriptor, 3, value.configuration.useArrayPolymorphism)
        compositeEncoder.encodeBooleanElement(descriptor, 4, value.configuration.allowSpecialFloatingPointValues)
        compositeEncoder.encodeStringElement(descriptor, 5, value.configuration.classDiscriminator)
        // SerializersModule can be encoded here if needed
        compositeEncoder.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Json {
        val compositeDecoder = decoder.beginStructure(descriptor)
        var prettyPrint = false
        var isLenient = false
        var ignoreUnknownKeys = false
        var useArrayPolymorphism = false
        var allowSpecialFloatingPointValues = false
        var classDiscriminator = "type"
        // Note: SerializersModule deserialization can be complex, so this is a simple placeholder

        loop@ while (true) {
            when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                0 -> prettyPrint = compositeDecoder.decodeBooleanElement(descriptor, 0)
                1 -> isLenient = compositeDecoder.decodeBooleanElement(descriptor, 1)
                2 -> ignoreUnknownKeys = compositeDecoder.decodeBooleanElement(descriptor, 2)
                3 -> useArrayPolymorphism = compositeDecoder.decodeBooleanElement(descriptor, 3)
                4 -> allowSpecialFloatingPointValues = compositeDecoder.decodeBooleanElement(descriptor, 4)
                5 -> classDiscriminator = compositeDecoder.decodeStringElement(descriptor, 5)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> throw SerializationException("Unexpected index $index")
            }
        }
        compositeDecoder.endStructure(descriptor)

        return Json {
            this.prettyPrint = prettyPrint
            this.isLenient = isLenient
            this.ignoreUnknownKeys = ignoreUnknownKeys
            this.useArrayPolymorphism = useArrayPolymorphism
            this.allowSpecialFloatingPointValues = allowSpecialFloatingPointValues
            this.classDiscriminator = classDiscriminator
            // Note: SerializersModule can be decoded here if needed
        }
    }
}

@Serializable
class KotlinXSerializerConfig(@Serializable(JsonSerializer::class) val json: Json) :
    SerializerConfig() {

    inline fun <reified T : Any> toStr(value: T): String {
        return toJson {
            json.encodeToString<T>(value)
        }
    }

    inline fun <reified T : Any> toStr(serializer: SerializationStrategy<T>, value: T): String {
        return toJson {
            json.encodeToString(serializer, value)
        }
    }

    inline fun <reified T : Any> fromStr(string: String): T? {
        return fromJson {
            json.decodeFromString<T>(string)
        }
    }

    inline fun <reified T : Any> fromStr(serializer: DeserializationStrategy<T>, string: String): T? {
        return fromJson {
            json.decodeFromString(serializer, string)
        }
    }
}

@Serializable
class GsonSerializerConfig(val gson: @Contextual Gson) : SerializerConfig() {

    inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type

    inline fun <reified T : Any> toStr(value: T): String {
        return toJson {
            gson.toJson(value, typeToken<T>())
        }
    }

    inline fun <reified T : Any> fromStr(string: String): T? {
        return fromJson {
            gson.fromJson(string, typeToken<T>())
        }
    }
}

@Serializable
class MoshiSerializerConfig(val moshi: @Contextual Moshi) : SerializerConfig() {

    inline fun <reified T : Any> toStr(value: T): String {
        return toJson {
            moshi.adapter(T::class.java).toJson(value)
        }
    }

    inline fun <reified T : Any> fromStr(string: String): T? {
        return fromJson {
            moshi.adapter(T::class.java).fromJson(string)
        }
    }
}