package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

import corp.tbm.cleanwizard.buildLogic.config.api.CleanWizardMultiModuleCodegenExtension

internal open class CleanWizardMultiModuleCodegenExtensionImplementation(
    override var domainProjectPath: String = "",
    override var presentationProjectPath: String = ""
) : CleanWizardMultiModuleCodegenExtension()