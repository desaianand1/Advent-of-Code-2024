package `2024`.day13

import utilities.readInput
import kotlin.math.abs
import kotlin.math.roundToLong

fun main() {
    val input = readInput("2024/day13/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Prize(val x: Long, val y: Long)
data class Button(val xMovement: Int, val yMovement: Int)
data class ClawMachine(val buttonA: Button, val buttonB: Button, val prize: Prize) {
    override fun toString(): String {
        return "{A: ${buttonA.xMovement}, ${buttonA.yMovement} | B: ${buttonB.xMovement}, ${buttonB.yMovement} | Prize: ${prize.x}, ${prize.y}}"
    }
}

fun parseInput(input: List<String>, usePartTwo: Boolean = false): List<ClawMachine> {
    val buttonRegex = Regex("X\\+(\\d+), Y\\+(\\d+)")
    val prizeRegex = Regex("X=(\\d+), Y=(\\d+)")
    var buttonA: Button? = null
    var buttonB: Button? = null
    var prize: Prize? = null
    val clawMachines = mutableListOf<ClawMachine>()
    val partTwoOffset = 10_000_000_000_000L
    for (line in input.indices step 4) {
        val (a, b, p) = Triple(input[line], input[line + 1], input[line + 2])
        val btnAMatches = buttonRegex.find(a)
        val btnBMatches = buttonRegex.find(b)
        val prizeMatches = prizeRegex.find(p)
        if (btnAMatches != null && btnBMatches != null && prizeMatches != null) {
            val (ax, ay) = btnAMatches.destructured
            val (bx, by) = btnBMatches.destructured
            val (px, py) = prizeMatches.destructured
            buttonA = Button(ax.toInt(), ay.toInt())
            buttonB = Button(bx.toInt(), by.toInt())
            prize = if (usePartTwo) {
                Prize(
                    px.toLong() + partTwoOffset,
                    py.toLong() + partTwoOffset,
                )
            } else {
                Prize(px.toLong(), py.toLong())
            }
            clawMachines.add(ClawMachine(buttonA, buttonB, prize))
        }
    }
    return clawMachines
}

fun isWholeNumber(value: Double): Boolean {
    return value >= 0 && abs(value - value.roundToLong()) < 1e-12
}

fun solveSimultaneousEquations(clawMachine: ClawMachine): Pair<Long, Long> {
    /**
     * Given:
     * (var) a: number of 'A Button' presses
     * (var) b: number of 'B Button' presses
     * (const) X₁ , X₂ : A & B Button's X movements
     * (const) Y₁ , Y₂ : A & B Button's Y movements
     *
     * From the X equation: aX₁ + bX₂ = prize_X
     * We can express 'a' in terms of 'b':
     * a = (prize_X - bX₂) / X₁
     *
     * Substitute this into Y equation: [(prize_X - bX₂)/X₁]Y₁ + bY₂ = prize_Y
     * Solve for b: b = (prize_Y * X₁ - prize_X * Y₁) / (X₁Y₂ - X₂Y₁)
     */
    with(clawMachine) {
        val denominator = buttonA.xMovement * buttonB.yMovement - buttonB.xMovement * buttonA.yMovement
        if (denominator == 0) return Pair(0L, 0L)

        val pressB = ((prize.y * buttonA.xMovement - prize.x * buttonA.yMovement).toDouble() / denominator)
        if (!isWholeNumber(pressB)) return Pair(0L, 0L)

        val pressA = (prize.x - pressB * buttonB.xMovement) / buttonA.xMovement.toDouble()
        if (!isWholeNumber(pressA)) return Pair(0L, 0L)
        val solution = pressA.roundToLong() to pressB.roundToLong()
        return if (solution.first in 0..100 && solution.second in 0..100) solution else 0L to 0L
    }
}

fun solveLargeNumbers(clawMachine: ClawMachine): Pair<Long, Long> {
    with(clawMachine) {
        val a = buttonA.xMovement.toLong()
        val b = buttonB.xMovement.toLong()
        val c = prize.x

        // Find GCD and check if solution exists for X equation
        val gcd = findGCD(a, b)
        if (c % gcd != 0L) return 0L to 0L

        // get base solution for X equation
        val (m, n) = extendedGCD(a, b)
        val factor = c / gcd
        var x0 = m * factor
        var y0 = n * factor

        // calculate step sizes
        val stepX = b / gcd
        val stepY = -a / gcd

        // check Y coordinate equation
        val targetY = prize.y
        var currentY = x0 * buttonA.yMovement + y0 * buttonB.yMovement
        val yStep = stepX * buttonA.yMovement + stepY * buttonB.yMovement

        if (yStep == 0L || (targetY - currentY) % yStep != 0L) return 0L to 0L

        // find k that satisfies Y coordinate
        val k = (targetY - currentY) / yStep

        // calculate final solution
        x0 += stepX * k
        y0 += stepY * k

        var minX = x0
        var minY = y0
        // find the minimum step that keeps both numbers non-negative
        if (stepX != 0L) {
            val maxSteps = if (stepX > 0) {
                -x0 / stepX
            } else {
                x0 / -stepX
            }

            for (step in 0..maxSteps) {
                val newX = x0 + step * stepX
                val newY = y0 + step * stepY
                if (newX >= 0 && newY >= 0 &&
                    (newX < minX || (newX == minX && newY < minY))
                ) {
                    minX = newX
                    minY = newY
                }
            }
        }
        return if (minX >= 0 && minY >= 0) minX to minY else 0L to 0L
    }
}

fun findGCD(a: Long, b: Long): Long {
    var x = abs(a)
    var y = abs(b)
    while (y != 0L) {
        val temp = y
        y = x % y
        x = temp
    }
    return x
}

fun extendedGCD(a: Long, b: Long): Pair<Long, Long> {
    if (b == 0L) return 1L to 0L

    val (x, y) = extendedGCD(b, a % b)
    return y to (x - (a / b) * y)
}

fun calculateCost(buttonPair: Pair<Long, Long>): Long = (buttonPair.first * 3) + buttonPair.second

fun part1(input: List<String>): Long {
    val clawMachines = parseInput(input)
    return clawMachines.sumOf { it -> calculateCost(solveSimultaneousEquations(it)) }
}

fun part2(input: List<String>): Long {
    val clawMachines = parseInput(input, true)
    return clawMachines.sumOf { it -> calculateCost(solveLargeNumbers(it)) }
}
