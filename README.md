<div align="center">

![logo](.github/documentation/logo.svg)
</div>

<div align="center">
<h1> A Kotlin Symbol Processor that generates classes for Clean Architecture layers</h1>
</div>

**Clean Wizard is a [`KSP Processor`](https://kotlinlang.org/docs/ksp-overview.html)
that processes annotations and generates classes for
[`Clean Architecture`](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) layers
using [`Kotlinpoet`](https://square.github.io/kotlinpoet)**.

## Basic Usage

1. **Define your `DTOSchema` that you want to generate classes from and annotate it with `@DTO`**

```kotlin
@DTO
data class ComputerDTOSchema(
    @SerialName("motherboard")
    val motherboard: MotherboardDTOSchema,
    @SerialName("cpu")
    val cpu: CpuDTOSchema,
    @SerialName("isWorking")
    val isWorking: Boolean
)

@DTO
data class MotherboardDTOSchema(
    @SerialName("name")
    val name: String,
)

@DTO
data class CpuDTOSchema(
    @SerialName("name")
    val name: String,
)
```

2. **See the result**

```kotlin
public data class ComputerDTO(
    @SerialName("motherboard")
    public val motherboard: MotherboardDTO,
    @SerialName("cpu")
    public val cpu: CpuDTO,
    @SerialName("isWorking")
    public val isWorking: Boolean,
)

public fun ComputerDTO.toDomain(): ComputerModel = ComputerModel(
    motherboard.toDomain(),
    cpu.toDomain(), isWorking
)

public data class ComputerModel(
    public val motherboard: MotherboardModel,
    public val cpu: CpuModel,
    public val isWorking: Boolean,
)

public data class ComputerUI(
    public val motherboard: MotherboardUI,
    public val cpu: CpuUI,
    public val isWorking: Boolean,
)

public fun ComputerModel.toUI(): ComputerUI = ComputerUI(
    motherboard.toUI(), cpu.toUI(),
    isWorking
)
```

> [!TIP]  
> In case your @SerialName annotation value is the same as field name
> you can just skip adding @SerialName, processor will do it for you, so

```kotlin
@DTO
data class ComputerDTOSchema(
    val motherboard: MotherboardDTOSchema,
    val cpu: CpuDTOSchema,
    val isWorking: Boolean
)

@DTO
data class MotherboardDTOSchema(
    val name: String,
)

@DTO
data class CpuDTOSchema(
    val name: String,
)
```

**will produce the same:**

```kotlin
public data class ComputerDTO(
    @SerialName("motherboard")
    public val motherboard: MotherboardDTO,
    @SerialName("cpu")
    public val cpu: CpuDTO,
    @SerialName("isWorking")
    public val isWorking: Boolean,
)

public fun ComputerDTO.toDomain(): ComputerModel = ComputerModel(
    motherboard.toDomain(),
    cpuDTO.toDomain(), isWorking
)

public data class ComputerModel(
    public val motherboard: MotherboardModel,
    public val cpu: CpuModel,
    public val isWorking: Boolean,
)

public data class ComputerUI(
    public val motherboard: MotherboardUI,
    public val cpu: CpuUI,
    public val isWorking: Boolean,
)

public fun ComputerModel.toUI(): ComputerUI = ComputerUI(
    motherboard.toUI(), cpu.toUI(),
    isWorking
)
```

**Generated classes can be found under build package**:

```
build/
  └── generated/
      └── ksp/
          └── main/
              └── corp/
                  └── tbm/
                      └── cleanwizard/
                          ├── computer/
                          │   ├── dto/
                          │   │   └── ComputerDTO.kt
                          │   ├── model/
                          │   │   └── ComputerModel.kt
                          │   └── ui/
                          │       └── ComputerUI.kt
                          ├── motherboard/
                          │   ├── dto/
                          │   │   └── MotherboardDTO.kt
                          │   ├── model/
                          │   │   └── MotherboardModel.kt
                          │   └── ui/
                          │       └── MotherboardUI.kt
                          └── cpu/
                              ├── dto/
                              │   └── CpuDTO.kt
                              ├── model/
                              │   └── CpuModel.kt
                              └── ui/
                                  └── CpuUI.kt
```

**Don't worry, `top-level extension functions to map` are imported!**

```kotlin
import corp.tbm.cleanwizard.computer.model.ComputerModel
import corp.tbm.cleanwizard.cpu.ui.CpuUI
import corp.tbm.cleanwizard.cpu.ui.toUI
import corp.tbm.cleanwizard.motherboard.ui.MotherboardUI
import corp.tbm.cleanwizard.motherboard.ui.toUI
import kotlin.Boolean

public data class ComputerUI(
    public val motherboardUI: MotherboardUI,
    public val cpuUI: CpuUI,
    public val isWorking: Boolean,
)

public fun ComputerModel.toUI(): ComputerUI = ComputerUI(
    motherboardModel.toUI(), cpuModel.toUI(),
    isWorking
)
```

2. **If you would like to map to domain using some kind of interface, I got you**:

```kotlin
@DTO(toDomainAsTopLevel = false)
data class ComputerDTOSchema(
    val motherboard: MotherboardDTOSchema,
    val cpu: CpuDTOSchema,
    val isWorking: Boolean
)
```

**It will produce the following output:**

```kotlin
public data class ComputerDTO(
    @SerialName("motherboard")
    public val motherboard: MotherboardDTO,
    @SerialName("cpu")
    public val cpu: CpuDTO,
    @SerialName("isWorking")
    public val isWorking: Boolean,
) : DTOMapper<ComputerModel> {
    override fun toDomain(): ComputerModel = ComputerModel(
        motherboard.toDomain(),
        cpu.toDomain(), isWorking
    )
}
```

2.1 If your schema has lists, don't worry everything will be mapped

```kotlin
@DTO
data class ComputerDTOSchema(
  @SerialName("motherboard")
  val motherboard: MotherboardDTOSchema,
  @SerialName("cpu")
  val cpu: CpuDTOSchema,
  @SerialName("ram")
  val ram: List<RamDTOSchema>,
  @SerialName("isWorking")
  val isWorking: Boolean
)

@DTO
data class MotherboardDTOSchema(
  @SerialName("name")
  val name: String,
)

@DTO
data class CpuDTOSchema(
  @SerialName("name")
  val name: String,
)

@DTO
data class RamDTOSchema(
  @SerialName("name")
  val name: String,
  val capacity: Int
)
```

```kotlin
public data class ComputerDTO(
  @SerialName("motherboard")
  public val motherboard: MotherboardDTO,
  @SerialName("cpu")
  public val cpu: CpuDTO,
  @SerialName("ram")
  public val ram: List<RamDTO>,
  @SerialName("isWorking")
  public val isWorking: Boolean,
)

public fun ComputerDTO.toDomain(): ComputerModel = ComputerModel(
  motherboard.toDomain(),
  cpu.toDomain(),
  ram.map { ramDTO -> ramDTO.toDomain() },
  isWorking
)

...
```

## Advanced Usage

1. You are able to generate enums, however, with only one parameter due to Kotlin annotations limitations.
   You are not able to use your custom predefined enum,
   [see this issue for details](https://github.com/Timbermir/clean-wizard/issues/46).

```kotlin
@DTO
data class ComputerDTOSchema(
    @SerialName("motherboard")
    val motherboard: MotherboardDTOSchema,
    @SerialName("cpu")
    val cpu: CpuDTOSchema,
    @SerialName("ram")
    val ram: List<RamDTOSchema>,
    @SerialName("isWorking")
    @IntEnum(
        enumName = "ComputerStatus",
        parameterName = "status",
        enumEntries = ["NO_POWER", "DISPLAY_NOT_WORKING", "WORKING", "CPU_PROBLEMS"],
        enumEntryValues = [1, 2, 3, 4]
    )
    val isWorking: Int
)
```

`build/generated/org.orgname/projectname/computer/model/enums/ComputerStatus.kt`
```kotlin
public enum class ComputerStatus(
    public val status: Int,
) {
    NO_POWER(status = 1),
    DISPLAY_NOT_WORKING(status = 2),
    WORKING(status = 3),
    CPU_PROBLEMS(status = 4),
    ;
}
```

```kotlin
public data class ComputerDTO(
    @SerialName("motherboard")
    public val motherboard: MotherboardDTO,
    @SerialName("cpu")
    public val cpu: CpuDTO,
    @SerialName("ram")
    public val ram: List<RamDTO>,
    @SerialName("isWorking")
    public val isWorking: ComputerStatus,
)
...

public data class ComputerModel(
    @SerialName("motherboard")
    public val motherboard: MotherboardModel,
    @SerialName("cpu")
    public val cpu: CpuModel,
    @SerialName("ram")
    public val ram: List<RamModel>,
    @SerialName("isWorking")
    public val isWorking: ComputerStatus,
)

public data class ComputerUI(
    @SerialName("motherboard")
    public val motherboard: MotherboardUI,
    @SerialName("cpu")
    public val cpu: CpuUI,
    @SerialName("ram")
    public val ram: List<RamUI>,
    @SerialName("isWorking")
    public val isWorking: ComputerStatus,
)
```

1.1 `enumName` and `parameterName` properties can be omitted. Property name will be used instead

**`ComputerDTOSchema.kt`**
```kotlin
@DTO
data class ComputerDTOSchema(
    @SerialName("motherboard")
    val motherboard: MotherboardDTOSchema,
    @SerialName("cpu")
    val cpu: CpuDTOSchema,
    @SerialName("ram")
    val ram: List<RamDTOSchema>,
    @SerialName("isWorking")
    @IntEnum(
        enumEntries = ["NO_POWER", "DISPLAY_NOT_WORKING", "WORKING", "CPU_PROBLEMS"],
        enumEntryValues = [1, 2, 3, 4]
    )
    val isComputerWorking: Int
)
```
`build/generated/org.orgname/projectname/computer/model/enums/ComputerStatus`
```kotlin
public enum class IsComputerWorking(
    public val isComputerWorking: Int,
) {
    NO_POWER(isComputerWorking = 1),
    DISPLAY_NOT_WORKING(isComputerWorking = 2),
    WORKING(isComputerWorking = 3),
    CPU_PROBLEMS(isComputerWorking = 4),
    ;
}
```

You can see all the available enums
available for 
generation [here](foundation/annotations/src/main/kotlin/corp/tbm/cleanwizard/foundation/annotations/Enum.kt)

2.0 Let's imagine that you want to change the suffix of the DTO classes from `DTO` to `Dto`.
Using `ksp` extension's `arg("KEY", "value")` is not type-safe and map-based,
so making mistake in a key is not uncommon.

For this case, `clean-wizard` introduces the custom extension for passing processor options.

You need to apply `clean-wizard` plugin to your root `build.gradle.kts`

<details open>
  <summary>Gradle (Groovy) - build.gradle(:project-name)</summary>

```gradle
plugins {
    id 'io.github.timbermir.clean-wizard' version '1.0.0'
}
```
</details>

<details>
  <summary>Gradle (Kotlin) - build.gradle.kts(:project-name)</summary>  

```gradle
plugins {
    id("io.github.timbermir.clean-wizard") version "1.0.0"
}
```
</details>

2.1
Use `` `clean-wizard` `` extension in your root `build.gradle.kts` and change the suffix 

```kotlin
`clean-wizard` {
    data {
        classSuffix = "DTO"
    }
}
```

2.2 See the result

`build/generated/org.orgname/projectname/computer/dto/ComputerDto.kt`
```kotlin
public data class ComputerDto(
  @SerialName("motherboard")
  public val motherboard: MotherboardDto,
  @SerialName("cpu")
  public val cpu: CpuDto,
  @SerialName("ram")
  public val ram: List<RamDto>,
  @SerialName("isComputerWorking")
  public val isComputerWorking: IsComputerWorking,
)

public fun ComputerDto.toModel(): ComputerDomain = ComputerDomain(
    motherboard.toModel(), 
    cpu.toModel(), 
    ram.map { ramDto -> ramDto.toModel() }, 
    isComputerWorking
)

...
```

Ready-to-use block with all the fields needed
```kotlin
`clean-wizard` {

    jsonSerializer {
        kotlinXSerialization {
            json {
                encodeDefaults = true
                prettyPrint = true
                explicitNulls = false
                @OptIn(ExperimentalSerializationApi::class)
                namingStrategy = JsonNamingStrategy.KebabCase
            }
        }
    }

    dataClassGenerationPattern = CleanWizardDataClassGenerationPattern.LAYER

    dependencyInjection {
        kodein {
            useSimpleFunctions = true
            binding = CleanWizardDependencyInjectionFramework.Kodein.KodeinBinding.Multiton()
        }
    }

    data {
        classSuffix = "DTO"
        packageName = "dtos"
        toDomainMapFunctionName = "toModel"
        interfaceMapper {
            className = "DTOMapper"
            pathToModuleToGenerateInterfaceMapper = projects.workloads.core.dependencyProject.name
        }
        room {
            typeConverters {
                classSuffix = "DTOConverters"
                packageName = "typeConverters"
                generateSeparateConverterForEachDTO = true
                useProvidedTypeConverter = true
            }
        }
    }

    domain {
        classSuffix = "Domain"
        packageName = "models"
        toDTOMapFunctionName = "fromDomain"
        toUIMapFunctionName = "toUI"
        useCase {
            packageName = "useCase"
            useCaseFunctionType = CleanWizardUseCaseFunctionType.CustomFunctionName("execute")
            classSuffix = "UseCase"
        }
    }

    presentation {
        moduleName = "ui"
        classSuffix = "Ui"
        packageName = "uis"
        shouldGenerate = true
        toDomainMapFunctionName = "fromUI"
    }
}
```

> [!TIP]  
> In case you don't know what block has, just type `this.` and available methods or properties will be shown.
> 
3.0 You can define the `serializer` that you want Clean Wizard to use in `jsonSerializer` block. The following
serializers are available:
- `kotlinx-serialization`
- `Gson`
- `Moshi`

3.1 You can define the config for `kotlinx-serialization` & `Gson` inside their respective blocks just like that:

```kotlin
jsonSerializer {
    json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = false
        allowStructuredMapKeys = true
        prettyPrint = true
        explicitNulls = false
        prettyPrintIndent = "   "
        @OptIn(ExperimentalSerializationApi::class)
        namingStrategy = JsonNamingStrategy.KebabCase
        ...
    }
}
```

```kotlin
jsonSerializer {
    gson {
        longSerializationPolicy = LongSerializationPolicy.DEFAULT
        fiendNamingPolicy = FieldNamingPolicy.IDENTITY
        serializeNulls = false
        datePattern = ""
        dateStyle = DateFormat.DEFAULT
        timeStyle = DateFormat.DEFAULT
        ...
    }
}
```

3.2 If you don't like defining the config using the extension, you can just pass the `Gson` directly.

```kotlin
jsonSerializer {
    gson {
        gson = GsonBuilder().serializeNulls().disableHtmlEscaping().create()
    }
}
```
The serializer that you have defined is going to be used when generating data classes and `Room` `TypeConverters`.

4.0 You can define the `Dependency Injection` framework that is used for generating use cases. You can choose from:
- Koin
- Koin Annotations
- Dagger2
- Kodein

This can be configured in the `dependencyInjection` block.

```kotlin
dependencyInjection {
        kodein {
            useSimpleFunctions = true
            binding = CleanWizardDependencyInjectionFramework.Kodein.KodeinBinding.Multiton()
        }
    }
```

4.0 You can completely disable the generation of presentation UI classes by assigning value of the `shouldGenerate`
property in
`presentation` block to `false`.

You can see the list of available
options [here](build-logic/convention/src/main/kotlin/corp/tbm/cleanwizard/buildLogic/convention/foundation/CleanWizardProcessorConfig.kt)

## Setup 🧩

**Clean Wizard is available via [Maven Central](https://central.sonatype.com/)**

1. **Add the KSP Plugin**

> **Note**: The KSP version you choose directly depends on the Kotlin version your project utilize </br>
> You can check https://github.com/google/ksp/releases for the list of KSP versions, then select the latest release that
> is compatible with
> your Kotlin version.
> Example:
> If you're using `1.9.22` Kotlin version, then the latest KSP version is `1.9.22-1.0.17`.

<details open>
  <summary>Gradle (Groovy) - build.gradle(:module-name)</summary>

```gradle
plugins {
    id 'com.google.devtools.ksp' version '1.9.22-1.0.17'
}
```

</details>

<details>
  <summary>Gradle (Kotlin) - build.gradle.kts(:module-name)</summary>  

```gradle
plugins {
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
}
```

</details>

2. **Add dependencies**

<details open>
  <summary>Gradle (Groovy) - build.gradle(:module-name)</summary>

```gradle
dependencies {
    implementation 'io.github.timbermir.clean-wizard:clean-wizard:1.0.0'
    ksp 'io.github.timbermir.clean-wizard:data-class-compiler:1.0.0'
}
```

</details>

<details>
  <summary>Gradle (Kotlin) - build.gradle.kts(:module-name)</summary>  

```gradle
dependencies {
    implementation("io.github.timbermir.clean-wizard:clean-wizard:1.0.0")
    ksp("io.github.timbermir.clean-wizard:data-class-compiler:1.0.0")
}
```

</details>

3. (Optional) **Apply `clean-wizard` plugin for custom processor options**
<details open>
  <summary>Gradle (Groovy) - build.gradle(:project-name)</summary>

```gradle
plugins {
    id 'io.github.timbermir.clean-wizard' version '1.0.0'
}
```
</details>

<details>
  <summary>Gradle (Kotlin) - build.gradle.kts(:project-name)</summary>  

```gradle
plugins {
    id("io.github.timbermir.clean-wizard") version "1.0.0"
}
```
</details>

## Current Processor limitations 🚧

- ~~**SUPPORTS data class generation only in a single module, in other words you can't generate `DTO`s for `data` module,
  or `Model`s for `domain module`, they are generated in module where `DTOSchema` is located**~~
- ~~**SUPPORTS only [kotlinx-serialization-json](https://github.com/Kotlin/kotlinx.serialization)**~~
- ~~**DOES NOT support `enums`, `collections` or any custom type but the source ones**~~
- ~~**DOES NOT support inheriting other annotations**~~
- ~~**DOES NOT support inheriting `@SerialName` value if present, generated `@SerialName` value is derived from field's
  name**~~
- ~~**DOES NOT support backwards mapping, i.e., from `model` to `DTO`**~~
- ~~**DOES NOT
  support [custom processor options](https://kotlinlang.org/docs/ksp-quickstart.html#pass-options-to-processors),
  i.e.,
  change `DTO` classes suffix to `Dto`**~~
- **DOES NOT support multiplatform**
- ~~**DOES NOT support [Room](https://developer.android.com/jetpack/androidx/releases/room) entity generation, therefore
  no `TypeConverters` generation**~~
- **DOES NOT utilize [Incremental processing](https://kotlinlang.org/docs/ksp-incremental.html)**
- ~~**DOES NOT utilize [Multiple round processing](https://kotlinlang.org/docs/ksp-multi-round.html)**~~

## Building

**It is recommended to use the latest released version of IntelliJ IDEA** (**Community** or **Ultimate Edition**).
You can download
[IntelliJ IDEA](https://www.jetbrains.com/idea/download/) here.

The project **relies on [**`Gradle`**](https://gradle.org/) as its main build tool**.
[**Currently used version is** `8.9`](https://docs.gradle.org/8.8/release-notes.html?_gl=1*1gusy0x*_ga*NDAwNDUzNzY3LjE3MTU4NDUzOTY.*_ga_7W7NC6YNPT*MTcxNzc1MDYxOS43LjEuMTcxNzc1MTAxMC40OS4wLjA.).
**IntelliJ will try to find it among the installed Gradle Versions** or **download it automatically if it
couldn't be found**.

The project **requires JDK 19 to build classes and to run tests**.
**Gradle will try to find it among the installed JDKs** or
**provision it automatically if it couldn't be found**.

For local builds, **you can use an earlier or later version of JDK if you don't have that version installed**.
Specify
the version of this JDK with the `jdk` property
in [`project-config.versions.toml`](gradle/project-config.versions.toml).

After that, `Gradle` will download all dependencies the project depends on.
Run the processor via [`Main.kt`](workload/src/main/kotlin/corp/tbm/cleanwizard/workload/Main.kt)

On Windows, you might need to add long paths setting to the repository:

```
git config core.longpaths true
```

The errors related to inline properties usage in `build.gradle.kts` files can occur when IntelliJ IDEA cannot
resolve `Target JVM Version` for Kotlin Compiler, causing it to fall back
to the default 1.8.
To resolve the errors, follow these steps

1. Navigate to `Settings -> Build, Execution, Deployment -> Compiler -> Kotlin Compiler`
2. Set the `Target JVM Version` to match the `jdk` property specified
   in [`project-config.versions.toml`](gradle/project-config.versions.toml).

## License

**clean-wizard** is distributed under the terms of the **Apache License (Version 2.0)**.
See the [license](LICENSE.txt) for more
information.