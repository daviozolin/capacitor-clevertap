import Capacitor
import CoreLocation
import Foundation

@objc(CleverTapPlugin)
public class CleverTapPlugin: CAPPlugin, CAPBridgedPlugin {
  public let identifier = "CleverTapPlugin"
  public let jsName = "CleverTapAnalytics"
  public let pluginMethods: [CAPPluginMethod] = [
    CAPPluginMethod(name: "checkPermissions", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(name: "requestPermissions", returnType: CAPPluginReturnPromise),
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
    CAPPluginMethod(
      name: "initGeofence", returnType: CAPPluginReturnPromise),
    CAPPluginMethod(
      name: "stopGeofence", returnType: CAPPluginReturnPromise),
  ]
  private let implementation = CleverTapAnalytics()

  @objc func profileGetID(_ call: CAPPluginCall) {
    call.resolve([
      "id": implementation.profileGetID()
    ])
  }

  @objc func stopGeofence(_ call: CAPPluginCall) {
    implementation.stopGeofence()
    call.resolve([
      "status": "Geofence Stopped"
    ])
  }

  @objc func initGeofence(_ call: CAPPluginCall) {
    call.keepAlive = true
    NotificationCenter.default.addObserver(
      forName: NSNotification.Name(rawValue: "CleverTapGeofenceEntered"), object: nil,
      queue: OperationQueue.main
    ) { (notification) in
      if let userInfo = notification.userInfo as? [String: Any] {
        self.notifyListeners("geofenceEnteredListener", data: userInfo)
      } else {
        print("Failed to cast notification.userInfo to [String: Any]")
        self.notifyListeners(
          "geofenceExitedListener",
          data: [
            "name": "Geofence Entered"
          ])
      }
    }

    NotificationCenter.default.addObserver(
      forName: NSNotification.Name(rawValue: "CleverTapGeofenceExited"), object: nil,
      queue: OperationQueue.main
    ) { (notification) in
      if let userInfo = notification.userInfo as? [String: Any] {
        self.notifyListeners("geofenceExitedListener", data: userInfo)
      } else {
        print("Failed to cast notification.userInfo to [String: Any]")
        self.notifyListeners(
          "geofenceExitedListener",
          data: [
            "name": "Geofence Exited"
          ])
      }
    }

    NotificationCenter.default.addObserver(
      forName: NSNotification.Name(rawValue: "CleverTapGeofencesDidUpdateNotification"),
      object: nil, queue: OperationQueue.main
    ) { (notification) in
      if let userInfo = notification.userInfo as? [String: Any] {
        self.notifyListeners("locationUpdateListener", data: userInfo)
      } else {
        print("Failed to cast notification.userInfo to [String: Any]")
        self.notifyListeners(
          "locationUpdateListener",
          data: [
            "name": "Geofence Updated"
          ])
      }
    }

    call.resolve([
      "status": "Geofence Initialized"
    ])
  }

  @objc override public func checkPermissions(_ call: CAPPluginCall) {
    let locationState: String
    let backgroundUpdate: String
    let locationManager = CLLocationManager()

    switch locationManager.authorizationStatus {
    case .notDetermined:
      locationState = "prompt"
      backgroundUpdate = "prompt"
    case .restricted, .denied:
      locationState = "denied"
      backgroundUpdate = "denied"
    case .authorizedAlways:
      locationState = "granted"
      backgroundUpdate = "granted"
    case .authorizedWhenInUse:
      locationState = "granted"
      backgroundUpdate = "prompt"
    @unknown default:
      locationState = "prompt"
      backgroundUpdate = "prompt"
    }

    call.resolve(["location": locationState, "backgroundUpdate": backgroundUpdate])
  }

  @objc override public func requestPermissions(_ call: CAPPluginCall) {
    let locationManager = CLLocationManager()

    locationManager.requestAlwaysAuthorization()
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
