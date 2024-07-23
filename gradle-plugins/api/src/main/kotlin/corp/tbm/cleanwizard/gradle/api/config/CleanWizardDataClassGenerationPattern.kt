package corp.tbm.cleanwizard.gradle.api.config

import corp.tbm.cleanwizard.gradle.api.annotations.CleanWizardExperimentalAPI

enum class CleanWizardDataClassGenerationPattern {

    /**
     * Layer-based generation looks like the following:
     * ```
     * build/
     *   └── generated/
     *       └── ksp/
     *           └── main/
     *               └── corp/
     *                   └── tbm/
     *                       └── cleanwizard/
     *                           ├── data
     *                           │   ├── ComputerDTO.kt
     *                           │   ├── MotherboardDTO.kt
     *                           │   └── CpuDTO.kt
     *                           ├── domain
     *                           │   ├── ComputerModel.kt
     *                           │   ├── MotherboardModel.kt
     *                           │   └── CpuModel.kt
     *                           └── presentation
     *                               ├── ComputerUI.kt
     *                               ├── MotherboardUI.kt
     *                               └── CpuUI.kt
     * ```
     * As of now, it is the recommended generation pattern due to its stability and reliability.
     * However, if it seems unreadable to you and the amount of classes is huge,
     * use [CleanWizardDataClassGenerationPattern.LAYER]
     * @see [CleanWizardDataClassGenerationPattern.LAYER]
     */
    LAYER,

    @CleanWizardExperimentalAPI
    /**
     * Type-based generation looks like the following
     *```
     * build/
     *   └── generated/
     *       └── ksp/
     *           └── main/
     *               └── corp/
     *                   └── tbm/
     *                       └── cleanwizard/
     *                           ├── computer/
     *                           │   ├── dto/
     *                           │   │   └── ComputerDTO.kt
     *                           │   ├── model/
     *                           │   │   └── ComputerModel.kt
     *                           │   └── ui/
     *                           │       └── ComputerUI.kt
     *                           ├── motherboard/
     *                           │   ├── dto/
     *                           │   │   └── MotherboardDTO.kt
     *                           │   ├── model/
     *                           │   │   └── MotherboardModel.kt
     *                           │   └── ui/
     *                           │       └── MotherboardUI.kt
     *                           └── cpu/
     *                               ├── dto/
     *                               │   └── CpuDTO.kt
     *                               ├── model/
     *                               │   └── CpuModel.kt
     *                               └── ui/
     *                                   └── CpuUI.kt
     *```
     * However, **it is not stable and not recommended to use** unless your package depth
     * is <= 5 due to how packages are generated and retrieved in the processor.
     * Use [CleanWizardDataClassGenerationPattern.LAYER] instead.
     * @see [CleanWizardDataClassGenerationPattern.LAYER]
     */
    TYPE;
}