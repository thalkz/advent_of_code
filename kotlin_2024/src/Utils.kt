package com.thalkz

import kotlin.io.path.Path
import kotlin.io.path.readText

fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

fun verify(name: String, solver: (List<String>) -> Int, expected: Int) {
    val input = readInput(name)
    val result = solver(input)
    if (expected != result) {
        throw IllegalStateException("expected $expected but got $result")
    }
}

private fun solve(name: String, part: String, solver: (List<String>) -> Int) {
    val input = readInput(name)
    val result = solver(input)
    println("$name $part: $result")
}

fun solvePart1(name: String, solver: (List<String>) -> Int) = solve(name, "part1", solver)

fun solvePart2(name: String, solver: (List<String>) -> Int) = solve(name, "part2", solver)