package `2024`.day06

import utilities.readInput

fun main() {
    val input = readInput("2024/day06/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Position(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"
    fun add(direction: Direction): Position = Position(x + direction.dx, y + direction.dy)
}

data class Direction(val dx: Int, val dy: Int) {
    fun turnRight(): Direction = when {
        dx == 0 && dy == -1 -> Direction(1, 0) // Up to Right
        dx == 1 && dy == 0 -> Direction(0, 1) // Right to Down
        dx == 0 && dy == 1 -> Direction(-1, 0) // Down to Left
        dx == -1 && dy == 0 -> Direction(0, -1) // Left to Up
        else -> throw IllegalStateException("Invalid direction: dx=$dx, dy=$dy")
    }

    override fun toString(): String = when {
        dx == 0 && dy == -1 -> "^"
        dx == 1 && dy == 0 -> ">"
        dx == 0 && dy == 1 -> "v"
        dx == -1 && dy == 0 -> "<"
        else -> "?"
    }
}

data class GuardState(val pos: Position, val dir: Direction)

class GuardPatrol(input: List<String>) {
    private val grid: List<CharArray> = input.map { it.toCharArray() }
    private val startPos: Position
    private val startDir: Direction
    private val obstacles = mutableSetOf<Position>()

    init {
        var tmpPos: Position? = null
        var tmpDir: Direction? = null

        val width = grid[0].size
        val height = grid.size
        // find start position and collect obstacles
        for (x in 0 until width) {
            for (y in 0 until height) {
                when (grid[y][x]) {
                    '#' -> obstacles.add(Position(x, y))
                    '^' -> {
                        tmpPos = Position(x, y)
                        tmpDir = Direction(0, -1)
                    }

                    'v' -> {
                        tmpPos = Position(x, y)
                        tmpDir = Direction(0, 1)
                    }

                    '>' -> {
                        tmpPos = Position(x, y)
                        tmpDir = Direction(1, 0)
                    }

                    '<' -> {
                        tmpPos = Position(x, y)
                        tmpDir = Direction(-1, 0)
                    }
                }
            }
        }

        startPos = tmpPos ?: throw IllegalArgumentException("No guard found!")
        startDir = tmpDir ?: throw IllegalArgumentException("No guard direction found!")
//        println("Initial state:")
//        println("Grid dimensions: width=$width, height=$height")
//        println("Guard starting at: $startPos facing $startDir")
    }

    private fun isInBounds(pos: Position): Boolean =
        pos.x in grid[0].indices && pos.y in grid.indices

    private fun hasObstacle(pos: Position, extraObstacle: Position? = null): Boolean =
        !isInBounds(pos) || pos in obstacles || pos == extraObstacle

    // get initial path without any extra obstacles
    private fun getInitialPath(): Set<Position> {
        val visited = mutableSetOf<Position>()
        var currentPos = startPos
        var currentDir = startDir
        visited.add(currentPos)

        while (true) {
            val nextPos = currentPos.add(currentDir)
            if (!isInBounds(nextPos)) {
                break
            }

            if (hasObstacle(nextPos)) {
                currentDir = currentDir.turnRight()
            } else {
                currentPos = nextPos
                visited.add(currentPos)
            }
        }

        return visited
    }

    // Check if adding an obstacle at this position creates a loop
    private fun simulateWithObstacle(obstaclePos: Position): Boolean {
        val visited = mutableSetOf<GuardState>()
        var currentPos = startPos
        var currentDir = startDir

        while (true) {
            val currentState = GuardState(currentPos, currentDir)
            // found a loop
            if (currentState in visited) {
                return true
            }
            visited.add(currentState)
            val nextPos = currentPos.add(currentDir)
            // left the map
            if (!isInBounds(nextPos)) {
                return false
            }

            if (hasObstacle(nextPos, obstaclePos)) {
                currentDir = currentDir.turnRight()
            } else {
                currentPos = nextPos
            }
        }
    }

    fun findLoopPositions(): Set<Position> {
        val loopPositions = mutableSetOf<Position>()
        for (y in grid.indices) {
            for (x in grid[0].indices) {
                val pos = Position(x, y)
                if (!hasObstacle(pos) && pos != startPos) {
                    if (simulateWithObstacle(pos)) {
                        loopPositions.add(pos)
                    }
                }
            }
        }

        return loopPositions
    }

    fun countVisitedPositions(): Int = getInitialPath().size
}

fun part1(input: List<String>): Int {
    val patrol = GuardPatrol(input)
    return patrol.countVisitedPositions()
}

fun part2(input: List<String>): Int {
    val patrol = GuardPatrol(input)
    return patrol.findLoopPositions().size
}
