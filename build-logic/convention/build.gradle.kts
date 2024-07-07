import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.dsl)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.gradle.publish)
    alias(libs.plugins.maven.signing)
    alias(libs.plugins.vanniktech.maven.publish)
}

dependencies {
    implementation(projects.config)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.vanniktech.maven.publish)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.google.devtools.ksp)
    compileOnly(files(ksp.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(projectConfig.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(files(pluginConfig.javaClass.superclass.protectionDomain.codeSource.location))
}

group = projectConfig.versions.group.get()
version = projectConfig.versions.version.get()
println(path)
mavenPublishing {

    coordinates(
        projectConfig.versions.group.get(),
        projectConfig.versions.artifact.get(),
        projectConfig.versions.version.get()
    )

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    pom {
        inceptionYear.set("2024")
        name.set("Clean Wizard")
        url.set("https://github.com/Timbermir/clean-wizard")
        description.set("Clean Wizard - A Kotlin Symbol Processor that becomes the wizard you are looking for when dealing with Clean Architecture")

        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        scm {
            url.set("https://github.com/Timbermir/clean-wizard")
            connection.set("scm:git:git://github.com/Timbermir/clean-wizard.git")
            developerConnection.set("scm:git@github.com:Timbermir/clean-wizard.git")
        }

        issueManagement {
            system.set("GitHub Issues")
            url.set("https://github.com/Timbermir/clean-wizard/issues/")
        }

        organization {
            name.set("Timbermir")
            url.set("https://github.com/Timbermir")
        }

        developers {
            developer {
                id.set(findProperty("developerId").toString())
                name.set("timplifier")
                email.set("timplifier@gmail.com")
                url.set("https://github.com/timplifier")
            }
        }
    }
    signAllPublications()
}

gradlePlugin {

    website.set("https://github.com/Timbermir/clean-wizard")
    vcsUrl.set("https://github.com/Timbermir/clean-wizard.git")

    plugins {
        libs.plugins.cleanwizard.apply {

            val pluginConfigVersions = pluginConfig.versions.cleanwizard

            register(core.pluginId) {
                id = core.pluginId
                implementationClass = pluginConfigVersions.root.implementation.get()
                displayName = "Clean Wizard Root Project Plugin"
                description = "This plugin is required for advanced configuration"
                tags.set(
                    listOf(
                        "kotlin",
                        "ksp",
                        "kotlin-symbol-processing",
                        "android",
                        "clean-architecture",
                        "domain-model",
                        "ui-model",
                        "dto-entity-mapper",
                    )
                )
            }

            register(multimodule.pluginId) {
                id = multimodule.pluginId
                implementationClass = pluginConfigVersions.multimodule.implementation.get()
                displayName = "Clean Wizard Multi Module Plugin"
                description = "This plugins is required for across module generation"
                tags.set(
                    listOf(
                        "kotlin",
                        "ksp",
                        "kotlin-symbol-processing",
                        "android",
                        "clean-architecture",
                        "domain-model",
                        "ui-model",
                        "dto-entity-mapper",
                    )
                )
            }

            register(internal.kotlin.pluginId) {
                id = internal.kotlin.pluginId
                implementationClass = pluginConfigVersions.kotlin.implementation.get()
            }

            register(internal.codegen.pluginId) {
                id = internal.codegen.pluginId
                implementationClass = pluginConfigVersions.codegen.implementation.get()
            }

            register(internal.processor.pluginId) {
                id = internal.processor.pluginId
                implementationClass = pluginConfigVersions.processor.implementation.get()
            }

            register(internal.visitor.pluginId) {
                id = internal.visitor.pluginId
                implementationClass = pluginConfigVersions.visitor.implementation.get()
            }

            register(internal.publish.pluginId) {
                id = internal.publish.pluginId
                implementationClass = pluginConfigVersions.publish.implementation.get()
            }
        }
    }
}

internal val Provider<PluginDependency>.pluginId
    get() = get().pluginId

publishing {
    publications {
        create<MavenPublication>("clean-wizard-core-plugin") {
            group = projectConfig.versions.group.get()
            artifactId = projectConfig.versions.artifact.get()
            version = projectConfig.versions.version.get()

            pom {
                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations["runtimeClasspath"].allDependencies.forEach {
                        if (it.group == "corp.tbm.cleanwizard" && !it.group.toString().contains("internal")) {
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                        }
                    }
                }
            }
        }

        create<MavenPublication>("clean-wizard-multi-module-plugin") {
            group = projectConfig.versions.group.get()
            artifactId = "${projectConfig.versions.artifact.get()}-multi-module"
            version = projectConfig.versions.version.get()
        }
    }
}