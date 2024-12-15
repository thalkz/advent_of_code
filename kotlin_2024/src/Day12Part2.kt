package com.thalkz

private fun Grid.visitArea(visited: MutableSet<Point>, borders: MutableSet<Border>, point: Point): Int {
    if (point in visited) return 0
    val char = at(point)
    visited += point
    var totalArea = 1
    for ((direction, adjPoint) in point.adjacent) {
        if (adjPoint !in this || at(adjPoint) != char) {
            borders.add(Border(point, direction))
        } else {
            val area = visitArea(visited, borders, adjPoint)
            totalArea += area
        }
    }
    return totalArea
}

data class Border(
    val point: Point,
    val direction: Direction,
)

fun Border.nextRight(): Border = Border(
    point = point.adjacent[direction.rotateRight()]!!,
    direction = direction
)

fun Border.nextLeft(): Border = Border(
    point = point.adjacent[direction.rotateLeft()]!!,
    direction = direction
)

fun Set<Border>.countSides(): Int {
    val visited = mutableSetOf<Border>()
    var sides = 0
    for (border in this) {
        if (border !in visited) {
            sides++

            // mark all side as visited
            var next = border.nextRight()
            while (next in this) {
                visited += next
                next = next.nextRight()
            }

            next = border.nextLeft()
            while (next in this) {
                visited += next
                next = next.nextLeft()
            }
        }
    }
    return sides
}

fun main() {
    fun parseInput(lines: List<String>): Grid {
        return Grid(lines)
    }

    fun part2(lines: List<String>): Int {
        val visited = mutableSetOf<Point>()
        val grid = parseInput(lines)
        var result = 0
        for (point in grid.points) {
            if (point !in visited) {
                val borders = mutableSetOf<Border>()
                val area = grid.visitArea(visited, borders, point)
                result += area * borders.countSides()
            }
        }
        return result
    }

    val inputFile = "Day12"
    verify("${inputFile}_test", ::part2, 1206)

    solvePart2(inputFile, ::part2)
}
