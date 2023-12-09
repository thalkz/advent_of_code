// The Swift Programming Language
// https://docs.swift.org/swift-book

extension [Int] {
    func diff() -> [Int] {
        var result: [Int] = []
        var last: Int? = nil
        for value in self {
            if let last {
                result.append(value - last)
            }
            last = value
        }
        return result
    }
    
    func allZeros() -> Bool {
        return !self.contains(where: {$0 != 0})
    }
}

func predict(array: [Int]) -> Int {
    // print(array)
    if array.allZeros() {
        return 0
    }
    
    do {
        let diff = array.diff()
        let lastValue = predict(array: diff)
        return (array.last ?? 0) + lastValue
    }
}

print("Part 1")

var total = 0
while let line = readLine() {
    let values = line.split(separator: " ").map({value in Int(value)!})
    let prediction = predict(array: values)
    total += prediction
}
print("total:", total)
