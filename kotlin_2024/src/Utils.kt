package com.thalkz

import java.math.BigInteger
import kotlin.io.path.Path
import kotlin.io.path.readText

fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

fun <T> verify(name: String, solver: (List<String>) -> T, expected: T) {
    val input = readInput(name)
    val result = solver(input)
    if (expected.toString() != result.toString()) {
        throw IllegalStateException("expected $expected but got $result")
    }
}

private  fun <T> solve(name: String, part: String, solver: (List<String>) -> T) {
    val input = readInput(name)
    val result = solver(input)
    println("$name $part: $result")
}

fun <T> solvePart1(name: String, solver: (List<String>) -> T) = solve(name, "part1", solver)

fun <T> solvePart2(name: String, solver: (List<String>) -> T) = solve(name, "part2", solver)