print("Part 1")

struct Point: Hashable {
    let x: Int
    let y: Int
    
    func getNeighbors() -> [Point] {
        return [
            Point(x: x+1, y: y),
            Point(x: x-1, y: y),
            Point(x: x, y: y+1),
            Point(x: x, y: y-1)
        ]
    }
}

func parseInput() -> [(String, Int, String)] {
    var result: [(String, Int, String)] = []
    while let line = readLine() {
        let tokens = line.split(separator: " ")
        let direction = String(tokens[0])
        let size = Int(tokens[1])!
        let color = String(tokens[2])
        result.append((direction, size, color))
    }
    return result
}

var instructions = parseInput()
var digged: Set<Point> = []
var x = 0
var y = 0

digged.insert(Point(x: 0, y: 0))

for instruction in instructions {
    switch instruction.0 {
    case "U":
        for _ in 1...instruction.1 {
            y -= 1
            digged.insert(Point(x: x, y: y))
        }
    case "D":
        for _ in 1...instruction.1 {
            y += 1
            digged.insert(Point(x: x, y: y))
        }
    case "R":
        for _ in 1...instruction.1 {
            x += 1
            digged.insert(Point(x: x, y: y))
        }
    case "L":
        for _ in 1...instruction.1 {
            x -= 1
            digged.insert(Point(x: x, y: y))
        }
    default:
        print("unknown direction")
    }
}

var minX = Int.max
var minY = Int.max
var maxX = Int.min
var maxY = Int.min
for p in digged {
    minX = min(minX, p.x)
    minY = min(minY, p.y)
    maxX = max(maxX, p.x)
    maxY = max(maxY, p.y)
}
minX -= 1
minY -= 1
maxX += 1
maxY += 1

var visited: Set<Point> = []
var queue: [Point] = []
queue.append(Point(x:minX,y:minY))
var outside = 0

while let current = queue.popLast() {
    for next in current.getNeighbors() {
        if !visited.contains(next) 
            && !digged.contains(next)
            && next.x >= minX && next.x <= maxX && next.y >= minY && next.y <= maxY {
            queue.append(next)
            visited.insert(next)
            outside += 1
        }
    }
}

let surface = (1 + maxX - minX) * (1 + maxY - minY)
print("surface:", surface)
print("outside:", outside)
print("digged:", surface - outside)

/*
var total = 0
for j in minY...maxY {
    var inside = false
    for i in minX...maxX {
        if digged.contains(Point(x:i, y:j)) {
            total += 1
            inside = !inside
            print("#", terminator: "")
        } else if inside {
            total += 1
            print("#", terminator: "")
        } else {
            print(".", terminator: "")
        }
    }
    print()
}
print(total)

*/
