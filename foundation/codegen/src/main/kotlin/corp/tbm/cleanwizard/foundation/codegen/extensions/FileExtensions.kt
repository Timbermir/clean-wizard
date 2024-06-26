package corp.tbm.cleanwizard.foundation.codegen.extensions

import corp.tbm.cleanwizard.foundation.codegen.json
import java.io.File

inline fun <reified T> File.readJsonFromFile(): T {
    return json.decodeFromString(readText())
}