import com.google.protobuf.gradle.id


val grpcVersion = "1.58.0"
val protobufVersion = "3.24.0"
val grpcKotlinVersion = "1.4.0"

plugins {
    // https://github.com/google/protobuf-gradle-plugin
    id("com.google.protobuf") version "0.9.2"
    kotlin("jvm") version "1.9.21"
}

// grpc kotlin(grpcKotlinVersion) : https://github.com/grpc/grpc-kotlin/blob/master/compiler/README.md
// grpc(grpcVersion) : https://github.com/grpc/grpc
// protobuf-kotlin(grpcKotlinVersion) : https://github.com/topics/protobuf-kotlin

dependencies {
//    implementation("io.grpc:grpc-stub:1.64.0")
//    implementation("io.grpc:grpc-protobuf:1.64.0")
//
//    runtimeOnly("io.grpc:grpc-netty-shaded:1.64.0")

    // https://dgahn.tistory.com/5 grpc kotlin은 flow로 이루어져 있어 추가해주어야 함
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")

//    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    implementation("com.google.protobuf:protobuf-kotlin:${protobufVersion}")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

sourceSets{
    getByName("main"){
        java {
            srcDirs(
                "build/generated/source/proto/main/java",
                "build/generated/source/proto/main/kotlin"
            )
        }
    }
}