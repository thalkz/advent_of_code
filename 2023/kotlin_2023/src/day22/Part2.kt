package day22

fun part2(lines: List<String>): Int {
    val fallingBlocks = parseFallingBlocks(lines)
    val groundBlocks = simulateFall(fallingBlocks)
    return groundBlocks
        .sumOf { block ->
            println("For block ${block.name}")
            countWouldFall(block, mutableSetOf(block))
        }
}

fun countWouldFall(block: Block, fallen: MutableSet<Block>): Int {
    if (block.supporting.isEmpty()) {
        return 0
    }


    return block.supporting
        .map { nextBlock ->
            if ((nextBlock.supportedBy - fallen).isEmpty()) {
                println("${nextBlock.name} would fall (+1)")
                fallen.add(nextBlock)
                1 + countWouldFall(nextBlock, fallen)
            } else {
                0
            }
        }
        .sumOf { it }
}
