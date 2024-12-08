package `2024`.day08

import utilities.readInput
import kotlin.math.abs

fun main() {
    val input = readInput("2024/day08/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Point(val x: Int, val y: Int)
data class Antenna(val position: Point, val frequency: Char)

fun part1(input: List<String>): Int {
    val antennas = parseInput(input)
    return findAntinodes(antennas, input[0].length, input.size).size
}

fun part2(input: List<String>): Int {
    val antennas = parseInput(input)
    return findResonantAntinodes(antennas, input[0].length, input.size).size
}

fun parseInput(input: List<String>): List<Antenna> {
    val antennas = mutableListOf<Antenna>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (char != '.') {
                antennas.add(Antenna(Point(x, y), char))
            }
        }
    }
    return antennas
}

private fun reflectPoint(pointToReflect: Point, centerPoint: Point): Point = Point(
    x = 2 * centerPoint.x - pointToReflect.x,
    y = 2 * centerPoint.y - pointToReflect.y,
)

private fun Point.isInBounds(width: Int, height: Int): Boolean =
    x in 0 until width && y in 0 until height

private fun findAntinodes(antennas: List<Antenna>, width: Int, height: Int): Set<Point> {
    val antinodes = mutableSetOf<Point>()

    // Group antennas by frequency and process each group
    antennas.groupBy { it.frequency }.values.forEach { sameFreqAntennas ->
        sameFreqAntennas.forEach { ant1 ->
            sameFreqAntennas.forEach { ant2 ->
                if (ant1 != ant2) {
                    val antinode = reflectPoint(ant2.position, ant1.position)
                    if (antinode.isInBounds(width, height)) {
                        antinodes.add(antinode)
                    }
                }
            }
        }
    }

    return antinodes
}

private fun findResonantAntinodes(antennas: List<Antenna>, width: Int, height: Int): Set<Point> {
    val antinodes = mutableSetOf<Point>()

    antennas.groupBy { it.frequency }.values.forEach { sameFreqAntennas ->
        // Skip frequencies with only one antenna
        if (sameFreqAntennas.size > 1) {
            // Add antenna positions as antinodes
            antinodes.addAll(sameFreqAntennas.map { it.position })

            for (i in sameFreqAntennas.indices) {
                for (j in i + 1 until sameFreqAntennas.size) {
                    val ant1 = sameFreqAntennas[i]
                    val ant2 = sameFreqAntennas[j]

                    findPointsInLine(ant1.position, ant2.position, width, height)
                        .forEach { antinodes.add(it) }
                }
            }
        }
    }

    return antinodes
}

private fun findPointsInLine(p1: Point, p2: Point, width: Int, height: Int): List<Point> {
    val points = mutableListOf<Point>()
    val dx = p2.x - p1.x
    val dy = p2.y - p1.y

    // If points are the same, return empty list
    if (dx == 0 && dy == 0) return points

    // Calculate the greatest common divisor to find the smallest step
    val gcd = gcd(abs(dx), abs(dy))
    val stepX = dx / gcd
    val stepY = dy / gcd

    // Extend the line in both directions
    var x = p1.x
    var y = p1.y

    // Go backwards from p1
    while (x in 0 until width && y in 0 until height) {
        points.add(Point(x, y))
        x -= stepX
        y -= stepY
    }

    // Go forwards from p1 (excluding p1 itself as it's already added)
    x = p1.x + stepX
    y = p1.y + stepY
    while (x in 0 until width && y in 0 until height) {
        points.add(Point(x, y))
        x += stepX
        y += stepY
    }

    return points
}

private fun gcd(a: Int, b: Int): Int {
    if (b == 0) return a
    return gcd(b, a % b)
}
