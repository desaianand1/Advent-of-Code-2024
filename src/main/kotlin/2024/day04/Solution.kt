package `2024`.day04

import utilities.readInput

fun main() {
    val input = readInput("2024/day04/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

private fun parseGrid(input: List<String>): Array<CharArray> =
    input
        .map { it.toCharArray() }
        .toTypedArray()

private fun areCoordsWithinBounds(x: Int, y: Int, grid: Array<CharArray>) = x in grid.indices && y in grid[x].indices

private fun countWordOccurrences(grid: Array<CharArray>, word: String): Int {
    val directions = listOf(
        // Right
        Pair(0, 1),
        // Down
        Pair(1, 0),
        // Diagonal Down-Right
        Pair(1, 1),
        // Diagonal Down-Left
        Pair(1, -1),
        // Left
        Pair(0, -1),
        // Up
        Pair(-1, 0),
        // Diagonal Up-Left
        Pair(-1, -1),
        // Diagonal Up-Right
        Pair(-1, 1),
    )
    return grid.indices.sumOf { x ->
        grid[x].indices.sumOf { y ->
            directions.count { direction -> wasWordFound(word, grid, x, y, direction) }
        }
    }
}

private fun wasWordFound(
    word: String,
    grid: Array<CharArray>,
    startX: Int,
    startY: Int,
    direction: Pair<Int, Int>,
): Boolean {
    var x = startX
    var y = startY

    for (char in word) {
        if (!areCoordsWithinBounds(x, y, grid) || grid[x][y] != char) return false
        x += direction.first
        y += direction.second
    }
    return true
}

private fun isXShapedMAS(grid: Array<CharArray>, centerX: Int, centerY: Int): Boolean {
    if (grid[centerX][centerY] != 'A') return false

    val patterns = listOf(
        /**
         * Standard:
         * M . S
         * . A .
         * M . S
         */
        listOf(
            Pair(centerX - 1, centerY - 1) to 'M',
            Pair(centerX - 1, centerY + 1) to 'S',
            Pair(centerX + 1, centerY - 1) to 'M',
            Pair(centerX + 1, centerY + 1) to 'S',
        ),
        /**
         * S . M
         * . A .
         * S . M
         */
        listOf(
            Pair(centerX - 1, centerY - 1) to 'S',
            Pair(centerX - 1, centerY + 1) to 'M',
            Pair(centerX + 1, centerY - 1) to 'S',
            Pair(centerX + 1, centerY + 1) to 'M',
        ),
        /**
         * S . S
         * . A .
         * M . M
         */
        listOf(
            Pair(centerX - 1, centerY - 1) to 'S',
            Pair(centerX - 1, centerY + 1) to 'S',
            Pair(centerX + 1, centerY - 1) to 'M',
            Pair(centerX + 1, centerY + 1) to 'M',
        ),
        /**
         * M . M
         * . A .
         * S . S
         */
        listOf(
            Pair(centerX - 1, centerY - 1) to 'M',
            Pair(centerX - 1, centerY + 1) to 'M',
            Pair(centerX + 1, centerY - 1) to 'S',
            Pair(centerX + 1, centerY + 1) to 'S',
        ),
    )

    return patterns.any { pattern ->
        pattern.all { (coord, expectedChar) ->
            areCoordsWithinBounds(coord.first, coord.second, grid) &&
                grid[coord.first][coord.second] == expectedChar
        }
    }
}

fun part1(input: List<String>): Int {
    val grid = parseGrid(input)
    val word = "XMAS"
    return countWordOccurrences(grid, word)
}

fun part2(input: List<String>): Int {
    val grid = parseGrid(input)
    // Iterate through the entire grid and check for X-MAS patterns centered at each 'A'
    return grid.indices.sumOf { x ->
        grid[x].indices.count { y ->
            isXShapedMAS(grid, x, y)
        }
    }
}
