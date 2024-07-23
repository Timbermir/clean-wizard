package corp.tbm.cleanwizard.gradle.implementation.builders.dependencyInjectionFrameworks

import corp.tbm.cleanwizard.gradle.api.builders.dependencyInjectionFrameworks.CleanWizardKoinDependencyInjectionFrameworkBuilder
import corp.tbm.cleanwizard.gradle.api.config.CleanWizardDependencyInjectionFramework

internal class CleanWizardKoinDependencyInjectionFrameworkBuilderImplementation :
    CleanWizardKoinDependencyInjectionFrameworkBuilder() {

    override var dependencyInjectionFramework: CleanWizardDependencyInjectionFramework =
        CleanWizardDependencyInjectionFramework.None

    private val koinCoreBuilder = KoinCoreBuilderImplementation()

    private val koinAnnotationsBuilder = KoinAnnotationsBuilderImplementation()

    override fun core(block: KoinCoreBuilder.() -> Unit) {
        dependencyInjectionFramework = koinCoreBuilder.apply(block).build()
    }

    override fun annotations(block: KoinAnnotationsBuilder.() -> Unit) {
        dependencyInjectionFramework = koinAnnotationsBuilder.apply(block).build()
    }

    internal fun build(): CleanWizardDependencyInjectionFramework {
        return dependencyInjectionFramework
    }

    private inner class KoinCoreBuilderImplementation :
        KoinCoreBuilder() {

        override var automaticallyCreateModule: Boolean = true
        override var useConstructorDSL: Boolean = true

        fun build(): CleanWizardDependencyInjectionFramework.Koin.Core {
            return CleanWizardDependencyInjectionFramework.Koin.Core(
                automaticallyCreateModule,
                useConstructorDSL
            )
        }
    }

    private inner class KoinAnnotationsBuilderImplementation :
        KoinAnnotationsBuilder() {

        override var automaticallyCreateModule: Boolean = false
        override var specifyUseCasePackageForComponentScan: Boolean = true

        fun build(): CleanWizardDependencyInjectionFramework.Koin.Annotations {
            return CleanWizardDependencyInjectionFramework.Koin.Annotations(
                automaticallyCreateModule,
                specifyUseCasePackageForComponentScan
            )
        }
    }
}