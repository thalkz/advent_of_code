extension String {
    func substr(_ range: ClosedRange<Int>) -> String {
        let start = self.index(self.startIndex, offsetBy: range.lowerBound)
        let end = self.index(self.startIndex, offsetBy: range.upperBound)
        return String(self[start...end])
    }
}

print("Part 1")

let path = readLine()!
// print(path)
let _ = readLine()

var nodes: [String: (String, String)] = [:]
while let line = readLine() {
    let node = line.substr(0...2)
    let left = line.substr(7...9)
    let right = line.substr(12...14)
    nodes.updateValue((left, right), forKey: node)
}

var current = "AAA"
var pathIndex = path.startIndex
var count = 0
while current != "ZZZ" {
    current = if path[pathIndex] == "L" {
        nodes[current]?.0 ?? "AAA"
    } else {
        nodes[current]?.1 ?? "AAA"
    }
    pathIndex = path.index(pathIndex, offsetBy: 1)
    if pathIndex == path.endIndex {
        pathIndex = path.startIndex
    }
    count += 1
}
print(count)
