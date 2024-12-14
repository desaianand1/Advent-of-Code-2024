package `2024`.day12

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day12Test {
    private val testInput = readInput("2024/day12/test_input.txt")
    private val realInput = readInput("2024/day12/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(1930, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(1450816, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(1206, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(865662, part2(realInput))
    }
}
