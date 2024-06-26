package corp.tbm.cleanwizard.foundation.codegen.universal

import kotlinx.serialization.json.Json

val json = Json {
    classDiscriminator = "type"
    encodeDefaults = true
}