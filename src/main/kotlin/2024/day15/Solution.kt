package `2024`.day15

fun main() {
    val sampleInput = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^>
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent().split("\n")

    println("Part 2 with debug:")
    part2(sampleInput, debug = true)
}

sealed interface Obstacle {
    val char: Char

    data object Wall : Obstacle {
        override val char = '#'
    }

    data object Robot : Obstacle {
        override val char = '@'
    }

    data object Box : Obstacle {
        override val char = 'O'
    }

    data object Empty : Obstacle {
        override val char = '.'
    }

    data object BoxLeft : Obstacle {
        override val char = '['
    }

    data object BoxRight : Obstacle {
        override val char = ']'
    }

    companion object {
        fun fromChar(char: Char) = when (char) {
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
    fun nextPosition(direction: Char) = when (direction) {
        '^' -> copy(row = row - 1)
        'v' -> copy(row = row + 1)
        '<' -> copy(col = col - 1)
        '>' -> copy(col = col + 1)
        else -> throw IllegalArgumentException("Invalid direction: $direction")
    }
}

class Warehouse(
    val grid: List<MutableList<Obstacle>>,
    var robotPosition: Point,
    val isWide: Boolean = false,
) {
    private fun findPushableBoxes(start: Point, direction: Char): List<Point> {
        if (!isWide) {
            // Original single box pushing logic
            return findSingleBoxStack(start, direction)
        }

        val boxPositions = mutableListOf<Point>()
        val seen = mutableSetOf<Point>()

        // For wide boxes, check boxes that could be affected by this push
        fun checkBox(pos: Point) {
            if (pos in seen) return
            if (pos.row !in grid.indices || pos.col !in grid[0].indices || pos.col + 1 !in grid[0].indices) return
            if (grid[pos.row][pos.col] != Obstacle.BoxLeft || grid[pos.row][pos.col + 1] != Obstacle.BoxRight) return

            seen.add(pos)
            boxPositions.add(pos)

            // Check for adjacent boxes that would also be pushed
            val nextPos = pos.nextPosition(direction)
            if (nextPos.row in grid.indices && nextPos.col in grid[0].indices) {
                checkBox(nextPos)

                // For horizontal movement, check boxes above and below
                if (direction in "<>") {
                    checkBox(Point(nextPos.row - 1, nextPos.col))
                    checkBox(Point(nextPos.row + 1, nextPos.col))
                }
            }
        }

        checkBox(start)

        // Verify all boxes can be moved
        val destinations = boxPositions.map { it.nextPosition(direction) }
        for (dest in destinations) {
            if (dest.row !in grid.indices ||
                dest.col !in grid[0].indices ||
                dest.col + 1 !in grid[0].indices
            ) {
                return emptyList()
            }

            // Destination must be empty or a box we're already moving
            val isDestEmpty = grid[dest.row][dest.col] == Obstacle.Empty &&
                grid[dest.row][dest.col + 1] == Obstacle.Empty
            val isDestMovingBox = dest in boxPositions

            if (!isDestEmpty && !isDestMovingBox) return emptyList()
        }

        return boxPositions
    }

    private fun findSingleBoxStack(start: Point, direction: Char): List<Point> {
        val boxPositions = mutableListOf<Point>()
        var currentPos = start

        while (currentPos.row in grid.indices && currentPos.col in grid[0].indices) {
            if (grid[currentPos.row][currentPos.col] != Obstacle.Box) break
            boxPositions.add(currentPos)
            currentPos = currentPos.nextPosition(direction)
        }

        return if (currentPos.row in grid.indices &&
            currentPos.col in grid[0].indices &&
            grid[currentPos.row][currentPos.col] == Obstacle.Empty
        ) {
            boxPositions
        } else {
            emptyList()
        }
    }

    fun move(direction: Char) {
        val nextRobotPos = robotPosition.nextPosition(direction)
        if (nextRobotPos.row !in grid.indices || nextRobotPos.col !in grid[0].indices) return

        when (grid[nextRobotPos.row][nextRobotPos.col]) {
            Obstacle.Wall -> return
            Obstacle.Empty -> moveRobot(nextRobotPos)
            Obstacle.BoxLeft, Obstacle.Box -> {
                val boxStack = findPushableBoxes(nextRobotPos, direction)
                if (boxStack.isEmpty()) return

                // Move all affected boxes in appropriate order
                val sortedBoxes = when (direction) {
                    '>' -> boxStack.sortedByDescending { it.col }
                    '<' -> boxStack.sortedBy { it.col }
                    'v' -> boxStack.sortedByDescending { it.row }
                    '^' -> boxStack.sortedBy { it.row }
                    else -> boxStack
                }

                sortedBoxes.forEach { boxPos ->
                    val nextPos = boxPos.nextPosition(direction)
                    if (isWide) {
                        grid[nextPos.row][nextPos.col] = Obstacle.BoxLeft
                        grid[nextPos.row][nextPos.col + 1] = Obstacle.BoxRight
                        grid[boxPos.row][boxPos.col] = Obstacle.Empty
                        grid[boxPos.row][boxPos.col + 1] = Obstacle.Empty
                    } else {
                        grid[nextPos.row][nextPos.col] = Obstacle.Box
                        grid[boxPos.row][boxPos.col] = Obstacle.Empty
                    }
                }
                moveRobot(nextRobotPos)
            }

            else -> return
        }
    }

    private fun moveRobot(newPos: Point) {
        grid[robotPosition.row][robotPosition.col] = Obstacle.Empty
        if (isWide && robotPosition.col + 1 < grid[0].size) {
            grid[robotPosition.row][robotPosition.col + 1] = Obstacle.Empty
        }
        grid[newPos.row][newPos.col] = Obstacle.Robot
        if (isWide && newPos.col + 1 < grid[0].size) {
            grid[newPos.row][newPos.col + 1] = Obstacle.Empty
        }
        robotPosition = newPos
    }

    fun calculateGPSSum(): Int =
        grid.flatMapIndexed { row, rowObstacles ->
            rowObstacles.withIndex()
                .filter { (_, obstacle) ->
                    if (isWide) {
                        obstacle == Obstacle.BoxLeft
                    } else {
                        obstacle == Obstacle.Box
                    }
                }
                .map { (col, _) -> (row * 100) + col }
        }.sum()

    fun print() {
        grid.forEach { row ->
            println(row.joinToString("") { it.char.toString() })
        }
        println()
    }
}

fun makeWideWarehouse(input: List<String>): List<String> {
    // Split into warehouse and moves sections
    val warehouse = input.takeWhile { it.isNotEmpty() }
    val moves = input.drop(warehouse.size)

    // Convert warehouse to wide format
    val wideWarehouse = warehouse.map { line ->
        line.map { char ->
            when (char) {
                '#' -> "##"
                'O' -> "[]"
                '.' -> ".."
                '@' -> "@."
                else -> throw IllegalArgumentException("Invalid character: $char")
            }
        }.joinToString("")
    }

    return wideWarehouse + moves
}

fun parseInput(input: List<String>, isWide: Boolean = false): Pair<Warehouse, String> {
    val warehouseLines = input.takeWhile { it.isNotEmpty() }
    val moves = input.dropWhile { it.isNotEmpty() }
        .dropWhile { it.isEmpty() }
        .joinToString("")
        .filter { it in "^v<>" }

    var robotPos = Point(0, 0)
    val grid = warehouseLines.mapIndexed { row, line ->
        line.mapIndexed { col, char ->
            val obstacle = Obstacle.fromChar(char)
            if (obstacle == Obstacle.Robot) {
                robotPos = Point(row, col)
            }
            obstacle
        }.toMutableList()
    }

    return Warehouse(grid, robotPos, isWide) to moves
}

fun part1(input: List<String>): Int {
    val (warehouse, moves) = parseInput(input)
    moves.forEach { warehouse.move(it) }
    return warehouse.calculateGPSSum()
}

fun part2(input: List<String>, debug: Boolean = false): Int {
    val wideInput = makeWideWarehouse(input)

    if (debug) {
        println("Wide warehouse initial state:")
        wideInput.takeWhile { it.isNotEmpty() }.forEach(::println)
        println()

        val moves = wideInput.dropWhile { it.isNotEmpty() }
            .dropWhile { it.isEmpty() }
            .joinToString("")
            .filter { it in "^v<>" }
        println("Moves sequence: $moves")
        println()
    }

    val (warehouse, moves) = parseInput(wideInput, isWide = true)

    if (debug) {
        println("Initial state:")
        warehouse.print()
    }

    moves.forEachIndexed { index, direction ->
        if (debug && (index == 0 || index % 100 == 99)) {
            println("\nAfter move ${index + 1} ($direction):")
            warehouse.print()
        }
        warehouse.move(direction)
    }

    if (debug) {
        println("\nFinal state:")
        warehouse.print()
        println("Final GPS sum: ${warehouse.calculateGPSSum()}")
    }

    return warehouse.calculateGPSSum()
}
