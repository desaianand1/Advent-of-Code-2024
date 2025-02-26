package `2024`.day15

import `2024`.day15.Direction.values
import utilities.readInput

fun main() {
    val input = readInput("2024/day15/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

sealed class Obstacle(val char: Char) {
    object Wall : Obstacle('#')
    object Robot : Obstacle('@')
    object Box : Obstacle('O')
    object Empty : Obstacle('.')
    object BoxLeft : Obstacle('[')
    object BoxRight : Obstacle(']')

    companion object {
        fun fromChar(char: Char): Obstacle = when (char) {
            '#' -> Wall
            '@' -> Robot
            'O' -> Box
            '.' -> Empty
            '[' -> BoxLeft
            ']' -> BoxRight
            else -> throw IllegalArgumentException("Invalid character: $char")
        }
    }
}

data class Point(val row: Int, val col: Int) {
    /**
     * Returns a new point after moving in the given direction.
     */
    operator fun plus(direction: Direction): Point = Point(row + direction.dy, col + direction.dx)
}

enum class Direction(val char: Char, val dx: Int, val dy: Int) {
    UP('^', 0, -1),
    DOWN('v', 0, 1),
    LEFT('<', -1, 0),
    RIGHT('>', 1, 0),
    ;

    companion object {
        fun fromChar(c: Char): Direction = values().first { it.char == c }
    }
}

fun part1(input: List<String>): Int {
    val (warehouse, moves) = parseWarehouse(input)

    moves.forEach { warehouse.move(it) }

    return warehouse.calculateGPSSum()
}

fun part2(input: List<String>): Int {
    val (warehouseMap, movesText) = extractMapAndMoves(input)
    val wideWarehouse = convertToWideFormat(warehouseMap)
    val moves = parseMoves(movesText)

    simulateRobotMovement(wideWarehouse, moves)

    return calculateGPSSum(wideWarehouse)
}

private fun extractMapAndMoves(input: List<String>): Pair<List<String>, String> {
    val warehouseMap = input.takeWhile { it.isNotEmpty() }
    val moves = input.dropWhile { it.isNotEmpty() }.dropWhile { it.isEmpty() }.joinToString("").filter { it in "^v<>" }

    return warehouseMap to moves
}

private fun parseMoves(movesText: String): List<Direction> = movesText.map { Direction.fromChar(it) }

private fun convertToWideFormat(warehouseMap: List<String>): List<CharArray> = warehouseMap.map { line ->
    line.map { char ->
        when (char) {
            '#' -> "##"
            'O' -> "[]"
            '.' -> ".."
            '@' -> "@."
            else -> throw IllegalArgumentException("Invalid character: $char")
        }
    }.joinToString("")
}.map { it.toCharArray() }

private fun simulateRobotMovement(warehouse: List<CharArray>, moves: List<Direction>) {
    var robotPosition = findRobotPosition(warehouse)

    moves.forEach { direction ->
        val nextPosition = robotPosition + direction

        when (warehouse[nextPosition]) {
            '[', 'O', ']' -> {
                // Try to push boxes
                val boxesToMove = findBoxesToMove(warehouse, nextPosition, direction)

                if (boxesToMove.isNotEmpty()) {
                    // Move all boxes in reverse order
                    boxesToMove.forEach { boxPosition ->
                        val targetPosition = boxPosition + direction
                        warehouse[targetPosition] = warehouse[boxPosition]
                        warehouse[boxPosition] = '.'
                    }

                    // Move robot
                    warehouse[robotPosition] = '.'
                    warehouse[nextPosition] = '@'
                    robotPosition = nextPosition
                }
            }

            !in "#" -> {
                // Simple move to empty space
                warehouse[robotPosition] = '.'
                warehouse[nextPosition] = '@'
                robotPosition = nextPosition
            }
        }
    }
}

private fun findRobotPosition(warehouse: List<CharArray>): Point {
    for (row in warehouse.indices) {
        for (col in warehouse[row].indices) {
            if (warehouse[row][col] == '@') {
                return Point(row, col)
            }
        }
    }
    throw IllegalStateException("Robot not found in warehouse")
}

private fun findBoxesToMove(warehouse: List<CharArray>, from: Point, direction: Direction): List<Point> {
    val queue = ArrayDeque(listOf(from))
    val visited = mutableSetOf<Point>()
    val boxesToMove = mutableListOf<Point>()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (current in visited) continue

        visited.add(current)
        boxesToMove.add(current)

        // For vertical movement, check connected parts of wide boxes
        if (direction == Direction.UP || direction == Direction.DOWN) {
            when (warehouse[current]) {
                '[' -> {
                    val rightPart = Point(current.row, current.col + 1)
                    if (rightPart.col < warehouse[0].size) queue.add(rightPart)
                }

                ']' -> {
                    val leftPart = Point(current.row, current.col - 1)
                    if (leftPart.col >= 0) queue.add(leftPart)
                }
            }
        }

        // Check what's in the next position
        val next = current + direction

        // If out of bounds or wall, can't push
        if (next.row !in warehouse.indices || next.col !in warehouse[0].indices || warehouse[next] == '#') {
            return emptyList()
        }

        // If next position has a box, it would also move
        if (warehouse[next] in "[O]") {
            queue.add(next)
        }
    }

    return boxesToMove.reversed()
}

private fun calculateGPSSum(warehouse: List<CharArray>): Int {
    var sum = 0
    for (row in warehouse.indices) {
        for (col in warehouse[row].indices) {
            if (warehouse[row][col] == '[') {
                sum += (row * 100) + col
            }
        }
    }
    return sum
}

private operator fun List<CharArray>.get(point: Point): Char =
    if (point.row in indices && point.col in this[point.row].indices) {
        this[point.row][point.col]
    } else {
        '#' // Treat out of bounds as walls
    }

private operator fun List<CharArray>.set(point: Point, value: Char) {
    if (point.row in indices && point.col in this[point.row].indices) {
        this[point.row][point.col] = value
    }
}

private fun parseWarehouse(input: List<String>): Pair<Warehouse, List<Direction>> {
    val warehousePart = input.takeWhile { it.isNotEmpty() }
    val movesPart =
        input.dropWhile { it.isNotEmpty() }.dropWhile { it.isEmpty() }.joinToString("").filter { it in "^v<>" }
            .map { Direction.fromChar(it) }

    var robotPos = Point(0, 0)
    val grid = warehousePart.mapIndexed { row, line ->
        line.mapIndexed { col, char ->
            val obstacle = Obstacle.fromChar(char)
            if (obstacle == Obstacle.Robot) {
                robotPos = Point(row, col)
            }
            obstacle
        }.toMutableList()
    }

    return Warehouse(grid, robotPos) to movesPart
}

class Warehouse(
    private val grid: List<MutableList<Obstacle>>,
    private var robotPosition: Point,
) {
    /**
     * Moves the robot in the given direction, potentially pushing boxes.
     */
    fun move(direction: Direction) {
        val nextPosition = robotPosition + direction

        when (getObstacle(nextPosition)) {
            Obstacle.Empty -> moveRobot(nextPosition)
            Obstacle.Box -> tryPushBoxes(nextPosition, direction)
            else -> {} // Wall or other obstacle - can't move
        }
    }

    /**
     * Calculates the GPS sum for all boxes.
     */
    fun calculateGPSSum(): Int {
        var sum = 0
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == Obstacle.Box) {
                    sum += (row * 100) + col
                }
            }
        }
        return sum
    }

    /**
     * Gets the obstacle at the given position.
     */
    private fun getObstacle(position: Point): Obstacle =
        if (position.row in grid.indices && position.col in grid[0].indices) {
            grid[position.row][position.col]
        } else {
            Obstacle.Wall
        }

    /**
     * Moves the robot to a new position.
     */
    private fun moveRobot(newPosition: Point) {
        grid[robotPosition.row][robotPosition.col] = Obstacle.Empty
        grid[newPosition.row][newPosition.col] = Obstacle.Robot
        robotPosition = newPosition
    }

    /**
     * Attempts to push boxes from a position in a direction.
     */
    private fun tryPushBoxes(boxPosition: Point, direction: Direction) {
        val boxesToPush = findPushableBoxes(boxPosition, direction)
        if (boxesToPush.isEmpty()) return

        // Move boxes in reverse order
        for (boxPos in boxesToPush.reversed()) {
            val newBoxPos = boxPos + direction
            grid[newBoxPos.row][newBoxPos.col] = Obstacle.Box
            grid[boxPos.row][boxPos.col] = Obstacle.Empty
        }

        moveRobot(boxPosition)
    }

    /**
     * Finds all boxes that can be pushed as a group.
     */
    private fun findPushableBoxes(start: Point, direction: Direction): List<Point> {
        val queue = ArrayDeque(listOf(start))
        val visited = mutableSetOf<Point>()
        val boxesToPush = mutableListOf<Point>()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current in visited) continue

            visited.add(current)

            if (getObstacle(current) == Obstacle.Box) {
                boxesToPush.add(current)

                // Check next box in chain
                val next = current + direction
                when (getObstacle(next)) {
                    Obstacle.Box -> queue.add(next)
                    Obstacle.Wall -> return emptyList()
                    Obstacle.Empty -> {} // End of box chain, can push
                    else -> return emptyList() // Can't push
                }
            }
        }

        return boxesToPush
    }
}
