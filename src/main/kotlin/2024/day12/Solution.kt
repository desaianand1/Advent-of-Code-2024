package `2024`.day12

import utilities.readInput

fun main() {
    val input = readInput("2024/day12/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Point(val row: Int, val col: Int) {

    override fun toString() = "[$row,$col]"

    fun getNeighbors(grid: List<CharArray>): List<Point> {
        val directions = listOf(
            Point(-1, 0), // up
            Point(1, 0), // down
            Point(0, -1), // left
            Point(0, 1), // right
        )

        return directions.mapNotNull { (dr, dc) ->
            val newRow = this.row + dr
            val newCol = this.col + dc
            if (newRow in grid.indices && newCol in grid[0].indices) {
                Point(newRow, newCol)
            } else {
                null
            }
        }
    }
}

data class Edge(val from: Point, val to: Point)

data class Region(
    val plant: Char,
    val points: Set<Point>,
    val grid: List<CharArray>,
) {
    val area: Int get() = points.size
    val perimeter: Int get() = calculatePerimeter(points, grid)
    val sides: Int get() = calculateSides(points, grid)
    val price: Int get() = area * perimeter
    val bulkPrice: Int get() = area * sides

    override fun toString(): String {
        val builder = StringBuilder()
        grid.forEach { row ->
            row.forEach { plot ->
                if (plot == plant) builder.append("$plant ") else builder.append(". ")
            }
            builder.append('\n')
        }
        return "Region ($plant):\n${builder}area: $area | perimeter: $perimeter | price: $price\n"
    }

    private fun calculatePerimeter(points: Set<Point>, grid: List<CharArray>): Int {
        var perimeter = 0
        for (point in points) {
            var sides = 4
            point.getNeighbors(grid).forEach { neighbor ->
                if (neighbor in points) sides--
            }
            perimeter += sides
        }
        return perimeter
    }

    private fun calculateSides(points: Set<Point>, grid: List<CharArray>): Int {
        val perimeter = mutableSetOf<Edge>()

        fun isValid(p: Point) = p.row in grid.indices && p.col in grid[0].indices

        // add all edges that border non-region points
        for (point in points) {
            val directions = listOf(
                Point(0, 1), // right
                Point(1, 0), // down
                Point(-1, 0), // up
                Point(0, -1), // left
            )

            for ((dx, dy) in directions) {
                val neighbor = Point(point.row + dx, point.col + dy)
                if (!isValid(neighbor) || neighbor !in points) {
                    perimeter.add(Edge(point, neighbor))
                }
            }
        }

        // filter out overlapping edges
        val uniqueEdges = mutableSetOf<Edge>()
        for (edge in perimeter) {
            var keep = true
            val (p1, p2) = edge
            // check if there are parallel edges that would make this not a unique side
            for ((dx, dy) in listOf(Point(1, 0), Point(0, 1))) {
                val shiftedEdge = Edge(
                    Point(p1.row + dx, p1.col + dy),
                    Point(p2.row + dx, p2.col + dy),
                )
                if (shiftedEdge in perimeter) {
                    keep = false
                    break
                }
            }
            if (keep) {
                uniqueEdges.add(edge)
            }
        }

        return uniqueEdges.size
    }
}

fun findRegion(grid: List<CharArray>, start: Point, visited: MutableSet<Point>): Region? {
    if (start in visited) return null

    val points = mutableSetOf<Point>()
    val toVisit = mutableListOf<Point>(start)
    val plant = grid[start.row][start.col]

    while (toVisit.isNotEmpty()) {
        val current = toVisit.removeFirst()
        if (current in visited) continue

        visited.add(current)
        points.add(current)

        current.getNeighbors(grid).forEach { neighbor ->
            if (neighbor !in visited && grid[neighbor.row][neighbor.col] == plant) {
                toVisit.add(neighbor)
            }
        }
    }

    return Region(plant, points, grid)
}

fun findAllRegions(grid: List<CharArray>): List<Region> {
    val visited = mutableSetOf<Point>()
    val regions = mutableListOf<Region>()

    for (row in grid.indices) {
        for (col in grid[0].indices) {
            val point = Point(row, col)
            if (point !in visited) {
                val region = findRegion(grid, point, visited)
                if (region != null) {
                    regions.add(region)
                }
            }
        }
    }
    return regions.toList()
}

fun part1(input: List<String>): Int {
    val grid = input.map { it.toCharArray() }
    return findAllRegions(grid).sumOf { it.price }
}

fun part2(input: List<String>): Int {
    val grid = input.map { it.toCharArray() }
    return findAllRegions(grid).sumOf { it.bulkPrice }
}
