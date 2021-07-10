import { Component, OnInit, Type } from '@angular/core';
import { Observable } from 'rxjs';
import { Demo } from 'src/app/app.component';
import { ChildComponent } from 'src/app/child/child.component';
import { EventBusService } from 'src/app/event-bus.service';
import { ChilNComponent } from '../chil-n/chil-n.component';
import { ChilZComponent } from '../chil-z/chil-z.component';

@Component({
  selector: 'app-parent',
  templateUrl: './parent.component.html',
  styleUrls: ['./parent.component.scss']
})
export class ParentComponent implements OnInit {
  compHolder: Type<any> | undefined ;
  listPages = [
    { 
      type : "ADD",
      component: ChilNComponent
    },
    {
      type: "EDIT",
      component: ChilZComponent
    }
  ];
  pages = {
    ADD : {component: ChilNComponent},
    EDIT: {component: ChilZComponent}
  }
  type = 'ADD';
  index: number | undefined;
  list = [{
    userName : 'alex',
    phone : '0981773084',
    chil: [
      {
        userName : 'alex',
        phone : '11111',
      },
      {
        userName : 'alex',
        phone : '22222',
      }
    ]
  },
  {
    userName : 'Phuong ',
    phone : '0981773084',
    chil: [
      {
        userName : 'alex',
        phone : '11111',
      },
      {
        userName : 'alex',
        phone : '3333',
      }
    ]
  }]
  constructor(private eventBusService: EventBusService) { 
    this.ofType(ChilZComponent).subscribe( z => {
      if(z.action=== ChilZComponent.action.SAVE) {
        this.list.push({
          userName : 'alex',
          phone : '0981773084',
          chil: [
            {
              userName : 'alex',
              phone : '11111',
            },
            {
              userName : 'alex',
              phone : '22222',
            }
          ]
        });
        console.log(this.list)
        this.goChild1();
      }
    })

    this.ofType(ChilNComponent).subscribe( data => {
      console.log(data)
      if(data.action=== ChilNComponent.action.EDIT) {
        this.index = data.state;
        this.goChild2();
      }
    })
  }
  ofType(name:  Type<any>):Observable<Demo> {
    return this.eventBusService.listenChange<Demo>(name);
  }
  ngOnInit(): void {
  }
  goChild1() {
    this.type = 'ADD';
  }
  goChild2() {
    this.type = 'EDIT';
  }

}
