package com.thalkz

import java.lang.Exception

enum class Opcode {
    Adv,
    Bxl,
    Bst,
    Jnz,
    Bxc,
    Out,
    Bdv,
    Cdv,
}

data class Instruction(
    val opcode: Opcode,
    val operand: ULong,
)

data class Computer(
    var a: ULong,
    var b: ULong,
    var c: ULong,
    var program: List<Instruction>,
) {
    private var pointer = 0

    fun reset(a: ULong) {
        this.a = a
        this.b = 0UL
        this.c = 0UL
        pointer = 0
    }

    private val Instruction.comboOperand: ULong
        get() = when (operand) {
        in 0UL..3UL -> operand
        4UL -> a
        5UL -> b
        6UL -> c
        else -> throw Exception("invalid operand")
    }

    private fun execute(inst: Instruction): ULong? {
        when (inst.opcode) {
            Opcode.Adv -> {
                a = a shr inst.comboOperand.toInt()
            }
            Opcode.Bxl -> {
                b = b xor inst.operand
            }
            Opcode.Bst -> {
                b = inst.comboOperand.mod(8UL)
            }
            Opcode.Jnz -> {
                if (a != 0UL) {
                    if (inst.operand != 0UL) {
                        println("jump to ${inst.operand}")
                        throw Exception("not handled")
                    }
                    pointer = (inst.operand.toInt()/2) - 1  // divide by two to get instruction pointer ?
                    return null
                }
            }
            Opcode.Bxc -> {
                b = b xor c
            }
            Opcode.Out -> {
                return inst.comboOperand.mod(8UL)
            }
            Opcode.Bdv -> {
                b = a shr inst.comboOperand.toInt()
            }
            Opcode.Cdv -> {
                c = a shr inst.comboOperand.toInt()
            }
        }
        return null
    }

    fun run(): List<ULong> = buildList {
        while (pointer < program.size) {
            val result = execute(program[pointer])
            if (result != null) {
                add(result)
            }
            pointer++
        }
    }

    fun findValue(a: ULong, index: Int, target: List<ULong>): ULong? {
        if (index < 0) return a
        for (offset in 0UL..7UL) {
            val attempt = (a shl 3) + offset
            reset(attempt)
            val result = run()
            if (target[index] == result.first()) {
                val success = findValue(attempt, index-1, target)
                if (success != null) return success
            }
        }
        return null
    }
}

fun main() {
    fun parseInput(lines: List<String>): Computer {
        val values = lines.map { it.split(": ").lastOrNull() ?: "" }
        val instructionValues = values[4].split(",")

        val instructions = mutableListOf<Instruction>()
        for (i in instructionValues.indices step 2) {
            instructions += Instruction(
                opcode = Opcode.entries[instructionValues[i].toInt()],
                operand = instructionValues[i+1].toULong(),
            )
        }
        return Computer(
            a = values[0].toULong(),
            b = values[1].toULong(),
            c = values[2].toULong(),
            program = instructions,
        )
    }

    fun part1(lines: List<String>): String {
        val computer = parseInput(lines)
        println(computer)
        return computer.run().joinToString(separator = ",")
    }

    fun part2(lines: List<String>): ULong {
        val computer = parseInput(lines)
        val expected = lines[4].split(": ")[1].split(",").map { it.toULong() }
        return computer.findValue(0UL, expected.lastIndex, expected) ?: 0UL
    }

    val inputFile = "Day17"
//    verify("${inputFile}_test", ::part1, "4,6,3,5,6,3,5,2,1,0")
//    verify("${inputFile}_test", ::part2, 117440)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}