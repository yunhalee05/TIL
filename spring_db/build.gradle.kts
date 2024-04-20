import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc:3.2.5")
    // 엔진을 다운 받아서 보려면 spring이 알아서 버전을 맞춰 다운 받아준 버전을 external libraries 에서 h2database:h2:{2.2.224} 버전을 확인 해서 받아야 한다.
    runtimeOnly("com.h2database:h2")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")
}
