package day20

import readInput
import verify

const val day = 20

fun main() {
    println("Day $day")

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(day, "small")
    verify(
        name = "Part1_small",
        actual = part1(testInput),
        expected = 0,
    )

    val input = readInput(day, "input")
    verify(
        name = "Part1",
        actual = part1(input),
        expected = 0,
    )
    verify(
        name = "Part2",
        actual = part1(input),
        expected = 0,
    )
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

