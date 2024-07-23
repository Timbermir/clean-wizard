package corp.tbm.cleanwizard.gradle.implementation.builders.serializers

import corp.tbm.cleanwizard.gradle.api.builders.jsonSerializers.CleanWizardGsonSerializationBuilder
import corp.tbm.cleanwizard.gradle.api.builders.jsonSerializers.CleanWizardJsonSerializerBuilder
import corp.tbm.cleanwizard.gradle.api.builders.jsonSerializers.CleanWizardKotlinXSerializationBuilder
import corp.tbm.cleanwizard.gradle.api.config.serializer.CleanWizardJsonSerializer

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