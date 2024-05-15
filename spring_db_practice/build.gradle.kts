import org.jetbrains.kotlin.fir.contracts.impl.FirEmptyContractDescription.source
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
}

val queryDslVersion = "5.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // jdbc
//    implementation("org.springframework.boot:spring-boot-starter-jdbc") // jpa나 mybatis가 이미 포함하고 있어 별도로 jdbc를 추가하지 않아도 된다.

    // mybatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")

    // jpa, spring-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // querydsl
    // spring boot 3.0대는 jakarta 붙여야함 : https://github.com/querydsl/querydsl/issues/3493
    // JPAAnnotationProcessor를 사용하기 위해 마지막에 :jpa를 붙임
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    // kotlin 코드가 아니라면 kapt 대신 annotationProcessor를 사용
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

//    implementation("com.querydsl:querydsl-apt:5.0.0:jakarta")
//    implementation("org.springframework.boot:spring-boot-configuration-processor")


    // h2
    runtimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompileOnly("org.projectlombok:lombok")
}

