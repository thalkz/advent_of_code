// swift-tools-version: 5.9

import PackageDescription

let package = Package(
    name: "day_15",
    platforms: [.macOS(.v13)],
    targets: [
        .executableTarget(
            name: "p1", path: "part_1"),
        .executableTarget(
            name: "p2", path: "part_2"),
    ]
)
