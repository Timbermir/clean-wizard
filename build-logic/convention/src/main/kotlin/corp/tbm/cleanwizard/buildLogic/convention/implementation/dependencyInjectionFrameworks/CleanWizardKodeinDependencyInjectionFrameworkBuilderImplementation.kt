package corp.tbm.cleanwizard.buildLogic.convention.implementation.dependencyInjectionFrameworks

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardKodeinDependencyInjectionFrameworkBuilder

internal class CleanWizardKodeinDependencyInjectionFrameworkBuilderImplementation :
    CleanWizardKodeinDependencyInjectionFrameworkBuilder() {

    override var useSimpleFunctions: Boolean = true
    override var binding: CleanWizardDependencyInjectionFramework.Kodein.KodeinBinding =
        CleanWizardDependencyInjectionFramework.Kodein.KodeinBinding.Provider

    internal fun build(): CleanWizardDependencyInjectionFramework.Kodein {
        return CleanWizardDependencyInjectionFramework.Kodein(
            useSimpleFunctions,
            binding
        )
    }
}