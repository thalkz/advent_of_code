package day24

import kotlin.math.sign

private data class Point2D(
    val x: Double,
    val y: Double,
)

private data class Formula(
    val p: Point2D,
    val dp: Point2D,
    val a: Double,
    val b: Double,
)

internal fun part1(lines: List<String>, min: Double, max: Double): Int {
    val pairs = lines.map {
        val parts = it.split("@")
        val left = parts[0].split(",")
        val p0 = Point2D(left[0].trim().toDouble(), left[1].trim().toDouble())
        val right = parts[1].split(",")
        val dp0 = Point2D(right[0].trim().toDouble(), right[1].trim().toDouble())
        p0 to dp0
    }

    val formulas = pairs.map { (p0, dp0) ->
        val b = p0.y + (-p0.x / dp0.x) * dp0.y
        val a = -b / (p0.x + (-p0.y / dp0.y) * dp0.x)

        Formula(
            p = p0,
            dp = dp0,
            a = a,
            b = b,
        )
    }

    var result = 0
    for (i in 0..formulas.lastIndex) {
        for (j in (i+1)..formulas.lastIndex) {
            val intersection = formulas[i].intersect(formulas[j])
            if (!formulas[i].isFuture(intersection) || !formulas[j].isFuture(intersection)) {
//                println("intersect in the past")
            } else if (!intersection.isInside(min, max)) {
//                println("intersect is outside the box")
            } else {
                result++
            }
        }
    }
    return result
}

private fun Formula.intersect(other: Formula): Point2D {
    return Point2D(
        x = -(b - other.b) / (a - other.a),
        y = (a * -(b - other.b) / (a - other.a)) + b,
    )
}

private fun Formula.isFuture(intersect: Point2D) : Boolean {
    return dp.x.sign == (intersect.x - p.x).sign || dp.y.sign == (intersect.y - p.y).sign
}

private fun Point2D.isInside(min: Double, max: Double) : Boolean {
    return x in min..max && y in min..max
}