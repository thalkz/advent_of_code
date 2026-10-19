package day22

import readInput
import verify

const val day = 22

fun main() {
    println("Day $day")

    val testInput = readInput(day, "small")
    verify(
        name = "Part1_small",
        actual = part1(testInput),
        expected = 5,
    )

    val input = readInput(day, "input")
    verify(
        name = "Part1",
        actual = part1(input),
        expected = 443,
    )

    verify(
        name = "Part2_small",
        actual = part2(testInput),
        expected = 7,
    )
    verify(
        name = "Part2",
        actual = part2(input),
        expected = 7,
    )
}
