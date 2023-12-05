// The Swift Programming Language
// https://docs.swift.org/swift-book

typealias Conversion = (src: Range<Int>, offset: Int)

func getSeedRanges() -> [Range<Int>] {
    let line = readLine() ?? ""
    let seeds: [Int] = line
        .split(separator: " ")
        .dropFirst()
        .map { value in Int(value)! }
    var ranges: [Range<Int>] = []
    for i in stride(from: 0, to: seeds.count, by: 2) {
        let rangeStart = seeds[i]
        let rangeEnd = seeds[i] + seeds[i+1]
        ranges.append(rangeStart..<rangeEnd)
    }
    return ranges
}

func getMaps() -> [[Conversion]] {
    var result: [[Conversion]] = []
    var current: [Conversion] = []
    while let line = readLine() {
        if line.isEmpty {
            if !current.isEmpty {
                result.append(current)
            }
            current = []
            let _ = readLine()
        } else {
            let values: [Int] = line
                .split(separator: " ")
                .map {value in Int(value)! }
            let dstStart = values[0]
            let srcStart = values[1]
            let length = values[2]
            let src = srcStart..<(srcStart+length)
            let offset = dstStart - srcStart
            current.append((src, offset))
        }
    }
    result.append(current)
    return result
}

func intersect(_ a: Range<Int>, _ b: Range<Int>) -> Range<Int>? {
    let lower = max(a.lowerBound, b.lowerBound)
    let upper = min(a.upperBound, b.upperBound)
    if lower >= upper {
        return nil
    }
    return lower..<upper
}

func shift(_ a: Range<Int>, _ offset: Int) -> Range<Int> {
    return (a.lowerBound+offset)..<(a.upperBound+offset)
}

func toDestinations(inputRange: Range<Int>, map: [Conversion]) -> [Range<Int>] {
    var sources = [inputRange]
    var destinations: [Range<Int>] = []
    
    for convertion in map {
        if sources.isEmpty {
            break
        }
        
        let first = sources[0]
        if let intersection = intersect(convertion.src, first) {
            sources.remove(at: 0)
            
            let dst = shift(intersection, convertion.offset)
            destinations.append(dst)
            
            if first.lowerBound < intersection.lowerBound {
                sources.append(first.lowerBound..<intersection.lowerBound)
            }
            if intersection.upperBound < first.upperBound {
                sources.append(intersection.upperBound..<first.upperBound)
            }
        }
    }
    
    for left in sources {
        destinations.append(left)
    }
    return destinations
}

func findLowest(inputRange: Range<Int>, maps: [[Conversion]], step: Int) -> Int {
    // All conversions are done
    if step == maps.count {
        return inputRange.lowerBound
    }
    
    // Find destinations
    let destinations = toDestinations(inputRange: inputRange, map: maps[step])
    
    // Recursive call and keep lowest
    var lowest = Int.max
    for dst in destinations {
        let result = findLowest(inputRange: dst, maps: maps, step: step+1)
        lowest = min(lowest, result)
    }
    return lowest
}

let seedRanges = getSeedRanges()
let maps = getMaps()

var result = Int.max
for range in seedRanges {
    let lowest = findLowest(inputRange: range, maps: maps, step: 0)
    result = min(result, lowest)
}
print("Min location:", result)


/*
--- Part 1 ---
var line = readLine() ?? ""
let seeds: [Int] = line
    .split(separator: " ")
    .dropFirst()
    .map { value in Int(value)! }

print("seeds", seeds)

var maps: [[[Int]]] = []
var currentMap: [[Int]] = []
while let line = readLine() {
    if line.isEmpty {
        if !currentMap.isEmpty {
            maps.append(currentMap)
        }
        currentMap = []
        let _ = readLine()
    } else {
        let values: [Int] = line
            .split(separator: " ")
            .map {value in Int(value)! }
        currentMap.append(values)
    }
}
maps.append(currentMap)

// print("maps", maps)

func convert(map: [[Int]], value: Int) -> Int {
    for convertion in map {
        let dstStart = convertion[0]
        let srcStart = convertion[1]
        let length = convertion[2]
        let srcRange = srcStart..<(srcStart+length)
        if srcRange.contains(value) {
            return dstStart - srcStart + value
        }
    }
    return value
}

var minLocation = Int.max
for seed in seeds {
    // print("seed:", seed)
    var value = seed
    for map in maps {
        value = convert(map: map, value: value)
        // print("->", value)
    }
    // print("location:", value)
    if value < minLocation {
        minLocation = value
    }
}

print("Min location:", minLocation)
*/
