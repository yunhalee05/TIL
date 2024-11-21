dependencies {
    implementation(project(":order_service:order-domain:order-domain-core"))
    implementation(project(":order_service:order-domain:order-application-service"))
    implementation(project(":order_service:order-application"))
    implementation(project(":order_service:order-data-access"))
    implementation(project(":order_service:order-messaging"))
}