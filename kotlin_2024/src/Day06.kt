package com.thalkz

import java.lang.Exception

private data class Guard(
    var point: Point,
    var dir: Direction,
)

private fun List<String>.findGuard(): Guard {
    for (j in 0..lastIndex) {
        for (i in 0..this[j].lastIndex) {
            if (this[j][i] == '^') return Guard(Point(i, j), Direction.Up)
        }
    }
    throw Exception("guard not found ?!")
}

private enum class Cell {
    Outside,
    Empty,
    Rock,
}

private fun List<String>.at(point: Point) = when {
    point.x < 0 || point.x >= this[0].length || point.y < 0 || point.y >= this.size -> Cell.Outside
    this[point.y][point.x] == '#' -> Cell.Rock
    else -> Cell.Empty
}

private fun Point.move(dir: Direction) = when (dir) {
        Direction.Up -> Point(x, y-1)
        Direction.Right -> Point(x+1, y)
        Direction.Down -> Point(x, y+1)
        Direction.Left -> Point(x-1, y)
    }

private fun Guard.walkUphill(grid: List<String>, obstacle: Point? = null): Boolean {
    val pointInFront = point.move(dir)
    val cell = if (obstacle == pointInFront) {
        Cell.Rock
    } else {
        grid.at(pointInFront)
    }
    return when (cell) {
        Cell.Empty -> {
            point = pointInFront
            true
        }
        Cell.Rock -> {
            dir = dir.rotateRight()
            true
        }
        Cell.Outside -> false
    }
}

fun main() {
    fun part1(grid: List<String>): Int {
        val walked = mutableSetOf<Point>()
        val guard = grid.findGuard()
        walked.add(guard.point)
        while (guard.walkUphill(grid)) {
            walked.add(guard.point)
        }
        return walked.size
    }

    fun part2(grid: List<String>): Int {
        val walked = mutableSetOf<Point>()
        val firstGuard = grid.findGuard()
        walked.add(firstGuard.point)
        while (firstGuard.walkUphill(grid)) {
            walked.add(firstGuard.point)
        }

        var result = 0
        val guard = grid.findGuard()
        for (obstacle in walked) {
            if (grid.isLooping(guard.copy(), obstacle)) {
                result++
            }
        }

        return result
    }

    val inputFile = "Day06"
    verify("${inputFile}_test", ::part1, 41)
    verify("${inputFile}_test", ::part2, 6)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}

private fun List<String>.isLooping(guard: Guard, obstacle: Point): Boolean {
    val walked = mutableSetOf<Pair<Point, Direction>>()
    while (guard.walkUphill(this, obstacle)) {
        if (guard.point to guard.dir in walked) {
            return true
        }
        walked.add(guard.point to guard.dir)
    }
    return false
}
