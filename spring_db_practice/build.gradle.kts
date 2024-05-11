plugins {
//    id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-jdbc") // jpa나 mybatis가 이미 포함하고 있어 별도로 jdbc를 추가하지 않아도 된다.
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("com.querydsl:querydsl-jpa")
//    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jpa")
//    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
//    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    runtimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

}

tasks.named("clean") {
    delete("src/main/generated")
}