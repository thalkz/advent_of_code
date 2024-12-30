package com.thalkz

import java.util.LinkedList
import java.util.Queue
import kotlin.math.absoluteValue

private data class RaceStep(
    val point: Point,
    val time: Int,
)

private data class RaceTrack(
    val grid: Grid,
    val start: Point,
    val end: Point,
) {
    val timeToEnd = computeTimeToEnd()

    private fun computeTimeToEnd(): Map<Point, Int> {
        val result = mutableMapOf<Point, Int>()
        val queue: Queue<RaceStep> = LinkedList()
        queue.offer(RaceStep(end, 0))

        while (queue.isNotEmpty()) {
            val (point, time) = queue.poll()
            if (point in result) continue
            result[point] = time
            point.adjacent.values
                .filter { grid[it] != '#' }
                .forEach { queue.offer(RaceStep(it, time+1)) }
        }
        return result
    }

    private fun RaceStep.cheat(cheatTime: Int) = buildList {
        for (i in -cheatTime..cheatTime) {
            for (j in -cheatTime..cheatTime) {
                val timeTaken = i.absoluteValue + j.absoluteValue
                if (timeTaken > cheatTime) continue
                val endPoint = point + Point(i, j)
                add(RaceStep(
                    point = endPoint,
                    time = time + timeTaken
                ))
            }
        }
    }

    fun findValidCheats(cheatTime: Int, minTimeToSave: Int): Int {
        val maxTime = timeToEnd[start]!! - minTimeToSave
        var validCheats = 0
        val visited = mutableSetOf<Point>()
        val queue: Queue<RaceStep> = LinkedList()

        queue.offer(RaceStep(start, 0))

        while (queue.isNotEmpty()) {
            val step = queue.poll()
            if (step.point in visited) continue
            visited += step.point

            validCheats += step.cheat(cheatTime)
                .filter { it.point in grid && grid[it.point] != '#' }
                .count { it.time + timeToEnd[it.point]!! <= maxTime }

            step.point.adjacent.values
                .filter { grid[it] != '#' }
                .forEach { queue.offer(RaceStep(it, step.time+1)) }
        }
        return validCheats
    }
}

fun main() {
    fun parseInput(lines: List<String>): RaceTrack {
        val grid = Grid(lines)
        return RaceTrack(
            grid = grid,
            start = grid.points.find { grid[it] == 'S' }!!,
            end = grid.points.find { grid[it] == 'E' }!!,
        )
    }

    fun part1(lines: List<String>): Int {
        val raceTrack = parseInput(lines)
        return raceTrack.findValidCheats(2, 100)
    }

    fun part2(lines: List<String>): Int {
        val raceTrack = parseInput(lines)
        return raceTrack.findValidCheats(20, 100)
    }

    val inputFile = "Day20"
    
    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
