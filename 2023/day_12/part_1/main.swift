print("Part 1")

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
        output.append(Line(springs: springs, groups: groups))
    }
    return output
}

func countSolutions(_ line: Line) -> Int {
    var count = 0
    print()
    for c in line.springs {
        print(c, terminator: "")
    }
    print(" ", line.groups)
    
    func solve(position: Int, groupId: Int, str: String) {
        var currentStr = str
        var currentPosition = position
        
        if groupId >= line.groups.count {
            while currentPosition < line.springs.count {
                if line.springs[currentPosition] == "#" {
                    return
                }
                currentStr += "."
                currentPosition += 1
            }
            
            print(currentStr)
            count += 1
            return
        }
        if (position >= line.springs.count) {
            return
        }
        
        var currentGroup = line.groups[groupId]
        while currentPosition < line.springs.count && line.springs[currentPosition] == "." {
            currentStr.append(".")
            currentPosition += 1
        }
        
        if currentPosition < line.springs.count && line.springs[currentPosition] == "?" {
            solve(position: currentPosition+1, groupId: groupId, str: currentStr+".")
        }
        
        while currentGroup > 0 {
            if currentPosition >= line.springs.count {
                return // Group not completed
            }
            
            if (line.springs[currentPosition] == ".") {
                return // No dot inside the group
            }
            
            currentPosition += 1
            currentGroup -= 1
            currentStr.append("#")
        }
        
        if (currentPosition < line.springs.count && line.springs[currentPosition] == "#") {
            return // Need a space after the group
        }
        
        solve(position: currentPosition+1, groupId: groupId+1, str: currentStr+".")
    }
    
    solve(position: 0, groupId: 0, str: "")
    print("COUNT:", count)
    return count
}

let lines = parseInput()
var total = 0
for line in lines {
    let count = countSolutions(line)
    total += count
}
print(total)
