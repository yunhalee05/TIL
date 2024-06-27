plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    kotlin("jvm") version "1.9.21"
}

dependencies {
    implementation(project(":proto")) {
        exclude(group = "io.grpc", module = "grpc-stub")
        exclude(group = "io.grpc", module = "google-protobuf")
    }
    implementation("net.devh:grpc-spring-boot-starter:2.14.0.RELEASE")
}

ktlint {
    enableExperimentalRules.set(true)

    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}
