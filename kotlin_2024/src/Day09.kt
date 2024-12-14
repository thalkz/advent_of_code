package com.thalkz

private sealed interface Block {
    data class File(val fileId: Int): Block {
        override fun toString() = fileId.toString()
    }

    data object FreeMemory: Block {
        override fun toString() = "."
    }
}

private class DiskReader(
    private val disk: String,
    range: IntProgression,
): Iterable<Block> {
    private val reader = sequence {
        var fileStart = 0
        for (cursor in range) {
            val fileSize = disk[cursor] - '0'
            val isFile = cursor%2 == 0
            for (i in 0..<fileSize) {
                if (isFile) {
                    yield(Block.File(cursor / 2))
                } else {
                    yield(Block.FreeMemory)
                }
            }
            fileStart+=fileSize
        }
    }

    override fun iterator() = reader.iterator()
}

fun main() {
    fun defragment(disk: String): Sequence<Block.File> = sequence {
        val diskSize = disk.sumOf { it - '0' }
        val leftReader = DiskReader(disk, 0..disk.lastIndex).iterator()
        val rightReader = DiskReader(disk, disk.lastIndex downTo 0).filterIsInstance<Block.File>().iterator()
        var leftBlock = leftReader.next()
        var rightBlock = rightReader.next()
        var counter = 2
        while (counter < diskSize) {
            when (leftBlock) {
                is Block.File -> {
                    yield(leftBlock)
                }
                is Block.FreeMemory -> {
                    yield(rightBlock)
                    rightBlock = rightReader.next()
                    counter++
                }
            }
            leftBlock = leftReader.next()
            counter++
        }
    }

    fun part1(lines: List<String>): ULong {
        return defragment(lines[0])
            .mapIndexed { position, block -> (position * block.fileId).toULong() }
            .sum()
    }

    val inputFile = "Day09"
    verify("${inputFile}_test", ::part1, 1928)

    solvePart1(inputFile, ::part1)
}
