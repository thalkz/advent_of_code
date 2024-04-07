package day21

data class Point(
    val i: Int,
    val j: Int,
) {
    fun getNeighbours(): Array<Point> {
        return arrayOf(
            Point(i + 1, j),
            Point(i - 1, j),
            Point(i, j+1),
            Point(i, j-1),
        )
    }
}


class Garden private constructor(
    private val grass: Array<Array<Boolean>>,
    val startingPoint: Point,
) {
    val height: Int = grass.size
    val width: Int = grass.first().size

    fun isGrass(point: Point): Boolean {
        return grass[point.j][point.i]
    }

    fun print(points: Set<Point>) {
        for (j in 0..<height) {
            for (i in 0..<width) {
                val point = Point(i,j)
                print(
                    when {
                        point in points -> "0"
                        isGrass(point) -> "."
                        else -> "#"
                    }
                )
            }
            println()
        }
        println()
    }

    companion object {
        fun fromLines(lines: List<String>): Garden {
            var startingPoint: Point? = null
            for (j in lines.indices) {
                for (i in lines[j].indices) {
                    if (lines[j][i] == 'S') {
                        startingPoint = Point(i,j)
                    }
                }
            }

            val grass = lines
                .map { line ->
                    line.map { it == '.' || it == 'S' }.toTypedArray()
                }
                .toTypedArray()

            return Garden(grass, startingPoint!!)
        }
    }
}