export interface CleverTapPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
