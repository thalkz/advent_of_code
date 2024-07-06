package day23

import readInput
import verify

const val day = 23

fun main() {
    println("Day $day")

    val testInput = readInput(day, "small")
    verify(
        name = "Part1_small",
        actual = part1(testInput),
        expected = 94,
    )

    val input = readInput(day, "input")
    verify(
        name = "Part1",
        actual = part1(input),
        expected = 94,
    )
}
