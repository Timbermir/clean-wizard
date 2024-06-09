package corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation

open class CleanWizardProcessorConfig(
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
    var uiToDomainMapFunctionName: String = "toDomain",
    var defaultJsonSerializer: CleanWizardJsonSerializer = CleanWizardJsonSerializer.KOTLINX_SERIALIZATION
)