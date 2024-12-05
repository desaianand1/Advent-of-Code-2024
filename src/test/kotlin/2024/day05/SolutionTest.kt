package `2024`.day05

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day05Test {
    private val testInput = readInput("2024/day05/test_input.txt")
    private val realInput = readInput("2024/day05/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(143, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(5713, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(123, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(5180, part2(realInput))
    }
}
