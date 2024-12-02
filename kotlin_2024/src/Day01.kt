package com.thalkz

import kotlin.math.absoluteValue

fun main() {
    fun parseLists(input: List<String>): Pair<IntArray, IntArray> {
        val left = IntArray(input.size) // int[]
        val right = IntArray(input.size) // int[]
        input.forEachIndexed { i, line -> // kotlin.collections.Iterable<T>::forEachIndexed
            val (leftValue, rightValue) = line.split("   ") // kotlin.text.CharSequence::split (return a java.util.ArrayList<String>)
            left[i] = leftValue.toInt()
            right[i] = rightValue.toInt()
        }
        return left to right // kotlin.Pair (serializable data class)
    }

    fun part1(input: List<String>): Int {
        val (left, right) = parseLists(input)
        left.sort() // java.util.DualPivotQuicksort::sort
        right.sort() // java.util.DualPivotQuicksort::sort
        return left.zip(right) // kotlin.collections.IntArray::zip (returns a java.util.ArrayList<Pair<Int, Int>>)
            .sumOf {
                (it.first - it.second).absoluteValue // java.lang.Math::abs
            }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = parseLists(input)
        val groupedRight = right.groupingBy().eachCount() // uses kotlin.collections.Grouping::foldTo
        return left.sumOf { it * (groupedRight[it] ?: 0) }
    }

    verify("Day01_test", ::part1, 11)
    verify("Day01_test", ::part2, 31)

    solvePart1("Day01", ::part1)
    solvePart2("Day01", ::part2)
}

// Does not exist in the std lib for IntArray
private fun IntArray.groupingBy(): Grouping<Int, Int> {
    return object : Grouping<Int, Int> { // kotlin.collections.Grouping<T, out K)
        override fun sourceIterator(): Iterator<Int> = this@groupingBy.iterator() // kotlin.collections.Iterator<out T>
        override fun keyOf(element: Int): Int = element
    }
}
