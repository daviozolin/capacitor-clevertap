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

</docgen-api>
