package day23

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

    return Solver(lines.lastIndex)
        .search(startNode, 0, setOf(startNode.point))
}

class Solver(private val lastIndexRow: Int) {
    private val bestPaths = mutableMapOf<String, Int>()

    private fun isEnd(p: Point) = p.y == lastIndexRow

    private fun toKey(path: Set<Point>, end: Point) = path.map { it.toString() }.sorted().joinToString() + end.toString()

    fun search(node: Node, distance: Int, trail: Set<Point>): Int {
        if (isEnd(node.point)) {
            return distance
        }

        return node.edges.maxOf {edge ->
            if (edge.to.point in trail) {
                return@maxOf 0
            }

            val newDistance = edge.distance + distance
            val newTrail = trail + edge.to.point
            val key = toKey(newTrail, edge.to.point)

            if (newDistance > bestPaths.getOrDefault(key, defaultValue = 0)) {
                bestPaths[key] = newDistance
                search(edge.to, newDistance, newTrail)
            } else {
                0
            }
        }
    }
}
