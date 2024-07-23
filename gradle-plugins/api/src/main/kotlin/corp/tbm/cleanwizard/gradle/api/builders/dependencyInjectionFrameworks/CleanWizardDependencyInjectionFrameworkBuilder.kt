package corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardConfigDsl
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDependencyInjectionFramework

@CleanWizardConfigDsl
abstract class CleanWizardDependencyInjectionFrameworkBuilder {

    protected abstract var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework

    abstract fun koin(block: CleanWizardKoinDependencyInjectionFrameworkBuilder.() -> Unit)

    abstract fun kodein(block: CleanWizardKodeinDependencyInjectionFrameworkBuilder.() -> Unit = {})

    abstract fun dagger()
}
abstract class NewBuilder