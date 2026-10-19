package com.thalkz

fun main() {
    val digitRegex = Regex("(\\d){1,3}")

    fun parseAndMultiply(match: String): Int {
        val digits = digitRegex.findAll(match).toList()
        val left = digits[0].value.toInt()
        val right = digits[1].value.toInt()
        return left*right
    }

    fun parseInput(input: List<String>, regex: Regex): List<String> {
        val matches = mutableListOf<String>()
        for (line in input) {
            matches.addAll(regex.findAll(line).map { it.value })
        }
        return matches
    }

    fun part1(input: List<String>): Int {
        val mulRegex = Regex("mul\\((\\d){1,3},(\\d){1,3}\\)") // kotlin.text.Regex
        val matches = parseInput(input, mulRegex)

        var result = 0
        for (match in matches) {
            result += parseAndMultiply(match)
        }
        return result
    }


    fun part2(input: List<String>): Int {
        val instrRegex = Regex("""(mul\((\d){1,3},(\d){1,3}\)|do\(\)|don\'t\(\))""") // kotlin.text.Regex
        val matches = parseInput(input, instrRegex)

        var result = 0
        var enabled = true
        for (match in matches) {
            when (match) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
                else -> if (enabled) {
                    result += parseAndMultiply(match)
                }
            }
        }
        return result
    }

    val inputFile = "Day03"
    verify("${inputFile}_test", ::part1, 161)
    verify("${inputFile}_test_2", ::part2, 48)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}