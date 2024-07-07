package corp.tbm.cleanwizard.foundation.codegen.extensions

import java.io.File

fun Map<String, String>.getFile(key: String): File {
    return File(get(key).toString())
}