package day24

import readInput
import verify

const val day = 24

fun main() {
    println("Day $day")

    val testInput = readInput(day, "small")
//    verify(
//        name = "Part1_small",
//        actual = part1(testInput, min = 7.0, max = 27.0),
//        expected = 2,
//    )
//
    val input = readInput(day, "input")
//    verify(
//        name = "Part1",
//        actual = part1(input, min = 200000000000000.0, max = 400000000000000.0),
//        expected = 2,
//    )

    verify(
        name = "Part2_small",
        actual = part2(testInput),
        expected = 47,
    )
}
