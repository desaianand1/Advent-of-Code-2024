package `2024`.day07

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day07Test {
    private val testInput = readInput("2024/day07/test_input.txt")
    private val realInput = readInput("2024/day07/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(3749, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(8401132154762, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(11387, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(95297119227552, part2(realInput))
    }
}
