let dot = Character(".")
let gear = Character("*")
var symbols: [Int: Int] = [:]
var dict: [[Character]] = []

func setSymbol(_ i: Int, _ j: Int, _ newValue: Int) {
  symbols[10000 * j + i] = newValue
}

func getSymbol(_ i: Int, _ j: Int) -> Int? {
  return symbols[10000 * j + i]
}

func searchGear(j: Int, from: Int, to: Int, newValue: Int) -> Int? {
  for dx in (from - 1)...(to + 1) {
    for dy in j - 1...j + 1 {
      let value = getSymbol(dx, dy)
      if let value {
        setSymbol(dx, dy, newValue)
        return value
      }
    }
  }
  return nil
}

// Init dict and symbols
var j = 0
while let line: String = readLine() {
  var i = 0
  var row: [Character] = []
  for char in line {
    row.append(char)
    if char == gear {
      setSymbol(i, j, 0)
    }
    i += 1
  }
  row.append(dot)
  dict.append(row)
  j += 1
}

var total = 0
for (j, row) in dict.enumerated() {
  var number = 0
  var start: Int? = nil
  for (i, char) in row.enumerated() {
    if let digit = Int("\(char)") {
      number = number * 10 + digit
      if start == nil {
        start = i
      }
    } else if start != nil {
      let otherNumber = searchGear(j: j, from: start!, to: i - 1, newValue: number)
      if let otherNumber {
        print(otherNumber, number)
        total += otherNumber * number
      }
      number = 0
      start = nil
    }
  }
}

print("total", total)
