package com.thalkz

fun main() {
    fun part1(input: List<String>): Int {
        val reports = parseInput(input)

        val increasing = reports.count { it.isValidOrder(1..3) }
        val decreasing = reports.count { it.isValidOrder(-3..-1) }

        return increasing + decreasing
    }

    fun part2(input: List<String>): Int {
        val reports = parseInput(input)

        val increasing = reports.count { it.isAnyValidWithSkipped(1..3) }
        val decreasing = reports.count { it.isAnyValidWithSkipped(-3..-1) }

        return increasing + decreasing
    }

    val inputFile = "Day02"
    verify("${inputFile}_test", ::part1, 2)
    verify("${inputFile}_test", ::part2, 4)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}

private fun parseInput(input: List<String>): List<List<Int>> = input.map { line ->
    line.split(" ").map { it.toInt() }
}

private fun List<Int>.isValidOrder(range: IntRange): Boolean {
    for (i in 0 until lastIndex) {
        if (this[i + 1] - this[i] !in range) {
            return false
        }
    }
    return true
}

private fun List<Int>.isAnyValidWithSkipped(range: IntRange): Boolean {
    if (isValidOrder(range)) return true

    for (skipped in 0..lastIndex) {
        if (isValidWithSkipped(range, skipped)) {
            return true
        }
    }
    return false
}

private fun List<Int>.isValidWithSkipped(range: IntRange, skipped: Int): Boolean {
    var lastLevel: Int? = null
    for (i in 0..lastIndex) {
        if (i == skipped) continue
        if (lastLevel != null && this[i] - lastLevel !in range) {
            return false
        }
        lastLevel = this[i]
    }
    return true
}
