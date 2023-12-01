import Foundation

func readFile(filename: String) -> String {
    var result = ""
    do {
        result = try String(contentsOfFile: "./"+filename, encoding: .utf8)
    }
    catch {
            print("ERROR: Cannot read file")
    }
    return result
}

var numbers: [String: Int] = [
    "one":1,
    "two":2,
    "three":3,
    "four":4,
    "five":5,
    "six":6,
    "seven":7,
    "eight":8,
    "nine":9,
]

let content = readFile(filename: "in.txt")
let lines = content.split(separator: "\n")

var total = 0
for line in lines {
    var first = -1
    var last = -1

    for index in line.indices {
        let integer = Int("\(line[index])")
        if let integer {
            if first == -1 {
                first = integer
            }
            last = integer
        } else {
            let suffix = line.suffix(from: index)
            for (key, value) in numbers {
                let regex = try Regex(key)
                let match = suffix.prefixMatch(of: regex)
                if match != nil {
                    if first == -1 {
                        first = value
                    }
                    last = value
                    break
                }
            }
        }
    }
    if first == -1 || last == -1 {
        print(line)
    }
    let toAdd = 10 * first + last
    total = total + toAdd
}

print(total)
