package corp.tbm.cleanwizard.gradle.implementation.extensions

import corp.tbm.cleanwizard.gradle.api.extensions.CleanWizardMultiModuleCodegenExtension

internal open class CleanWizardMultiModuleCodegenExtensionImplementation(
    override var domainProjectPath: String = "",
    override var presentationProjectPath: String = ""
) : CleanWizardMultiModuleCodegenExtension()