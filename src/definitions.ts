export interface CleverTapPlugin {
  profileGetID(): Promise<{ id: string }>;

  recordEvent(props: { event: string; properties: any }): Promise<void>;

  recordChargedEvent(props: { details: any; items: any[] }): Promise<void>;

  profileIncrementValue(props: { key: string; value: number }): Promise<void>;

  profilePush(props: { profileProperties: any }): Promise<void>;

  setLocation(props: { lat: number; lng: number }): Promise<void>;

  setPushTokenAs(props: { token: string }): Promise<void>;

  onUserLogin(props: { profileProperties: any }): Promise<void>;
}
