package `2024`.day17

import `2024`.day17.OpCode.entries
import utilities.readInput
import kotlin.math.pow

fun main() {
    val input = readInput("2024/day17/input.txt")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

data class Register(var a: Long, var b: Long, var c: Long) {
    override fun toString() = "Register(a=$a, b=$b, c=$c)"
}

data class Instruction(val opcode: OpCode, val operand: Int)

data class Program(val instructions: List<Int>) {
    var currentInstruction = 0
    var isHalted = false

    fun next(): Instruction? {
        if (currentInstruction >= instructions.size - 1) {
            isHalted = true
            return null
        }
        val opcode = instructions[currentInstruction]
        val operand = instructions[currentInstruction + 1]
        currentInstruction += 2
        return Instruction(OpCode.fromInt(opcode), operand)
    }

    fun jump(newIndex: Int) {
        currentInstruction = newIndex
    }

    fun hasHalted(): Boolean = isHalted
}

enum class OpCode(val code: Int) {
    ADV(0), // Divide A by 2^operand, store in A
    BXL(1), // XOR B with literal operand
    BST(2), // Store operand mod 8 in B
    JNZ(3), // Jump if A != 0
    BXC(4), // XOR B with C
    OUT(5), // Output operand mod 8
    BDV(6), // Divide A by 2^operand, store in B
    CDV(7), // Divide A by 2^operand, store in C
    ;

    companion object {
        fun fromInt(value: Int): OpCode = entries.first { it.code == value }

        fun getComboOperand(operand: Int, register: Register): Int {
            // Convert to Int at the last possible moment, only for comparison
            val result = when (operand) {
                in 0..3 -> operand
                4 -> (register.a % 8).toInt()
                5 -> (register.b % 8).toInt()
                6 -> (register.c % 8).toInt()
                else -> throw IllegalArgumentException("Invalid combo operand: $operand")
            }
            return result
        }
    }
}

fun parseInput(input: List<String>): Pair<Register, Program> {
    val regA = input[0].substringAfter(": ").trim().toLong()
    val regB = input[1].substringAfter(": ").trim().toLong()
    val regC = input[2].substringAfter(": ").trim().toLong()
    val register = Register(regA, regB, regC)

    val numbers = input[4].substringAfter(": ")
        .split(",")
        .map { it.trim().toInt() }

    return register to Program(numbers)
}

fun executeProgram(register: Register, program: Program): String {
    val output = mutableListOf<Int>()

    while (!program.hasHalted()) {
        val instruction = program.next() ?: break
        executeInstruction(instruction, program, register, output)
    }

    return output.joinToString(",")
}

private fun executeInstruction(
    instruction: Instruction,
    program: Program,
    register: Register,
    output: MutableList<Int>? = null,
) {
    when (instruction.opcode) {
        OpCode.ADV -> {
            val power = OpCode.getComboOperand(instruction.operand, register)
            register.a /= 1L shl power
        }

        OpCode.BXL -> register.b = register.b xor instruction.operand.toLong()
        OpCode.BST -> register.b = OpCode.getComboOperand(instruction.operand, register).toLong()
        OpCode.JNZ -> if (register.a != 0L) program.jump(instruction.operand)
        OpCode.BXC -> register.b = register.b xor register.c
        OpCode.OUT -> {
            val value = OpCode.getComboOperand(instruction.operand, register)
            output?.add(value)
        }

        OpCode.BDV -> {
            val power = OpCode.getComboOperand(instruction.operand, register)
            register.b = register.a / (1L shl power)
        }

        OpCode.CDV -> {
            val power = OpCode.getComboOperand(instruction.operand, register)
            register.c = register.a / (1L shl power)
        }
    }
}

fun constructProgramInput(target: List<Int>): Long {
    // println("Sequence: $target")
    var result = 1L
    while (true) {
        // Run the program with the current value
        val output = runProgramForOutput(result, target)

        // If output matches target, we're done!
        if (output == target) {
            break
        }

        // If output is too short, we need more octal digits
        if (output.size < target.size) {
            result *= 8
            continue
        }

        // Find the rightmost position that doesn't match
        var updatedValue = false
        for (i in target.indices.reversed()) {
            if (i >= output.size || target[i] != output[i]) {
                // change the digit at position i, add 8^i
                result += 8.0.pow(i.toDouble()).toLong()
                updatedValue = true
                break
            }
        }

        if (!updatedValue) {
            // If we couldn't find a position to update, increment by 1
            result += 1
        }
    }

    // println("Found minimum value: $result (octal: ${result.toString(8)})")
    return result
}

private fun runProgramForOutput(initialA: Long, programSequence: List<Int>): List<Int> {
    val register = Register(initialA, 0L, 0L)
    val program = Program(programSequence)
    val output = mutableListOf<Int>()

    while (!program.hasHalted()) {
        val instruction = program.next() ?: break

        if (instruction.opcode == OpCode.OUT) {
            // For output instructions, capture the output
            val value = OpCode.getComboOperand(instruction.operand, register)
            output.add(value)
        } else {
            // For non-output instructions, execute normally
            executeInstruction(instruction, program, register)
        }
    }

    return output
}

fun part1(input: List<String>): String {
    val (register, program) = parseInput(input)
    return executeProgram(register, program)
}

fun part2(input: List<String>): Long {
    val (_, originalProgram) = parseInput(input)
    val target = originalProgram.instructions
    return constructProgramInput(target)
}
