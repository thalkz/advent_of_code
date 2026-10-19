var numbers = [
  try! Regex("one"),
  try! Regex("two"),
  try! Regex("three"),
  try! Regex("four"),
  try! Regex("five"),
  try! Regex("six"),
  try! Regex("seven"),
  try! Regex("eight"),
  try! Regex("nine"),
]

func parseDigit(_ line: String, _ index: String.Index) -> Int? {
  if let integer = Int("\(line[index])") {
    return integer
  }
  let suffix = line.suffix(from: index)
  for (i, regex) in numbers.enumerated() {
    if suffix.prefixMatch(of: regex) != nil {
      return i + 1
    }
  }
  return nil
}

var total = 0
while let line = readLine() {
  var first = -1
  var last = -1

  for index in line.indices {
    if let digit = parseDigit(line, index) {
      if first == nil {
        first = digit
      }
      last = digit
    }
  }

  total += 10 * first + last
}

print(total)
