package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardDependencyInjectionFrameworkBuilder {

    protected abstract var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework

    abstract fun koin(block: CleanWizardKoinDependencyInjectionFrameworkBuilder.() -> Unit)

    abstract fun kodein(block: CleanWizardKodeinDependencyInjectionFrameworkBuilder.() -> Unit = {})

    abstract fun dagger()
}