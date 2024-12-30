package com.thalkz

private class Matcher(
    val patterns: List<String>,
    val input: String,
) {
    val startIndexToCount = mutableMapOf<Int, ULong>()

    private fun countAt(index: Int): ULong = startIndexToCount[index] ?: 0UL

    private fun match(startIndex: Int, pattern: String): Boolean {
        return input.regionMatches(startIndex, pattern, 0, pattern.length)
    }

    private fun next(startIndex: Int, count: ULong) {
        patterns.forEach { pattern ->
            if (match(startIndex, pattern)) {
                val endIndex = startIndex + pattern.length
                startIndexToCount[endIndex] = count + countAt(endIndex)
            }
        }
    }

    fun countValid(): ULong {
        next(0, 1UL)
        for (i in input.indices) {
            val count = countAt(i)
            if (count > 0UL) {
                next(i, count)
            }
        }
        return countAt(input.length)
    }

    fun isValid() = countValid() > 0UL
}

fun main() {
    fun parseInput(lines: List<String>): List<Matcher> {
        val patterns = lines[0].split(", ")
        return lines.drop(2).map { Matcher(patterns, it) }
    }

    fun part1(lines: List<String>): Int {
        val matchers = parseInput(lines)
        return matchers.count { it.isValid() }
    }

    fun part2(lines: List<String>): ULong {
        val matchers = parseInput(lines)
        return matchers.sumOf { it.countValid() }
    }

    val inputFile = "Day19"
    verify("${inputFile}_test", ::part1, 6)
    verify("${inputFile}_test", ::part2, 16)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
