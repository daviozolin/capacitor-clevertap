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
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", "7.0.0"..<"9.0.0"),
        .package(url: "https://github.com/CleverTap/clevertap-ios-sdk.git", from: "7.8.2"),
        .package(url: "https://github.com/CleverTap/clevertap-geofence-ios.git", from: "1.0.7")
    ],
    targets: [
        .target(
            name: "CleverTapPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm"),
                .product(name: "CleverTapSDK", package: "clevertap-ios-sdk"),
                .product(name: "CleverTapGeofence", package: "clevertap-geofence-ios")
            ],
            path: "ios/Sources/CleverTapPlugin"),
        .testTarget(
            name: "CleverTapPluginTests",
            dependencies: ["CleverTapPlugin"],
            path: "ios/Tests/CleverTapPluginTests")
    ]
)