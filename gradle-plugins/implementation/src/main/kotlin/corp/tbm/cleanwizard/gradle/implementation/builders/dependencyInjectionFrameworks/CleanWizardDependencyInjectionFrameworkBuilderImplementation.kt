package corp.tbm.cleanwizard.gradle.implementation.builders.dependencyInjectionFrameworks

import corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks.CleanWizardDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks.CleanWizardKodeinDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks.CleanWizardKoinDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDependencyInjectionFramework

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