import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    
    // Useful utilities
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.google.code.gson:gson:2.10.1")  // For JSON parsing if needed
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

sourceSets {
    main {
        kotlin.srcDirs(
            "utilities",
            "2024"
        )
    }
    test {
        kotlin.srcDirs(
            "utilities",
            "2024"
        )
    }
}

// Custom task to run a specific day's solution
tasks.register<JavaExec>("day") {
    description = "Run a specific day's solution. Usage: ./gradlew day -Pyear=2024 -Pday=1"
    
    mainClass.set("aoc${project.findProperty("year") ?: "2024"}.day${String.format("%02d", (project.findProperty("day") as String? ?: "1").toInt())}.SolutionKt")
    
    classpath = sourceSets["main"].runtimeClasspath
    
    // Enable standard input in case it's needed for any solutions
    standardInput = System.`in`
}

// Default run task configuration
application {
    mainClass.set("aoc2024.day01.SolutionKt") // Default to day 1 if no specific day is provided
}