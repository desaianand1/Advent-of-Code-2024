package `2024`.day17

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class Day17Test {
    private val testInput = readInput("2024/day17/test_input.txt")
    private val testInputP2 = readInput("2024/day17/test_input_p2.txt")
    private val difficultTestInputP2 = readInput("2024/day17/test_input_p2_difficult.txt")
    private val realInput = readInput("2024/day17/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals("4,6,3,5,6,3,5,2,1,0", part1(testInput))
    }

    @Test
    fun `Part 1 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals("1,5,0,1,7,4,1,0,3", part1(realInput))
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(117440, part2(testInputP2))
    }

    @Test
    fun `Part 2 with difficult test input`() {
        assertEquals(202797954918051, part2(difficultTestInputP2))
    }

    @Test
    fun `Part 2 with actual input`() {
        if (realInput.isEmpty()) return
        assertEquals(47910079998866, part2(realInput))
    }
}
