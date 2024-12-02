package aoc2024.day01

import utilities.readInput

fun main() {
    val input = readInput("2024/day01/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun createLists(input: List<String>): Pair<List<Int>, List<Int>> {
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()

    input.forEach { line ->
        val (left, right) = line.trim().split(Regex("\\s+"))
        leftList.add(left.toInt())
        rightList.add(right.toInt())
    }

    return Pair(leftList, rightList)
}

fun part1(input: List<String>): Int {
    val (leftList, rightList) = createLists(input)
    val sortedLeft = leftList.sorted()
    val sortedRight = rightList.sorted()

    var totalDistance = 0
    for (i in sortedLeft.indices) {
        val distance = Math.abs(sortedLeft[i] - sortedRight[i])
        totalDistance += distance
    }

    return totalDistance
}

fun part2(input: List<String>): Int {
    val (leftList, rightList) = createLists(input)
    val rightFrequency = rightList.groupingBy { it }.eachCount()

    val similarityScore = leftList.sumOf { num ->
        num * (rightFrequency[num] ?: 0)
    }
    return similarityScore
}
