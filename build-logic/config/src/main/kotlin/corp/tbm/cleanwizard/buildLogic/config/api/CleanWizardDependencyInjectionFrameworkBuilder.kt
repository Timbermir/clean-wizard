package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.dsl.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class ICleanWizardDependencyInjectionFrameworkBuilder {

    protected abstract var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework

    abstract fun koin(block: IKoinBuilder.() -> Unit)

    abstract fun kodein(block: CleanWizardDependencyInjectionFramework.Kodein.() -> Unit = {})

    abstract fun dagger()

    @CleanWizardConfigDsl
    abstract class IKoinBuilder {

        abstract fun core(block: CleanWizardDependencyInjectionFramework.Koin.Core.() -> Unit = {})

        abstract fun annotations(block: CleanWizardDependencyInjectionFramework.Koin.Annotations.() -> Unit = {})
    }
}