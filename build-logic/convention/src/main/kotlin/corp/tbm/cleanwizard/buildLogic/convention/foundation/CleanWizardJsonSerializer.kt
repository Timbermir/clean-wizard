package corp.tbm.cleanwizard.buildLogic.convention.foundation

enum class CleanWizardJsonSerializer(val serializer: String) {
    KOTLINX_SERIALIZATION("kotlinx-serialization"),
    GSON("gson"),
    MOSHI("moshi"),
    JACKSON("jackson");
}