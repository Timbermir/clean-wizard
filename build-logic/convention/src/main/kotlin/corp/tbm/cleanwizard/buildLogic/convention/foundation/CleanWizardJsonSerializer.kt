package corp.tbm.cleanwizard.buildLogic.convention.foundation

enum class CleanWizardJsonSerializer(val serializer: String, val dependency: String) {
    KOTLINX_SERIALIZATION("kotlinx-serialization", "org.jetbrains.kotlinx:kotlinx-serialization-json"),
    GSON("gson", "com.google.code.gson:gson"),
    MOSHI("moshi", "com.squareup.moshi:moshi"),
    JACKSON("jackson", "com.fasterxml.jackson.core:jackson-databind"), ;
}