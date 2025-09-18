// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorClevertap",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorClevertap",
            targets: ["CleverTapPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "CleverTapPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/CleverTapPlugin"),
        .testTarget(
            name: "CleverTapPluginTests",
            dependencies: ["CleverTapPlugin"],
            path: "ios/Tests/CleverTapPluginTests")
    ]
)