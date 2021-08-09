import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { DynamicFormBuilder } from '..';

@Injectable()
export class DynamicFormService {

  constructor(private formBuilder: DynamicFormBuilder) { }
 public  formGroup<T>(data: T): FormGroup {
  return this.formBuilder.formGroup<T>(data);
 }
}
