package `2024`.day15

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.Ignore
import kotlin.test.assertEquals

class Day15Test {
    private val testInput = readInput("2024/day15/test_input.txt")
    private val realInput = readInput("2024/day15/input.txt")

    @Ignore
    @Test
    fun `Part 1 with test input`() {
        assertEquals(10092, part1(testInput))
    }

    @Ignore
    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(1318523, part1(realInput))
    }

    @Ignore
    @Test
    fun `Part 2 with test input`() {
        assertEquals(9021, part2(testInput))
    }

    @Ignore
    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(0, part2(realInput)) // TODO: Update with actual answer
    }
}
