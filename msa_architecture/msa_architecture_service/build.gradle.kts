import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    val kotlinVersion = "1.9.21"

    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    // gradle degraph 플러그인 추가
    id("com.vanniktech.dependency.graph.generator") version "0.7.0"

}

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

allprojects {
    group = "com.yunhalee.msa.architecture.service"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}


subprojects {

    apply {
        plugin("java")
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("io.spring.dependency-management")
        plugin("kotlin-spring")
        plugin("org.springframework.boot")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("com.vanniktech.dependency.graph.generator")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")


        // mockk
        testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.2")
        testImplementation("org.mockito:mockito-core:5.15.2")

    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    configurations.forEach {
        it.exclude(module = "slf4j-log4j12")
        it.exclude(module = "log4j-slf4j-impl")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }


}
