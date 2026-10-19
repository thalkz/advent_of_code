typealias Rule = (Part) -> (to: String, moved: Part, left: Part)
typealias Part = [String: ClosedRange<Int>]

func parseWorkflows() -> [String:[Rule]] {
    var workflows: [String:[Rule]] = [:]
    
    while let line = readLine() {
        if line.isEmpty {
            break
        }
        
        let tokens = line.split(separator: "{")
        let name = String(tokens[0])
        let endIndex = tokens[1].index(tokens[1].endIndex, offsetBy: -1)
        let rulesStr = tokens[1].prefix(upTo: endIndex)
        let rulesTokens = rulesStr.split(separator: ",")
        
        var rules: [Rule] = []
        for rt in rulesTokens {
            
            let parts = rt.split(separator: ":")
            if parts.count == 1 {
                // CASE 1) DEFAULT CASE
                
                let to = String(parts[0])
                
                rules.append { p in (
                    to: to,
                    moved: p,
                    left: [:]
                ) }
            } else if parts[0].contains("<") {
                // CASE 2) LOWER THAN VALUE
                
                let operands = parts[0].split(separator: "<")
                let variable = String(operands[0])
                let limit = Int(operands[1])!
                let to = String(parts[1])
                
                rules.append { p in
                    let range = p[variable] ?? 0...0
                    var movedRange: ClosedRange<Int> = 0...0
                    var leftRange: ClosedRange<Int> = 0...0
                    
                    if limit > range.lowerBound {
                        movedRange = range.lowerBound...min(limit-1, range.upperBound)
                    }
                    if limit <= range.upperBound {
                        leftRange = max(range.lowerBound, limit)...range.upperBound
                    }
                    var moved: [String:ClosedRange<Int>] = [:]
                    var left: [String:ClosedRange<Int>] = [:]
                    for (k,v) in p {
                        if k == variable {
                            moved[k] = movedRange
                            left[k] = leftRange
                        } else {
                            moved[k] = v
                            left[k] = v
                        }
                    }
                    return (to: to, moved: moved, left: left)
                }
            } else {
                // CASE 2) UPPER THAN VALUE
                
                let operands = parts[0].split(separator: ">")
                let variable = String(operands[0])
                let limit = Int(operands[1])!
                let to = String(parts[1])
                
                rules.append { p in
                    let range = p[variable] ?? 0...0
                    var movedRange: ClosedRange<Int> = 0...0
                    var leftRange: ClosedRange<Int> = 0...0
                    
                    if limit < range.upperBound {
                        movedRange = max(range.lowerBound, limit+1)...range.upperBound
                    }
                    if limit >= range.lowerBound {
                        leftRange = range.lowerBound...min(limit, range.upperBound)
                    }
                    
                    var moved: [String:ClosedRange<Int>] = [:]
                    var left: [String:ClosedRange<Int>] = [:]
                    for (k,v) in p {
                        if k == variable {
                            moved[k] = movedRange
                            left[k] = leftRange
                        } else {
                            moved[k] = v
                            left[k] = v
                        }
                    }
                    return (to: to, moved: moved, left: left)
                }
            }
        }
        
        workflows[name] = rules
        // print(name, rulesStr)
    }
    
    return workflows
}

func countParts(_ p: Part) -> Int {
    var count = 1
    for (_, v) in p {
        count *= v.count
    }
    return count
}

func solve(_ queue: inout [Part: String], _ workflows: [String:[Rule]]) -> Int {
    var totalParts = 0
    while let (part, w) = queue.popFirst() {
        let rules: [Rule] = workflows[w]!
        var current = part
        for rule in rules {
            let (to, moved, left) = rule(current)
            
            current = left
            
            if to == "A" {
                totalParts += countParts(moved) // Perform actual count
            } else if to != "R" {
                queue[moved] = to
            }
        }
    }
    return totalParts
}

// SCRIPT

let workflows = parseWorkflows()
let initial: Part = [
    "x": 1...4000,
    "m": 1...4000,
    "a": 1...4000,
    "s": 1...4000,
]
var queue = [initial: "in"]
let accepted = solve(&queue, workflows)

print("Part 2:", accepted)
