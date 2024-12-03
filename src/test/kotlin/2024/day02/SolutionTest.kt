package `2024`.day02

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day02Test {
    private val testInput = readInput("2024/day02/test_input.txt")
    private val realInput = readInput("2024/day02/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(2, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(516, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(4, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(561, part2(realInput))
    }
}
