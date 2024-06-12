package corp.tbm.cleanwizard.foundation.codegen.universal.processor

sealed class ClassGenerationConfig(open val moduleName: String, open val suffix: String, open val packageName: String) {

    data class DTO(
        override val moduleName: String = "data",
        override val suffix: String = "DTO",
        override val packageName: String = "dto",
        val dataModuleName: String = "data",
        val dtoInterfaceMapperName: String = "DTOMapper",
        val dtoToDomainMapFunctionName: String = "toDomain",
        val domainToDtoMapFunctionName: String = "toDTO",
    ) : ClassGenerationConfig(moduleName, suffix, packageName) {

        internal companion object {

            private const val DATA_MODULE_NAME_KEY = "DATA_MODULE_NAME"
            private const val DTO_CLASS_SUFFIX_KEY = "DTO_CLASS_SUFFIX"
            private const val DTO_CLASS_PACKAGE_NAME_KEY = "DTO_CLASS_PACKAGE_NAME"
            private const val DTO_INTERFACE_MAPPER_NAME_KEY = "DTO_INTERFACE_MAPPER_NAME"
            private const val DTO_TO_DOMAIN_MAP_FUNCTION_NAME_KEY = "DTO_TO_DOMAIN_MAP_FUNCTION_NAME"
            private const val DOMAIN_TO_DTO_MAP_FUNCTION_NAME_KEY = "DOMAIN_TO_DTO_MAP_FUNCTION_NAME"

            fun constructConfig(): DTO {
                return DTO(
                    processorOptions[DATA_MODULE_NAME_KEY] ?: "data",
                    processorOptions[DTO_CLASS_SUFFIX_KEY] ?: "DTO",
                    processorOptions[DTO_CLASS_PACKAGE_NAME_KEY] ?: "dto",
                    processorOptions[DTO_INTERFACE_MAPPER_NAME_KEY] ?: "DTOMapper",
                    processorOptions[DTO_TO_DOMAIN_MAP_FUNCTION_NAME_KEY] ?: "toDomain",
                    processorOptions[DOMAIN_TO_DTO_MAP_FUNCTION_NAME_KEY] ?: "toDTO"
                )
            }
        }
    }

    data class Domain(
        override val moduleName: String = "domain",
        override val suffix: String = "Model",
        override val packageName: String = "model",
    ) : ClassGenerationConfig(moduleName, suffix, packageName) {

        internal companion object {

            private const val DOMAIN_MODULE_NAME_KEY = "DOMAIN_MODULE_NAME"
            private const val DOMAIN_CLASS_SUFFIX_KEY = "DOMAIN_CLASS_SUFFIX"
            private const val DOMAIN_CLASS_PACKAGE_NAME_KEY = "DOMAIN_CLASS_PACKAGE_NAME"

            fun constructConfig(): Domain {
                return Domain(
                    processorOptions[DOMAIN_MODULE_NAME_KEY] ?: "domain",
                    processorOptions[DOMAIN_CLASS_SUFFIX_KEY] ?: "Model",
                    processorOptions[DOMAIN_CLASS_PACKAGE_NAME_KEY] ?: "model"
                )
            }
        }
    }

    data class UI(
        override val moduleName: String = "presentation",
        override val suffix: String = "UI",
        override val packageName: String = "ui",
        val domainToUiMapFunctionName: String = "toUI",
        val uiToDomainMapFunctionName: String = "toDomain",
    ) : ClassGenerationConfig(moduleName, suffix, packageName) {

        internal companion object {
            private const val PRESENTATION_MODULE_NAME_KEY = "PRESENTATION_MODULE_NAME"
            private const val UI_CLASS_SUFFIX_KEY = "UI_CLASS_SUFFIX"
            private const val UI_CLASS_PACKAGE_NAME_KEY = "UI_CLASS_PACKAGE_NAME"
            private const val DOMAIN_TO_UI_MAP_FUNCTION_NAME_KEY = "DOMAIN_TO_UI_MAP_FUNCTION_NAME"
            private const val UI_TO_DOMAIN_MAP_FUNCTION_NAME_KEY = "UI_TO_DOMAIN_MAP_FUNCTION_NAME"

            fun constructConfig(): UI {
                return UI(
                    processorOptions[PRESENTATION_MODULE_NAME_KEY] ?: "presentation",
                    processorOptions[UI_CLASS_SUFFIX_KEY] ?: "UI",
                    processorOptions[UI_CLASS_PACKAGE_NAME_KEY] ?: "ui",
                    processorOptions[DOMAIN_TO_UI_MAP_FUNCTION_NAME_KEY] ?: "toUI",
                    processorOptions[UI_TO_DOMAIN_MAP_FUNCTION_NAME_KEY] ?: "toDomain",
                )
            }
        }
    }

    internal companion object {
        private var processorOptions: Map<String, String> = mapOf()

        fun setProcessorOptions(processorOptions: Map<String, String>) {
            Companion.processorOptions = processorOptions
        }
    }
}