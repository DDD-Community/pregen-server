dependencies {
    implementation(project(":common"))
    implementation(project(":account-api"))
    implementation(project(":crypto"))

    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework:spring-messaging:6.1.3")
    implementation("org.springframework.security:spring-security-messaging")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
