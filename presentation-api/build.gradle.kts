dependencies {
    implementation(project(":common"))
//    implementation(project(":account-api"))
    implementation(project(":crypto"))
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}