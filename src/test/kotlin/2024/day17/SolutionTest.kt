package `2024`.day17

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.Ignore
import kotlin.test.assertEquals

class Day17Test {
    private val testInput = readInput("2024/day17/test_input.txt")
    private val realInput = readInput("2024/day17/input.txt")

    @Ignore
    @Test
    fun `Part 1 with test input`() {
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(0, part1(realInput)) // TODO: Update with actual answer
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(0, part2(testInput)) // TODO: Update expected value
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(0, part2(realInput)) // TODO: Update with actual answer
    }
}
