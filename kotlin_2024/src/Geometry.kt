package com.thalkz

data class Point(
    val x: Int,
    val y: Int,
) {
    operator fun plus(p: Point) = Point(x+p.x, y+p.y)
    operator fun minus(p: Point) = Point(x-p.x, y-p.y)
}

val Point.adjacent: List<Point>
    get() = listOf(
        Point(x+1, y),
        Point(x, y+1),
        Point(x-1, y),
        Point(x, y-1),
    )

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
    fun at(p: Point) = lines[p.y][p.x]
    val points = sequence {
        for (j in 0..<height) {
            for (i in 0..<width) {
                yield(Point(i, j))
            }
        }
    }
}