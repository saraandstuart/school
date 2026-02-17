plugins {
    java
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
}

group = "com.shannoncode"
version = "0.0.1-SNAPSHOT"
description = "Online school"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

openApi {
    customBootRun {
        workingDir.set(file(projectDir))
        args.set(
            listOf(
                "--spring.datasource.url=jdbc:h2:mem:testdb",
                "--spring.datasource.driver-class-name=org.h2.Driver"
            )
        )
    }
    apiDocsUrl.set("http://localhost:8080/api-docs.yaml")
    outputDir.set(layout.buildDirectory.dir("openapi"))
    outputFileName.set("openapi.yaml")
    waitTimeInSeconds.set(60)
}

extra["springCloudVersion"] = "2025.1.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-h2console")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-restclient-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    // Explicitly enable layering for Docker optimization
    layered {
        enabled.set(true)
    }
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
    // Use the multi-arch tiny builder for ARM64/Mac compatibility
    builder.set("paketobuildpacks/builder-jammy-java-tiny:latest")

    // Add the specialized health-checker buildpack
    buildpacks.set(listOf("paketobuildpacks/java", "paketobuildpacks/health-checker"))

    environment.set(
        mapOf(
            "BP_HEALTH_CHECKER_ENABLED" to "true", // Contributes the native binary
            "BPL_JVM_CDS_ENABLED" to "true"        // Performance optimization for Java 25
        )
    )
}

tasks.named("generateOpenApiDocs") {
    notCompatibleWithConfigurationCache("springdoc-openapi-gradle-plugin is not yet compatible")
}

tasks.named("forkedSpringBootRun") {
    notCompatibleWithConfigurationCache("springdoc-openapi-gradle-plugin is not yet compatible")
}
