package aocYEAR.dayDAY

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class DayDAYTest {
    private val testInput =
        """
        // TODO: Add test input here
        """.trimIndent().split("\n")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(0, part1(testInput)) // TODO: Update expected value
    }

    @Test
    fun `Part 1 with actual input`() {
        val input = readInput("YEAR/dayDAY/input.txt")
        assertEquals(0, part1(input)) // TODO: Update with actual answer
    }

    @Test
    fun `Part 2 with test input`() {
        assertEquals(0, part2(testInput)) // TODO: Update expected value
    }

    @Test
    fun `Part 2 with actual input`() {
        val input = readInput("YEAR/dayDAY/input.txt")
        assertEquals(0, part2(input)) // TODO: Update with actual answer
    }
}
