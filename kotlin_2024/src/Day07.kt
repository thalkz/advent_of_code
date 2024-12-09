package com.thalkz

import java.math.BigInteger

private class Equation(
    val total: BigInteger,
    val operands: List<BigInteger>
) {
    fun isValid(operators: List<Operator>): Boolean = operators.dfs(operands.first(), 1)

    private fun List<Operator>.dfs(partialResult: BigInteger, index: Int): Boolean = when {
            index == operands.size -> partialResult == total
            partialResult > total -> false
            else -> any { dfs(it.eval(partialResult, operands[index]), index+1) }
        }
}

private enum class Operator {
    Add,
    Multiply,
    Concat,
}

private fun Operator.eval(a: BigInteger, b: BigInteger) = when(this) {
    Operator.Add -> a + b
    Operator.Multiply -> a * b
    Operator.Concat -> "$a$b".toBigInteger()
}

fun main() {
    fun parseInput(lines: List<String>): List<Equation> {
        return lines.map { line ->
            val split = line.split(": ")
            Equation(
                total = split[0].toBigInteger(),
                operands = split[1].split(" ").map { it.toBigInteger() }
            )
        }
    }

    fun part1(lines: List<String>): BigInteger {
        val input = parseInput(lines)
        val operators = listOf(Operator.Add, Operator.Multiply)
        return input
            .filter { it.isValid(operators) }
            .sumOf { it.total }
    }

    fun part2(lines: List<String>): BigInteger {
        val input = parseInput(lines)
        return input
            .filter { it.isValid(Operator.entries) }
            .sumOf { it.total }
    }

    val inputFile = "Day07"
    verify("${inputFile}_test", ::part1, 3749)
    verify("${inputFile}_test", ::part2, 11387)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
