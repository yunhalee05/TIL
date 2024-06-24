plugins {
//    kotlin("plugin.jpa")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-web")
//    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
//    implementation("org.springframework.kafka:spring-kafka:3.1.3")

    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.kotest:kotest-property:5.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0")

    // object mapper
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // hibernate-validator
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // springdoc-openapi
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // common package
    implementation("com.yunhalee:common:0.0.3-SNAPSHOT")
}
//
// noArg {
//    annotation("jakarta.persistence.Entity")
// }
//
// allOpen {
//    annotation("jakarta.persistence.Entity")
// }

ktlint {
    enableExperimentalRules.set(true)

    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/yunhalee05/TIL")
        credentials {
            username = project.findProperty("repo.user") as String? ?: System.getenv("PERSONAL_USER_ID")
            password = project.findProperty("repo.key") as String? ?: System.getenv("PERSONAL_USER_KEY")
        }
    }
}
