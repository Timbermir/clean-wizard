package corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDependencyInjectionFramework

@CleanWizardConfigDsl
abstract class CleanWizardKodeinDependencyInjectionFrameworkBuilder {
    abstract var useSimpleFunctions: Boolean

    abstract var binding: CleanWizardDependencyInjectionFramework.Kodein.KodeinBinding
}