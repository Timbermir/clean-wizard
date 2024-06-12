package corp.tbm.cleanwizard.buildLogic.convention.foundation

open class CleanWizardProcessorConfig(
    var dataClassGenerationPattern: CleanWizardDataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER,
    var jsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KOTLINX_SERIALIZATION,
    var dataModuleName: String = "`data`",
    var domainModuleName: String = "domain",
    var presentationModuleName: String = "presentation",
    var dtoClassSuffix: String = "DTO",
    var dtoClassPackageName: String = "dto",
    var dtoInterfaceMapperName: String = "DTOMapper",
    var dtoToDomainMapFunctionName: String = "toDomain",
    var domainToDtoMapFunctionName: String = "toDTO",
    var domainClassSuffix: String = "Model",
    var domainClassPackageName: String = "model",
    var uiClassSuffix: String = "UI",
    var uiClassPackageName: String = "ui",
    var domainToUiMapFunctionName: String = "toUI",
    var uiToDomainMapFunctionName: String = "toDomain"
)