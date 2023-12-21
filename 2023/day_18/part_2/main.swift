print("Part 2")

struct Point: Hashable {
    let x: Int
    let y: Int
}

func parseInput() -> [(String, Int)] {
    var result: [(String, Int)] = []
    while let line = readLine() {
        let tokens = line.split(separator: " ")
        let color = String(tokens[2])
        
        
        let start = color.index(color.startIndex, offsetBy: 2)
        let end = color.index(color.endIndex, offsetBy: -2)
        
        let hex = String(color[start...end])
        let value = UInt(hex, radix: 16)!
        
        let distance = value >> 4
        var direction = ""
        switch value & 7 {
        case 0:
            direction = "R"
        case 1:
            direction = "D"
        case 2:
            direction = "L"
        case 3:
            direction = "U"
        default:
            print("unknown direction: \(value & 7)")
        }
        print(direction, distance)
        result.append((direction, Int(distance)))
    }
    return result
}

func computeCorners(instructions: [(String, Int)]) -> [Point] {
    var result: [Point] = []
    var x = 0
    var y = 0
    
    result.append(Point(x: 0, y: 0))
    for instruction in instructions {
        let direction = instruction.0
        let distance =  instruction.1
        switch direction {
        case "U":
            y -= distance
        case "D":
            y += distance
        case "R":
            x += distance
        case "L":
            x -= distance
        default:
            print("unknown direction")
        }
        result.append(Point(x:x, y:y))
        print("point (\(x),\(y))")
    }
    return result
}

let instr = parseInput()
let corners = computeCorners(instructions: instr)
var total = 0

for i in 1...(corners.count-1) {
    let (a, b) = (corners[i-1], corners[i])
    total += (a.y + b.y) * (a.x - b.x)
    total += abs(a.x - b.x) + abs(a.y - b.y)
}

let area = 1 + abs(total) / 2
print("area", area)
