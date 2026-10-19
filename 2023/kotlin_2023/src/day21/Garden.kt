package day21

data class Point(
    val i: Int,
    val j: Int,
) {
    fun getNeighbours(): Array<Point> {
        return arrayOf(
            Point(i + 1, j),
            Point(i - 1, j),
            Point(i, j + 1),
            Point(i, j - 1),
        )
    }
}

class Garden private constructor(
    private val grass: Array<Array<Boolean>>,
    val startingPoint: Point,
) {
    private val size: Int = 131

    fun isGrass(point: Point): Boolean {
        if (point.i < 0 || point.i >= size || point.j < 0 || point.j >= size) {
            return false
        }
        return grass[Math.floorMod(point.j, 131)][Math.floorMod(point.i, 131)]
    }

    fun getAllGrass(): List<Point> {
        return buildList {
            for (j in 0..<131) {
                for (i in 0..<131) {
                    val point = Point(i, j)
                    if (isGrass(point)) {
                        add(point)
                    }
                }
            }
        }
    }

    companion object {
        fun fromLines(lines: List<String>): Garden {
            var startingPoint: Point? = null
            for (j in lines.indices) {
                for (i in lines[j].indices) {
                    if (lines[j][i] == 'S') {
                        startingPoint = Point(i, j)
                    }
                }
            }

            val grass = lines.map { line ->
                line.map { it == '.' || it == 'S' }.toTypedArray()
            }.toTypedArray()

            return Garden(grass, startingPoint!!)
        }
    }
}
