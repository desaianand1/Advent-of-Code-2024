package aoc2024.day01

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day01Test {
    private val testInput = readInput("2024/day01/test/test_input.txt")
    private val realInput = readInput("2024/day01/input.txt")

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
        assertEquals(23150395, part2(input))
    }
}
