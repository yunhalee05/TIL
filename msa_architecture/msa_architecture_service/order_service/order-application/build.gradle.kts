dependencies {
    implementation(project(":order_service:order-domain:order-application-service"))
    // order-domain:order-application-service 의 dependency 추가
    implementation(project(":order_service:order-domain:order-domain-core"))
    implementation(project(":common:common-domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(project(":common:common-application"))
}