package day21

import readInput
import verify

const val day = 21

fun main() {
    println("Day $day")

    val testInput = readInput(day, "small")
    verify(
        name = "Part1_small",
        actual = part1(testInput),
        expected = 16,
    )

    val mediumInput = readInput(day, "input")
    verify(
        name = "Part1",
        actual = part1(mediumInput),
        expected = 3733,
    )
//
//    val input = readInput(day, "input")
//    verify(
//        name = "Part1",
//        actual = part1(input),
//        expected = 731517480,
//    )
//    verify(
//        name = "Part2",
//        actual = part2(input),
//        expected = 244178746156661,
//    )
}