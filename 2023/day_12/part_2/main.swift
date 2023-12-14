print("Part 2")

struct Line {
    let springs: [Character]
    let groups: [Int]
}

func parseInput() -> [Line] {
    var output: [Line] = []
    while let line = readLine() {
        var springs: [Character] = []
        let parts = line.split(separator: " ")
        for char in parts[0] {
            springs.append(char)
        }
        
        var groups: [Int] = []
        for char in parts[1].split(separator: ",") {
            groups.append(Int(char) ?? -1)
        }
        
        var fullSprings: [Character] = []
        var fullGroups: [Int] = []
        for _ in 0...3 {
            fullGroups.append(contentsOf: groups)
            fullSprings.append(contentsOf: springs)
            fullSprings.append("?")
        }
        fullGroups.append(contentsOf: groups)
        fullSprings.append(contentsOf: springs)
        fullSprings.append(".")
        
        output.append(Line(springs: fullSprings, groups: fullGroups))
    }
    return output
}

struct Position: Hashable {
    let position: Int
    let groupSize: Int
    let targetGroupId: Int
}

func countSolutions(_ line: Line) -> Int {
    var memo: [Position: Int] = [:]
    
    func solve(position: Int, groupSize: Int, targetGroupId: Int) -> Int {
        let key = Position(position:position, groupSize:groupSize, targetGroupId:targetGroupId)
        if let savedSolutions = memo[key] {
            return savedSolutions
        }
        
        if position >= line.springs.count {
            if targetGroupId >= line.groups.count  {
                return 1
            }
            return 0
        }
        
        let character = line.springs[position]
        let targetGroupSize = targetGroupId < line.groups.count ? line.groups[targetGroupId] : -1
        var solutions = 0
        
        if character == "." || character == "?" {
            if groupSize == 0 {
                solutions += solve(position: position+1, groupSize: groupSize, targetGroupId: targetGroupId)
            } else if groupSize == targetGroupSize {
                solutions += solve(position: position+1, groupSize: 0, targetGroupId: targetGroupId+1)
            }
        }
        
        if character == "#" || character == "?" {
            if groupSize < targetGroupSize {
                solutions += solve(position: position+1, groupSize: groupSize+1, targetGroupId: targetGroupId)
            }
        }
        
        memo[key] = solutions
        return solutions
    }
    
    return solve(position: 0, groupSize: 0, targetGroupId: 0)
}

let lines = parseInput()
var total = 0
for line in lines {
    let count = countSolutions(line)
    total += count
}
print(total)
