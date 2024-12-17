package `2024`.day16

import utilities.readInput
import java.util.*

fun main() {
    val input = readInput("2024/day16/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

enum class Direction(val dx: Int, val dy: Int) {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    ;

    fun turnClockwise(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    fun turnCounterClockwise(): Direction = when (this) {
        NORTH -> WEST
        EAST -> NORTH
        SOUTH -> EAST
        WEST -> SOUTH
    }
}

data class Position(val x: Int, val y: Int, val direction: Direction)
data class Point(val x: Int, val y: Int)

class Maze(private val grid: List<String>) {
    val height: Int = grid.size
    val width: Int = grid[0].length
    private lateinit var start: Pair<Int, Int>
    private lateinit var end: Pair<Int, Int>

    init {
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                when (grid[y][x]) {
                    'S' -> start = x to y
                    'E' -> end = x to y
                }
            }
        }
        requireNotNull(start) { "Start position not found" }
        requireNotNull(end) { "End position not found" }
    }

    fun isWall(x: Int, y: Int): Boolean =
        x !in 0 until width || y !in 0 until height || grid[y][x] == '#'

    fun getStart(): Pair<Int, Int> = start
    fun getEnd(): Pair<Int, Int> = end

    fun visualizeOptimalPaths(optimalTiles: Set<Point>): List<String> {
        return grid.mapIndexed { y, row ->
            row.mapIndexed { x, char ->
                when {
                    Point(x, y) in optimalTiles -> 'O'
                    else -> char
                }
            }.joinToString("")
        }
    }
}

fun part1(input: List<String>): Int {
    val maze = Maze(input)
    return findBestPathScore(maze).first
}

fun part2(input: List<String>): Int {
    val maze = Maze(input)
    val (_, optimalTiles) = findBestPathScore(maze)
    return optimalTiles.size
}

fun findBestPath(maze: Maze): Int {
    val start = maze.getStart()
    val end = maze.getEnd()

    val queue = PriorityQueue<Pair<Position, Int>>(compareBy { it.second })
    val visited = mutableMapOf<Position, Int>()

    val initialPosition = Position(start.first, start.second, Direction.EAST)
    queue.offer(initialPosition to 0)
    visited[initialPosition] = 0

    while (queue.isNotEmpty()) {
        val (currentState, currentScore) = queue.poll()

        // reached the end, return the score
        if (currentState.x == end.first && currentState.y == end.second) {
            return currentScore
        }
        if (visited[currentState]!! < currentScore) continue

        val newX = currentState.x + currentState.direction.dx
        val newY = currentState.y + currentState.direction.dy
        if (!maze.isWall(newX, newY)) {
            val newPosition = Position(newX, newY, currentState.direction)
            val newScore = currentScore + 1
            if (newScore < visited.getOrDefault(newPosition, Int.MAX_VALUE)) {
                queue.offer(newPosition to newScore)
                visited[newPosition] = newScore
            }
        }

        // Try turning clockwise
        val clockwisePosition = Position(currentState.x, currentState.y, currentState.direction.turnClockwise())
        val clockwiseScore = currentScore + 1000
        if (clockwiseScore < visited.getOrDefault(clockwisePosition, Int.MAX_VALUE)) {
            queue.offer(clockwisePosition to clockwiseScore)
            visited[clockwisePosition] = clockwiseScore
        }

        // Try turning counter-clockwise
        val counterClockwisePosition =
            Position(currentState.x, currentState.y, currentState.direction.turnCounterClockwise())
        val counterClockwiseScore = currentScore + 1000
        if (counterClockwiseScore < visited.getOrDefault(counterClockwisePosition, Int.MAX_VALUE)) {
            queue.offer(counterClockwisePosition to counterClockwiseScore)
            visited[counterClockwisePosition] = counterClockwiseScore
        }
    }

    return Int.MAX_VALUE // No path found
}

fun findBestPathScore(maze: Maze): Pair<Int, Set<Point>> {
    val start = maze.getStart()
    val end = maze.getEnd()

    val queue = PriorityQueue<Pair<Position, Int>>(compareBy { it.second })
    val visited = mutableMapOf<Position, Int>()

    val optimalPaths = mutableMapOf<Position, MutableSet<Position>>()

    val initialPosition = Position(start.first, start.second, Direction.EAST)
    queue.offer(initialPosition to 0)
    visited[initialPosition] = 0
    optimalPaths[initialPosition] = mutableSetOf()

    var bestScore = Int.MAX_VALUE
    val endPositions = mutableSetOf<Position>()

    while (queue.isNotEmpty()) {
        val (currentPos, currentScore) = queue.poll()

        // found a path to the end
        if (currentPos.x == end.first && currentPos.y == end.second) {
            if (currentScore <= bestScore) {
                bestScore = currentScore
                endPositions.add(currentPos)
            }
            continue
        }

        // already worse than our best path to the end, skip it
        if (currentScore > bestScore) continue

        // found a better path to this state, skip
        if (visited[currentPos]!! < currentScore) continue

        // process each possible move (forward, clockwise, counter-clockwise)
        val moves = listOf(
            Triple(
                Position(
                    currentPos.x + currentPos.direction.dx,
                    currentPos.y + currentPos.direction.dy,
                    currentPos.direction,
                ),
                currentScore + 1,
                currentPos,
            ),
            Triple(
                Position(currentPos.x, currentPos.y, currentPos.direction.turnClockwise()),
                currentScore + 1000,
                currentPos,
            ),
            Triple(
                Position(currentPos.x, currentPos.y, currentPos.direction.turnCounterClockwise()),
                currentScore + 1000,
                currentPos,
            ),
        )

        for ((newPos, newScore, prevPos) in moves) {
            // skip if moving into a wall
            if (newPos.direction == currentPos.direction &&
                maze.isWall(newPos.x, newPos.y)
            ) {
                continue
            }

            if (newScore <= visited.getOrDefault(newPos, Int.MAX_VALUE)) {
                if (newScore < visited.getOrDefault(newPos, Int.MAX_VALUE)) {
                    optimalPaths[newPos] = mutableSetOf()
                }
                queue.offer(newPos to newScore)
                visited[newPos] = newScore
                optimalPaths.getOrPut(newPos) { mutableSetOf() }.add(prevPos)
            }
        }
    }

    // reconstruct all optimal paths and collect unique positions
    val optimalTiles = mutableSetOf<Point>()
    fun reconstructPaths(pos: Position, visited: MutableSet<Position>) {
        if (pos in visited) return
        visited.add(pos)
        optimalTiles.add(Point(pos.x, pos.y))
        optimalPaths[pos]?.forEach { prevPos ->
            reconstructPaths(prevPos, visited)
        }
    }

    // start reconstruction from all end states that achieved the best score
    endPositions.forEach { endPos ->
        reconstructPaths(endPos, mutableSetOf())
    }

    return bestScore to optimalTiles
}
