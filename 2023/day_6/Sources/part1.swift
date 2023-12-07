func readInput() -> [(time: Int, distance: Int)]{
    var line = readLine() ?? ""
    let times: [Int] = line
            .split(separator: " ")
            .suffix(from: 1)
            .map({str in Int(str)!})
    
    line = readLine() ?? ""
    let distances: [Int] = line
            .split(separator: " ")
            .suffix(from: 1)
            .map({str in Int(str)!})
    var result: [(time: Int, distance: Int)] = []
    for (i, _) in times.enumerated() {
        result.append((time: times[i], distance: distances[i]))
    }
    return result
}

func getWinningRange(time: Int, distance: Int) -> ClosedRange<Int> {
    var min = 0
    var max = 0
    for hold in 0...distance {
        if hold * (time - hold) > distance {
            if min == 0 {
                min = hold
            }
            max = hold
        }
    }
    return min...max
}

func part1() -> Int {
    let races = readInput()
    // print(races)

    var total = 1
    for race in races {
        let winningRange: ClosedRange<Int> = getWinningRange(time: race.time, distance: race.distance)
        // print(race, winningRange)
        total *= winningRange.count
    }
    return total
}
