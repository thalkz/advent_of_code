package day25

import readInput
import verify

const val day = 25

fun main() {
    println("Day $day")

//    val testInput = readInput(day, "small")
//    verify(
//        name = "Part1_small",
//        actual = part1(testInput),
//        expected = 54,
//    )

    val input = readInput(day, "input")
    verify(
        name = "Part1",
        actual = part1(input),
        expected = 2,
    )
}
