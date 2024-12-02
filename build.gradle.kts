import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
    application
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

group = "aoc24"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Testing
    testImplementation(kotlin("test"))
    
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

sourceSets {
    main {
        kotlin {
            // Only utilities and solution files
            srcDir("utilities")
            srcDir(fileTree(".") {
                include("*/day*/solution.kt")
                exclude("utilities/**")  // Ensure we don't double-include utilities
                exclude("*/day*/test.kt")  // Explicitly exclude test files
                exclude("**/template*.kt")  // Exclude templates
            }.map { it.parent })
        }
        resources {
            srcDir(fileTree(".") {
                include("*/day*/input.txt")
            }.map { it.parent })
        }
    }
    test {
        kotlin {
            // Only test files
            srcDir(fileTree(".") {
                include("*/day*/test.kt")
                exclude("**/template*.kt")
            }.map { it.parent })
        }
    }
}

// Custom task to run a specific day's solution
tasks.register<JavaExec>("day") {
    description = "Run a specific day's solution. Usage: ./gradlew day -Pyear=2024 -Pday=1"
    
    dependsOn("classes")
    
    val year = project.findProperty("year")?.toString() ?: throw GradleException(
        "Year parameter is required. Use -Pyear=YYYY (e.g., -Pyear=2024)"
    )
    val day = (project.findProperty("day")?.toString() ?: throw GradleException(
        "Day parameter is required. Use -Pday=DD (e.g., -Pday=1)"
    )).padStart(2, '0')
    
    // Verify year format
    require(year.matches(Regex("\\d{4}"))) { "Year must be in YYYY format" }
    // Verify day is between 1 and 25
    require(day.toInt() in 1..25) { "Day must be between 1 and 25" }
    
    mainClass.set("aoc$year.day$day.SolutionKt")
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    
    doFirst {
        println("Running Year $year Day $day solution...")
    }
}

// Add a task to list all implemented solutions
tasks.register("listSolutions") {
    description = "List all implemented solutions"
    
    doLast {
        val solutions = fileTree(".")
            .filter { it.isDirectory && it.name.matches(Regex("\\d{4}")) }
            .flatMap { year ->
                fileTree(year.path)
                    .filter { it.isDirectory && it.name.matches(Regex("day\\d{2}")) }
                    .map { day -> "${year.name}/${day.name}" }
            }
            .sorted()
        
        if (solutions.isEmpty()) {
            println("No solutions implemented yet!")
        } else {
            println("Implemented solutions:")
            solutions.forEach { println("- $it") }
        }
    }
}