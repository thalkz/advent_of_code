print("Part 2")

enum Direction {
    case up, right, down, left
}

struct Point: Hashable {
    let x: Int
    let y: Int
    let direction: Direction
    let lastTurn: Int
}

class Grid {
    var nodes: [[Int]] = []
    let minLastTurn = 3
    let maxLastTurn = 8
    
    init(nodes: [[Int]]) {
        self.nodes = nodes
    }
    
    func isValid(_ point: Point) -> Bool {
        return point.x >= 0 && point.x < nodes[0].count && point.y >= 0 && point.y < nodes.count
    }
    
    func isTarget(_ point: Point) -> Bool {
        return point.x == nodes[0].count-1 && point.y == nodes.count - 1 && point.lastTurn >= minLastTurn
    }
    
    func lossAt(_ point: Point) -> Int {
        return nodes[point.y][point.x]
    }
    
    func getNeighbors(_ point: Point) -> [Point] {
        var result: [Point] = []
        switch point.direction {
            
        case .up:
            if point.lastTurn >= minLastTurn {
                result.append(Point(x: point.x+1, y: point.y, direction: .right, lastTurn: 0))
                result.append(Point(x: point.x-1, y: point.y, direction: .left, lastTurn: 0))
            }
            if point.lastTurn <= maxLastTurn {
                result.append(Point(x: point.x, y: point.y - 1, direction: .up, lastTurn: point.lastTurn + 1))
            }
            
        case .right:
            if point.lastTurn >= minLastTurn {
                result.append(Point(x: point.x, y: point.y + 1, direction: .down, lastTurn: 0))
                result.append(Point(x: point.x, y: point.y - 1, direction: .up, lastTurn: 0))
            }
            if point.lastTurn <= maxLastTurn {
                result.append(Point(x: point.x + 1, y: point.y, direction: .right, lastTurn: point.lastTurn + 1))
            }
            
        case .down:
            if point.lastTurn >= minLastTurn {
                result.append(Point(x: point.x + 1, y: point.y, direction: .right, lastTurn: 0))
                result.append(Point(x: point.x - 1, y: point.y, direction: .left, lastTurn: 0))
            }
            if point.lastTurn <= maxLastTurn {
                result.append(Point(x: point.x, y: point.y + 1, direction: .down, lastTurn: point.lastTurn + 1))
            }
            
        case .left:
            if point.lastTurn >= minLastTurn {
                result.append(Point(x: point.x, y: point.y + 1, direction: .down, lastTurn: 0))
                result.append(Point(x: point.x, y: point.y - 1, direction: .up, lastTurn: 0))
            }
            if point.lastTurn <= maxLastTurn {
                result.append(Point(x: point.x - 1, y: point.y, direction: .left, lastTurn: point.lastTurn + 1))
            }
        }
        
        return result.filter({p in isValid(p)})
    }
}

func parseInput() -> Grid {
    var nodes: [[Int]] = []
    while let line = readLine() {
        var row: [Int] = []
        for char in line {
            row.append(Int("\(char)") ?? 0)
        }
        nodes.append(row)
    }
    return Grid(nodes: nodes)
}

// SCRIPT

let grid = parseInput()
var priorityQueue = BinaryHeap<(point: Point, priority: Int)>(comparator: {a, b in a.priority < b.priority})
var prev: [Point: Point] = [:]

let startPointA = Point(x: 0, y: 0, direction: .right, lastTurn: 0)
let startPointB = Point(x: 0, y: 0, direction: .down, lastTurn: 0)

priorityQueue.insert((point: startPointA, priority: 0))
priorityQueue.insert((point: startPointB, priority: 0))

var lossSoFar: [Point: Int] = [startPointA: 0, startPointB: 0]
var end: Point = startPointA

while let (currentPoint, currentLoss) = priorityQueue.pop() {
    if grid.isTarget(currentPoint) {
        print("Loss:", currentLoss)
        end = currentPoint
        break
    }
    
    for nextPoint in grid.getNeighbors(currentPoint) {
        let nextLoss = currentLoss + grid.lossAt(nextPoint)
        if nextLoss < lossSoFar[nextPoint] ?? Int.max {
            prev[nextPoint] = currentPoint
            lossSoFar[nextPoint] = nextLoss
            priorityQueue.insert((point: nextPoint, priority: nextLoss))
        }
    }
}

var path: Set<Point> = []
while let point = prev[end] {
    path.insert(point)
    end = point
}

for (j, row) in grid.nodes.enumerated() {
    for (i, _) in row.enumerated() {
        if path.contains(where: {p in p.x == i && p.y == j}) {
            print("#", terminator: "")
        } else {
            print(".", terminator: "")
        }
    }
    print()
}
