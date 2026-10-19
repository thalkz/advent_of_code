print("Part 2")

typealias Board = [[Character]]

func parseInput() -> Board {
    var result: Board = []
    while let line = readLine() {
        var row: [Character] = []
        for char in line {
            row.append(char)
        }
        result.append(row)
    }
    return result
}

func printBoard(_ b: Board) {
    for row in b {
        for char in row {
            print(char, terminator: "")
        }
        print()
    }
}

func getTotal(_ b: Board) -> Int {
    var total = 0
    for (j, row) in b.enumerated() {
        for char in row {
            if char == "O" {
                total += b.count - j
            }
        }
    }
    return total
}

func tilt(_ b: inout Board, _ direction: (x: Int, y: Int)) {
    let xStride = direction.x < 0 ? stride(from: 0, to: b[0].count, by: 1) : stride(from: b[0].count-1, to: -1, by: -1)
    let yStride = direction.y < 0 ? stride(from: 0, to: b.count, by: 1) : stride(from: b.count-1, to: -1, by: -1)
    
    for j in yStride {
        for i in xStride {
            if b[j][i] == "O" {
                b[j][i] = "."
                var cursor = (x: i + direction.x, y: j + direction.y)
                
                while cursor.x >= 0
                        && cursor.x < b[0].count
                        && cursor.y >= 0
                        && cursor.y < b.count
                        && b[cursor.y][cursor.x] == "." {
                    cursor.x += direction.x
                    cursor.y += direction.y
                }
                
                cursor.x -= direction.x
                cursor.y -= direction.y
                
                b[cursor.y][cursor.x] = "O"
            }
        }
    }
}

func performCycle(_ b: inout Board) {
    tilt(&b, (0, -1))
    tilt(&b, (-1, 0))
    tilt(&b, (0, 1))
    tilt(&b, (1, 0))
}

var board = parseInput()
var cycles = 1000
var m: [Board: Int] = [:]

for i in 1...cycles  {
    performCycle(&board)
    let copy = board
    if let last = m[copy] {
        print("FOUND: \(last) is same as \(i) ")
        let size = i - last
        let cyclesLeft = cycles - i
        let toPerform = cyclesLeft % size
        print("size=\(size) cyclesLeft=\(cyclesLeft) toPerform=\(toPerform)")
        
        for _ in 0..<toPerform {
            performCycle(&board)
        }
        break
    }
    
    m[copy] = i
}

print()
print("AFTER:")
printBoard(board)

print(getTotal(board))
