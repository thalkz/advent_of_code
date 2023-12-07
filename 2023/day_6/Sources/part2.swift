func readInputPart2() -> (time: Int, distance: Int){
    let timeStr = readLine()!
        .split(separator: " ")
        .suffix(from: 1)
        .joined()
    
    let distanceStr = readLine()!
        .split(separator: " ")
        .suffix(from: 1)
        .joined()
    
    return (Int(timeStr) ?? 0, Int(distanceStr) ?? 0)
}

func getWinningRangePart2(time: Int, distance: Int) -> ClosedRange<Int> {
    func win(_ holdTime: Int) -> Bool {
        return Double(time - holdTime) * Double(holdTime) > Double(distance)
    }
    
    var winningHoldTime = 0
    while !win(winningHoldTime) {
        winningHoldTime += 10000
    }
    
    var left = 0
    var right = winningHoldTime
    while left < right {
        let mid = left + (right - left) / 2
        if !win(mid) {
            left = mid + 1
        } else {
            right = mid
        }
    }
    let min = left
    assert(win(min-1) == false, "min - 1 should be loosing")
    assert(win(min) == true, "min should be winning")
    
    left = winningHoldTime
    right = distance
    while left < right {
        let mid = left + (right - left) / 2
        if win(mid) {
            left = mid + 1
        } else {
            right = mid
        }
    }
    let max = left - 1
    assert(win(max) == true, "max - 1 should be winning")
    assert(win(max+1) == false, "max should be loosing")
    
    return min...max
}

func part2() -> Int {
    let race = readInputPart2()
    print(race)
    
    let winningRange: ClosedRange<Int> = getWinningRangePart2(time: race.time, distance: race.distance)
    return winningRange.count
}
