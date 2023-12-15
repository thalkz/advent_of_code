print("Part 2")

typealias Lens = (key: String, value: Int)

let regex = try! Regex("[-=]")

func parseToken(_ token: String) -> (hash: Int, key: String, value: Int) {
    let parts = token.split(separator: regex)
    var hash = 0
    for char in parts[0] {
        hash = ((Int(char.asciiValue ?? 0) + hash) * 17) % 256
    }
    
    if token.last == "-" {
        return (hash: hash, key: String(parts[0]), value: -1)
    } else {
        return (hash: hash, key: String(parts[0]), value: Int(parts[1]) ?? 0)
    }
}

func printBoxes(_ boxes: [[Lens]]) {
    for (index, box) in boxes.enumerated() {
        if box.isEmpty {
            continue
        }
        print("Box \(index): ", terminator: "")
        for lens in box {
            print("[\(lens.key) \(lens.value)]", terminator: "")
        }
        print()
    }
}

func compute(_ boxes: [[Lens]]) -> Int {
    var total = 0
    for (index, box) in boxes.enumerated() {
        for (position, lens) in box.enumerated() {
            total += (index+1) * (position+1) * lens.value
        }
    }
    return total
}

let line = readLine() ?? ""
let tokens = line.split(separator: ",")
var boxes: [[Lens]] = Array(repeating: [], count: 256)

for token in tokens {
    let (hash, key, value) = parseToken(String(token))
    // print(hash, key, value)
    
    if value == -1 {
        boxes[hash].removeAll(where: {(k, _) in k == key})
    } else if let lensIndex = boxes[hash].firstIndex(where: {(k, _) in k == key}) {
        boxes[hash][lensIndex].value = value
    } else {
        boxes[hash].append((key: key, value: value))
    }
}

printBoxes(boxes)
let total = compute(boxes)
print("TOTAL: \(total)")
