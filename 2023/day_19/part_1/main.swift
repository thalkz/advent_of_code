typealias Rule = (Part) -> String?

typealias Part = [String: Int]

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
                // print("DEFAULT", String(parts[0]))
                rules.append { _ in String(parts[0])}
            } else if parts[0].contains("<") {
                let operands = parts[0].split(separator: "<")
                let variable = String(operands[0])
                let value = Int(operands[1])!
                // print("\(variable) < \(value): \(parts[1])")
                rules.append { (a: Part) in a[variable]! < value ? String(parts[1]) : nil }
            } else {
                let operands = parts[0].split(separator: ">")
                let variable = String(operands[0])
                let value = Int(operands[1])!
                // print("\(variable) > \(value): \(parts[1])")
                rules.append { (a: Part) in a[variable]! > value ? String(parts[1]) : nil }
            }
        }
        
        workflows[name] = rules
        // print(name, rulesStr)
    }
    
    return workflows
}



func parseParts() throws -> [Part] {
    var parts: [Part] = []
    while let line = readLine() {
        let startIndex = line.index(line.startIndex, offsetBy: 1)
        let endIndex = line.index(line.endIndex, offsetBy: -2)
        let trimmedLine = String(line[startIndex...endIndex])
        
        var part: [String: Int] = [:]
        let tokens = trimmedLine.split(separator: ",")
        for token in tokens {
            // print(token)
            let operands = token.split(separator: "=")
            part[String(operands[0])] = Int(operands[1]) ?? 0
        }
        
        parts.append(part)
        // print(part)
    }
    return parts
}

func compute(_ part: Part) -> Int {
    var total = 0
    for (_, v) in part {
        total += v
    }
    return total
}

func part1(_ workflows: [String:[Rule]], _ parts: [Part]) -> Int {
    var result = 0
    for part in parts {
        print(part, terminator: ": ")
        
        var current = "in"
        
        while current != "A" && current != "R" {
            print(current, terminator: " -> ")
            for rule in workflows[current]! {
                if let next = rule(part) {
                    current = next
                    break
                }
            }
        }
        
        if current == "A" {
            result += compute(part)
            print("A")
        } else {
            print("R")
        }
    }
    return result
}

// SCRIPT

let workflows = parseWorkflows()
let parts = try! parseParts()

print("Part 1:", part1(workflows, parts))
