package com.thalkz

private class Robot(
    val p: Point,
    val v: Point,
) {
    fun at(size: Size, t: Int) = Point(
        (p.x + t * v.x).mod(size.width),
        (p.y + t * v.y).mod(size.height),
    )
}

private fun List<Robot>.computeAt(size: Size, t: Int) : Map<Point, Int> {
   return map { it.at(size, t) }.groupingBy { it }.eachCount()
}

private fun String.toPoint(): Point {
    val split = split(",")
    return Point(
        x = split[0].toInt(),
        y = split[1].toInt(),
    )
}

fun Point.isTopLeft(size: Size) = x < size.width/2 && y < size.height/2
fun Point.isTopRight(size: Size) = x > size.width/2 && y < size.height/2
fun Point.isBottomLeft(size: Size) = x < size.width/2 && y > size.height/2
fun Point.isBottomRight(size: Size) = x > size.width/2 && y > size.height/2

fun main() {
    fun parseInput(lines: List<String>): List<Robot> {
        return lines.map {
            val parts = it.substring(2).split(" v=")
            Robot(
                p = parts[0].toPoint(),
                v = parts[1].toPoint()
            )
        }
    }

    fun printGrid(size: Size, points: Map<Point, Int>) {
        for (j in 0 until size.height) {
            val line = (0 until size.width).map { Point(it,j) }.joinToString(separator = "") { points[it]?.toString() ?: "." }
            println(line)
        }
        println()
    }

    fun part1(lines: List<String>): Int {
        val size = Size(101, 103)
        val robots = parseInput(lines)

        val points = robots.computeAt(size , t=100)
//        println(points)
//        printGrid(size, points)
       val a = points.entries.sumOf { (p, count) -> if (p.isTopLeft(size)) count else 0 }
        val b = points.entries.sumOf { (p, count) -> if (p.isTopRight(size)) count else 0 }
        val c = points.entries.sumOf { (p, count) -> if (p.isBottomLeft(size)) count else 0 }
        val d = points.entries.sumOf { (p, count) -> if (p.isBottomRight(size)) count else 0 }
        return a * b * c * d
    }

    fun part2(lines: List<String>): Int {
        val size = Size(101, 103)
        val robots = parseInput(lines)
        for (t in 0.. 10000) {
            val points = robots.computeAt(size , t)
            val score = points.keys.sumOf { point ->
                point.adjacent.values.count { ajd -> ajd in points }
            }
            if (score > 500) {
                println("t=$t")
                printGrid(size, points)
            }
        }
        return 0
    }

    val inputFile = "Day14"
//    verify("${inputFile}_test", ::part1, 12)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
