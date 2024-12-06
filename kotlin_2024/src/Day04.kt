package com.thalkz

const val part1Word = "XMAS"

fun main() {
    fun part1(input: List<String>): Int {
        val width = input[0].length
        val height = input.size

        var count = 0
        for (i in 0..width) {
            for (j in 0..height) {
                if (input.at(i,j) != part1Word[0]) continue
                count += input.checkXmasStartingAt(i, j)
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val width = input[0].length
        val height = input.size

        var count = 0
        for (i in 0..width) {
            for (j in 0..height) {
                if (input.checkMasCrossAt(i, j)) {
                    count++
                }
            }
        }
        return count
    }

    val inputFile = "Day04"
    verify("${inputFile}_test", ::part1, 18)
    verify("${inputFile}_test", ::part2, 9)

    solvePart1(inputFile, ::part1)
    solvePart2(inputFile, ::part2)
}

fun List<String>.checkXmasStartingAt(i: Int, j: Int): Int {
    var result = 0
    for (di in -1..1) {
        for (dj in -1..1) {
            if (di == 0 && dj == 0) continue
            if (checkXmasStartingAtWithDirection(i, j, di, dj)) result++
        }
    }
    return result
}

fun List<String>.checkXmasStartingAtWithDirection(i: Int, j: Int, di: Int, dj: Int): Boolean {
    part1Word.forEachIndexed { index, char ->
        if (this.at(i +index * di,j+index * dj) != char) {
            return false
        }
    }
    return true
}

fun List<String>.checkMasCrossAt(i: Int, j: Int): Boolean {
    if (at(i,j) != 'A') return false
    return (isMasDiagonal(i,j, 1, 1) || isMasDiagonal(i,j, -1, -1))
            && (isMasDiagonal(i,j, 1, -1) || isMasDiagonal(i,j, -1, 1))
}

fun List<String>.isMasDiagonal(i: Int, j: Int, di: Int, dj: Int): Boolean {
    return at(i-di,j-dj) == 'M' && at(i+di,j+dj) == 'S'
}

fun List<String>.at(i : Int, j:Int) : Char? {
    if (i < 0 || i >= this[0].length ||  j < 0 || j >= this.size) {
        return null
    } else {
        return this[j][i]
    }
}