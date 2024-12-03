package `2024`.day03

import utilities.readInput

fun main() {
    val input = readInput("2024/day03/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()
    var sum = 0
    input.forEach { line ->
        val multiplicationPairs = mulRegex.findAll(line).map { it.groupValues[1] to it.groupValues[2] }.toList()
        sum += multiplicationPairs.sumOf { it.first.toInt() * it.second.toInt() }
    }
    return sum
}

fun part2(input: List<String>): Int {
    val multipleTermRegex = """(do\(\)|don't\(\)|mul\((\d+),(\d+)\))""".toRegex()
    var isMulEnabled = true
    var sum = 0
    input.forEach { line ->
        val instructions = multipleTermRegex.findAll(line)
        instructions.forEach { match ->
            val instruction = match.value
            when {
                instruction == "do()" -> isMulEnabled = true
                instruction == "don't()" -> isMulEnabled = false
                instruction.startsWith("mul(") && isMulEnabled -> {
                    val (_, num1, num2) = match.destructured
                    sum += num1.toInt() * num2.toInt()
                }
            }
        }
    }
    return sum
}
