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
* [`addListener('geofenceEnteredListener', ...)`](#addlistenergeofenceenteredlistener-)
* [`addListener('geofenceExitedListener', ...)`](#addlistenergeofenceexitedlistener-)
* [`checkPermissions()`](#checkpermissions)
* [`requestPermissions()`](#requestpermissions)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

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

--------------------


### initGeofence()

```typescript
initGeofence() => Promise<void>
```

--------------------


### triggerLocation()

```typescript
triggerLocation() => Promise<void>
```

--------------------


### setDebugLevel(...)

```typescript
setDebugLevel(props: { level: number; }) => Promise<void>
```

| Param       | Type                            |
| ----------- | ------------------------------- |
| **`props`** | <code>{ level: number; }</code> |

--------------------


### addListener('geofenceInitializedListener', ...)

```typescript
addListener(eventName: 'geofenceInitializedListener', listenerFunc: (event: { status: string; }) => void) => Promise<PluginListenerHandle>
```

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

| Param              | Type                                                           |
| ------------------ | -------------------------------------------------------------- |
| **`eventName`**    | <code>'locationUpdateListener'</code>                          |
| **`listenerFunc`** | <code>(event: { lat: number; lng: number; }) =&gt; void</code> |

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
checkPermissions() => Promise<{ location: PermissionState; backgroundUpdate: PermissionState; }>
```

**Returns:** <code>Promise&lt;{ location: <a href="#permissionstate">PermissionState</a>; backgroundUpdate: <a href="#permissionstate">PermissionState</a>; }&gt;</code>

--------------------


### requestPermissions()

```typescript
requestPermissions() => Promise<void>
```

--------------------


### Interfaces


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


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


### Type Aliases


#### PermissionState

<code>'prompt' | 'prompt-with-rationale' | 'granted' | 'denied'</code>

</docgen-api>
