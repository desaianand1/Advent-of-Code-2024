package `2024`.day14

import utilities.readInput
import kotlin.math.abs

data class Position(val x: Int, val y: Int)
data class Velocity(val x: Int, val y: Int)
data class Robot(val position: Position, val velocity: Velocity)

fun main() {
    val input = readInput("2024/day14/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun parseInput(input: List<String>): List<Robot> {
    return input.map { line ->
        val (pos, vel) = line.split(" ")
        val (px, py) = pos.removePrefix("p=").split(",").map { it.toInt() }
        val (vx, vy) = vel.removePrefix("v=").split(",").map { it.toInt() }
        Robot(Position(px, py), Velocity(vx, vy))
    }
}

fun simulateRobots(
    robots: List<Robot>,
    width: Int = 101,
    height: Int = 103,
    seconds: Int = 100,
): List<Position> {
    return robots.map { robot ->
        var finalX = (robot.position.x + robot.velocity.x * seconds) % width
        var finalY = (robot.position.y + robot.velocity.y * seconds) % height

        if (finalX < 0) finalX += width
        if (finalY < 0) finalY += height

        Position(finalX, finalY)
    }
}

fun calculateSafetyFactor(positions: List<Position>, width: Int = 101, height: Int = 103): Int {
    val midX = width / 2
    val midY = height / 2

    val quadrants = Array(4) { 0 }

    positions.forEach { pos ->
        if (pos.x != midX && pos.y != midY) {
            val quadrant = when {
                pos.x < midX && pos.y < midY -> 0
                pos.x > midX && pos.y < midY -> 1
                pos.x < midX && pos.y > midY -> 2
                else -> 3
            }
            quadrants[quadrant]++
        }
    }
    // reduce, starting at 1 (since we're multiplying)
    return quadrants.fold(1) { acc, count -> acc * count }
}

fun part1(input: List<String>): Int {
    val robots = parseInput(input)
    return calculateSafetyFactor(simulateRobots(robots))
}

fun part2(input: List<String>): Int {
    val robots = parseInput(input)
    return findChristmasTreeCandidate(robots)
}

fun findChristmasTreeCandidate(
    robots: List<Robot>,
    width: Int = 101,
    height: Int = 103,
    simulationLength: Int = 15000,
): Int {
    val candidates = mutableListOf<Int>()
    for (second in 0..simulationLength) {
        val positions = simulateRobots(robots, width, height, second)
        // check if positions are non-overlapping
        if (positions.size == positions.toSet().size) {
            // find some clusters
            val clusters = findNonOverlappingClusters(positions)
            if (clusters.isNotEmpty()) {
                println("\nFound non-overlapping cluster at second $second:")
                candidates.add(second)
                printPositions(positions)
            }
        }
    }
    // return the first non-overlapping cluster found.
    // This was added later on after I had found the Christmas tree by just looking through printed clusters.
    // It just so happened to be the first cluster lol
    return candidates.min()
}

fun printPositions(positions: List<Position>) {
    if (positions.isEmpty()) {
        println("No robots to display")
        return
    }

    // find the bounds of occupied positions
    val minX = positions.minOf { it.x }
    val maxX = positions.maxOf { it.x }
    val minY = positions.minOf { it.y }
    val maxY = positions.maxOf { it.y }

    println("Occupied area: ($minX,$minY) to ($maxX,$maxY)")

    val positionSet = positions.toSet()

    // print only the lines that contain robots
    for (y in minY..maxY) {
        var hasRobotsInLine = false
        val line = StringBuilder()

        for (x in minX..maxX) {
            if (Position(x, y) in positionSet) {
                line.append('*')
                hasRobotsInLine = true
            } else {
                line.append(' ')
            }
        }

        if (hasRobotsInLine) {
            println(line)
        }
    }
}

fun findNonOverlappingClusters(positions: List<Position>): List<List<Position>> {
    val visited = mutableSetOf<Position>()
    val clusters = mutableListOf<List<Position>>()

    fun isClose(pos1: Position, pos2: Position): Boolean {
        val dx = abs(pos1.x - pos2.x)
        val dy = abs(pos1.y - pos2.y)
        return dx <= 2 && dy <= 2
    }

    fun findCluster(start: Position): List<Position> {
        val cluster = mutableListOf<Position>()
        val queue = ArrayDeque<Position>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current in visited) continue
            visited.add(current)
            cluster.add(current)

            positions.filter { pos ->
                pos !in visited && isClose(current, pos)
            }.forEach { queue.add(it) }
        }

        return cluster
    }

    positions.forEach { pos ->
        if (pos !in visited) {
            val cluster = findCluster(pos)
            if (cluster.size >= 5) {
                clusters.add(cluster)
            }
        }
    }

    return clusters
}
