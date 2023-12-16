print("Part 1")

func parseInput() -> Grid {
    var grid: Grid = []
    while let line = readLine() {
        var row: [Character] = []
        for char in line {
            row.append(char)
        }
        grid.append(row)
    }
    return grid
}

typealias Grid = [[Character]]

enum Direction {
    case up, down, right, left
    
    func isVertical() -> Bool {
        return self == .up || self == .down
    }
    func isHorizontal() -> Bool {
        return self == .right || self == .left
    }
    
    func transform(_ char: Character) -> [Direction] {
        switch char {
        case ".",
            "-" where self.isHorizontal(),
            "|" where self.isVertical():
            return [self]
        case "-":
            return [.left, .right]
        case "|":
            return [.up, .down]
        case "/":
            switch self {
            case .up:
                return [.right]
            case .down:
                return [.left]
            case .right:
                return [.up]
            case .left:
                return [.down]
            }
        default:
            switch self {
            case .up:
                return [.left]
            case .down:
                return [.right]
            case .right:
                return [.down]
            case .left:
                return [.up]
            }
        }
    }
}

struct Point: Hashable {
    let x: Int
    let y: Int
    let dir: Direction
}

func move(_ x: Int, _ y: Int, _ dir: Direction) -> Point {
    switch dir {
    case .up:
        return Point(x: x, y: y-1, dir: dir)
    case .down:
        return Point(x: x, y: y+1, dir: dir)
    case .left:
        return Point(x: x-1,y: y, dir: dir)
    case .right:
        return Point(x: x+1, y:y, dir: dir)
    }
}

let grid = parseInput()
var visited: Set<Point> = []

func propagate(_ light: Point) {
//    print(light)
    if light.x < 0 || light.x >= grid[0].count || light.y < 0 || light.y >= grid.count {
        return
    }
    
    if visited.contains(light) {
        return
    }
    
    visited.insert(light)
    
    let outputDirs = light.dir.transform(grid[light.y][light.x])
    for dir in outputDirs {
        propagate(move(light.x, light.y, dir))
    }
}

propagate(Point(x:0, y:0, dir:.right))

var unique: Set<Point> = []
for visit in visited {
    unique.insert(Point(x: visit.x, y: visit.y, dir: .up))
}

print(unique.count)
