package day22

data class Point(
    val x: Int,
    val y: Int,
    val z: Int,
)

data class FallingBlock(
    val name: String,
    val low: Point,
    val high: Point,
)

class Block(
    val name: String,
    val low: Point,
    val high: Point,
    val supporting: MutableSet<Block>,
    val supportedBy: MutableSet<Block>,
)

typealias TowerGrid = MutableList<MutableList<Block>>

fun part1(lines: List<String>): Int {
    val fallingBlocks = parseFallingBlocks(lines)
    val groundBlocks = simulateFall(fallingBlocks)
    return groundBlocks
        .count { block ->
            block.supporting.all { it.supportedBy.size > 1 } || block.supporting.isEmpty()
        }
}

fun simulateFall(fallingBlocks: List<FallingBlock>): List<Block> {
    val groundBlock = Block(
        name = "GROUND",
        low = Point(0, 0, 0),
        high = Point(9, 9, 0),
        supporting = mutableSetOf(),
        supportedBy = mutableSetOf()
    )

    val towerGrid: TowerGrid = MutableList(10) { MutableList(10) { groundBlock } }

    val groundBlocks = fallingBlocks
        .map { fallingBlock ->
            computeFall(fallingBlock, towerGrid)
                .apply {
                    towerGrid.addFallenBlock(this)
                }
        }


//    for (block in groundBlocks) {
//        if (block.supporting.all { it.supportedBy.size > 1 } || block.supporting.isEmpty()) {
//            println("Block ${block.name} can be disintegrated")
//        }
//    }

    return groundBlocks
}

fun TowerGrid.addFallenBlock(block: Block) {
    for (x in block.low.x..block.high.x) {
        for (y in block.low.y..block.high.y) {
            this[y][x] = block
        }
    }
}

fun computeFall(fallingBlock: FallingBlock, towerGrid: TowerGrid): Block {
    var maxGroundZ = 0
    val supportingBlocks = mutableSetOf<Block>()
    for (x in fallingBlock.low.x..fallingBlock.high.x) {
        for (y in fallingBlock.low.y..fallingBlock.high.y) {
            val contactBlock = towerGrid[y][x]
            val groundZ = contactBlock.high.z
            if (groundZ > maxGroundZ) {
                maxGroundZ = groundZ
                supportingBlocks.clear()
                supportingBlocks.add(towerGrid[y][x])
            } else if (groundZ == maxGroundZ) {
                supportingBlocks.add(towerGrid[y][x])
            }
        }
    }
    assert(maxGroundZ < fallingBlock.low.z)
    val fallingDistance = fallingBlock.low.z - maxGroundZ - 1

    val block = Block(
        name = fallingBlock.name,
        low = fallingBlock.low.copy(
            z = fallingBlock.low.z - fallingDistance
        ),
        high = fallingBlock.high.copy(
            z = fallingBlock.high.z - fallingDistance
        ),
        supporting = mutableSetOf(),
        supportedBy = supportingBlocks,
    )

//    println("${fallingBlock.name} falls by ${fallingDistance} to ${block}")
    for (supportingBlock in supportingBlocks) {
//        println("${block.name} supported by ${supportingBlock.name}")
        supportingBlock.supporting.add(block)
    }

    return block
}

fun parseFallingBlocks(lines: List<String>): List<FallingBlock> {
    return lines
        .mapIndexed { index, line ->
            val tokens = line.split("~")
            FallingBlock(
                name = ('A' + index).toString(),
                low = parsePoint(tokens[0]),
                high = parsePoint(tokens[1]),
            )
        }
        .onEach {
            assert(it.low.x <= it.high.x)
            assert(it.low.y <= it.high.y)
            assert(it.low.z <= it.high.z)
        }
        .sortedBy { it.low.z }
}

fun parsePoint(str: String): Point {
    val values = str.split(",").map { it.toInt() }
    return Point(
        x = values[0],
        y = values[1],
        z = values[2],
    )
}
