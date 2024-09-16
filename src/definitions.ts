export interface CleverTapPlugin {
  getProfileID(): Promise<{ id: string }>;

  recordEventWithNameAndProps(props: {
    event: string;
    properties: any;
  }): Promise<void>;

  recordChargedEventWithDetailsAndItems(props: {
    details: any;
    items: [any];
  }): Promise<void>;

  profileIncrementValueBy(props: { key: string; value: number }): Promise<void>;

  profileSet(props: { profileProperties: any }): Promise<void>;

  setLocation(props: { lat: number; lng: number }): Promise<void>;

  setPushToken(props: { token: string }): Promise<void>;
}
