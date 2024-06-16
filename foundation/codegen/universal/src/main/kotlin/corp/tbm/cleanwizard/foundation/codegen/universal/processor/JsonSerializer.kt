package corp.tbm.cleanwizard.foundation.codegen.universal.processor

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlin.reflect.KClass


enum class JsonSerializer(
    val serializer: String,
    val annotation: KClass<out Annotation>,
    val nameProperty: String = "value"
) {
    KOTLINX_SERIALIZATION("kotlinx-serialization", SerialName::class),
    GSON("gson", SerializedName::class),
    MOSHI("moshi", Json::class, "name"),
    JACKSON("jackson", JsonProperty::class);
}