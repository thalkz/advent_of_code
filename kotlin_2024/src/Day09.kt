package com.thalkz

private sealed interface Block {
    val position: Int

    data class File(
        val fileId: Int,
        override val position: Int
    ): Block {
        override fun toString() = fileId.toString()
    }

    data class FreeMemory(
        override val position: Int
    ): Block {
        override fun toString() = "."
    }
}

private class DiskReader(
    private val encodedMemory: String,
    private val readFromLeft: Boolean
): Iterable<Block> {

    val memorySize = encodedMemory.sumOf { it - '0' }

    val range = if (readFromLeft) {
        0..encodedMemory.lastIndex
    } else {
        encodedMemory.lastIndex downTo 0
    }

    fun Int.toPosition() = if (readFromLeft) {
        this
    } else {
        memorySize - 1 - this
    }

    private val reader = sequence {
        var fileStart = 0
        for (cursor in range) {
            val fileSize = encodedMemory[cursor] - '0'
            val isFile = cursor%2 == 0
            for (i in 0..<fileSize) {
                val position = (fileStart + i).toPosition()
                if (isFile) {
                    yield(Block.File(cursor / 2, position))
                } else {
                    yield(Block.FreeMemory(position))
                }
            }
            fileStart+=fileSize
        }
    }

    override fun iterator() = reader.iterator()
}

fun main() {
    fun parseInput(lines: List<String>): String {
        return lines[0]
    }

    fun defragment(disk: String): Sequence<Block.File> = sequence {
        val leftReader = DiskReader(disk, readFromLeft = true).iterator()
        val rightReader = DiskReader(disk, readFromLeft = false).filterIsInstance<Block.File>().iterator()
        var leftBlock = leftReader.next()
        var rightBlock = rightReader.next()
        while (leftBlock.position <= rightBlock.position) {
            when (leftBlock) {
                is Block.File -> {
                    yield(leftBlock)
                }
                is Block.FreeMemory -> {
                    yield(rightBlock)
                    rightBlock = rightReader.next()
                }
            }
            leftBlock = leftReader.next()
        }
    }

    fun part1(lines: List<String>): ULong {
        val disk = parseInput(lines)
        return defragment(disk)
            .mapIndexed { position, block -> (position * block.fileId).toULong() }
            .sum()
    }

    fun part2(lines: List<String>): Int {
        val line = parseInput(lines)
        return 0
    }

    val inputFile = "Day09"
    verify("${inputFile}_test", ::part1, 1928)
//    verify("${inputFile}_test", ::part2, 2858)

    solvePart1(inputFile, ::part1)
//    solvePart2(inputFile, ::part2)
}
