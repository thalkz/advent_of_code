enum Card: Int {
    case J = 1
    case two
    case three, four, five, six, seven, eight, nine
    case T, Q, K, A
}

func toCard(_ c: Character) -> Card {
    if let digit = Int(String(c)) {
        let card = Card(rawValue: digit)
        if card == nil {
            fatalError("failed to parse \(c) to card")
        }
        return card!
    }
    
    switch c {
    case "T":
        return .T
    case "J":
        return .J
    case "Q":
        return .Q
    case "K":
        return .K
    case "A":
        return .A
    default:
        fatalError("failed to parse \(c) to card (not digit)")
    }
}

func removeMax(map: inout [Card: Int]) -> Int {
    var max = 0
    var maxCard = Card.A
    for (k, v) in map {
        if k != .J && v > max {
            maxCard = k
            max = v
        }
    }
    
    if let j = map[.J] {
        max += j
        map.removeValue(forKey: .J)
    }
    
    map.removeValue(forKey: maxCard)
    return max
}

func toType(_ first: Int, _ second: Int) -> Int {
    if first == 5 {
        return 7
    } else if first == 4 {
        return 6
    } else if first == 3 && second == 2 {
        return 5
    } else if first == 3 {
        return 4
    } else if first == 2 && second == 2 {
        return 3
    } else if first == 2 {
        return 2
    } else {
        return 1
    }
}

struct Hand {
    var str: String
    var cards: [Card]
    var type = 0
    
    init(_ str: String) {
        self.str = str
        self.cards = []
        var map: [Card: Int] = [:]
        for character in str {
            let card = toCard(character)
            self.cards.append(card)
            map[card] = 1 + (map[card] ?? 0)
        }
        
        let first = removeMax(map: &map)
        let second = removeMax(map: &map)
        self.type = toType(first, second)
    }
}

var input: [(hand: Hand, bid: Int)] = []
while let line = readLine() {
    let tokens = line.split(separator: " ")
    input.append((hand: Hand(String(tokens[0])), bid: Int(tokens[1]) ?? 0))
}

print("Day 7 - part 1")
print(part1(&input))
//print("Day 7 - part 2")
//print(part2(input))
