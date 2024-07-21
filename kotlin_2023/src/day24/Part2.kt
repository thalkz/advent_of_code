package day24

import kotlin.math.roundToLong

data class Point(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    operator fun plus(other: Point): Point {
        return Point(
            x + other.x,
            y + other.y,
            z + other.z,
        )
    }

    operator fun minus(other: Point): Point {
        return Point(
            x - other.x,
            y - other.y,
            z - other.z,
        )
    }

    operator fun div(value: Double): Point {
        return Point(
            x / value,
            y / value,
            z / value,
        )
    }

    operator fun times(p: Point): Double {
        return (x * p.x) + (y * p.y) + (z * p.z)
    }

    fun cross(p: Point): Point {
        return Point(
            x = y*p.z - z*p.y,
            y = -x*p.z + z*p.x,
            z = x*p.y - y*p.x,
        )
    }
}

operator fun Double.times(p: Point): Point {
    return Point(
        this * p.x,
        this * p.y,
        this * p.z,
    )
}

data class Trajectory(
    val name: String,
    val p: Point,
    val v: Point,
) {
    fun at(t: Double) = Point(
        x = p.x + t * v.x,
        y = p.y + t * v.y,
        z = p.z + t * v.z,
    )
}

fun part2(lines: List<String>): Long  {
    val hailstones = lines.mapIndexed { index, str ->
        val name = 'A' + index
        val parts = str.split("@")
        val left = parts[0].split(",")
        val start = Point(
            left[0].trim().toDouble(),
            left[1].trim().toDouble(),
            left[2].trim().toDouble(),
        )
        val right = parts[1].split(",")
        val velocity = Point(
            right[0].trim().toDouble(),
            right[1].trim().toDouble(),
            right[2].trim().toDouble(),
        )
        Trajectory(
            name = name.toString(),
            p = start,
            v = velocity,
        )
    }

    val originIndex = 4
    val index1 = 1
    val index2 = 2

    val origin = hailstones[originIndex]

    val hailstonesFromOrigin = hailstones.map {
        Trajectory(
            name = it.name + "_0",
            p = it.p - origin.p,
            v = it.v - origin.v,
        )
    }

    val s1 = hailstonesFromOrigin[index1]
    val s2 = hailstonesFromOrigin[index2]
    val p1 = s1.p
    val v1 = s1.v
    val p2 = s2.p
    val v2 = s2.v

    val t1 = -((p1.cross(p2)) * v2) / ((v1.cross(p2)) * v2)
    println("t1=$t1")
    val t2 = -((p1.cross(p2)) * v1) / ((p1.cross(v2)) * v1)
    println("t2=$t2")

    println("hit hailstone $index1 at t=$t1 point=${hailstones[index1].at(t1)}")
    println("hit hailstone $index2 at t=$t2 point=${hailstones[index2].at(t2)}")

    val velocity = (hailstones[index2].at(t2) - hailstones[index1].at(t1)) / (t2 - t1)
    println("velocity=$velocity")
    val start = hailstones[index1].at(t1) - t1 * velocity
    println("start=$start")

    return (start.x + start.y + start.z).roundToLong()
}
