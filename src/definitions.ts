import type { PluginListenerHandle, PermissionState } from '@capacitor/core';

import type { DEBUG_LEVEL } from '.';

export interface CleverTapPlugin {
  profileGetID(): Promise<{ id: string }>;

  recordEvent(props: { event: string; properties: any }): Promise<void>;

  recordChargedEvent(props: { details: any; items: any[] }): Promise<void>;

  profileIncrementValue(props: { key: string; value: number }): Promise<void>;

  profilePush(props: { profileProperties: any }): Promise<void>;

  setLocation(props: { lat: number; lng: number }): Promise<void>;

  setPushTokenAs(props: { token: string }): Promise<void>;

  onUserLogin(props: { profileProperties: any }): Promise<void>;

  stopGeofence(): Promise<void>;

  initGeofence(): Promise<void>;

  triggerLocation(): Promise<void>;

  setDebugLevel(props: { level: DEBUG_LEVEL }): Promise<void>;

  addListener(
    eventName: 'geofenceInitializedListener',
    listenerFunc: (event: { status: string }) => void,
  ): Promise<PluginListenerHandle>;

  addListener(
    eventName: 'locationUpdateListener',
    listenerFunc: (event: { lat: number; lng: number }) => void,
  ): Promise<PluginListenerHandle>;

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

  checkPermissions(): Promise<{
    location: PermissionState;
    backgroundUpdate: PermissionState;
  }>;

  requestPermissions(): Promise<void>;
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