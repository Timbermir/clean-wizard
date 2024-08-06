package corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions

interface PublishExtension {
    var artifactId: String
}

internal open class PublishExtensionImplementation : PublishExtension {
    override var artifactId: String = ""
}