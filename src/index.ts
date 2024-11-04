import { registerPlugin } from '@capacitor/core';

import type { CleverTapPlugin } from './definitions';

export enum DEBUG_LEVEL {
  OFF = -1,
  INFO = 0,
  DEBUG = 2,
  VERBOSE = 3,
}

const CleverTapAnalytics =
  registerPlugin<CleverTapPlugin>('CleverTapAnalytics');

export * from './definitions';
export { CleverTapAnalytics };
