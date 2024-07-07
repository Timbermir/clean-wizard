package corp.tbm.cleanwizard.buildLogic.convention.implementation

import corp.tbm.cleanwizard.buildLogic.config.CleanWizardDependencyInjectionFramework
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardKodeinDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardKoinDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.buildLogic.convention.implementation.dependencyInjectionFrameworks.CleanWizardKodeinDependencyInjectionFrameworkBuilderImplementation
import corp.tbm.cleanwizard.buildLogic.convention.implementation.dependencyInjectionFrameworks.CleanWizardKoinDependencyInjectionFrameworkBuilderImplementation

internal class CleanWizardDependencyInjectionFrameworkBuilderImplementation :
    CleanWizardDependencyInjectionFrameworkBuilder() {

    override var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework =
        CleanWizardDependencyInjectionFramework.None

    private val koinBuilder =
        CleanWizardKoinDependencyInjectionFrameworkBuilderImplementation()

    private val kodeinBuilder =
        CleanWizardKodeinDependencyInjectionFrameworkBuilderImplementation()

    override fun koin(block: CleanWizardKoinDependencyInjectionFrameworkBuilder.() -> Unit) {
        dependencyInjectionFramework = koinBuilder.apply(block).build()
    }

    override fun kodein(block: CleanWizardKodeinDependencyInjectionFrameworkBuilder.() -> Unit) {
        dependencyInjectionFramework = kodeinBuilder.apply(block).build()
    }

    override fun dagger() {
        dependencyInjectionFramework = CleanWizardDependencyInjectionFramework.Dagger
    }

    internal fun build(): CleanWizardDependencyInjectionFramework {
        return dependencyInjectionFramework
    }
}