package aoc2024.day02

import utilities.readInput
import kotlin.math.abs

fun main() {
    val input = readInput("2024/day02/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun createReports(input: List<String>): List<List<Int>> = input.map { it.trim().split(' ').map(String::toInt) }

fun isIncreasingOrDecreasing(difference: Int, isAscending: Boolean): Boolean =
    difference != 0 && (difference > 0 == isAscending)

fun isWithinTolerance(difference: Int): Boolean = abs(difference) in 1..3

private fun isReportSafe(
    report: List<Int>,
    canDiscardBadLevel: Boolean,
): Boolean {
    var prevLevel = report.first()
    var isAscending = true
    for ((index, level) in report.withIndex()) {
        if (index == 0) continue
        val difference = level - prevLevel
        if (index == 1) {
            isAscending = difference > 0
        }
        if (!isIncreasingOrDecreasing(difference, isAscending) || !isWithinTolerance(difference)) {
            return if (canDiscardBadLevel) {
                // Try removing each level once and see if any result is valid
                report.indices.any { i -> isReportSafe(report.filterIndexed { idx, _ -> idx != i }, false) }
            } else {
                false
            }
        }
        prevLevel = level
    }
    return true
}

fun part1(input: List<String>): Int =
    createReports(input).count { report -> isReportSafe(report, false) }

fun part2(input: List<String>): Int =
    createReports(input).count { report -> isReportSafe(report, true) }
