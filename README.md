# capacitor-clevertap

Implements CleverTapAnalytics SDK

## Install

```bash
npm install capacitor-clevertap
npx cap sync
```

## API

<docgen-index>

* [`getProfileID()`](#getprofileid)
* [`recordEventWithNameAndProps(...)`](#recordeventwithnameandprops)
* [`recordChargedEventWithDetailsAndItems(...)`](#recordchargedeventwithdetailsanditems)
* [`profileIncrementValueBy(...)`](#profileincrementvalueby)
* [`profileSet(...)`](#profileset)
* [`setLocation(...)`](#setlocation)
* [`setPushToken(...)`](#setpushtoken)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getProfileID()

```typescript
getProfileID() => Promise<{ id: string; }>
```

**Returns:** <code>Promise&lt;{ id: string; }&gt;</code>

--------------------


### recordEventWithNameAndProps(...)

```typescript
recordEventWithNameAndProps(props: { event: string; properties: any; }) => Promise<void>
```

| Param       | Type                                             |
| ----------- | ------------------------------------------------ |
| **`props`** | <code>{ event: string; properties: any; }</code> |

--------------------


### recordChargedEventWithDetailsAndItems(...)

```typescript
recordChargedEventWithDetailsAndItems(props: { details: any; items: [any]; }) => Promise<void>
```

| Param       | Type                                         |
| ----------- | -------------------------------------------- |
| **`props`** | <code>{ details: any; items: [any]; }</code> |

--------------------


### profileIncrementValueBy(...)

```typescript
profileIncrementValueBy(props: { key: string; value: number; }) => Promise<void>
```

| Param       | Type                                         |
| ----------- | -------------------------------------------- |
| **`props`** | <code>{ key: string; value: number; }</code> |

--------------------


### profileSet(...)

```typescript
profileSet(props: { profileProperties: any; }) => Promise<void>
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


### setPushToken(...)

```typescript
setPushToken(props: { token: string; }) => Promise<void>
```

| Param       | Type                            |
| ----------- | ------------------------------- |
| **`props`** | <code>{ token: string; }</code> |

--------------------

</docgen-api>
