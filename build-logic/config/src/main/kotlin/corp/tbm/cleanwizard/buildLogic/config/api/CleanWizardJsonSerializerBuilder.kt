package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardJsonSerializerBuilder {

    protected abstract var jsonSerializer: CleanWizardJsonSerializer

    abstract fun kotlinXSerialization(block: CleanWizardKotlinXSerializationBuilder.() -> Unit = {})

    abstract fun gson(block: CleanWizardGsonSerializationBuilder.() -> Unit = {})

    abstract fun moshi(block: CleanWizardJsonSerializer.Moshi.() -> Unit = {})
}