plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("kapt") version "1.9.21"
}

group = "com.yunhalee.cache"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// redis cache
	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.4.1")
	// jackson
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")
	// actuator
	implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.1")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
