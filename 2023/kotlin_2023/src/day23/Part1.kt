package day23


enum class Direction(val char: Char) {
    Top('^'), Right('>'), Bottom('v'), Left('<')
}

fun Direction.opposite() = when (this) {
    Direction.Top -> Direction.Bottom
    Direction.Right -> Direction.Left
    Direction.Bottom -> Direction.Top
    Direction.Left -> Direction.Right
}

data class Point(
    val x: Int, val y: Int
)

class Node(
    val point: Point,
    val edges: MutableList<Edge> = mutableListOf(),
)

class Edge(
    val distance: Int,
    val to: Node,
)

class Grid(
    val lines: List<String>,
    val nodes: MutableMap<Point, Node>,
    val visited: MutableSet<Point>,
) {
    private fun at(p: Point) = lines[p.y][p.x]

    private fun nextPoints(p: Point, fromDirection: Direction): Map<Direction, Point> = mapOf(
        Direction.Top to Point(p.x, p.y - 1),
        Direction.Right to Point(p.x + 1, p.y),
        Direction.Bottom to Point(p.x, p.y + 1),
        Direction.Left to Point(p.x - 1, p.y),
    ).filter { (direction, _) ->
        fromDirection != direction.opposite()
    }.filter { (direction, point) ->
        when (at(point)) {
            '.' -> true
            direction.char -> true
            else -> false
        }
    }

    fun walk(
        fromNode: Node,
        lastPoint: Point,
        distance: Int,
        lastDirection: Direction,
    ) {
        val isEnd = lastPoint.y == lines.lastIndex
        if (isEnd) {
            val node = nodes.getOrPut(lastPoint) { Node(lastPoint) }
            fromNode.edges.add(
                Edge(
                    distance = distance,
                    to = node
                )
            )

            visited.add(node.point)
            return
        }

        val next = nextPoints(lastPoint, lastDirection)
        val isNode = next.size > 1
//        println("current=$lastPoint dir=$lastDirection next=${next} isNode=$isNode")

        if (isNode) {
            val node = nodes.getOrPut(lastPoint) { Node(lastPoint) }
            fromNode.edges.add(
                Edge(
                    distance = distance,
                    to = node
                )
            )

            if (node.point !in visited) {
                visited.add(node.point)
                next.forEach { (direction, point) ->
                    walk(node, point, 1, direction)
                }
            }
            return
        } else {
            assert(next.size == 1)
            next.forEach { (direction, point) ->
                walk(fromNode, point, 1 + distance, direction)
            }
        }
    }
}

fun part1(lines: List<String>): Int {
    val start = Point(1, 0)
    val path = Point(1, 1)
    val startNode = Node(start)

    val grid = Grid(
        lines = lines,
        nodes = mutableMapOf(start to startNode),
        visited = mutableSetOf(start),
    )

    grid.walk(startNode, path, 1, Direction.Bottom)

    val maxDistance = dfs(startNode, 0)
//    println(grid.visited)
//    println(grid.nodes)

    return maxDistance
}

fun dfs(node: Node, distance: Int): Int {
    if (node.edges.isEmpty()) {
        return distance
    }

    return node.edges.maxOf {
        it.distance + dfs(it.to, distance)
    }
}
