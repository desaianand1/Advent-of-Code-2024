package `2024`.day17

import `2024`.day17.OpCode.entries
import utilities.readInput
import kotlin.math.pow

fun main() {
    val input = readInput("2024/day17/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

enum class OpCode(val code: Int, operation: (Int, Int, Int, Int) -> Int) {
    ADV(0, { operand: Int, regA: Int, regB: Int, regC: Int ->
        (
            regA / 2.0.pow(
                OpCode.getActualOperand(
                    operand,
                    regA,
                    regB,
                    regC,
                ).toDouble(),
            )
            ).toInt()
    }),
    BXL(1, { operand: Int, regA: Int, regB: Int, regC: Int -> regB.xor(operand) }),
    BST(2, { operand: Int, regA: Int, regB: Int, regC: Int ->
        OpCode.getActualOperand(
            operand,
            regA,
            regB,
            regC,
        ) % 8
    }),
    JNZ(3, { operand: Int, regA: Int, regB: Int, regC: Int -> operand }),
    BXC(4, { operand: Int, regA: Int, regB: Int, regC: Int -> operand }),
    OUT(5, { operand: Int, regA: Int, regB: Int, regC: Int -> operand }),
    BDV(6, { operand: Int, regA: Int, regB: Int, regC: Int -> operand }),
    CDV(7, { operand: Int, regA: Int, regB: Int, regC: Int -> operand }),
    ;

    companion object {
        fun fromChar(c: Int): OpCode = entries.first { it.code == c }
        fun getActualOperand(operand: Int, regA: Int, regB: Int, regC: Int): Int {
            return if (operand >= 0 && operand <= 3) {
                operand
            } else if (operand == 4) {
                regA
            } else if (operand == 5) {
                regB
            } else if (operand == 6) {
                regC
            } else {
                throw IllegalArgumentException("Invalid operand $operand! Combo operand $operand is reserved and will not appear in valid programs. ")
            }
        }
    }
}

fun part1(input: List<String>): Int {
    return 0 // TODO: Implement solution
}

fun part2(input: List<String>): Int {
    return 0 // TODO: Implement solution
}
