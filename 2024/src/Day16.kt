package com.thalkz

import java.util.Collections.min
import java.util.LinkedList
import java.util.Queue

private data class MazePath(
    val score: Int,
    val point: Point,
    val direction: Direction,
    val trail: List<Point>,
)

private class Maze(
    val grid: Grid,
    val start: Point,
    val end: Point,
) {
    val queue: Queue<MazePath> = LinkedList()
    val visited = mutableMapOf<Pair<Point, Direction>, Int>()
    val paths = mutableListOf<MazePath>()

    fun dijkstra(): List<MazePath> {
        queue.offer(MazePath(0, start, Direction.Right, mutableListOf(start)))
        while (queue.isNotEmpty()) {
            val path = queue.poll()
            if (path.point == end) {
                paths.add(path)
                continue
            }
            val current = path.point to path.direction
            if ((visited[current] ?: Int.MAX_VALUE) < path.score) continue
            visited[current] = path.score

            val forward = path.point.adjacent(path.direction)
            if (grid[forward] != '#') {
                queue.offer(MazePath(path.score + 1, forward, path.direction, path.trail+forward))
            }
            queue.offer(MazePath(path.score + 1000, path.point, path.direction.rotateLeft(), path.trail))
            queue.offer(MazePath(path.score + 1000, path.point, path.direction.rotateRight(), path.trail))
        }
        return paths
    }
}

fun main() {
    fun parseInput(lines: List<String>): Maze {
        val grid = Grid(lines)
        return Maze(
            grid = grid,
            start = grid.points.first { grid[it] == 'S' },
            end = grid.points.first { grid[it] == 'E' },
        )
    }

    fun part1(lines: List<String>): Int {
        val maze = parseInput(lines)
        return maze.dijkstra().minOf { it.score }
    }

    fun part2(lines: List<String>): Int {
        val maze = parseInput(lines)
        val paths = maze.dijkstra()
        val bestScore = paths.minOf { it.score }
        val bestPoints = paths.filter { it.score == bestScore }
            .fold(mutableSetOf<Point>()) { acc, path ->
                acc.apply { addAll(path.trail) }
            }
//        maze.grid.println {
//            if (it in bestPoints) "0" else maze.grid[it].toString()
//        }
        return bestPoints.size
    }

    val inputFile = "Day16"
    verify("${inputFile}_test", ::part1, 7036)
    verify("${inputFile}_test", ::part2, 45)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
