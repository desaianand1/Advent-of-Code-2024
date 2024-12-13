package `2024`.day13

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day13Test {
    private val testInput = readInput("2024/day13/test_input.txt")
    private val realInput = readInput("2024/day13/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(480, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(36954, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(875318608908, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(79352015273424, part2(realInput))
    }
}
