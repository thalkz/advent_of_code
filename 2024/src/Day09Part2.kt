package com.thalkz

typealias FileId = Int?

private class File(
    val fileId: FileId, // null if free memory
    val size: Int,
)

private val File.isFreeMemory
    get() = fileId== null

private class FilesReader(
    private val disk: String,
): Iterable<File> {
    val readFiles = mutableSetOf<FileId>()

    inline fun foEachFile(range: IntProgression, action: (File) -> Unit) {
        for (cursor in range) {
            val fileSize = disk[cursor] - '0'
            val isFile = cursor%2 == 0
            val fileId = (cursor/2).takeIf { isFile }
            action(File(fileId, fileSize))
        }
    }

    private val reader = sequence {
        var index = 0
        foEachFile(0..disk.lastIndex) {
            if (!it.isFreeMemory && it.fileId !in readFiles) {
                readFiles += it.fileId
                yield(it)
            } else {
                var remainingSize = it.size
                while(remainingSize > 0) {
                    val movedFile = findFileToMove(remainingSize) ?: break
                    readFiles += movedFile.fileId
                    yield(movedFile)
                    remainingSize -= movedFile.size
                }
                if (remainingSize > 0) {
                    yield(File(fileId = null, size = remainingSize))
                }
            }
            index+=it.size
        }
    }

    private fun findFileToMove(maxSize: Int): File? {
        foEachFile(disk.lastIndex downTo 0) {
            if (!it.isFreeMemory && it.size <= maxSize && it.fileId !in readFiles) {
                return it
            }
        }
        return null
    }

    override fun iterator() = reader.iterator()
}

fun main() {
    fun part2(lines: List<String>): ULong {
        val reader = FilesReader(lines[0])
        var index = 0
        var result = 0UL
        for (file in reader) {
            for (i in 0..<file.size) {
                if (!file.isFreeMemory) {
                    result += ((index + i) * (file.fileId ?: 0)).toULong()
                }
            }
            index += file.size
        }
        return result
    }

    val inputFile = "Day09"
    verify("${inputFile}_test", ::part2, 2858)

    solvePart2(inputFile, ::part2)
}
