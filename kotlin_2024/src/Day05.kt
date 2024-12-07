package com.thalkz

class Page(
    val id: Int,
    val before: MutableSet<Page> = mutableSetOf()
)

class Input(
    val pages: Map<Int, Page>,
    val toPrintList: Array<ToPrint>,
)

class ToPrint(
    private val toPrintIds: List<Int>
) {
    val middleValue = toPrintIds[toPrintIds.size / 2]

    fun isValid(pages: Map<Int, Page>): Boolean {
        val before = mutableSetOf<Int>()
        for (currentId in toPrintIds) {
            if (currentId in before) {
                return false
            }
            before += pages[currentId]!!.before.map { it.id }
        }
        return true
    }
}

fun main() {
    fun parseInput(lines: List<String>): Input {
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
        val toPrintList = Array(lines.size - firstLineIndex) {
            val toPrintIds = lines[firstLineIndex + it].split(",").map { it.toInt() }
            ToPrint(toPrintIds)
        }

        return Input(pages, toPrintList)
    }

    fun part1(lines: List<String>): Int {
        val input = parseInput(lines)

        var result = 0
        for (toPrint in input.toPrintList) {
            if (toPrint.isValid(input.pages)) {
                result += toPrint.middleValue
            }
        }

        return result
    }

    fun part2(input: List<String>): Int {
        val parsed = parseInput(input)
        return 0
    }

    val inputFile = "Day05"
    verify("${inputFile}_test", ::part1, 143)
//    verify("${inputFile}_test", ::part2, 0)

    solvePart1(inputFile, ::part1)
//    solvePart2(inputFile, ::part2)
}
