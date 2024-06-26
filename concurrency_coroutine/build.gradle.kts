// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
//    kotlin("plugin.noarg") version "1.9.23"
    kotlin("plugin.jpa")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.4")
//    implementation("org.redisson:redisson-spring-boot-starter:3.27.2")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.kotest:kotest-property:5.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0")

//    ktlint {
//        enableExperimentalRules.set(true)
//
//        filter {
//            exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
//        }
//    }
}

noArg {
    annotation("jakarta.persistence.Entity")
}

allOpen {
    annotation("jakarta.persistence.Entity")
}

ktlint {
    enableExperimentalRules.set(true)

    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}
