package `2024`.day10

import utilities.readInput

fun main() {
    val input = readInput("2024/day10/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Position(val x: Int, val y: Int)

private fun findTrailheads(trailMap: List<List<Int>>): List<Position> {
    val trailheads = mutableListOf<Position>()
    trailMap.forEachIndexed { i, row ->
        row.forEachIndexed { j, _ ->
            if (trailMap[i][j] == 0) {
                trailheads.add(Position(i, j))
            }
        }
    }
    return trailheads
}

private fun countTrailScore(trailMap: List<List<Int>>, trailHead: Position): Int {
    val reachablePeaks = mutableSetOf<Position>()
    val visited = mutableSetOf<Position>()

    fun dfs(current: Position, height: Int) {
        if (height == 9) {
            reachablePeaks.add(current)
            return
        }

        for (neighbor in getValidNeighbors(trailMap, current)) {
            if (neighbor !in visited) {
                visited.add(neighbor)
                dfs(neighbor, height + 1)
            }
        }
    }
    visited.add(trailHead)
    dfs(trailHead, 0)
    return reachablePeaks.size
}

private fun countUniquePaths(trailMap: List<List<Int>>, trailHead: Position): Int {
    val paths = mutableSetOf<List<Position>>()
    val currentPath = mutableListOf<Position>()

    fun dfs(current: Position, height: Int) {
        currentPath.add(current)

        if (height == 9) {
            paths.add(currentPath.toList())
            currentPath.removeLast()
            return
        }

        for (neighbor in getValidNeighbors(trailMap, current)) {
            if (neighbor !in currentPath) {
                dfs(neighbor, height + 1)
            }
        }
        currentPath.removeLast()
    }

    dfs(trailHead, 0)
    return paths.size
}

private fun getValidNeighbors(trailMap: List<List<Int>>, pos: Position): List<Position> {
    val (row, col) = pos
    val currentHeight = trailMap[row][col]
    return listOf(
        Position(row - 1, col), // up
        Position(row + 1, col), // down
        Position(row, col - 1), // left
        Position(row, col + 1), // right
    ).filter { (r, c) ->
        r in 0 until trailMap.first().size &&
            c in 0 until trailMap.size &&
            trailMap[r][c] == currentHeight + 1
    }
}

fun part1(input: List<String>): Int {
    val trailMap = input.map { line -> line.map { it.digitToInt() } }
    return findTrailheads(trailMap).sumOf { head -> countTrailScore(trailMap, head) }
}

fun part2(input: List<String>): Int {
    val trailMap = input.map { line -> line.map { it.digitToInt() } }
    return findTrailheads(trailMap).sumOf { head -> countUniquePaths(trailMap, head) }
}
