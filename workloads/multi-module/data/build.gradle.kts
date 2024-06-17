plugins {
    alias(libs.plugins.cleanwizard.multimodule)
}

dependencies {
    implementation(projects.workloads.multiModule.domain)
}

`clean-wizard-codegen` {
    domainProject = projects.workloads.multiModule.domain.dependencyProject.path
    presentationProject = projects.workloads.multiModule.presentation.dependencyProject.path
}
ksp {
    arguments.forEach {
        println(it)
    }
}