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

fun Sequence<Stone>.blink() = sequence {
    for (stone in iterator()) {
        if (stone == 0UL) {
            yield(1UL)
        } else {
            val digitsCount = stone.countDigits()
            if (digitsCount%2 == 0) {
                yield(stone.firstHalf(digitsCount))
                yield(stone.secondHalf(digitsCount))
            } else {
                yield(stone*2024UL)
            }
        }
    }
}

fun main() {
    fun parseInput(lines: List<String>) = lines[0].split(" ")
        .map { it.toULong() }
        .asSequence()

    fun part1(lines: List<String>): Int {
        val initialStones = parseInput(lines)
        var stones = initialStones
        repeat(25) {
            stones = stones.blink()
        }
        return stones.count()
    }

    fun part2(lines: List<String>): Int {
//        val initialStones = parseInput(lines)
//        var stones = initialStones
//        val memo = mutableMapOf<ULong, List<ULong>>()
//        repeat(15) {
//            stones = stones.blinkTimes(5, memo)
//        }
//        return stones.count()
    }

    val inputFile = "Day11"
    verify("${inputFile}_test", ::part1, 55312)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}
