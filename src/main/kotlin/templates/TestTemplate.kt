package `YEAR`.dayDAY

import org.junit.jupiter.api.Test
import utilities.readInput
import kotlin.test.assertEquals

class DayDAYTest {
    private val testInput = readInput("YEAR/dayDAY/test/test_input.txt")
    private val realInput = readInput("YEAR/dayDAY/input.txt")

    @Test
    fun `Part 1 with test input`() {
        assertEquals(0, part1(testInput)) // TODO: Update expected value
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
