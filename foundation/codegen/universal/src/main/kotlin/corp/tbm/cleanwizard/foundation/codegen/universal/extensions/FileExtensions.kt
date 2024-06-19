package corp.tbm.cleanwizard.foundation.codegen.universal.extensions

import corp.tbm.cleanwizard.foundation.codegen.universal.json
import java.io.File

inline fun <reified T> File.readJsonFromFile(): T {
    return json.decodeFromString(readText())
}