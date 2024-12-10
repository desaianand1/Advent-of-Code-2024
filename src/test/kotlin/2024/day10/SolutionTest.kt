package `2024`.day10

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day10Test {
    private val testInput = readInput("2024/day10/test_input.txt")
    private val realInput = readInput("2024/day10/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(36, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(717, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(81, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(1686, part2(realInput))
    }
}
