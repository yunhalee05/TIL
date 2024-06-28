plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    kotlin("jvm") version "1.9.21"
    id("com.google.protobuf") version "0.9.4"
}

val grpcVersion = "1.64.0"
val protobufVersion = "4.27.1"
val grpcKotlinVersion = "1.4.1"

dependencies {
    implementation(project(":proto"))
    implementation("net.devh:grpc-spring-boot-starter:2.14.0.RELEASE")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
}

ktlint {
    enableExperimentalRules.set(true)

    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}
