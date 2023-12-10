print("Part 2")

var grid: [[Character]] = []
var sand: Set<Point> = []
var loopStart = Point(x: 0, y: 0)
var j = 0

// Parse grid, add space between each node
while let line = readLine() {
    var row: [Character] = []
    var i = 0
    for char in line {
        row.append(char)
        if char.right {
            row.append("-")
        } else {
            row.append("o")
        }
        
        
        if char == "S" {
            loopStart = Point(x: i*2, y: j*2)
        }
        sand.insert(Point(x: i*2, y: j*2))
        i+=1
    }
    
    var secondRow: [Character] = []
    for node in row {
        if node.down {
            secondRow.append("|")
        } else {
            secondRow.append("o")
        }
    }
    
    grid.append(row)
    grid.append(secondRow)
    j+=1
}

func toKey(_ p: Point) -> Int {
    return p.x * 100000 + p.y
}

var loop: Set<Point> = []
var last = loopStart
var current = grid.adjacentPipe(p: loopStart)[0]

// Handle loopStart
loop.insert(loopStart)
sand.remove(loopStart)

// Find complete loop
while current != loopStart {
    loop.insert(current)
    sand.remove(current)
    for adj in grid.adjacentPipe(p: current) {
        if adj != last {
            last = current
            current = adj
            break
        }
    }
}

// Visit all outer sand (including outer ring)
var visited: Set<Point> = []
var nextVisits: Set<Point> = [Point(x: -1, y: -1)]

while let node = nextVisits.popFirst() {
    visited.insert(node)
    
    if sand.contains(node) {
        sand.remove(node)
    }
    
    for target in grid.adjacent(node) {
        if !visited.contains(target) && !loop.contains(target) {
            nextVisits.insert(target)
        }
    }
}

print("inner sand:", sand.count)
