package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class ICleanWizardJsonSerializerBuilder {

    protected abstract var jsonSerializer: CleanWizardJsonSerializer

    abstract fun `kotlinx-serialization`(block: CleanWizardJsonSerializer.KotlinXSerialization.() -> Unit = {})

    abstract fun gson(block: CleanWizardJsonSerializer.Gson.() -> Unit = {})

    abstract fun moshi(block: CleanWizardJsonSerializer.Moshi.() -> Unit = {})
}