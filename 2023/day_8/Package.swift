// swift-tools-version: 5.9

import PackageDescription

let package = Package(
    name: "day_8",
    platforms: [.macOS(.v13)],
    targets: [
        .executableTarget(name: "p1", path: "Part1"),
        .executableTarget(name: "p2", path: "Part2"),
    ]
)
