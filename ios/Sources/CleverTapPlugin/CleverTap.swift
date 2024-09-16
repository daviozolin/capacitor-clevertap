import CleverTapSDK
import Foundation

@objc public class CleverTapAnalytics: NSObject {
  @objc public func getProfileID() -> String {
    guard let id = CleverTap.sharedInstance()?.profileGetID() as? String else {
      return "ERROR"
    }
    return id
  }

  @objc func recordEventWithNameAndProps(event: String, properties: [String: Any]) {
    CleverTap.sharedInstance()?.recordEvent(event, withProps: properties)
  }

  @objc func recordChargedEventWithDetailsAndItems(details: [String: Any], items: [Any]) {
    CleverTap.sharedInstance()?.recordChargedEvent(withDetails: details, andItems: items)
  }

  @objc func profileIncrementValueBy(forKey: String, by: NSNumber) {
    CleverTap.sharedInstance()?.profileIncrementValue(by: by, forKey: forKey)
  }

  @objc func profileSet(properties: [String: Any]) {
    CleverTap.sharedInstance()?.profilePush(properties)
  }

  @objc func setLocation(coordinates: CLLocationCoordinate2D) {
    CleverTap.sharedInstance()?.setLocation(coordinates)
  }

  @objc func setPushToken(token: String) {
    CleverTap.sharedInstance()?.setPushTokenAs(token)
  }

}
