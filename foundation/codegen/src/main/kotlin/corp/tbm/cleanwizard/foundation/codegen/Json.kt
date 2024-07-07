package corp.tbm.cleanwizard.foundation.codegen

import kotlinx.serialization.json.Json

val json = Json {
    classDiscriminator = "type"
    encodeDefaults = true
}