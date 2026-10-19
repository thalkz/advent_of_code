package day20

import readInput
import verify
import java.util.*

const val day = 20

fun main() {
    println("Day $day")

    val testInput = readInput(day, "small")
    verify(
        name = "Part1_small",
        actual = part1(testInput),
        expected = 32000000,
    )

    val mediumInput = readInput(day, "medium")
    verify(
        name = "Part1_medium",
        actual = part1(mediumInput),
        expected = 11687500,
    )

    val input = readInput(day, "input")
    verify(
        name = "Part1",
        actual = part1(input),
        expected = 731517480,
    )
    verify(
        name = "Part2",
        actual = part2(input),
        expected = 244178746156661,
    )
}

