import { Component, Type } from '@angular/core';
import { ActionEvent, EventBusService } from './event-bus.service';
import{ filter, tap } from 'rxjs/operators'
import { ChildComponent } from './child/child.component';
import { commonIconsArtist } from 'projects/common-icons/src/public-api';
import { FormBuilder, FormGroup } from '@angular/forms';
import { WebSocketSubscriberService } from './services/web-socket-subscriber.service';
import { User } from './dynamic-form/entitys/user.form';
import { DynamicFormService } from './dynamic-form/services/dynamic-form.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'svg-common-icons';
  userFormGroup: FormGroup;

  constructor(private dynamicFormService: DynamicFormService, private eventBusService: EventBusService,fb: FormBuilder,private webSocketSubscriberService: WebSocketSubscriberService) {
    const temp  = {
      currentExperience : 6,
      userAddress  : {
          mobileNo: "1000"
        },
      courses:  [
        {
        courseName: "DEMO"
        }
      ]
    }
    let user = new User(temp);
 
    // user.currentExperience = 5; // set as default value.
    // user.userAddress = new UserAddress(); // create nested object, this will bind as a `FormGroup`.
    // let course = new Course();
    // user.courses = new Array<Course>(); // create nested array object, this will bind as a `FormArray`.
    // user.courses.push(course);
   this.userFormGroup = this.dynamicFormService.formGroup<User>(user);
   console.log(this.userFormGroup.getRawValue())
    console.log(user)

    this.webSocketSubscriberService.register( '/dome' , (data) => {
      console.log(data)
    })

    eventBusService.listenChange<Demo>(ChildComponent).pipe(
      filter( z =>
        (z instanceof Demo )
      )
    ).subscribe( z => {
      console.log(z)
    })
const f = new Date();
f.setDate(f.getDate());
var days = this.formatTime(f);

console.log(days)

const f2 = new Date();
f2.setDate(f2.getDate() -1 );
var days2 = this.formatTime(f2);

console.log(days2)


const f3 = new Date();
f3.setDate(f3.getDate() - 2 );
var days3 = this.formatTime(f3);

console.log(days3)





const f4 = new Date();
f4.setDate(f4.getDate() - 8 );
var days4 = this.formatTime(f4);

console.log(days4)
  }

 formatTime(time: Date) {
  const fn = (value: Number) => String(value).padStart(2, '0');
  const type = Math.abs(Date.now() - time.getTime()) / (1000 * 3600 * 24) ;
  switch (type) {
      case 0:
          return `Today - ${fn(time.getHours())}:${fn(time.getMinutes())}` ;
      case 1:
          return `Yesterday - ${fn(time.getHours())}:${fn(time.getMinutes())}`
      case 2 || 3 || 4 || 5 || 6 || 7:
           return `${type +  1} days ago`
      default:
           return  `${fn(time.getDay())} ${time.toLocaleString("defalt", { month : 'long'})} ${time.getFullYear().toString().substr(-2)}`;
    }
}
submit() {
 
  console.log( this.userFormGroup.valid)
  console.log( this.userFormGroup.getRawValue())
}
}

export class Demo extends ActionEvent{
  makeSound(input: string): string {
    throw new Error('Method not implemented.');
  }
  action: string;
  state: any
  constructor(action: string, state: any) {
    super(action,state);
    this.action = action;
    this.state =state;
  }
}
