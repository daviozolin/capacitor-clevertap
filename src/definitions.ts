import type { PluginListenerHandle, PermissionState } from '@capacitor/core';

import type { DEBUG_LEVEL } from '.';

export interface CleverTapPlugin {
  profileGetID(): Promise<{ id: string }>;

  recordEvent(props: { event: string; properties: any }): Promise<void>;

  recordChargedEvent(props: { details: any; items: any[] }): Promise<void>;

  profileIncrementValue(props: { key: string; value: number }): Promise<void>;

  profilePush(props: { profileProperties: any }): Promise<void>;

  setLocation(props: { lat: number; lng: number }): Promise<void>;

  /**
   * Registers the device's push token with CleverTap.
   *
   * Pass the FCM registration token on Android and the APNs device token
   * (hex string) on iOS, e.g. the value from `@capacitor/push-notifications`'
   * `registration` event on each platform.
   *
   * Android: this only registers the token. If another FirebaseMessagingService
   * (e.g. `@capacitor/push-notifications`') receives `MESSAGING_EVENT` instead of
   * CleverTap's, CleverTap pushes reach that service, and it has to hand them to
   * CleverTap natively (`CTFcmMessageHandler`) for them to be shown.
   */
  setPushTokenAs(props: { token: string }): Promise<void>;

  onUserLogin(props: { profileProperties: any }): Promise<void>;

  /**
   * Stops geofence monitoring.
   */
  stopGeofence(): Promise<void>;

  /**
   * Android: initializes the CleverTap Geofence SDK and starts monitoring.
   * Location permission must already be granted.
   *
   * iOS: only subscribes the geofence listeners. Monitoring itself is started
   * natively with `CleverTapGeofence.monitor.start(didFinishLaunchingWithOptions:)`.
   */
  initGeofence(): Promise<void>;

  /**
   * Fetches the current location and sends it to CleverTap to refresh the
   * monitored geofences. Rejects if `initGeofence()` hasn't been called or the
   * `location` permission isn't granted.
   *
   * Android only: the iOS Geofence SDK has no equivalent.
   */
  triggerLocation(): Promise<void>;

  /**
   * Sets the CleverTap SDK log level. Until this is called, the SDK's default applies.
   *
   * iOS has a single debug level, so `DEBUG` and `VERBOSE` behave the same there.
   */
  setDebugLevel(props: { level: DEBUG_LEVEL }): Promise<void>;

  /**
   * Android only.
   */
  addListener(
    eventName: 'geofenceInitializedListener',
    listenerFunc: (event: { status: string }) => void,
  ): Promise<PluginListenerHandle>;

  /**
   * Android: fired on each location update with `{ lat, lng }`.
   *
   * iOS: fired when the list of monitored geofences is updated, with the
   * Geofence SDK's notification payload instead of `{ lat, lng }`.
   */
  addListener(
    eventName: 'locationUpdateListener',
    listenerFunc: (event: { lat: number; lng: number }) => void,
  ): Promise<PluginListenerHandle>;

  /**
   * Fired when a CleverTap push notification is tapped. Taps that launch the
   * app are delivered once the first listener is added.
   *
   * Android only.
   */
  addListener(
    eventName: 'onPushClicked',
    listenerFunc: (data: CleverTapPushNotificationPayload) => void,
  ): Promise<PluginListenerHandle>;

  addListener(
    eventName: 'geofenceEnteredListener',
    listenerFunc: (event: GeofenceStatusChange) => void,
  ): Promise<PluginListenerHandle>;

  addListener(
    eventName: 'geofenceExitedListener',
    listenerFunc: (event: GeofenceStatusChange) => void,
  ): Promise<PluginListenerHandle>;

  checkPermissions(): Promise<PermissionStatus>;

  /**
   * On Android 11+ the system ignores a request that asks for background
   * location together with foreground location, so request `location` first
   * and `backgroundUpdate` in a second call. iOS ignores `permissions` and
   * always requests "Always" authorization.
   */
  requestPermissions(options?: CleverTapPluginPermissions): Promise<PermissionStatus>;
}

export type CleverTapPermissionType = 'location' | 'backgroundUpdate';

export interface CleverTapPluginPermissions {
  permissions?: CleverTapPermissionType[];
}

export interface PermissionStatus {
  location: PermissionState;
  backgroundUpdate: PermissionState;
}

export interface GeofenceStatusChange {
  id: number;
  gcId: number;
  gcName: string;
  lat: number;
  lng: number;
  r: number;
  triggered_lat: number;
  triggered_lng: number;
}

export interface CleverTapPushNotificationPayload {
  title: string;
  body: string;
  data: any;
  image: string;
}