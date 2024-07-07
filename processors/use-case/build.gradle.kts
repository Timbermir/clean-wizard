import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.cleanwizard.internal.processor)
    alias(libs.plugins.vanniktech.maven.publish)
}

dependencies {
    implementation(libs.bundles.di)
}

mavenPublishing {

    coordinates(
        projectConfig.versions.group.get(),
        "${projectConfig.versions.artifact.get()}-use-case-compiler",
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