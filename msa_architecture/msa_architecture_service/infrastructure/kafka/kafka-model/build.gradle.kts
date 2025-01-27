plugins {
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.confluent:kafka-avro-serializer:7.8.0")
    implementation("org.apache.avro:avro:1.12.0")
}

avro {
    isCreateSetters.set(false) // Setter 생성 여부 (false로 설정하면 Kotlin과 더 호환 가능)
    stringType.set("String")  // Avro의 String 타입을 Java String으로 매핑
    fieldVisibility.set("PRIVATE") // 생성된 필드의 접근 제한자 설정
    outputCharacterEncoding.set("UTF-8") // 출력 파일의 인코딩 설정
}

val generateAvro = tasks.register<com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask>("generateAvro") {
    source("src/main/resources/avro") // Avro 스키마 경로
    setOutputDir(File("src/main/java"))
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


tasks {
    // Avro 파일 생성 작업과 Kotlin 컴파일러 간 의존성을 설정
    named("compileKotlin") {
        dependsOn("generateAvro") // Kotlin 컴파일 전에 Avro Java 생성 작업을 수행
    }

    named("compileTestKotlin") {
        dependsOn("generateAvro") // 테스트 컴파일 전에 Test Avro Java 생성 작업 수행
    }
}