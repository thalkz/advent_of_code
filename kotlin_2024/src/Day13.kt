package com.thalkz

data class PointLong(
    val x: Long,
    val y: Long,
) {
    operator fun plus(p: PointLong) = PointLong(x+p.x, y+p.y)
    operator fun minus(p: PointLong) = PointLong(x-p.x, y-p.y)
}

private data class ClawMachine(
    val a: PointLong,
    val b: PointLong,
    val p: PointLong,
) {
    fun countTokens() : Long {
        val u = (p.y*a.x-p.x*a.y)
        val v = (b.y*a.x-b.x*a.y)
        if (u % v != 0L) return 0L
        val bTokens = u / v
        val s = p.x - bTokens*b.x
        val t = a.x
        if (s % t != 0L) return 0L
        val aTokens = s / t
        return 3L*aTokens + bTokens
    }
}

fun main() {
    val buttonARegex = Regex("\\d+")

    fun parsePoint(line: String): PointLong {
        val values = buttonARegex.findAll(line).map { it.value.toLong() }.toList()
        return PointLong(values[0], values[1])
    }

    fun parseInput(lines: List<String>, additionalPrice: Long = 0L): List<ClawMachine> {
        val result = mutableListOf<ClawMachine>()
        for (i in 0..lines.lastIndex step 4) {
            result.add(
                ClawMachine(
                    a = parsePoint(lines[i]),
                    b = parsePoint(lines[i+1]),
                    p = parsePoint(lines[i+2]) + PointLong(additionalPrice, additionalPrice),
                )
            )
        }
        return result
    }

    fun part1(lines: List<String>): Long {
        val clawMachines = parseInput(lines)
        return clawMachines
            .sumOf { it.countTokens() }
    }

    fun part2(lines: List<String>): Long {
        val clawMachines = parseInput(lines, additionalPrice = 10000000000000L)
        return clawMachines
            .sumOf { it.countTokens() }
    }

    val inputFile = "Day13"
    verify("${inputFile}_test", ::part1, 480)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
