# capacitor-clevertap

This plugin partially implements CleverTapAnalytics SDK. It's still under development and has many missing functions.

Follow [`iOS`](https://developer.clevertap.com/docs/ios-quickstart-guide) and [`Android`](https://developer.clevertap.com/docs/android-quickstart-guide) initial setup instructions.

## Install

```bash
npm install capacitor-clevertap
npx cap sync
```

## API

<docgen-index>

* [`profileGetID()`](#profilegetid)
* [`recordEvent(...)`](#recordevent)
* [`recordChargedEvent(...)`](#recordchargedevent)
* [`profileIncrementValue(...)`](#profileincrementvalue)
* [`profilePush(...)`](#profilepush)
* [`setLocation(...)`](#setlocation)
* [`setPushTokenAs(...)`](#setpushtokenas)
* [`onUserLogin(...)`](#onuserlogin)
* [`stopGeofence()`](#stopgeofence)
* [`initGeofence()`](#initgeofence)
* [`triggerLocation()`](#triggerlocation)
* [`setDebugLevel(...)`](#setdebuglevel)
* [`addListener('geofenceInitializedListener', ...)`](#addlistenergeofenceinitializedlistener-)
* [`addListener('locationUpdateListener', ...)`](#addlistenerlocationupdatelistener-)
* [`addListener('onPushClicked', ...)`](#addlisteneronpushclicked-)
* [`addListener('geofenceEnteredListener', ...)`](#addlistenergeofenceenteredlistener-)
* [`addListener('geofenceExitedListener', ...)`](#addlistenergeofenceexitedlistener-)
* [`checkPermissions()`](#checkpermissions)
* [`requestPermissions(...)`](#requestpermissions)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)
* [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### profileGetID()

```typescript
profileGetID() => Promise<{ id: string; }>
```

**Returns:** <code>Promise&lt;{ id: string; }&gt;</code>

--------------------


### recordEvent(...)

```typescript
recordEvent(props: { event: string; properties: any; }) => Promise<void>
```

| Param       | Type                                             |
| ----------- | ------------------------------------------------ |
| **`props`** | <code>{ event: string; properties: any; }</code> |

--------------------


### recordChargedEvent(...)

```typescript
recordChargedEvent(props: { details: any; items: any[]; }) => Promise<void>
```

| Param       | Type                                         |
| ----------- | -------------------------------------------- |
| **`props`** | <code>{ details: any; items: any[]; }</code> |

--------------------


### profileIncrementValue(...)

```typescript
profileIncrementValue(props: { key: string; value: number; }) => Promise<void>
```

| Param       | Type                                         |
| ----------- | -------------------------------------------- |
| **`props`** | <code>{ key: string; value: number; }</code> |

--------------------


### profilePush(...)

```typescript
profilePush(props: { profileProperties: any; }) => Promise<void>
```

| Param       | Type                                     |
| ----------- | ---------------------------------------- |
| **`props`** | <code>{ profileProperties: any; }</code> |

--------------------


### setLocation(...)

```typescript
setLocation(props: { lat: number; lng: number; }) => Promise<void>
```

| Param       | Type                                       |
| ----------- | ------------------------------------------ |
| **`props`** | <code>{ lat: number; lng: number; }</code> |

--------------------


### setPushTokenAs(...)

```typescript
setPushTokenAs(props: { token: string; }) => Promise<void>
```

Registers the device's push token with CleverTap.

Pass the FCM registration token on Android and the APNs device token
(hex string) on iOS, e.g. the value from `@capacitor/push-notifications`'
`registration` event on each platform.

Android: this only registers the token. If another FirebaseMessagingService
(e.g. `@capacitor/push-notifications`') receives `MESSAGING_EVENT` instead of
CleverTap's, CleverTap pushes reach that service, and it has to hand them to
CleverTap natively (`CTFcmMessageHandler`) for them to be shown.

| Param       | Type                            |
| ----------- | ------------------------------- |
| **`props`** | <code>{ token: string; }</code> |

--------------------


### onUserLogin(...)

```typescript
onUserLogin(props: { profileProperties: any; }) => Promise<void>
```

| Param       | Type                                     |
| ----------- | ---------------------------------------- |
| **`props`** | <code>{ profileProperties: any; }</code> |

--------------------


### stopGeofence()

```typescript
stopGeofence() => Promise<void>
```

Stops geofence monitoring.

--------------------


### initGeofence()

```typescript
initGeofence() => Promise<void>
```

Android: initializes the CleverTap Geofence SDK and starts monitoring.
Location permission must already be granted.

iOS: only subscribes the geofence listeners. Monitoring itself is started
natively with `CleverTapGeofence.monitor.start(didFinishLaunchingWithOptions:)`.

--------------------


### triggerLocation()

```typescript
triggerLocation() => Promise<void>
```

Fetches the current location and sends it to CleverTap to refresh the
monitored geofences. Rejects if `initGeofence()` hasn't been called or the
`location` permission isn't granted.

Android only: the iOS Geofence SDK has no equivalent.

--------------------


### setDebugLevel(...)

```typescript
setDebugLevel(props: { level: DEBUG_LEVEL; }) => Promise<void>
```

Sets the CleverTap SDK log level. Until this is called, the SDK's default applies.

iOS has a single debug level, so `DEBUG` and `VERBOSE` behave the same there.

| Param       | Type                                                            |
| ----------- | --------------------------------------------------------------- |
| **`props`** | <code>{ level: <a href="#debug_level">DEBUG_LEVEL</a>; }</code> |

--------------------


### addListener('geofenceInitializedListener', ...)

```typescript
addListener(eventName: 'geofenceInitializedListener', listenerFunc: (event: { status: string; }) => void) => Promise<PluginListenerHandle>
```

Android only.

| Param              | Type                                                 |
| ------------------ | ---------------------------------------------------- |
| **`eventName`**    | <code>'geofenceInitializedListener'</code>           |
| **`listenerFunc`** | <code>(event: { status: string; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('locationUpdateListener', ...)

```typescript
addListener(eventName: 'locationUpdateListener', listenerFunc: (event: { lat: number; lng: number; }) => void) => Promise<PluginListenerHandle>
```

Android: fired on each location update with `{ lat, lng }`.

iOS: fired when the list of monitored geofences is updated, with the
Geofence SDK's notification payload instead of `{ lat, lng }`.

| Param              | Type                                                           |
| ------------------ | -------------------------------------------------------------- |
| **`eventName`**    | <code>'locationUpdateListener'</code>                          |
| **`listenerFunc`** | <code>(event: { lat: number; lng: number; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('onPushClicked', ...)

```typescript
addListener(eventName: 'onPushClicked', listenerFunc: (data: CleverTapPushNotificationPayload) => void) => Promise<PluginListenerHandle>
```

Fired when a CleverTap push notification is tapped. Taps that launch the
app are delivered once the first listener is added.

Android only.

| Param              | Type                                                                                                             |
| ------------------ | ---------------------------------------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'onPushClicked'</code>                                                                                     |
| **`listenerFunc`** | <code>(data: <a href="#clevertappushnotificationpayload">CleverTapPushNotificationPayload</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('geofenceEnteredListener', ...)

```typescript
addListener(eventName: 'geofenceEnteredListener', listenerFunc: (event: GeofenceStatusChange) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                                                      |
| ------------------ | ----------------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'geofenceEnteredListener'</code>                                                    |
| **`listenerFunc`** | <code>(event: <a href="#geofencestatuschange">GeofenceStatusChange</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('geofenceExitedListener', ...)

```typescript
addListener(eventName: 'geofenceExitedListener', listenerFunc: (event: GeofenceStatusChange) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                                                      |
| ------------------ | ----------------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'geofenceExitedListener'</code>                                                     |
| **`listenerFunc`** | <code>(event: <a href="#geofencestatuschange">GeofenceStatusChange</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### checkPermissions()

```typescript
checkPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#permissionstatus">PermissionStatus</a>&gt;</code>

--------------------


### requestPermissions(...)

```typescript
requestPermissions(options?: CleverTapPluginPermissions | undefined) => Promise<PermissionStatus>
```

On Android 11+ the system ignores a request that asks for background
location together with foreground location, so request `location` first
and `backgroundUpdate` in a second call. iOS ignores `permissions` and
always requests "Always" authorization.

| Param         | Type                                                                              |
| ------------- | --------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#clevertappluginpermissions">CleverTapPluginPermissions</a></code> |

**Returns:** <code>Promise&lt;<a href="#permissionstatus">PermissionStatus</a>&gt;</code>

--------------------


### Interfaces


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


#### CleverTapPushNotificationPayload

| Prop        | Type                |
| ----------- | ------------------- |
| **`title`** | <code>string</code> |
| **`body`**  | <code>string</code> |
| **`data`**  | <code>any</code>    |
| **`image`** | <code>string</code> |


#### GeofenceStatusChange

| Prop                | Type                |
| ------------------- | ------------------- |
| **`id`**            | <code>number</code> |
| **`gcId`**          | <code>number</code> |
| **`gcName`**        | <code>string</code> |
| **`lat`**           | <code>number</code> |
| **`lng`**           | <code>number</code> |
| **`r`**             | <code>number</code> |
| **`triggered_lat`** | <code>number</code> |
| **`triggered_lng`** | <code>number</code> |


#### PermissionStatus

| Prop                   | Type                                                        |
| ---------------------- | ----------------------------------------------------------- |
| **`location`**         | <code><a href="#permissionstate">PermissionState</a></code> |
| **`backgroundUpdate`** | <code><a href="#permissionstate">PermissionState</a></code> |


#### CleverTapPluginPermissions

| Prop              | Type                                   |
| ----------------- | -------------------------------------- |
| **`permissions`** | <code>CleverTapPermissionType[]</code> |


### Type Aliases


#### PermissionState

<code>'prompt' | 'prompt-with-rationale' | 'granted' | 'denied'</code>


#### CleverTapPermissionType

<code>'location' | 'backgroundUpdate'</code>


### Enums


#### DEBUG_LEVEL

| Members       | Value           |
| ------------- | --------------- |
| **`OFF`**     | <code>-1</code> |
| **`INFO`**    | <code>0</code>  |
| **`DEBUG`**   | <code>2</code>  |
| **`VERBOSE`** | <code>3</code>  |

</docgen-api>
