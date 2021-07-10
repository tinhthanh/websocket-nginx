import {EventEmitter, Injectable, Type} from '@angular/core';
import {Observable, Subject} from "rxjs";
import { filter, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class EventBusService {
  private events: Map<String, EventEmitter<any>> = new Map<String, EventEmitter<any>>();
  constructor() {
  }
  pushChange<T extends ActionEvent>(type: Type<any>, value: T): void {
    if (!this.events.has(type.name)) {
      this.events.set(type.name, new EventEmitter<T>());
    }
    (this.events.get(type.name) as  EventEmitter<T>).next(value);
  }
  listenChange<T extends ActionEvent >(name: Type<any>): Observable<T> {
    if (!this.events.has(name.name)) {
      this.events.set(name.name, new EventEmitter<T>());
    }
 return (this.events.get(name.name) as  Subject<T>).pipe(
      tap(k =>{
        console.log(k)
      })
    );
  }
  unsubscribeChange (nameArr: Type<any>): void;
  unsubscribeChange (nameArr: Array<Type<any>>): void;
  unsubscribeChange<T>(nameArr?: (Type<any> | Array<Type<any>>)): void {
    if(!nameArr || nameArr.length == 0) return;
    if(Array.isArray(nameArr)){
      nameArr.forEach(name => {
        this.deleteEvents(name);
      })
    } else {
      this.deleteEvents(nameArr);
    }
  }
  deleteEvents<T>(name:  Type<any>): void {
    if (this.events.has(name.name)) {
      let subscription = this.events.get(name.name);
      subscription && typeof subscription.unsubscribe === "function" && subscription.unsubscribe();
      this.events.delete(name.name);
    }
  }
}
export abstract class ActionEvent{
  action: string;
  state: any ;
   constructor(action: string, state: any) {
     this.action =action;
     this.state = state;
   }
   abstract makeSound(input : string) : string;
}