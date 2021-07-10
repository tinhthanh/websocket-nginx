import { Component, Input, OnInit, Output ,EventEmitter} from '@angular/core';
import { Demo } from 'src/app/app.component';
import { EventBusService } from 'src/app/event-bus.service';

@Component({
  selector: 'app-chil-n',
  templateUrl: './chil-n.component.html',
  styleUrls: ['./chil-n.component.scss']
})
export class ChilNComponent implements OnInit {
 @Input() list: any[]  = [];
 @Output() eventBus:EventEmitter<any> = new EventEmitter()
 @Input() index:number  = 0;
 public static action:{ EDIT : string } = {
    EDIT : 'EDIT'
 }
  constructor(private eventBusService: EventBusService) { }

  ngOnInit(): void {
  }
  edit(i:number) {
    console.log(i);
    this.eventBusService.pushChange<Demo>(ChilNComponent,new Demo(ChilNComponent.action.EDIT, i))

  
  }

}
