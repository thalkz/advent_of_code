print("Part 1")

typealias Ground = [[Bool]]

func parseInput() -> [Ground] {
    var result: [Ground] = []
    
    var ground: Ground = []
    while let line = readLine() {
        if line.isEmpty {
            result.append(ground)
            ground = []
            continue
        }
        
        var row: [Bool] = []
        for char in line {
            row.append(char == "#")
        }
        ground.append(row)
    }
    result.append(ground)
    return result
}

func hasVerticalReflection(_ ground: Ground, _ col: Int) -> Bool {
    for row in 0...(ground.count-1) {
        var left = col
        var right = col + 1
        while left >= 0 && right < ground[0].count {
            if ground[row][left] != ground[row][right] {
                return false
            }
            left -= 1
            right += 1
        }
    }
    return true
}

func hasHorizontalReflection(_ ground: Ground, _ row: Int) -> Bool {
    for col in 0...(ground[0].count-1) {
        var left = row
        var right = row + 1
        while left >= 0 && right < ground.count {
            if ground[left][col] != ground[right][col] {
                return false
            }
            left -= 1
            right += 1
        }
    }
    return true
}

// Groud[ROW][COL]

func solve(_ ground: Ground) -> Int {
    for col in 0..<(ground[0].count - 1) {
        if hasVerticalReflection(ground, col) {
            print("reflection at col=", col)
            return (col + 1) * 1
        }
    }
    
    for row in 0..<(ground.count - 1) {
        if hasHorizontalReflection(ground, row) {
            print("reflection at row=", row)
            return (row + 1) * 100
        }
    }
    print("No reflection found ?!")
    return -1
}

var grounds = parseInput()
print(grounds)
var total = 0
for ground in grounds {
    total += solve(ground)
}
print("TOTAL", total)
