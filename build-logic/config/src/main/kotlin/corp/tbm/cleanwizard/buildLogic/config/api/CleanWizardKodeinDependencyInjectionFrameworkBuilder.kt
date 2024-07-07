package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardKodeinDependencyInjectionFrameworkBuilder {
    abstract var useSimpleFunctions: Boolean

    abstract var binding: CleanWizardDependencyInjectionFramework.Kodein.KodeinBinding
}