package `2024`.day04

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day04Test {
    private val testInput = readInput("2024/day04/test_input.txt")
    private val realInput = readInput("2024/day04/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(18, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(2414, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(9, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(1871, part2(realInput))
    }
}
