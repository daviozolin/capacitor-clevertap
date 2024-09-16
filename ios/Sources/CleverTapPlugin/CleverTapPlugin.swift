import Capacitor
import CoreLocation
import Foundation

@objc(CleverTapPlugin)
public class CleverTapPlugin: CAPPlugin, CAPBridgedPlugin {
  public let identifier = "CleverTapPlugin"
  public let jsName = "CleverTapAnalytics"
  public let pluginMethods: [CAPPluginMethod] = [
    CAPPluginMethod(name: "getProfileID", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(name: "recordEventWithNameAndProps", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "recordChargedEventWithDetailsAndItems", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "profileIncrementValueBy", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "profileSet", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "setLocation", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "setPushToken", returnType: CAPPluginReturnPromise),
  ]
  private let implementation = CleverTapAnalytics()

  @objc func getProfileID(_ call: CAPPluginCall) {
    call.resolve([
      "id": implementation.getProfileID()
    ])
  }

  @objc func recordEventWithNameAndProps(_ call: CAPPluginCall) {
    guard let event = call.getString("event") else {
      call.reject("Event name missing or malformatted")
      return
    }

    guard let props = call.getObject("properties") else {
      call.reject("Properties missing or malformatted")
      return
    }

    implementation.recordEventWithNameAndProps(event: event, properties: props)
    call.resolve()
  }

  @objc func recordChargedEventWithDetailsAndItems(_ call: CAPPluginCall) {

    guard let details = call.getObject("details") else {
      call.reject("Purchese details missing or malformatted")
      return
    }

    guard let items = call.getArray("items") as? [JSObject] else {
      call.reject("Items missing or malformatted")
      return
    }

    implementation.recordChargedEventWithDetailsAndItems(details: details, items: items)
    call.resolve()
  }

  @objc func profileIncrementValueBy(_ call: CAPPluginCall) {
    guard let key = call.getString("key") else {
      call.reject("Key missing or malformatted")
      return
    }

    guard let value = call.getFloat("value") else {
      call.reject("value missing or malformatted")
      return
    }

    implementation.profileIncrementValueBy(forKey: key, by: NSNumber(value: value))
    call.resolve()
  }

  @objc func profileSet(_ call: CAPPluginCall) {

    guard let properties = call.getObject("profileProperties") else {
      call.reject("Profile properties details missing or malformatted")
      return
    }

    implementation.profileSet(properties: properties)
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

  @objc func setPushToken(_ call: CAPPluginCall) {
    guard let token = call.getString("token") else {
      call.reject("Push token missing or malformatted")
      return
    }

    implementation.setPushToken(token: token)
    call.resolve()
  }

}
