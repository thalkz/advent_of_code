extension String {
    func substr(_ range: ClosedRange<Int>) -> String {
        let start = self.index(self.startIndex, offsetBy: range.lowerBound)
        let end = self.index(self.startIndex, offsetBy: range.upperBound)
        return String(self[start...end])
    }
    
    func isEndPosition() -> Bool {
        let isEndRegex = try! Regex("..Z")
        return (try? isEndRegex.wholeMatch(in: self)) != nil
    }
    
    func isStartPosition() -> Bool {
        let isEndRegex = try! Regex("..A")
        return (try? isEndRegex.wholeMatch(in: self)) != nil
    }
}

func parseInput() -> (path: String, nodes: [String: (String, String)], positions: [String]) {
    let path = readLine()!
    let _ = readLine()

    var nodes: [String: (String, String)] = [:]
    var positions: [String] = []

    while let line = readLine() {
        let node = line.substr(0...2)
        let left = line.substr(7...9)
        let right = line.substr(12...14)
        nodes.updateValue((left, right), forKey: node)
        
        if node.isStartPosition() {
            positions.append(node)
        }
    }
    return (path, nodes, positions)
}

func findCycles(path: String, nodes: [String: (String, String)], positions: [String]) -> [Int] {
    var positions = positions
    var pathIndex = path.startIndex
    var count = 0
    var cycles: [Int] = []
    for _ in positions {
        cycles.append(0)
    }

    while cycles.contains(where: {$0 == 0}) {
        for (index, pos) in positions.enumerated() {
            if pos.isEndPosition() {
                cycles[index] = count - cycles[index]
            }
        }
        
        let isLeft = path[pathIndex] == "L"
        for (index, c) in positions.enumerated() {
            if isLeft {
                positions[index] = nodes[c]?.0 ?? "AAA"
            } else {
                positions[index] = nodes[c]?.1 ?? "AAA"
            }
        }
        
        // Increment path
        pathIndex = path.index(pathIndex, offsetBy: 1)
        if pathIndex == path.endIndex {
            pathIndex = path.startIndex
        }
        count += 1
    }
    return cycles
}

func findDividers(cycles: [Int]) -> [Int] {
    var cycles = cycles
    var dividers: [Int] = []
    var i = 2
    while cycles.contains(where: {$0 > 1}) {
        var hasDivided = false
        for (index, value) in cycles.enumerated() {
            if value % i == 0 {
                hasDivided = true
                cycles[index] = value / i
            }
        }
        if hasDivided {
            dividers.append(i)
        } else {
            i += 1
        }
    }
    return dividers
}

// SCRIPT

print("Part 2")

let (path, nodes, positions) = parseInput()
print("positions:", positions.count)

let cycles = findCycles(path: path, nodes: nodes, positions: positions)
print("cycles:", cycles)

let dividers = findDividers(cycles: cycles)
print("dividers:", dividers)

var total = dividers.reduce(1, { a, b in a * b })
print("total:", total)

