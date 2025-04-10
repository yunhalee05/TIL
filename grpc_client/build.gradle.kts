val grpcVersion = "1.58.0"
val protobufVersion = "3.24.0"
val grpcKotlinVersion = "1.4.0"

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("com.google.protobuf") version "0.9.2"
}

dependencies {
    implementation(project(":proto"))
//    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("net.devh:grpc-client-spring-boot-starter:2.15.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")

    // aop
    implementation("org.springframework:spring-aspects:6.1.7")

    // map-struct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.3.4")
}

ktlint {
    enableExperimentalRules.set(true)

    filter {
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}
