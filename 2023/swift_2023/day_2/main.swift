func getMaxAmount(_ line: String, _ regex: Regex<AnyRegexOutput>) -> Int {
  var max = 0
  let allMatches = line.matches(of: regex)
  for match in allMatches {
    let substring = line[match.range]
    let amountStr = substring.split(separator: " ")[0]
    let amount = Int(amountStr)!
    if amount > max {
      max = amount
    }
  }
  return max
}

let blueRegex = try! Regex("[0-9]+ blue")
let redRegex = try! Regex("[0-9]+ red")
let greenRegex = try! Regex("[0-9]+ green")

var gameIndex = 1
var total = 0
while let line = readLine() {
  let blueMax = getMaxAmount(line, blueRegex)
  let redMax = getMaxAmount(line, redRegex)
  let greenMax = getMaxAmount(line, greenRegex)

  total += blueMax * redMax * greenMax
  gameIndex += 1
}

print(total)