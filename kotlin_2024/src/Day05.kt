package com.thalkz

class Page(
    val id: Int,
    val before: MutableSet<Page> = mutableSetOf() // java.util.LinkedHashSet<E>
) : Comparable<Page> {

    override fun compareTo(other: Page) = if (other in before) -1 else 1

    override fun equals(other: Any?) = (id == (other as? Page)?.id)

    override fun hashCode() = id.hashCode()
}

val List<Page>.middleValue
    get() = this[this.size / 2].id

fun List<Page>.isValidOrder(): Boolean {
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

        var lineIndex = 0
        while (lines[lineIndex].isNotEmpty()) {
            val (fromId, toId) = lines[lineIndex].split("|").map { it.toInt() }
            val from = pages.getOrPut(fromId) { Page(fromId) }
            val to = pages.getOrPut(toId) { Page(toId) }
            to.before.add(from)
            lineIndex++
        }

        lineIndex += 1 // skip empty line
        val orders = Array(lines.size - lineIndex) { i ->
            lines[lineIndex + i]
                .split(",")
                .map { pages[it.toInt()]!! }
        }

        return orders
    }

    fun part1(lines: List<String>): Int {
        val orders = parseInput(lines)

        return orders
            .filter { it.isValidOrder() }
            .sumOf { it.middleValue }
    }

    fun part2(lines: List<String>): Int {
        val orders = parseInput(lines)

        return orders
            .filter { !it.isValidOrder() }
            .sumOf { it.sorted().middleValue }
    }

    val inputFile = "Day05"
    verify("${inputFile}_test", ::part1, 143)
    verify("${inputFile}_test", ::part2, 123)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
