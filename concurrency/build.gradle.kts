plugins {
//    java
//    id("org.springframework.boot") version "3.2.4"
//    id("io.spring.dependency-management") version "1.1.4"
}
//
//group = "com.yunhalee"
//version = "0.0.1-SNAPSHOT"
//
//java {
//    sourceCompatibility = JavaVersion.VERSION_17
//}
//
//repositories {
//    mavenCentral()
//}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.4")
    implementation("org.redisson:redisson-spring-boot-starter:3.27.2")
    runtimeOnly("com.mysql:mysql-connector-j")
}
//
//tasks.withType<Test> {
//    useJUnitPlatform()
//}
