dependencies {
    implementation(project(":common"))
    implementation(project(":account-api"))
    implementation(project(":crypto"))

    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.3.1")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.3.1")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.3.1")
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
