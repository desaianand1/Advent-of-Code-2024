package `2024`.day09

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day09Test {
    private val testInput = readInput("2024/day09/test_input.txt")
    private val realInput = readInput("2024/day09/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(1928, part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(6307275788409, part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(2858, part2(testInput))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(6327174563252, part2(realInput))
    }
}
