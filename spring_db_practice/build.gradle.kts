import com.ewerk.gradle.plugins.tasks.QuerydslCompile
import io.spring.gradle.dependencymanagement.org.codehaus.plexus.util.StringUtils.clean
import org.jetbrains.kotlin.gradle.utils.extendsFrom

plugins {
    id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
    kotlin("kapt")
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
//    implementation("com.querydsl:querydsl-jpa")
////    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jpa")
//    //    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
//
//    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
//    implementation("com.querydsl:querydsl-spatial:${queryDslVersion}")
//    annotationProcessor("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
//    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
//    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // spring boot 3.0대는 jakarta 붙여야함 : https://github.com/querydsl/querydsl/issues/3493
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
//    api("com.querydsl:querydsl-jpa:5.0.0:jakarta")

    // h2
    runtimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

//    kapt(":annotation-compile")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("clean") {
    delete("src/main/generated")
}

//val querydslDir = "${projectDir}/build/generated/querydsl"
//
//
//sourceSets {
//    main {
//        java {
//            srcDirs("$projectDir/build/generated", "$projectDir/src/main/java")
//
//        }
//    }
//}
//
//configurations {
//    compileOnly {
//        extendsFrom(configurations.annotationProcessor.get())
//    }
//    querydsl.extendsFrom(compileClasspath)
//}
//
//
//querydsl {
//    library = "com.querydsl:querydsl-apt:5.0.0:jakarta"
//    jpa = true
//    querydslSourcesDir = "$projectDir/build/generated"
//}
//
//
//tasks.withType<QuerydslCompile> {
//    options.annotationProcessorPath = configurations.querydsl.get()
//}
