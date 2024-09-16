import { registerPlugin } from '@capacitor/core';

import type { CleverTapPlugin } from './definitions';

const CleverTapAnalytics = registerPlugin<CleverTapPlugin>('CleverTapAnalytics');

export * from './definitions';
export { CleverTapAnalytics };
