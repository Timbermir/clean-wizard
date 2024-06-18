package corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

inline fun <reified T> Json.createTempFileWithEncodedString(value: T): File {
    return File.createTempFile(T::class.simpleName.toString(), ".json").also {
        it.writeText(encodeToString<T>(value))
    }
}