package `2024`.day11

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day11Test {
    private val testInput = readInput("2024/day11/test_input.txt")
    private val realInput = readInput("2024/day11/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(55312, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(217812, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(65601038650482, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(259112729857522, part2(realInput))
    }
}
