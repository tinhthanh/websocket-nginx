import { Injectable } from '@angular/core';
import { CommonIcon } from './common-icons';

@Injectable({
  providedIn: 'root'
})
export class CommonIconsService {

  private registry = new Map<string, string>();

  public registerIcons(icons: CommonIcon[]): void {
      icons.forEach((icon: CommonIcon) => this.registry.set(icon.name, icon.data));
  }

  public getIcon(iconName: string): string | undefined {
      if (!this.registry.has(iconName)) {
          console.warn(`We could not find the common Icon with the name ${iconName}, did you add it to the Icon registry?`);
      }
      return this.registry.get(iconName);
  }
}
