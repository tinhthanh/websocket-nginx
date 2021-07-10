import {Injectable} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {Message} from '@stomp/stompjs';
import {StompConfig, StompHeaders, StompRService} from '@stomp/ng2-stompjs';
import * as SockJS from 'sockjs-client';
@Injectable()
export class WebSocketSubscriberService {

  private topicPool: Map<string, Observable<Message>> = new Map<string, Observable<Message>>();

  constructor(private stompService: StompRService) {
    this.stompService.config = stompConfig;
    this.stompService.initAndConnect();
  }

  private registerSubscription<T>(url: string, header: StompHeaders, func: (value: T) => void): Subscription {
    let subscriber = this.topicPool.get(url);
    if (!subscriber) {
      subscriber = this.stompService.subscribe(url, header);
      this.topicPool.set(url, subscriber);
    }
    return subscriber.subscribe((message: Message) => {
      try {
        func(JSON.parse(message.body) as T);
      } catch (e) {}
    });
  }
  public deleteSubscription(url: string): void {
    this.topicPool.delete(url);
  }
  private getHeaders(id: any): StompHeaders {
    return <StompHeaders>{
      id: `${id}`
    };
  }
  public register(url: string, func: (value: any) => void): Subscription {
    let header = this.getHeaders(url);
    return this.registerSubscription(url, header, func);
  }

}

const stompConfig: StompConfig = {
  // Which server?
  url: `${location.protocol === 'https:' ? 'wss' : 'ws'}://${location.host}/mewebsocket/mews`,
//    url: () => {
//        return new SockJS('/mewebsocket/mews')
//    } ,
// Headers
  // Typical keys: login, passcode, host
  headers: {},

  // How often to heartbeat?
  // Interval in milliseconds, set to 0 to disable
  heartbeat_in: 0, // Typical value 0 - disabled
  heartbeat_out: 20000, // Typical value 20000 - every 20 seconds
  // Wait in milliseconds before attempting auto reconnect
  // Set to 0 to disable
  // Typical value 5000 (5 seconds)
  reconnect_delay: 5000,

  // Will log diagnostics on console
  debug: false
};