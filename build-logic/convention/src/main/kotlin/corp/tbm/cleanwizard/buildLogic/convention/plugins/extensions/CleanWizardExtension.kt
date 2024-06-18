package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.*

open class CleanWizardExtension(
    var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization,
    var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
    internal var cleanWizardLayerConfigWrapper: CleanWizardLayerConfigWrapper = CleanWizardLayerConfigWrapper(),
    internal var dataConfig: CleanWizardLayerConfig.Data = CleanWizardLayerConfig.Data(),
    internal var domainConfig: CleanWizardLayerConfig.Domain = CleanWizardLayerConfig.Domain(),
    internal var presentationConfig: CleanWizardLayerConfig.Presentation = CleanWizardLayerConfig.Presentation()
) {

    fun data(configuration: CleanWizardLayerConfig.Data.() -> Unit) {
        cleanWizardLayerConfigWrapper.dataConfig.apply(configuration)
        dataConfig.apply(configuration)
    }

    fun domain(configuration: CleanWizardLayerConfig.Domain.() -> Unit) {
        cleanWizardLayerConfigWrapper.domainConfig.apply(configuration)
        domainConfig.apply(configuration)
    }

    fun presentation(configuration: CleanWizardLayerConfig.Presentation.() -> Unit) {
        cleanWizardLayerConfigWrapper.presentationConfig.apply(configuration)
        presentationConfig.apply(configuration)
    }
}