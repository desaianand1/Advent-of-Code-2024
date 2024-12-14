package `2024`.day14

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.Ignore
import kotlin.test.assertEquals

class Day14Test {
    private val testInput = readInput("2024/day14/test_input.txt")
    private val realInput = readInput("2024/day14/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(21, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(222901875, part1(realInput))
    }

    @Ignore
    @Test
    fun `Part 2 with test input`() {
        // Day 14 actually doesn't have a "test input" so to speak for part 2
        // Part 2 only makes sense on the real input, not a sub-problem or smaller input.
        assertEquals(0, 0)
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(6243, part2(realInput))
    }
}
