package day21

// Garden is 131x131
// 26501365 = 65 + 131 * 202300
// n = 202300

fun part2(lines: List<String>): Long {
    val garden = Garden.fromLines(lines)

    val visited = mutableSetOf(garden.startingPoint)
    var current = setOf(garden.startingPoint)
    val visitedWithDistance = mutableListOf<Pair<Point, Int>>()
    visitedWithDistance.add(garden.startingPoint to 0)

    for (i in 1..200) {
        val next = mutableSetOf<Point>()
        for (point in current) {
            for (neighbour in point.getNeighbours()) {
                if (garden.isGrass(neighbour) && neighbour !in visited && neighbour !in next) {
                    next.add(neighbour)
                    visitedWithDistance.add(neighbour to i)
                }
            }
        }

        current = next
        visited.addAll(next)
    }

    val fullEven = visitedWithDistance.count { (it.first.i + it.first.j) % 2 == 0 }
    val fullOdd = visitedWithDistance.count { (it.first.i + it.first.j) % 2 == 1 }
    println("fullEven=${fullEven}")
    println("fullOdd=${fullOdd}")


    val cornerEven = visitedWithDistance.count { (it.first.i + it.first.j) % 2 == 0 && it.second > 65 }
    val cornerOdd = visitedWithDistance.count { (it.first.i + it.first.j) % 2 == 1 && it.second > 65 }
    println("cornerEven=${cornerEven}")
    println("cornerOdd=${cornerOdd}")

    val n = 202300L
    val oddTiles = (n + 1) * (n + 1)
    val evenTiles = n * n
    println("oddTiles=$oddTiles")
    println("evenTiles=$evenTiles")

    return oddTiles * fullOdd + evenTiles * fullEven - (n + 1) * cornerOdd + n * cornerEven
}
