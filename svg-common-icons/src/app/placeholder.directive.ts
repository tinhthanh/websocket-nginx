import {ComponentFactoryResolver, Directive, Input, OnChanges, SimpleChanges, Type, ViewContainerRef} from '@angular/core';


@Directive({
  selector: '[appPlaceholder]'
})
export class PlaceholderDirective implements OnChanges {

  @Input()
  appPlaceholder: Type<any> | undefined;
  @Input()
  initData: any;
  @Input() initEvent: any;
  private component: any;

  constructor(public viewContainer: ViewContainerRef,
              private componentFactoryResolver: ComponentFactoryResolver) {
    viewContainer.clear();
    if (this.appPlaceholder) {
      this.component = viewContainer.createComponent(this.componentFactoryResolver.resolveComponentFactory(this.appPlaceholder)).instance;
      Object.assign(this.component, this.initData || {});
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['appPlaceholder'] && changes['appPlaceholder'].previousValue != changes['appPlaceholder'].currentValue) {
      this.viewContainer.clear();
      if (this.appPlaceholder) {
        this.component = this.viewContainer.createComponent(this.componentFactoryResolver.resolveComponentFactory(this.appPlaceholder)).instance;
        Object.assign(this.component, changes['initData'] ? (changes['initData'].currentValue || {}) : {});
        Object.keys(this.initEvent || {}).forEach( key => {
            if(this.component[key]){
                this.component[key].subscribe( (data: any) => this.initEvent[key](data) )
            }
        })
    }
    } else if (this.component && changes['initData'] && changes['initData'].previousValue != changes['initData'].currentValue) {
      Object.assign(this.component, changes['initData'].currentValue || {});
    }
  }
}