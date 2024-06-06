plugins {
    id(libs.plugins.cleanarchitecturemapper.workload.get().pluginId)
}
ksp {
    arg("DTO_CLASS_SUFFIX", "Dto")
    arg("DOMAIN_CLASS_SUFFIX", "Model")
    arg("UI_CLASS_SUFFIX", "Ui")
    arg("DTO_CLASS_PACKAGE_NAME", "dto")
    arg("DOMAIN_CLASS_PACKAGE_NAME", "model")
    arg("UI_CLASS_PACKAGE_NAME", "ui")
    arg("DTO_TO_DOMAIN_MAP_FUNCTION_NAME", "toDomain")
    arg("DOMAIN_TO_UI_MAP_FUNCTION_NAME", "toUI")
    arg("DOMAIN_TO_DTO_MAP_FUNCTION_NAME", "fromDomain")
    arg("UI_TO_DOMAIN_MAP_FUNCTION_NAME", "fromUI")
    arg("DEFAULT_JSON_SERIALIZER", "kotlinx-serialization")
}