print("Part 1")

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
    let maxLastTurn = 2
    
    init(nodes: [[Int]]) {
        self.nodes = nodes
    }
    
    func isValid(_ point: Point) -> Bool {
        return point.x >= 0 && point.x < nodes[0].count && point.y >= 0 && point.y < nodes.count
    }
    
    func isTarget(_ point: Point) -> Bool {
        return point.x == nodes[0].count-1 && point.y == nodes.count - 1
    }
    
    func lossAt(_ point: Point) -> Int {
        return nodes[point.y][point.x]
    }
    
    func getNeighbors(_ point: Point) -> [Point] {
        var result: [Point] = []
        switch point.direction {
            
        case .up:
            result.append(Point(x: point.x+1, y: point.y, direction: .right, lastTurn: 0))
            result.append(Point(x: point.x-1, y: point.y, direction: .left, lastTurn: 0))
            if point.lastTurn < maxLastTurn {
                result.append(Point(x: point.x, y: point.y - 1, direction: .up, lastTurn: point.lastTurn + 1))
            }
            
        case .right:
            result.append(Point(x: point.x, y: point.y + 1, direction: .down, lastTurn: 0))
            result.append(Point(x: point.x, y: point.y - 1, direction: .up, lastTurn: 0))
            if point.lastTurn < maxLastTurn {
                result.append(Point(x: point.x + 1, y: point.y, direction: .right, lastTurn: point.lastTurn + 1))
            }
            
        case .down:
            result.append(Point(x: point.x + 1, y: point.y, direction: .right, lastTurn: 0))
            result.append(Point(x: point.x - 1, y: point.y, direction: .left, lastTurn: 0))
            if point.lastTurn < maxLastTurn {
                result.append(Point(x: point.x, y: point.y + 1, direction: .down, lastTurn: point.lastTurn + 1))
            }
            
        case .left:
            result.append(Point(x: point.x, y: point.y + 1, direction: .down, lastTurn: 0))
            result.append(Point(x: point.x, y: point.y - 1, direction: .up, lastTurn: 0))
            if point.lastTurn < maxLastTurn {
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

let startPoint = Point(x: 0, y: 0, direction: .right, lastTurn: 0)

priorityQueue.insert((point: startPoint, priority: 0))
var lossSoFar: [Point: Int] = [startPoint: 0]

while let (currentPoint, currentLoss) = priorityQueue.pop() {
    if grid.isTarget(currentPoint) {
        print("Loss:", currentLoss)
         break
    }
    
    for nextPoint in grid.getNeighbors(currentPoint) {
        let nextLoss = currentLoss + grid.lossAt(nextPoint)
        if nextLoss < lossSoFar[nextPoint] ?? Int.max {
            lossSoFar[nextPoint] = nextLoss
            priorityQueue.insert((point: nextPoint, priority: nextLoss))
        }
    }
}
