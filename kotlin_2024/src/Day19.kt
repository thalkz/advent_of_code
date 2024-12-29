package com.thalkz

import java.util.LinkedList
import java.util.Queue

private data class State(
    val inputIndex: Int,
    val patternIndex: Int,
    val charIndex: Int,
)

private class Matcher(
    val patterns: List<String>,
    val input: String,
) {
    val queue: Queue<State> = LinkedList()
    val seen = mutableSetOf<State>()

    private fun next(
        inputIndex: Int,
        patternIndex: Int,
        charIndex: Int,
    ): Boolean {
        if (inputIndex == input.length) {
            return charIndex == patterns[patternIndex].length
        }

        when {
            patternIndex == -1 || charIndex == patterns[patternIndex].length -> {
                patterns.indices.forEach {
                    queue.offer(State(
                        inputIndex = inputIndex,
                        patternIndex = it,
                        charIndex = 0,
                    ))
                }
            }

            patterns[patternIndex][charIndex] == input[inputIndex] -> {
                queue.offer(State(
                    inputIndex = inputIndex + 1,
                    patternIndex = patternIndex,
                    charIndex = charIndex + 1
                ))
            }
        }

        return false
    }

    fun match(): Boolean {
        queue.offer(State(0, -1, 0))
        while (queue.isNotEmpty()) {
            val state = queue.poll()
//            println(state)
            if (state in seen) continue
            seen.add(state)
            val success = next(state.inputIndex, state.patternIndex, state.charIndex)
            if (success) return true
        }
        return false
    }
}

fun main() {
    fun parseInput(lines: List<String>): List<Matcher> {
        val patterns = lines[0].split(", ")
        return lines.drop(2).map { Matcher(patterns, it) }
    }

    fun part1(lines: List<String>): Int {
        val matchers = parseInput(lines)
        return matchers.count { it.match() }
    }

    fun part2(lines: List<String>): Int {
        val input = parseInput(lines)
        return 0
    }

    val inputFile = "Day19"
    verify("${inputFile}_test", ::part1, 6)
//    verify("${inputFile}_test", ::part2, 16)

    solvePart1(inputFile, ::part1)
//    solvePart2(inputFile, ::part2)
}
