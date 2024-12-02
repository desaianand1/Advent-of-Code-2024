package aoc2024.day01

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day01Test {
    private val testInput =
        """
        4   3
        2   5
        1   3
        3   9
        3   3
        """.trimIndent().split("\n")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(11, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        val input = readInput("2024/day01/input.txt")
        assertEquals(936063, part1(input))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(31, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        val input = readInput("2024/day01/input.txt")
        assertEquals(0, part2(input)) // TODO: Update with actual answer
    }
}
