print("Part 1")

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

var board = parseInput()
for (j, row) in board.enumerated() {
    for (i, char) in row.enumerated() {
        if char == "O" {
            board[j][i] = "."
            var cursor = j - 1
            while cursor >= 0 && board[cursor][i] == "." {
                cursor -= 1
            }
            board[cursor+1][i] = "O"
        }
    }
}

printBoard(board)
var total = 0
for (j, row) in board.enumerated() {
    for char in row {
        if char == "O" {
            total += board.count - j
        }
    }
}
print(total)
