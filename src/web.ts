import { WebPlugin } from '@capacitor/core';

import type { CleverTapPlugin } from './definitions';

export class CleverTapWeb extends WebPlugin implements CleverTapPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
