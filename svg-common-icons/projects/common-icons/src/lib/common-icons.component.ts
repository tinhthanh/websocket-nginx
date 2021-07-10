import {ChangeDetectionStrategy, Component, ElementRef, HostBinding, Inject, Input, Optional, ViewEncapsulation} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import { CommonIconsService } from './common-icons.service';
@Component({
  selector: 'lib-common-icons',
  template: `
        <ng-content></ng-content>
    `,
    styles: [':host::ng-deep svg{width: 100%; height: 100%} :host(lib-common-icons) { display: flex; }'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class CommonIconsComponent {
    private svgIcon: SVGElement | undefined;

    @Input()
    set name(iconName: string) {
        if (this.svgIcon) {
            this.element.nativeElement.removeChild(this.svgIcon);
        }
        const svgData = this.commonIconsService.getIcon(iconName);
        this.svgIcon = this.svgElementFromString(svgData);
        this.element.nativeElement.appendChild(this.svgIcon);
    }

    constructor(private element: ElementRef, private commonIconsService: CommonIconsService,
                @Optional() @Inject(DOCUMENT) private document: any) {
    }

    private svgElementFromString(svgContent: string | undefined): SVGElement {
        const div = this.document.createElement('DIV');
        div.innerHTML = svgContent;
        return div.querySelector('svg') || this.document.createElementNS('http://www.w3.org/2000/svg', 'path');
    }
}
