package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal.processor

sealed class ClassGenerationConfig(open val suffix: String, open val packageName: String) {

    data class DTO(
        override val suffix: String,
        override val packageName: String,
        val dtoToDomainMapFunctionName: String,
        val domainToDtoMapFunctionName: String,
    ) : ClassGenerationConfig(suffix, packageName) {
        companion object {
            const val DTO_CLASS_PREFIX_KEY = "DTO_CLASS_PREFIX"
            const val DTO_CLASS_PACKAGE_NAME_KEY = "DTO_CLASS_PACKAGE_NAME"
            const val DTO_TO_DOMAIN_MAP_FUNCTION_NAME_KEY = "DTO_TO_DOMAIN_MAP_FUNCTION_NAME"
            const val DOMAIN_TO_DTO_MAP_FUNCTION_NAME_KEY = "DOMAIN_TO_DTO_MAP_FUNCTION_NAME"

            fun constructConfig(): DTO {
                return DTO(
                    mProcessorOptions[DTO_CLASS_PREFIX_KEY] ?: "DTO",
                    mProcessorOptions[DTO_CLASS_PACKAGE_NAME_KEY] ?: "dto",
                    mProcessorOptions[DTO_TO_DOMAIN_MAP_FUNCTION_NAME_KEY] ?: "toDomain",
                    mProcessorOptions[DOMAIN_TO_DTO_MAP_FUNCTION_NAME_KEY] ?: "toDTO"
                )
            }
        }
//        arg("DTO_CLASS_PREFIX", "Dto")
//        arg("DOMAIN_CLASS_PREFIX", "Model")
//        arg("UI_CLASS_PREFIX", "Ui")
//        arg("DTO_CLASS_PACKAGE_NAME", "dto")
//        arg("DOMAIN_CLASS_PACKAGE_NAME", "model")
//        arg("UI_CLASS_PACKAGE_NAME", "ui")
//        arg("DTO_TO_DOMAIN_MAP_FUNCTION_NAME", "toDomain")
//        arg("DOMAIN_TO_UI_MAP_FUNCTION_NAME", "toUI")
//        arg("DOMAIN_TO_DTO_MAP_FUNCTION_NAME", "fromDomain")
//        arg("UI_TO_DOMAIN_MAP_FUNCTION_NAME", "fromUI")
//        arg("DEFAULT_JSON_SERIALIZER", "kotlinx-serialization")
    }

    data class Domain(
        override val suffix: String,
        override val packageName: String,
    ) : ClassGenerationConfig(suffix, packageName) {
        companion object {
            const val DOMAIN_CLASS_PREFIX_KEY = "DOMAIN_CLASS_PREFIX"
            const val DOMAIN_CLASS_PACKAGE_NAME_KEY = "DOMAIN_CLASS_PACKAGE_NAME"

            fun constructConfig(processorOptions: Map<String, String>): Domain {
                return Domain(
                    processorOptions[DOMAIN_CLASS_PREFIX_KEY] ?: "Model",
                    processorOptions[DOMAIN_CLASS_PACKAGE_NAME_KEY] ?: "model"
                )
            }
        }
    }

    data class UI(
        override val suffix: String,
        override val packageName: String,
        val uiToDomainMapFunctionName: String,
    ) : ClassGenerationConfig(suffix, packageName) {

        companion object {
            const val UI_ClASS_PREFIX_KEY = "UI_CLASS_PREFIX"
            const val UI_CLASS_PACKAGE_NAME_KEY = "UI_CLASS_PACKAGE_NAME"
            const val UI_TO_DOMAIN_MAP_FUNCTION_NAME_KEY = "UI_TO_DOMAIN_MAP_FUNCTION_NAME"

            fun constructConfig(): UI {
                return UI(
                    mProcessorOptions[UI_ClASS_PREFIX_KEY] ?: "UI ",
                    mProcessorOptions[UI_CLASS_PACKAGE_NAME_KEY] ?: "ui",
                    mProcessorOptions[UI_TO_DOMAIN_MAP_FUNCTION_NAME_KEY] ?: "toDomain"
                )
            }
        }
    }

    companion object {
        private var mProcessorOptions: Map<String, String> = mapOf()

        fun setProcessorOptions(processorOptions: Map<String, String>) {
            mProcessorOptions = processorOptions
        }
    }
}