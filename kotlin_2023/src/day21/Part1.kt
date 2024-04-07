package day21

import kotlin.math.abs

fun part1(lines: List<String>, steps: Int): Int {
    val garden = Garden.fromLines(lines)

    val visited = mutableSetOf(garden.startingPoint)
    var current = setOf(garden.startingPoint)
    for (i in 1..steps) {
        val next = mutableSetOf<Point>()
        for (point in current) {
            for (neighbour in point.getNeighbours()) {
                if (garden.isGrass(neighbour) && neighbour !in visited) {
                    next.add(neighbour)
                }
            }
        }

        current = next
        visited.addAll(next)
//        garden.print(next)
    }

    val pair = steps % 2
    val filtered = visited.filter { abs(it.i - it.j) % 2 == pair }
    return filtered.size
}