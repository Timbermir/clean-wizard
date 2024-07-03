package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

abstract class KotlinXSerializationBuilder {

    var delimiter: String = ""

    abstract var json: Json

    @CleanWizardConfigDsl
    abstract fun json(builder: JsonBuilder.() -> Unit)
    abstract fun build(): CleanWizardJsonSerializer.KotlinXSerialization
}