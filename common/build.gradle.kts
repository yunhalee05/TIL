plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    application
}

group = "com.yunhalee.common"
version = "0.0.1-SNAPSHOT"


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.kotest:kotest-property:5.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0")

    // object mapper
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // webclient
}

ktlint {
    enableExperimentalRules.set(true)

    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}

apply {
    plugin("maven-publish")
}


configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.kotlinSourcesJar.get())
            artifact(tasks.jar.get())
        }
    }

    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/yunhalee05/til")
            credentials {
                username = project.findProperty("repo.user") as String? ?: System.getenv("")
                password = project.findProperty("repo.key") as String? ?: System.getenv("")
            }
        }
    }
}
