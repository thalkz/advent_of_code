typealias Position =  (x: Int, y: Int)

extension Character {
    var right: Bool {
        get {
            return self == "-" || self == "L" || self == "F" || self == "S"
        }
    }
    
    var left: Bool {
        get {
            return self == "-" || self == "J" || self == "7" || self == "S"
        }
    }
    
    var up: Bool {
        get {
            return self == "|" || self == "L" || self == "J" || self == "S"
        }
    }
    
    var down: Bool {
        get {
            return self == "|" || self == "F" || self == "7" || self == "S"
        }
    }
}

extension [[Character]] {
    func at(_ pos: (x: Int, y: Int)) -> Character {
        if pos.x < 0 || pos.x >= self[0].count || pos.y < 0 || pos.y >= self.count {
            return "."
        }
        return self[pos.y][pos.x]
    }
    
    func adjacent(p: Position) -> [Position] {
        var result: [Position] = []
        let char = self.at(p)
        if p.x > 0 && char.left {
            let adj = (x: p.x-1, y: p.y)
            if self.at(adj).right {
                result.append(adj)
            }
        }
        if p.x < self[0].count - 1 && char.right {
            let adj = (x: p.x+1, y: p.y)
            if self.at(adj).left {
                result.append(adj)
            }
        }
        if p.y > 0 && char.up {
            let adj = (x: p.x, y: p.y-1)
            if self.at(adj).down {
                result.append(adj)
            }
        }
        if p.y < self.count - 1 && char.down {
            let adj = (x: p.x, y: p.y+1)
            if self.at(adj).up {
                result.append(adj)
            }
        }
        return result
    }
}

print("Part 1")

var grid: [[Character]] = []
var start: (x: Int, y: Int) = (0, 0)
var j = 0
while let line = readLine() {
    print(line)
    var row: [Character] = []
    var i = 0
    for char in line {
        row.append(char)
        if char == "S" {
            start = (x:i, y:j)
        }
        i+=1
    }
    grid.append(row)
    j+=1
}

// print(grid)
// print(start)

func toKey(_ p: Position) -> Int {
    return p.x * 100000 + p.y
}

var next: [(x: Int, y: Int)] = grid.adjacent(p: start)

var distance = 1
var last = start
var current = next[0]
while current != start {
    print("current", current, "dist:", distance)
    for adj in grid.adjacent(p: current) {
        if adj != last {
            last = current
            current = adj
            distance += 1
            break
        }
    }
}

var maxDistance = (distance + 1) / 2
print("maxDistance", maxDistance)
