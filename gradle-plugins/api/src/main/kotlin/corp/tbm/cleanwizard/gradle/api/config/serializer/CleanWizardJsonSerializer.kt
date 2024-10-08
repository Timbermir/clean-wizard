package corp.tbm.cleanwizard.gradle.api.config.serializer

import com.google.gson.annotations.SerializedName
import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

private object KClassSerializer : KSerializer<KClass<out Annotation>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("KClass", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: KClass<out Annotation>) {
        encoder.encodeString(value.qualifiedName ?: "")
    }

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): KClass<out Annotation> {
        val className = decoder.decodeString()
        return Class.forName(className).kotlin as KClass<out Annotation>
    }
}

@Serializable
@CleanWizardConfigDsl
sealed class CleanWizardJsonSerializer(
    val dependency: String,
    @Serializable(KClassSerializer::class)
    val annotation: KClass<out Annotation>,
    val nameProperty: String = "value"
) {
    @Serializable
    @SerialName("none")
    data object None : CleanWizardJsonSerializer("", Nothing::class)

    @Serializable
    @SerialName("kotlinx-serialization")
    data class KotlinXSerialization(
        val serializerConfig: CleanWizardKotlinXSerializationConfig = CleanWizardKotlinXSerializationConfig()
    ) : CleanWizardJsonSerializer(
        "org.jetbrains.kotlinx:kotlinx-serialization-json",
        SerialName::class
    )

    @Serializable
    @SerialName("gson")
    data class Gson(
        val serializerConfig: CleanWizardGsonSerializationConfig = CleanWizardGsonSerializationConfig()
    ) : CleanWizardJsonSerializer("com.google.code.gson:gson", SerializedName::class)

    @Serializable
    @SerialName("moshi")
    data object Moshi : CleanWizardJsonSerializer("com.squareup.moshi:moshi", com.squareup.moshi.Json::class, "name")
}