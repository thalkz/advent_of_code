
print("Part 1")

struct Point: Hashable {
    let x: Int
    let y: Int
}

func parseInput() -> [Point] {
    var galaxies: [Point] = []
    var j = 0
    while let line = readLine() {
        var i = 0
        for character in line {
            if character == "#" {
                galaxies.append(Point(x: i, y: j))
            }
            i += 1
        }
        j+=1
    }
    return galaxies
}

func getSpace(_ galaxies: [Point]) -> (spaceX: Set<Int>, spaceY: Set<Int>) {
    let maxX = galaxies.reduce(0, {total, p in max(total, p.x)})
    let maxY = galaxies.reduce(0, {total, p in max(total, p.y)})
    
    var xs: Set<Int> = []
    for x in 0...maxX {
        if !galaxies.contains(where: {p in p.x == x}) {
            xs.insert(x)
        }
    }
    var ys: Set<Int> = []
    for y in 0...maxY {
        if !galaxies.contains(where: {p in p.y == y}) {
            ys.insert(y)
        }
    }
    return (spaceX: xs, spaceY: ys)
}

func distance(_ galaxies: [Point], _ spaceX: Set<Int>, _ spaceY: Set<Int>, _ i: Int, _ j: Int) -> Int {
    let start = galaxies[i]
    let end = galaxies[j]
    
    var expandedSpace = 0
    for k in min(start.x,end.x)...max(start.x, end.x) {
        if spaceX.contains(k) {
            expandedSpace += 1
        }
    }
    for k in min(start.y,end.y)...max(start.y, end.y) {
        if spaceY.contains(k) {
            expandedSpace += 1
        }
    }
    return abs(start.x - end.x) + abs(start.y - end.y) + expandedSpace
}

let galaxies = parseInput()
let (spaceX, spaceY) = getSpace(galaxies)
var total = 0
for i in 0..<galaxies.count {
    for j in (i+1)..<galaxies.count {
        let dist = distance(galaxies, spaceX, spaceY, i, j)
        // print("from \(galaxies[i]) to \(galaxies[j]): \(dist)")
        total += dist
    }
}

// print(galaxies)
print("total", total)
