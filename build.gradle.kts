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
            srcDirs("utilities", ".")
            exclude(
                "**/test/**",
                "template/**",
                ".gradle/**",
                "build/**"
            )
            include(
                "*/day*/solution.kt",
                "utilities/**"
            )
        }
        resources {
            srcDir(".")
            include("*/day*/input.txt")
            exclude("**/test/**")
        }
    }
    test {
        kotlin {
            srcDir(".")
            include("*/day*/test/test.kt")
            exclude("template/**")
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