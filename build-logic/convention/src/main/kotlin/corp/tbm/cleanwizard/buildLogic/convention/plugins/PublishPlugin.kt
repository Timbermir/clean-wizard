package corp.tbm.cleanwizard.buildLogic.convention.plugins

import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.alias
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.libs
import corp.tbm.cleanwizard.buildLogic.convention.foundation.extensions.projectConfig
import corp.tbm.cleanwizard.buildLogic.convention.plugins.extensions.PublishExtensionImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugins.signing.SigningExtension

internal class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            alias(libs.plugins.maven.publish)
            alias(libs.plugins.gradle.signing)

            val extension = extensions.create("publish", PublishExtensionImplementation::class)
            afterEvaluate {

                extensions.configure<JavaPluginExtension> {
                    withSourcesJar()
                    withJavadocJar()
                }

                extensions.configure<PublishingExtension> {
                    repositories {
                        mavenCentral()
                    }
                    publications {
                        create<MavenPublication>("mavenPublication") {
                            from(components["kotlin"])
                            groupId = projectConfig.versions.group.get()
                            artifactId = extension.artifactId.takeIf { it.isNotEmpty() }
                                ?: project.displayName.replace("project", "").replaceFirst(":", "").replace(":", "-")
                                    .replace("'", "").replace(" ", "")
                            version = projectConfig.versions.version.get()
                            pom {
                                inceptionYear.set("2024")
                                name.set("Clean Wizard")
                                url.set("https://github.com/Timbermir/clean-wizard")
                                description.set("Clean Wizard - Kotlin Symbol Processor that becomes the wizard you are looking for when dealing with Clean Architecture")

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
                        }
                    }
                }

                extensions.configure<SigningExtension> {
                    useInMemoryPgpKeys(
                        findProperty("signing.keyId").toString(),
                        findProperty("signing.passphrase").toString(),
                        findProperty("signing.secretKeyRingFile").toString()
                    )
                    sign(extensions.getByType<PublishingExtension>().publications["mavenPublication"])
                }
            }
        }
    }
}