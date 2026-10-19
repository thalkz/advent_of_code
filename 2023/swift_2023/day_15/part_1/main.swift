print("Part 1")

let line = readLine() ?? ""
let tokens = line.split(separator: ",")

var total = 0
for token in tokens {
    var hash = 0
    for char in token {
        hash = ((Int(char.asciiValue ?? 0) + hash) * 17) % 256
    }
    print(hash)
    total += hash
}

print("Total", total)
