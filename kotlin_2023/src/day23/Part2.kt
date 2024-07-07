package day23

import java.util.PriorityQueue

class Part2Grid(
    val lines: List<String>
) {
    val allNodes = mutableMapOf<Point, Node>()

    private val visited = mutableSetOf<Node>()

    private fun getOrPutNode(p: Point) = allNodes.getOrPut(p) { Node(p) }

    private fun pointsAt(p: Point) = lines[p.y][p.x]

    private fun isEnd(p: Point) = p.y == lines.lastIndex

    private fun findNextPaths(p: Point, fromDirection: Direction): Map<Direction, Point> = mapOf(
        Direction.Top to Point(p.x, p.y - 1),
        Direction.Right to Point(p.x + 1, p.y),
        Direction.Bottom to Point(p.x, p.y + 1),
        Direction.Left to Point(p.x - 1, p.y),
    ).filter { (direction, point) ->
        fromDirection != direction.opposite() && pointsAt(point) != '#'
    }

    fun buildGraph(
        fromNode: Node,
        fromDirection: Direction,
    ) {
        visited.add(fromNode)

        if (isEnd(fromNode.point)) {
            return
        }

        val paths = findNextPaths(fromNode.point, fromDirection)

        for (startingPath in paths) {
            var (lastDirection, lastPoint) = startingPath
            var next = findNextPaths(lastPoint, lastDirection)
            var distance = 1
            while (next.size == 1) {
                distance++
                val (dir, point) = next.entries.toList()[0]
                lastPoint = point
                lastDirection = dir
                if (isEnd(point)) {
                    break
                }

                next = findNextPaths(point, dir)
            }

            val toNode = getOrPutNode(lastPoint)
            fromNode.edges.add(Edge(distance, toNode))
            toNode.edges.add(Edge(distance, fromNode))

            if (toNode !in visited) {
                buildGraph(toNode, lastDirection)
            }
        }
    }
}

fun part2(lines: List<String>): Int {
    val startNode = Node(Point(1, 0))

    Part2Grid(lines).apply {
        buildGraph(startNode, Direction.Bottom)
        println("nodes = ${allNodes.size}")
    }

    return Solver(
        lines.lastIndex,
        mutableSetOf(startNode.point)
    ).dfsWithVisited(startNode, 0)
}

class Solver(
    private val lastIndexRow: Int,
    private val visited: MutableSet<Point>
) {
    var max = 0

    private fun isEnd(p: Point) = p.y == lastIndexRow

    fun dfsWithVisited(node: Node, totalDistance: Int): Int {
        if (isEnd(node.point)) {
            if (totalDistance > max) {
                max = totalDistance
                println("dist=$totalDistance, visited=${visited.size}")
            }
            return totalDistance
        }

       return node.edges.maxOf { edge ->
            if (edge.to.point !in visited) {
                visited.add(edge.to.point)
                dfsWithVisited(edge.to, edge.distance + totalDistance).also {
                    visited.remove(edge.to.point)
                }
            } else {
                0
            }
        }
    }
}