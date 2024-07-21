package day25

class Node(
    val name: String,
    val edges: MutableSet<Edge> = mutableSetOf(),
) {
    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Node && name.equals(other.name)
    }

    override fun toString() = "[" + name + ":" + edges.joinToString() + "]"
}

data class Edge(
    val from: Node,
    val to: Node,
) {
    override fun toString() = "${from.name}-${to.name}"

    fun take(start: Node) : Node {
        return if (from == start) {
            to
        } else if (to == start) {
            from
        } else {
            throw Exception("wut")
        }
    }
}


val nodes = mutableMapOf<String, Node>()
val markedEdges = mutableMapOf<Edge, Int>()

fun getOrCreateNode(name: String): Node {
    if (name !in nodes) {
        nodes[name] = Node(name)
    }
    return nodes[name]!!
}

fun link(a: Node, b: Node) {
    val edge = if (a.name < b.name) {
        Edge(a, b)
    } else {
        Edge(b, a)
    }
    a.edges.add(edge)
    b.edges.add(edge)
}

fun bfs(start: Node): Int {
    val visited = mutableSetOf<Node>()
    val queue = mutableListOf<Node>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val node = queue.removeAt(0)
        node.edges.forEach {
            val dst = it.take(start = node)
            if (dst !in visited) {
                visited.add(dst)
                markedEdges[it] = 1 + ( markedEdges[it] ?: 0)
                queue.add(dst)
            }
        }
    }
    return visited.size
}

internal fun part1(lines: List<String>): Int {
    lines.forEach { line ->
        val tokens = line.split(": ")
        val from = getOrCreateNode(tokens[0])
        tokens[1]
            .split(" ")
            .map { getOrCreateNode(it) }
            .forEach { link(from, it) }
    }

    println("nodes = ${nodes.values.size}")

    for (i in 0..2) {
        for (node in nodes.values) {
            bfs(node)
        }
        val maxEdge = markedEdges.maxBy { it.value }.key
        maxEdge.from.edges.remove(maxEdge)
        maxEdge.to.edges.remove(maxEdge)
        markedEdges.clear()
        println(maxEdge)
    }

    val total = nodes.size
    val g1 = bfs(nodes.values.first())
    return g1 * (total - g1)
}
