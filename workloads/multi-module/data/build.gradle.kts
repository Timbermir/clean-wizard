import com.google.devtools.ksp.gradle.KspTask
import corp.tbm.cleanarchitecturemapper.buildLogic.convention.foundation.CleanWizardProcessorConfig

plugins {
    alias(libs.plugins.cleanarchitecturemapper.workload)
}
dependencies {
    implementation(projects.workloads.multiModule.domain)
}

tasks.register<Copy>("moveGeneratedFiles") {
    dependsOn("kspKotlin")

    from("build/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/data/") {
        rootProject.extensions.findByType(CleanWizardProcessorConfig::class.java)?.let {
            exclude("**/${it.dtoClassPackageName}/**")
            exclude("**/${it.uiClassPackageName}/**")
        }
        include("**/*.kt")
    }.also {
        it.source.forEach {
            println(it.path)
        }
    }

    into("${projects.workloads.multiModule.domain.dependencyProject.layout.buildDirectory.asFile.get().path}/generated/ksp/main/kotlin/corp/tbm/cleanwizard/workloads/multimodule/domain/")
}
tasks.withType<KspTask>().configureEach {
    finalizedBy("moveGeneratedFiles")
}
tasks.named("compileKotlin") {
    dependsOn("moveGeneratedFiles")
}