plugins {
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.4")
	implementation("org.redisson:redisson-spring-boot-starter:3.27.2")
	implementation("org.springframework.kafka:spring-kafka:3.1.3")
	runtimeOnly("com.mysql:mysql-connector-j")
}
