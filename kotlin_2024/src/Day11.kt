package com.thalkz

private typealias Stone = ULong

private fun Stone.countDigits(): Int {
    var current = this
    var count = 0
    while (current > 0UL) {
        count++
        current /= 10UL
    }
    return count
}

private fun ULong.pow(x: Int): ULong {
    if (x == 0) {
        return 1UL
    } else {
        var result = this
        repeat(x-1) {
            result *= this
        }
        return result
    }
}

private fun Stone.secondHalf(digits: Int) = this % 10UL.pow(digits/2)

private  fun Stone.firstHalf(digits: Int) = this / 10UL.pow(digits/2)

fun Map<Stone, ULong>.blink(): Sequence<Pair<Stone, ULong>> = sequence {
    for ((stone, count) in iterator()) {
        if (stone == 0UL) {
            yield(1UL to count)
        } else {
            val digitsCount = stone.countDigits()
            if (digitsCount%2 == 0) {
                yield(stone.firstHalf(digitsCount) to count)
                yield(stone.secondHalf(digitsCount)  to count)
            } else {
                yield(stone*2024UL  to count)
            }
        }
    }
}

fun Map<Stone, ULong>.blinkTimes(times: Int): Map<Stone, ULong> {
    val initialStones = this
    var stones = initialStones
    repeat(times) {
        stones = stones.blink().fold(mutableMapOf()) { acc, (stone, count) ->
            val previousCount = acc.getOrDefault(stone, 0UL)
            acc[stone] = previousCount + count
            acc
        }
    }
    return stones
}

fun main() {
    fun parseInput(lines: List<String>): Map<Stone, ULong> = lines[0].split(" ")
        .map { it.toULong() }
        .groupingBy { it }
        .eachCount()
        .mapValues { entry -> entry.value.toULong() }

    fun part1(lines: List<String>): ULong {
        val initialStones = parseInput(lines)
        return initialStones.blinkTimes(25)
            .entries.sumOf { it.value }
    }

    fun part2(lines: List<String>): ULong {
        val initialStones = parseInput(lines)
        return initialStones.blinkTimes(75)
            .entries.sumOf { it.value }
    }

    val inputFile = "Day11"
    verify("${inputFile}_test", ::part1, 55312)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
