package com.thalkz

private class Warehouse(
    var robot: Point,
    val walls: Set<Point>,
    val boxes: MutableSet<Point>,
    val instructions: List<Direction>,
) {

    fun moveRobot(direction: Direction) {
//        println("move $direction")
        val movedRobot = robot.push(direction) ?: return
        boxes.remove(movedRobot)
        robot = movedRobot
    }

    // returns null if cannot be pushed
    fun Point.push(direction: Direction): Point? {
        return when (val point = adjacent(direction)) {
            in walls -> null

            in boxes -> {
                val moved = point.push(direction)
                if (moved == null) {
                    null
                } else {
                    boxes.add(moved)
                    point
                }
            }
            else -> point
        }
    }

    fun println() {
        val size = Size(
            width = walls.maxBy { it.x }.x +1 ,
            height = walls.maxBy { it.y }.y+1,
        )
        for (j in 0 until size.height) {
            val line = (0 until size.width).map { Point(it,j) }.joinToString(separator = "") {
                when (it) {
                    robot -> "@"
                    in walls -> "#"
                    in boxes -> "0"
                    else -> "."
                }
            }
            println(line)
        }
        kotlin.io.println()
    }
}

fun main() {
    fun parseInput(lines: List<String>): Warehouse {
        var robot: Point? = null
        val walls =  mutableSetOf<Point>()
        val boxes = mutableSetOf<Point>()
        var j = 0
        while(lines[j].isNotEmpty()) {
            for (i in 0..lines[j].lastIndex) {
                when (lines[j][i]) {
                    '#' -> walls.add(Point(i, j))
                    'O' -> boxes.add(Point(i, j))
                    '@' -> robot = Point(i, j)
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

        return Warehouse(
            robot = robot!!,
            boxes = boxes,
            walls = walls,
            instructions = instructions,
        )
    }

    fun part1(lines: List<String>): Int {
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

    fun part2(lines: List<String>): Int {
        val input = parseInput(lines)
        return 0
    }

    val inputFile = "Day15"
    verify("${inputFile}_test", ::part1, 10092)
    verify("${inputFile}_test", ::part2, 9021)

    solvePart1(inputFile, ::part1)
//    solvePart2(inputFile, ::part2)
}
