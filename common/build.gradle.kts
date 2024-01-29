import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {	// (1)

    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}

// (2)
val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true
