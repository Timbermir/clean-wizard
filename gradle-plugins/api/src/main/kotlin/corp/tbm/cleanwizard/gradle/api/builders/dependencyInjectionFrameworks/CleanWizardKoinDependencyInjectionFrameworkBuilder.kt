package corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDependencyInjectionFramework

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