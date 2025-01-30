plugins {
    kotlin("plugin.jpa")
}

dependencies {
    implementation(project(":order_service:order-domain:order-application-service"))
    implementation(project(":common:common-domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    implementation(project(":order_service:order-domain:order-domain-core"))
}

noArg {
    annotation("jakarta.persistence.Entity")
}

allOpen {
    annotation("jakarta.persistence.Entity")
}