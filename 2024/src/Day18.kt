package com.thalkz

import java.lang.Exception
import java.util.LinkedList
import java.util.Queue

private class Step(
    val point: Point,
    val count: Int,
)

private fun bfs(size: Size, corrupted: Set<Point>): Int {
    val target = Point(size.width-1, size.height-1)
    val next: Queue<Step> = LinkedList()
    val visited = mutableSetOf<Point>()
    next.offer(Step(Point(0, 0), 0))
    visited.add(Point(0, 0))
    while (next.isNotEmpty() && next.peek().point != target) {
        val step = next.poll()
        step.point.adjacent.values.forEach { adj ->
            if (adj in size && adj !in corrupted && adj !in visited) {
                next.offer(Step(adj, step.count+1))
                visited.add(adj)
            }
        }
    }
    if (next.isEmpty()) {
        return -1
    }
    return next.peek().count
}

fun main() {
    fun parseInput(lines: List<String>): List<Point> {
        return lines.map {
            val parts = it.split(",")
            Point(
                x = parts[0].toInt(),
                y = parts[1].toInt()
            )
        }
    }

    fun part1(lines: List<String>): Int {
        val corrupted = parseInput(lines).take(1024).toSet()
        val size = Size(71, 71)
        return bfs(size, corrupted)
    }

    fun part2(lines: List<String>): String {
        val corrupted = parseInput(lines)
        val size = Size(71, 71)

        for (i in corrupted.indices) {
            val steps = bfs(size, corrupted.take(i).toSet())
            if (steps == -1) {
                val (x, y) = corrupted[i-1]
                return "$x,$y"
            }
        }
        throw Exception("not found")
    }

    val inputFile = "Day18"
//    verify("${inputFile}_test", ::part1, 22)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
