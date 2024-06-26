package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardDependencyInjectionFrameworkBuilder {

    protected abstract var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework

    abstract fun koin(block: KoinBuilder.() -> Unit)

    abstract fun kodein(block: CleanWizardDependencyInjectionFramework.Kodein.() -> Unit = {})

    abstract fun dagger()

    @CleanWizardConfigDsl
    abstract class KoinBuilder {

        abstract fun core(block: CleanWizardDependencyInjectionFramework.Koin.Core.() -> Unit = {})

        abstract fun annotations(block: CleanWizardDependencyInjectionFramework.Koin.Annotations.() -> Unit = {})
    }
}