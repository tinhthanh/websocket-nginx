import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Demo } from 'src/app/app.component';
import { EventBusService,EventHashKey } from 'src/app/event-bus.service';

@Component({
  selector: 'app-chil-z',
  templateUrl: './chil-z.component.html',
  styleUrls: ['./chil-z.component.scss']
})
@EventHashKey()
export class ChilZComponent implements OnInit {
  @Input() list: any[] = [];
  @Output() eventBus:EventEmitter<any> = new EventEmitter();
  @Input() index:number   =0;
  public static action: {SAVE:string } = {
    SAVE : 'SAVE'
  }
  constructor(private eventBusService: EventBusService) { }

  ngOnInit(): void {
  }

  back() {
    console.log("ok na")
    //this.eventBus.emit({type:'SAVE' , state: this.list[this.index]})
    this.eventBusService.pushChange<Demo>(ChilZComponent,new Demo(ChilZComponent.action.SAVE, this.list[this.index]))

  }
}
