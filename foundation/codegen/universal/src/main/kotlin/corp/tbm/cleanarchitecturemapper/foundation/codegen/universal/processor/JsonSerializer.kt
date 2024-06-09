package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.processor

enum class JsonSerializer(val serializer: String) {
    KOTLINX_SERIALIZATION("kotlinx-serialization"),
    GSON("gson"),
    MOSHI("moshi"),
    JACKSON("jackson");
}