dependencies {
    implementation(project(":common"))
    implementation(project(":crypto"))

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}
