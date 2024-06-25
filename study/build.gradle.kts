import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
//    kotlin("plugin.jpa")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"

    // kover
    id("org.jetbrains.kotlinx.kover") version "0.8.1"
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

    // mockk
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
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

kover {
    reports {
        filters {
            excludes {
                classes("com.yunhalee.study.domain.State")
            }
        }

        verify {
            rule("Basic Line Converage") {
                bound {
                    minValue = 20
                    maxValue = 100
                    coverageUnits = CoverageUnit.LINE
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                }
            }

            rule("Branch Coverage") {
                bound {
                    minValue = 70
                    maxValue = 100
                    coverageUnits = CoverageUnit.BRANCH
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                }
            }

            rule {
                bound {
                    minValue = 80
                }

            }
        }
    }

}