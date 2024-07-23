package corp.tbm.cleanwizard.gradle.implementation.builders.dependencyInjectionFrameworks

import corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks.CleanWizardKodeinDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDependencyInjectionFramework

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