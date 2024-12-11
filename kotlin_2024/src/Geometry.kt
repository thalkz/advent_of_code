package com.thalkz

data class Point(
    val x: Int,
    val y: Int,
) {
    operator fun plus(p: Point) = Point(x+p.x, y+p.y)
    operator fun minus(p: Point) = Point(x-p.x, y-p.y)
}

operator fun Int.times(p: Point) = Point(this * p.x, this * p.y)

data class Size(
    val width: Int,
    val height: Int,
) {
    operator fun contains(p: Point) = p.x in 0..<width && p.y in 0..<height
}