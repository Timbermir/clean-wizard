package corp.tbm.cleanwizard.buildLogic.config.api

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.annotations.CleanWizardConfigDsl

@CleanWizardConfigDsl
abstract class CleanWizardKoinDependencyInjectionFrameworkBuilder {

    protected abstract var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework

    abstract fun core(block: KoinCoreBuilder.() -> Unit = {})

    abstract fun annotations(block: KoinAnnotationsBuilder.() -> Unit = {})

    @CleanWizardConfigDsl
    abstract class KoinCoreBuilder {
        abstract var automaticallyCreateModule: Boolean
        abstract var useConstructorDSL: Boolean
    }

    @CleanWizardConfigDsl
    abstract class KoinAnnotationsBuilder {
        abstract var automaticallyCreateModule: Boolean
        abstract var specifyUseCasePackageForComponentScan: Boolean
    }
}