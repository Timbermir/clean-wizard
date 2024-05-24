plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "clean-architecture-mapper"
include("clean-architecture-mapper-processor")
include("workload")
