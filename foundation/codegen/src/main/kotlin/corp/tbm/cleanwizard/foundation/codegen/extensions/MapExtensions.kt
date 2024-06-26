package corp.tbm.cleanwizard.foundation.codegen.universal.extensions

import java.io.File

fun Map<String, String>.getFile(key: String): File {
    return File(get(key).toString())
}