package com.thalkz

import kotlin.math.absoluteValue

fun main() {
    fun parseLists(input: List<String>): Pair<IntArray, IntArray> {
        val left = IntArray(input.size)
        val right = IntArray(input.size)
        input.forEachIndexed { i, line ->
            val (leftValue, rightValue) = line.split("   ").map { it.toInt() }
            left[i] = leftValue
            right[i] = rightValue
        }
        return left to right
    }

    fun part1(input: List<String>): Int {
        val (left, right) = parseLists(input)
        left.sort()
        right.sort()
        return left.zip(right)
            .sumOf {
                (it.first - it.second).absoluteValue
            }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = parseLists(input)
        val groupedRight = right.groupingBy().eachCount()
        return left.sumOf { it * (groupedRight[it] ?: 0) }
    }

    verify("Day01_test", ::part1, 11)
    verify("Day01_test", ::part2, 31)

    solvePart1("Day01", ::part1)
    solvePart2("Day01", ::part2)
}

// Does not exist in the std lib for IntArray
private fun IntArray.groupingBy(): Grouping<Int, Int> {
    return object : Grouping<Int, Int> {
        override fun sourceIterator(): Iterator<Int> = this@groupingBy.iterator()
        override fun keyOf(element: Int): Int = element
    }
}