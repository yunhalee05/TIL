dependencies {
    implementation(project(":order_service:order-domain:order-domain-core"))
    implementation(project(":common:common-domain"))
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.1")
    implementation("org.springframework:spring-tx:6.2.2")
}
