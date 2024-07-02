package corp.tbm.cleanwizard.buildLogic.config

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.*
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
        override val serializerConfig: SerializerConfig = KotlinXSerializerConfig(kotlinx.serialization.json.Json.Default)
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
        override val serializerConfig: SerializerConfig = GsonSerializerConfig(com.google.gson.Gson())
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
        override val serializerConfig: SerializerConfig = MoshiSerializerConfig(
            com.squareup.moshi.Moshi.Builder().build()
        )
    ) :
        CleanWizardJsonSerializer(
            "com.squareup.moshi:moshi",
            Json::class,
            "name",
            serializerConfig = serializerConfig
        )
}

interface SerializerConfig

inline fun SerializerConfig.toJson(encoding: () -> String): String {
    return encoding()
}

inline fun <reified T : Any> SerializerConfig.fromJson(decoding: () -> T?): T? {
    return decoding()
}

@Suppress("OVERRIDE_BY_INLINE")
class KotlinXSerializerConfig(val json: kotlinx.serialization.json.Json) : SerializerConfig {

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

class GsonSerializerConfig(val gson: Gson) : SerializerConfig {

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

class MoshiSerializerConfig(val moshi: Moshi) : SerializerConfig {

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