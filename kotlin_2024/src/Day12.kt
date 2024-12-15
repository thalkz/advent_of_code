package com.thalkz

private val EmptyResult = 0 to 0

private fun Grid.visitArea(visited: MutableSet<Point>, point: Point): Pair<Int, Int> {
    if (point in visited) return EmptyResult
    val char = at(point)
    visited += point
    var totalArea = 1
    var totalPerimeter = 0
    for (adj in point.adjacent.values) {
        if (adj !in this || at(adj) != char) {
            totalPerimeter += 1
        } else {
            val (area, perimeter) = visitArea(visited, adj)
            totalArea += area
            totalPerimeter += perimeter
        }
    }
    return totalArea to totalPerimeter
}

fun main() {
    fun parseInput(lines: List<String>): Grid {
        return Grid(lines)
    }

    fun part1(lines: List<String>): Int {
        val visited = mutableSetOf<Point>()
        val grid = parseInput(lines)

        var result = 0
        for (point in grid.points) {
            if (point !in visited) {
                val (area, perimeter) = grid.visitArea(visited, point)
                result += area * perimeter
            }
        }
        return result
    }

    fun part2(lines: List<String>): Int {
        val input = parseInput(lines)
        return 0
    }

    val inputFile = "Day12"
    verify("${inputFile}_test", ::part1, 1930)
    verify("${inputFile}_test", ::part2, 1206)

    solvePart1(inputFile, ::part1)
//    solvePart2(inputFile, ::part2)
}
