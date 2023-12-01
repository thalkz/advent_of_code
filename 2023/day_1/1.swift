import Foundation

func readFile(filename:String) -> String {
    var result = ""
    do {
        result = try String(contentsOfFile: "./"+filename, encoding: .utf8)
    }
    catch {
            print("ERROR: Cannot read file")
    }
    return result
}

let content = readFile(filename: "in.txt")
let lines = content.split(separator: "\n")

var total = 0
for line in lines {
    var first = -1
    var last = -1
    for c in line {
        let value = Int("\(c)") ?? -1
        if value > -1 {
            if first == -1 {
                first = value
            }
            last = value
        }
    }
    total = total + 10 * first + last
}
print(total)
