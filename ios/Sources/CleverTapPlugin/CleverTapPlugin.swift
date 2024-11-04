import Capacitor
import CoreLocation
import Foundation

@objc(CleverTapPlugin)
public class CleverTapPlugin: CAPPlugin, CAPBridgedPlugin {
  public let identifier = "CleverTapPlugin"
  public let jsName = "CleverTapAnalytics"
  public let pluginMethods: [CAPPluginMethod] = [
    CAPPluginMethod(name: "setDebugLevel", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(name: "profileGetID", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(name: "recordEvent", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "recordChargedEvent", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "profileIncrementValue", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "profilePush", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "onUserLogin", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "setLocation", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "setPushTokenAs", returnType: CAPPluginReturnPromise),
  ]
  private let implementation = CleverTapAnalytics()

  @objc func profileGetID(_ call: CAPPluginCall) {
    call.resolve([
      "id": implementation.profileGetID()
    ])
  }

  @objc func setDebugLevel(_ call: CAPPluginCall) {
    guard let level = call.getInt("level") else {
      call.reject("Debug level missing or malformatted")
      return
    }

    implementation.setDebugLevel(level: level)

    call.resolve([
      "status": "Log level set to \(level)"
    ])
  }

  @objc func recordEvent(_ call: CAPPluginCall) {
    guard let event = call.getString("event") else {
      call.reject("Event name missing or malformatted")
      return
    }

    guard let props = call.getObject("properties") else {
      call.reject("Properties missing or malformatted")
      return
    }

    implementation.recordEvent(event: event, properties: props)
    call.resolve()
  }

  @objc func recordChargedEvent(_ call: CAPPluginCall) {
    guard let details = call.getObject("details") else {
      call.reject("Purchese details missing or malformatted")
      return
    }

    guard let items = call.getArray("items") as? [JSObject] else {
      call.reject("Items missing or malformatted")
      return
    }

    implementation.recordChargedEvent(details: details, items: items)
    call.resolve()
  }

  @objc func profileIncrementValue(_ call: CAPPluginCall) {
    guard let key = call.getString("key") else {
      call.reject("Key missing or malformatted")
      return
    }

    guard let value = call.getFloat("value") else {
      call.reject("value missing or malformatted")
      return
    }

    implementation.profileIncrementValue(forKey: key, by: NSNumber(value: value))
    call.resolve()
  }

  @objc func profilePush(_ call: CAPPluginCall) {
    guard let properties = call.getObject("profileProperties") else {
      call.reject("Profile properties details missing or malformatted")
      return
    }

    implementation.profilePush(properties: properties)
    call.resolve()
  }

  @objc func onUserLogin(_ call: CAPPluginCall) {
    guard let properties = call.getObject("profileProperties") else {
      call.reject("Profile properties details missing or malformatted")
      return
    }

    implementation.onUserLogin(properties: properties)
    call.resolve()
  }

  @objc func setLocation(_ call: CAPPluginCall) {
    guard let lat = call.getDouble("lat") else {
      call.reject("Latitude missing or malformatted")
      return
    }

    guard let lng = call.getDouble("lng") else {
      call.reject("Longitude missing or malformatted")
      return
    }

    let coordinates = CLLocationCoordinate2D(latitude: lat, longitude: lng)
    implementation.setLocation(coordinates: coordinates)
    call.resolve()
  }

  @objc func setPushTokenAs(_ call: CAPPluginCall) {
    guard let token = call.getString("token") else {
      call.reject("Push token missing or malformatted")
      return
    }

    implementation.setPushTokenAs(token: token)
    call.resolve()
  }

}
