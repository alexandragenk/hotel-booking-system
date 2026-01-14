import java.util.concurrent.Executors

plugins {
    java
    id("org.springframework.boot") version "4.0.1" apply false
}

group = "com.example"
version = "1.0.0"

val springBootVersion = "4.0.1"
val springCloudVersion = "2025.1.0"

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:$springBootVersion"))
        implementation(platform("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion"))
        testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

tasks.register("runAll") {
    dependsOn("build")
    doLast {
        val jars = listOf(
            "eureka-server",
            "api-gateway",
            "hotel-service",
            "booking-service"
        )

        val pool = Executors.newFixedThreadPool(jars.size)

        jars.forEach { jarFile ->
            pool.submit {
                val process = ProcessBuilder("java", "-jar", "$jarFile/build/libs/$jarFile.jar")
                    .redirectErrorStream(true)
                    .start()

                process.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { println("[$jarFile] $it") }
                }

                process.waitFor()
            }
        }

        pool.shutdown()
        pool.awaitTermination(1, TimeUnit.HOURS)
    }
}
