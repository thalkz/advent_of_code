let splitRegex = try! Regex(":|\\|")

func getSortedNumbers(input: String) -> [Int] {
    var list =
        input
        .split(separator: " ")
        .map { Int($0) ?? 0 }
    list.sort()
    return list
}

func computeCount(cardNumbers: [Int], winningNumbers: [Int]) -> Int {
    var result = 0
    var i = 0
    var j = 0
    while i < cardNumbers.count && j < winningNumbers.count {
        if cardNumbers[i] == winningNumbers[j] {
            result += 1
            i += 1
            j += 1
        } else if cardNumbers[i] > winningNumbers[j] {
            j += 1
        } else {
            i += 1
        }
    }
    return result
}

func getWinningCount(line: String) -> Int {
    let split = line.split(separator: splitRegex)
    let cardNumbers = getSortedNumbers(input: String(split[1]))
    let winningNumbers = getSortedNumbers(input: String(split[2]))
    return computeCount(cardNumbers: cardNumbers, winningNumbers: winningNumbers)
}

func sumCards(cards: [Int: Int]) -> Int {
    return cards.reduce(into: 0, { (total, pair) in total += pair.value })
}

var cards: [Int: Int] = [:]
var lineIndex = 0
while let line = readLine() {
    cards[lineIndex] = 1 + (cards[lineIndex] ?? 0)  // Add original
    let count = getWinningCount(line: line)

    // print("Card \(lineIndex+1): \(count) matches (\(cards[lineIndex] ?? 0) times)")
    for i in (lineIndex + 1)..<(lineIndex + 1 + count) {
        cards[i] = (cards[lineIndex] ?? 0) + (cards[i] ?? 0)
    }
    lineIndex += 1
}

print(sumCards(cards: cards))
