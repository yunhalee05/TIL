import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
}

dependencies {
    implementation("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.2")
    // spring batch
    implementation("org.springframework.batch:spring-batch-core")
    implementation("org.springframework.batch:spring-batch-integration")
    // partitioner
    implementation("org.springframework.batch:spring-batch-integration:4.3.5")
}
