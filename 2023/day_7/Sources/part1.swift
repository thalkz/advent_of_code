func compare(a: (hand: Hand, bid: Int), b: (hand: Hand, bid: Int)) -> Bool {
    if a.hand.type == b.hand.type {
        for i in 0..<5 {
            if a.hand.cards[i] == b.hand.cards[i] {
                continue
            } else {
                return a.hand.cards[i].rawValue < b.hand.cards[i].rawValue
            }
        }
        print("same hand", a.hand.cards, b.hand.cards)
        return false // same hand
    } else {
        return a.hand.type < b.hand.type
    }
}

func part1(_ input: inout [(hand: Hand, bid: Int)]) -> Int {
    var total = 0
    input.sort(by: compare)
    for (rank, v) in input.enumerated() {
        print(v.hand.str, v.hand.type, v.bid, "->", (rank+1) * v.bid)
        total += (rank+1) * v.bid
    }
    return total
}
