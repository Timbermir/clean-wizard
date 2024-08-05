package corp.tbm.cleanwizard.gradle.api.builders.jsonSerializers

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.gradle.api.config.serializer.CleanWizardJsonSerializer

@CleanWizardConfigDsl
abstract class CleanWizardJsonSerializerBuilder {

    protected abstract var jsonSerializer: CleanWizardJsonSerializer

    abstract fun kotlinXSerialization(block: CleanWizardKotlinXSerializationBuilder.() -> Unit = {})

    abstract fun gson(block: CleanWizardGsonSerializationBuilder.() -> Unit = {})

    abstract fun moshi(block: CleanWizardJsonSerializer.Moshi.() -> Unit = {})
}