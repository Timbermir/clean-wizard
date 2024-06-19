package corp.tbm.cleanwizard.buildLogic.config

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
sealed class CleanWizardJsonSerializer(
    val dependency: String,
    val annotation: KClass<out Annotation>,
    val nameProperty: String = "value"
) {

    @Serializable
    @SerialName("kotlinx-serialization")
    data object KotlinXSerialization :
        CleanWizardJsonSerializer("org.jetbrains.kotlinx:kotlinx-serialization-json", SerialName::class)

    @Serializable
    @SerialName("gson")
    data object Gson : CleanWizardJsonSerializer("com.google.code.gson:gson", SerializedName::class)

    @Serializable
    @SerialName("moshi")
    data object Moshi : CleanWizardJsonSerializer("com.squareup.moshi:moshi", Json::class, "name")
}