package com.thalkz

data class Point(
    val x: Int,
    val y: Int,
) {
    operator fun plus(p: Point) = Point(x+p.x, y+p.y)
    operator fun minus(p: Point) = Point(x-p.x, y-p.y)
}

val Point.adjacent: Map<Direction, Point>
    get() = mapOf(
        Direction.Right to Point(x+1, y),
        Direction.Down to  Point(x, y+1),
        Direction.Left to Point(x-1, y),
        Direction.Up to Point(x, y-1),
    )

fun Point.adjacent(direction: Direction) = when (direction) {
    Direction.Right -> Point(x+1, y)
    Direction.Down -> Point(x, y+1)
    Direction.Left -> Point(x-1, y)
    Direction.Up -> Point(x, y-1)
}

enum class Direction {
    Up,
    Right,
    Down,
    Left,
}

fun Direction.rotateRight() = Direction.entries[(ordinal + 1) % Direction.entries.size]

fun Direction.rotateLeft() = Direction.entries[(ordinal + 3) % Direction.entries.size]

operator fun Int.times(p: Point) = Point(this * p.x, this * p.y)

data class Size(
    val width: Int,
    val height: Int,
) {
    operator fun contains(p: Point) = p.x in 0..<width && p.y in 0..<height
}

class Grid(private val lines: List<String>) {
    private val size = Size(lines[0].length, lines.size)

    val width get() = size.width

    val height get() = size.height

    operator fun contains(p: Point) = size.contains(p)

    operator fun get(p: Point) = lines[p.y][p.x]

    val points = sequence {
        for (j in 0..<height) {
            for (i in 0..<width) {
                yield(Point(i, j))
            }
        }
    }

    fun println() = println { this[it].toString() }

    fun println(callback: (Point) -> String) {
        for (j in 0..<height) {
            val line = (0..<width).joinToString(separator = "") { callback(Point(it, j)) }
            println(line)
        }
    }
}