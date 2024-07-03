package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardJsonSerializerBuilder {

    protected abstract var jsonSerializer: CleanWizardJsonSerializer

    abstract fun kotlinXSerialization(block: KotlinXSerializationBuilder.() -> Unit = {})

    abstract fun gson(block: CleanWizardJsonSerializer.Gson.() -> Unit = {})

    abstract fun moshi(block: CleanWizardJsonSerializer.Moshi.() -> Unit = {})
}