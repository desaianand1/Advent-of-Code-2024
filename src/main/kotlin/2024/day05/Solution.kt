package `2024`.day05

import utilities.readInput

fun main() {
    val input = readInput("2024/day05/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

// Represents a rule where page X must come before page Y
data class PageRule(val before: Int, val after: Int)

// Represents a sequence of page numbers to be validated/sorted
typealias Update = List<Int>

fun parseInput(input: List<String>): Pair<List<PageRule>, List<Update>> {
    val (ruleLines, updateLines) = input.splitAt { it.isEmpty() }

    val rules = ruleLines.map { line ->
        val (before, after) = line.split("|").map { it.toInt() }
        PageRule(before, after)
    }

    val updates = updateLines.map { line ->
        line.split(",").map { it.toInt() }
    }

    return rules to updates
}

fun isValidOrder(update: Update, pageRules: List<PageRule>): Boolean {
    val pagePositions = update.withIndex().associate { it.value to it.index }

    return pageRules.all { (before, after) ->
        // Skip rule if either page isn't in this update
        if (before !in pagePositions || after !in pagePositions) return@all true

        pagePositions.getValue(before) < pagePositions.getValue(after)
    }
}

fun sortUpdate(update: Update, pageRules: List<PageRule>): Update {
    val pages = update.toSet()
    val dependencies = pageRules.filter { it.before in pages && it.after in pages }

    val graph = dependencies.groupBy({ it.before }, { it.after })
    val inDegrees = dependencies.fold(mutableMapOf<Int, Int>()) { acc, rule ->
        acc.merge(rule.after, 1, Int::plus)
        acc
    }

    // Topological sort using Kahn's algorithm
    val sorted = mutableListOf<Int>()
    val noIncoming = pages.filter { it !in inDegrees }.toMutableList()

    while (noIncoming.isNotEmpty()) {
        val page = noIncoming.removeLast()
        sorted.add(page)

        graph[page]?.forEach { next ->
            inDegrees.merge(next, -1, Int::plus)
            if (inDegrees[next] == 0) {
                noIncoming.add(next)
                inDegrees.remove(next)
            }
        }
    }

    return sorted
}

fun List<String>.splitAt(predicate: (String) -> Boolean): Pair<List<String>, List<String>> {
    val index = indexOfFirst(predicate)
    return take(index) to drop(index + 1)
}

fun part1(input: List<String>): Int {
    val (rules, updates) = parseInput(input)
    return updates
        .filter { update -> isValidOrder(update, rules) }
        .sumOf { it[it.size / 2] }
}

fun part2(input: List<String>): Int {
    val (rules, updates) = parseInput(input)
    return updates
        .filterNot { update -> isValidOrder(update, rules) }
        .map { update -> sortUpdate(update, rules) }
        .sumOf { it[it.size / 2] }
}
