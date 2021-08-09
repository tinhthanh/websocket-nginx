import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RxFormBuilder } from '@rxweb/reactive-form-validators';
import { DynamicFormService } from './services/dynamic-form.service';



@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [RxFormBuilder, DynamicFormService]
})
export class DynamicFormModule { }
