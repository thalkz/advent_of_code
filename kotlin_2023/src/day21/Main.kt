package day21

import readInput
import verify

const val day = 21

fun main() {
    println("Day $day")

    val testInput = readInput(day, "small")
    verify(
        name = "Part1_small",
        actual = part1(testInput, steps = 6),
        expected = 16,
    )
    val input = readInput(day, "input")
    verify(
        name = "Part1",
        actual = part1(input, steps = 64),
        expected = 3733,
    )

    verify(
        name = "Part2",
        actual = part2(input),
        expected = 617729401414635,
    )
}
