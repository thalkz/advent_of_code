package com.thalkz

import java.lang.Exception

private class WarehousePart2(
    var robot: Point,
    val walls: Set<Point>,
    val boxes: MutableSet<Point>,
    val instructions: List<Direction>,
) {

    fun Point.isWall() = this in walls
    fun Point.isBox() = this in boxes || adjacent(Direction.Left) in boxes

    fun moveRobot(direction: Direction) {
        val point = robot.adjacent(direction)
        val success = when {
            point.isWall() -> false
            point.isBox() -> point.canPush(direction)
            else -> true
        }
        if (success) {
            if (point.isBox()) {
                point.push(direction)
            }
            robot = point
        }
    }

    fun Point.toBoxOrigin() = if (this !in boxes) {
            adjacent(Direction.Left)
        } else if (this in boxes) {
            this
        } else {
            throw Exception("$this is not a box")
    }

    fun Point.adjacentToBox(direction: Direction): List<Point> {
        return when (direction){
            Direction.Up -> listOf(Point(x, y-1), Point(x+1, y-1))
            Direction.Right -> listOf(Point(x+2, y))
            Direction.Down -> listOf(Point(x, y+1), Point(x+1, y+1))
            Direction.Left -> listOf(Point(x-1, y))
        }
    }

    fun Point.push(direction: Direction)  {
        val boxOrigin = toBoxOrigin()
        val points = boxOrigin.adjacentToBox(direction)

        points
            .filter { it.isBox() }
            .map { it.toBoxOrigin() }
            .toSet()
            .forEach { point ->
                point.push(direction)
            }

        boxes.remove(boxOrigin)
        boxes.add(boxOrigin.adjacent(direction))
    }

    fun Point.canPush(direction: Direction): Boolean {
        val boxOrigin = toBoxOrigin()
        val points = boxOrigin.adjacentToBox(direction)

        return points.all { p ->
            when {
                p.isWall() -> false
                p.isBox() -> p.canPush(direction)
                else -> true
            }
        }
    }

    fun println() {
        val size = Size(
            width = walls.maxBy { it.x }.x + 1,
            height = walls.maxBy { it.y }.y + 1,
        )
        for (j in 0 until size.height) {
            val line = (0 until size.width).map { Point(it,j) }.joinToString(separator = "") {
                when {
                    it == robot -> "@"
                    it in walls -> "#"
                    it in boxes -> "["
                    it.adjacent(Direction.Left) in boxes -> "]"
                    else -> "."
                }
            }
            println(line)
        }
        println()
    }
}

fun main() {
    fun parseInput(lines: List<String>): WarehousePart2 {
        var robot: Point? = null
        val walls =  mutableSetOf<Point>()
        val boxes = mutableSetOf<Point>()
        var j = 0
        while(lines[j].isNotEmpty()) {
            for (i in 0..lines[j].lastIndex) {
                when (lines[j][i]) {
                    '#' -> {
                        walls.add(Point(i*2, j))
                        walls.add(Point((i*2)+1, j))
                    }
                    'O' -> boxes.add(Point(i*2, j))
                    '@' -> robot = Point(i*2, j)
                    else -> {}
                }
            }
            j++
        }

        val instructions = mutableListOf<Direction>()
        while (j < lines.size) {
            for (i in 0..lines[j].lastIndex) {
                when (lines[j][i]) {
                    '^' -> instructions.add(Direction.Up)
                    '>' -> instructions.add(Direction.Right)
                    '<' -> instructions.add(Direction.Left)
                    'v' -> instructions.add(Direction.Down)
                }
            }
            j++
        }

        return WarehousePart2(
            robot = robot!!,
            boxes = boxes,
            walls = walls,
            instructions = instructions,
        )
    }

    fun part2(lines: List<String>): Int {
        val warehouse = parseInput(lines)
//        warehouse.println()

        warehouse.instructions
            .forEach {
                warehouse.moveRobot(it)
//                warehouse.println()
            }
        return warehouse.boxes
            .sumOf { it.x + 100 * it.y }
    }

    val inputFile = "Day15"
    verify("${inputFile}_test", ::part2, 9021)

    solvePart2(inputFile, ::part2)
}
