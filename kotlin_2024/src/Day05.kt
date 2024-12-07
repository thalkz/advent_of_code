package com.thalkz

class Page(
    val id: Int,
    val before: MutableSet<Page> = mutableSetOf()
) : Comparable<Page> {

    override fun compareTo(other: Page): Int {
        if (other in before) return -1
        return 1
    }

    override fun equals(other: Any?) = (id == (other as? Page)?.id)

    override fun hashCode() = id.hashCode()
}

val List<Page>.middleValue
    get() = this[this.size / 2].id

fun List<Page>.isValid(): Boolean {
    val allBefore = mutableSetOf<Page>()
    for (current in this) {
        if (current in allBefore) {
            return false
        }
        allBefore += current.before
    }
    return true
}

fun main() {
    fun parseInput(lines: List<String>): Array<List<Page>> {
        val pages = mutableMapOf<Int, Page>()
        var i = 0
        while (lines[i].isNotEmpty()) {
            val (fromId, toId) = lines[i].split("|").map { it.toInt() }
            val from = pages.getOrPut(fromId) { Page(fromId) }
            val to = pages.getOrPut(toId) { Page(toId) }
            to.before.add(from)
            i++
        }

        val firstLineIndex = i+1
        val orders = Array(lines.size - firstLineIndex) { index ->
            lines[firstLineIndex + index]
                .split(",")
                .map { pages[it.toInt()]!! }
        }

        return orders
    }

    fun part1(lines: List<String>): Int {
        val orders = parseInput(lines)

        var result = 0
        for (order in orders) {
            if (order.isValid()) {
                result += order.middleValue
            }
        }

        return result
    }

    fun part2(lines: List<String>): Int {
        val orders = parseInput(lines)

        var result = 0
        for (order in orders) {
            if (!order.isValid()) {
                result += order.sorted().middleValue
            }
        }

        return result
    }

    val inputFile = "Day05"
    verify("${inputFile}_test", ::part1, 143)
    verify("${inputFile}_test", ::part2, 123)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
