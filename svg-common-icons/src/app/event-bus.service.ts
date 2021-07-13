import {EventEmitter, Injectable, Type} from '@angular/core';
import {Observable, Subject} from "rxjs";
import { filter, tap } from 'rxjs/operators';
import { v4 as uuidv4 } from 'uuid';
@Injectable({
  providedIn: 'root',
})
export class EventBusService {
  private events: Map<String, EventEmitter<any>> = new Map<String, EventEmitter<any>>();
  constructor() {
  }
  pushChange<T extends ActionEvent>(type: Type<any>, value: T): void {
    console.log(type.prototype.name)
    if (!this.events.has(type.prototype.name)) {
      this.events.set(type.prototype.name, new EventEmitter<T>());
    }
    (this.events.get(type.prototype.name) as  EventEmitter<T>).next(value);
  }
  listenChange<T extends ActionEvent >(name: Type<any>): Observable<T> {
    console.log(name.prototype.name)
    if (!this.events.has(name.prototype.name)) {
      this.events.set(name.prototype.name, new EventEmitter<T>());
    }
 return (this.events.get(name.prototype.name) as  Subject<T>).pipe(
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
    if (this.events.has(name.prototype.name)) {
      let subscription = this.events.get(name.prototype.name);
      subscription && typeof subscription.unsubscribe === "function" && subscription.unsubscribe();
      this.events.delete(name.prototype.name);
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
export function EventHashKey({
  hashKey = ""
} = {}) {
  return function(constructor: Function) {
    constructor.prototype.name = hashKey || uuidv4();
  }
}
