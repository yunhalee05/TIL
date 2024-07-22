plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("com.google.protobuf") version "0.9.2"
}

val grpcVersion = "1.58.0"
val protobufVersion = "3.24.0"
val grpcKotlinVersion = "1.4.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.kotest:kotest-property:5.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    // springdoc-openapi
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // object mapper
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // springdoc-openapi
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // grpc server
    implementation(project(":proto"))
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")
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
