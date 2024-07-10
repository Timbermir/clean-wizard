package corp.tbm.cleanwizard.buildLogic.convention.implementation

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardGsonSerializationBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardJsonSerializerBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardKotlinXSerializationBuilder
import corp.tbm.cleanwizard.buildLogic.convention.implementation.serializers.CleanWizardGsonSerializationBuilderImplementation
import corp.tbm.cleanwizard.buildLogic.convention.implementation.serializers.CleanWizardKotlinXSerializationBuilderImplementation

internal class CleanWizardJsonSerializerBuilderImplementation : CleanWizardJsonSerializerBuilder() {

    override var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization()

    private val kotlinXSerializationBuilder = CleanWizardKotlinXSerializationBuilderImplementation()

    private val gsonSerializationBuilder = CleanWizardGsonSerializationBuilderImplementation()

    override fun kotlinXSerialization(block: CleanWizardKotlinXSerializationBuilder.() -> Unit) {
        jsonSerializer = kotlinXSerializationBuilder.apply(block).build()
    }

    override fun gson(block: CleanWizardGsonSerializationBuilder.() -> Unit) {
        jsonSerializer = gsonSerializationBuilder.apply(block).build()
    }

    override fun moshi(block: CleanWizardJsonSerializer.Moshi.() -> Unit) {
        jsonSerializer = CleanWizardJsonSerializer.Moshi.apply(block)
    }

    internal fun build(): CleanWizardJsonSerializer {
        return jsonSerializer
    }
}