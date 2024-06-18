package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDataClassGenerationPattern
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardJsonSerializer
import corp.tbm.cleanwizard.buildLogic.config.CleanWizardLayerConfig

open class CleanWizardExtension(
    var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KotlinXSerialization,
    var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework = CleanWizardDependencyInjectionFramework.None,
    internal var dataConfig: CleanWizardLayerConfig.Data = CleanWizardLayerConfig.Data(),
    internal var domainConfig: CleanWizardLayerConfig.Domain = CleanWizardLayerConfig.Domain(),
    internal var presentationConfig: CleanWizardLayerConfig.Presentation = CleanWizardLayerConfig.Presentation()
) {

    fun data(configuration: CleanWizardLayerConfig.Data.() -> Unit) {
        dataConfig.apply(configuration)
    }

    fun domain(configuration: CleanWizardLayerConfig.Domain.() -> Unit) {
        domainConfig.apply(configuration)
    }

    fun presentation(configuration: CleanWizardLayerConfig.Presentation.() -> Unit) {
        presentationConfig.apply(configuration)
    }
}