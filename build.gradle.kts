import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "aoc24"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.google.code.gson:gson:2.11.0")
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
            // Include all year directories dynamically
            fileTree(".")
                .filter { it.isDirectory && it.name.matches(Regex("\\d{4}")) }
                .map { it.name }
        )
        resources.srcDirs(
            // Same for resources
            fileTree(".")
                .filter { it.isDirectory && it.name.matches(Regex("\\d{4}")) }
                .map { it.name }
        )
    }
    test {
        kotlin.srcDirs(
            "utilities",
            // And for test sources
            fileTree(".")
                .filter { it.isDirectory && it.name.matches(Regex("\\d{4}")) }
                .map { it.name }
        )
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