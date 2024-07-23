package corp.tbm.cleanwizard.gradle.foundation.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

internal val json = Json {
    classDiscriminator = "type"
    encodeDefaults = true
}

inline fun <reified T> Json.createTempFileWithEncodedString(value: T): File {
    return File.createTempFile(T::class.simpleName.toString(), ".json").also {
        it.writeText(encodeToString<T>(value))
    }
}