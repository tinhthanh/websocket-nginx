import { Component, OnInit } from '@angular/core';
import { Demo } from '../app.component';
import {  EventBusService, EventHashKey } from '../event-bus.service';

@Component({
  selector: 'app-child',
  templateUrl: './child.component.html',
  styleUrls: ['./child.component.scss']
})
@EventHashKey()
export class ChildComponent implements OnInit {

  constructor(private eventBusService: EventBusService) { }
  ngOnInit(): void {
  }
  demo(){
    this.eventBusService.pushChange<Demo>(ChildComponent,new Demo("EDIT",{ name: "alex"}))
  }

}
