import { registerPlugin } from '@capacitor/core';

import type { CleverTapPlugin } from './definitions';

const CleverTap = registerPlugin<CleverTapPlugin>('CleverTap', {
  web: () => import('./web').then(m => new m.CleverTapWeb()),
});

export * from './definitions';
export { CleverTap };
