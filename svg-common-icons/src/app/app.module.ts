import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { commonIconsArtist, CommonIconsModule, CommonIconsService } from 'projects/common-icons/src/public-api';

import { AppComponent } from './app.component';
import { ChildComponent } from './child/child.component';
import { ParentComponent } from './solution-step/parent/parent.component';
import { ChilNComponent } from './solution-step/chil-n/chil-n.component';
import { ChilZComponent } from './solution-step/chil-z/chil-z.component';
import { PlaceholderDirective } from './placeholder.directive';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PhoneMaskDirective } from './directives/phone-mask.directive';
import { WebSocketSubscriberService } from './services/web-socket-subscriber.service';
import { StompRService } from '@stomp/ng2-stompjs';

@NgModule({
  declarations: [
    AppComponent,
    ChildComponent,
    ParentComponent,
    ChilNComponent,
    ChilZComponent,
    PlaceholderDirective,
    PhoneMaskDirective
  ],
  imports: [
    BrowserModule,
    CommonIconsModule,
    FormsModule,
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [WebSocketSubscriberService, StompRService],
  bootstrap: [AppComponent],
  entryComponents: []
})
export class AppModule {
  constructor(private commonIconService: CommonIconsService) {
    this.commonIconService.registerIcons( [
       commonIconsArtist
    ]);
  }
}
