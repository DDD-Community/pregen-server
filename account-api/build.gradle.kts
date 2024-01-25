dependencies {
    implementation(project(":common"))
    implementation(project(":crypto"))

    // oauth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
}