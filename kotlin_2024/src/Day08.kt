package com.thalkz

private fun <T> List<T>.forEachPair(action: (a: T, b: T) -> Unit) {
    for (i in 0..lastIndex) {
        for (j in (i+1)..lastIndex) {
            action(this[i], this[j])
        }
    }
}

private fun List<String>.toSize() =  Size(this[0].length, this.size)

fun main() {
    fun parseInput(lines: List<String>): Sequence<List<Point>> {
        val map = mutableMapOf<Char, MutableList<Point>>()
        for (j in 0..lines.lastIndex) {
            val line = lines[j]
            for (i in 0..lines[0].lastIndex) {
                if (line[i] == '.') continue
                val points = map.getOrPut(line[i], { mutableListOf() })
                points.add(Point(i, j))
            }
        }
        return map.values.asSequence()
    }

    fun generateAntiNodes(antennas: List<Point>, size: Size) = buildList {
        antennas.forEachPair { a, b ->
            val left = 2*a - b
            if (left in size) {
                add(left)
            }
            val right = 2*b - a
            if (right in size) {
                add(right)
            }
        }
    }

    fun part1(lines: List<String>): Int {
        val antennas = parseInput(lines)
        val size = lines.toSize()
        return antennas
            .map { generateAntiNodes(it, size) }
            .flatten()
            .toSet()
            .count()
    }

    fun generateAllAntiNodes(antennas: List<Point>, size: Size) = buildList {
        antennas.forEachPair { a, b ->
            val diff = b - a
            var point = b
            while (point in size) {
                add(point)
                point+=diff
            }
            point = b - diff
            while (point in size) {
                add(point)
                point-=diff
            }
        }
    }

    fun part2(lines: List<String>): Int {
        val antennas = parseInput(lines)
        val size = lines.toSize()
        return antennas
            .map { generateAllAntiNodes(it, size) }
            .flatten()
            .toSet()
            .count()
    }

    val inputFile = "Day08"
    verify("${inputFile}_test", ::part1, 14)
    verify("${inputFile}_test", ::part2, 34)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
