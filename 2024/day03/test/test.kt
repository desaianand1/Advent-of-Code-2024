package aoc2024.day03

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day03Test {
    private val testInput = readInput("2024/day03/test/test_input.txt")
    private val testInput2 = readInput("2024/day03/test/test_input-2.txt")
    private val realInput = readInput("2024/day03/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(161, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(184122457, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(48, part2(testInput2))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(107862689, part2(realInput))
    }
}
