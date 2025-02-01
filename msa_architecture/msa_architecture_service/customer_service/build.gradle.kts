plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    kotlin("plugin.jpa")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
}

ktlint {
    enableExperimentalRules.set(true)

    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
