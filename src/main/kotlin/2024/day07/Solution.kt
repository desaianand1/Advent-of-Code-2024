package `2024`.day07

import utilities.readInput
import kotlin.math.pow

fun main() {
    val input = readInput("2024/day07/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

fun Int.countDigits() = when (this) {
    0 -> 1
    else -> this.toString().length
}

data class Equation(val testValue: Long, val operands: List<Int>) {

    fun evaluate(operators: Array<Char>): Long {
        var result = operands.first().toLong()
        operators.forEachIndexed { i, operator ->
            when (operator) {
                // Note: operands size > operators size (by 1)
                '+' -> result += operands[i + 1]
                '*' -> result *= operands[i + 1]
                '|' -> {
                    val nextOperand = operands[i + 1]
                    result = result * (10.0.pow(nextOperand.countDigits()).toLong()) + nextOperand
                }
            }
        }
        return result
    }
}

fun parseInput(input: List<String>): List<Equation> {
    return input.map { line ->
        val (testValue, numbersStr) = line.split(": ")
        val numbers = numbersStr.trim().split(" ").map { it.toInt() }
        Equation(testValue.toLong(), numbers)
    }
}

fun getOperatorCombinations(numOperators: Int, useConcatenation: Boolean): List<Array<Char>> {
    val operators = if (useConcatenation) listOf('+', '*', '|') else listOf('+', '*')
    val result = mutableListOf<Array<Char>>()

    fun generate(current: Array<Char>) {
        if (current.size == numOperators) {
            result.add(current)
            return
        }
        operators.forEach { op ->
            generate(current + op)
        }
    }
    generate(emptyArray<Char>())
    return result
}

fun canEquationBeSolved(equation: Equation, useConcatenation: Boolean): Boolean {
    val numOperators = equation.operands.size - 1
    val possibleOperators = getOperatorCombinations(numOperators, useConcatenation)
    return possibleOperators.any { operators ->
        equation.evaluate(operators) == equation.testValue
    }
}

fun part1(input: List<String>): Long {
    val equations = parseInput(input)
    return equations.filter { canEquationBeSolved(it, false) }.sumOf { it.testValue }
}

fun part2(input: List<String>): Long {
    val equations = parseInput(input)
    return equations.filter { canEquationBeSolved(it, true) }.sumOf { it.testValue }
}
