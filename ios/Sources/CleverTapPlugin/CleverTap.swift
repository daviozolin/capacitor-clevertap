import CleverTapSDK
import Foundation

@objc public class CleverTapAnalytics: NSObject {
  @objc public func profileGetID() -> String {
    guard let id = CleverTap.sharedInstance()?.profileGetID() as? String else {
      return "ERROR"
    }
    return id
  }

  @objc func recordEvent(event: String, properties: [String: Any]) {
    CleverTap.sharedInstance()?.recordEvent(event, withProps: properties)
  }

  @objc func recordChargedEvent(details: [String: Any], items: [Any]) {
    CleverTap.sharedInstance()?.recordChargedEvent(withDetails: details, andItems: items)
  }

  @objc func profileIncrementValue(forKey: String, by: NSNumber) {
    CleverTap.sharedInstance()?.profileIncrementValue(by: by, forKey: forKey)
  }

  @objc func profilePush(properties: [String: Any]) {
    CleverTap.sharedInstance()?.profilePush(properties)
  }

  @objc func onUserLogin(properties: [String: Any]) {
    CleverTap.sharedInstance()?.onUserLogin(properties)
  }

  @objc func setLocation(coordinates: CLLocationCoordinate2D) {
    CleverTap.sharedInstance()?.setLocation(coordinates)
  }

  @objc func setPushTokenAs(token: String) {
    CleverTap.sharedInstance()?.setPushTokenAs(token)
  }

}
