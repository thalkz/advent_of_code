package com.thalkz

fun Grid.walkUphill(visited: MutableSet<Point>, point: Point): Int {
    if (point in visited) return 0
    visited.add(point)
    val altitude = at(point) - '0'
    if (altitude == 9) return 1
    return point.adjacent.values.sumOf {
        if (it in this && at(it) - '0' == altitude+1) {
            walkUphill(visited, it)
        } else {
            0
        }
    }
}

fun Grid.walkRating(point: Point): Int {
    val altitude = at(point) - '0'
    if (altitude == 9) return 1
    return point.adjacent.values.sumOf {
        if (it in this && at(it) - '0' == altitude+1) {
            walkRating(it)
        } else {
            0
        }
    }
}

fun main() {
    fun part1(lines: List<String>): Int {
        val grid = Grid(lines)
        return grid.points
            .filter { grid.at(it) == '0' }
            .sumOf { grid.walkUphill(mutableSetOf(), it) }
    }

    fun part2(lines: List<String>): Int {
        val grid = Grid(lines)
        return grid.points
            .filter { grid.at(it) == '0' }
            .sumOf { grid.walkRating(it) }
    }

    val inputFile = "Day10"
    verify("${inputFile}_test", ::part1, 36)
    verify("${inputFile}_test", ::part2, 81)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
