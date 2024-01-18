dependencies {
    implementation(project(":common"))
    implementation(project(":crypto"))

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
}
