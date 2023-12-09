// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "day_9",
    platforms: [.macOS(.v13)],
    targets: [
        .executableTarget(name: "p1", path: "part_1"),
        .executableTarget(name: "p2", path: "part_2"),
    ]
)
