import {
    propObject,
    prop,
    alphaNumeric,
    alpha,
    compare,
    contains,
    digit,
    email,
    greaterThanEqualTo,
    greaterThan,
    hexColor,
    json,
    lessThanEqualTo,
    lowerCase,
    maxLength,
    maxNumber,
    minNumber,
    password,
    pattern,
    lessThan,
    range,
    required,
    time,
    upperCase,
    url,
    propArray,
    minLength,
    minDate,
    maxDate
  } from '..';
  import { UserAddress } from './user-address.form';
  import { Course } from './course.form';
import { FormGroup } from '@angular/forms';
import { DynamicFormService } from '../services/dynamic-form.service';
  
  export  class BaseUser {
    @alpha()
    @required()
    userName?: string;
  }
  export class User extends BaseUser {
  
    @alphaNumeric()
    areaCode?: string;
  
    @prop() oldPassword?: string;
  
    @compare({ fieldName: 'oldPassword' })
    newPassword?: string;
  
    @contains({ value: 'Admin' })
    roleName?: string;
  
  
    @digit()
    joiningAge?: number;
  
    @email()
    email?: string;
  
    @greaterThan({ fieldName: 'joiningAge' })
    retirementAge?: number;
  
    @greaterThanEqualTo({ fieldName: 'joiningAge' })
    currentAge?: number;
  
    @hexColor()
    teamColorCode?: string;
  
    @json()
    json?: string;
  
    @prop()
    currentExperience?: number;
  
    @lessThanEqualTo({ fieldName: 'currentExperience' })
    minimumExperience?: string;
  
    @lessThan({ fieldName: 'currentExperience' })
    experience?: string;
  
    @lowerCase()
    cityName?: string;
  
    @maxLength({ value: 10 })
    mobileNumber?: string;
  
    @maxNumber({ value: 3 })
    maximumBankAccount?: string;
  
    @minLength({ value: 8 })
    landlineNo?: string;
  
    @minNumber({ value: 1 })
    minimumBankAccount?: string;
  
    @password({ validation: { maxLength: 10, minLength: 5, alphabet: true } })
    password?: string;
  
    @pattern({ expression: { zipCode: /^\d{5}(?:[-\s]\d{4})?$/ } })
    zipCode?: string;
  
    @range({ minimumNumber: 18, maximumNumber: 60 })
    eligiblityAge?: number;
  
    @required()
    stateName?: string;
  
    @time()
    entryTime?: string;
  
    @upperCase()
    countryCode?: string;
  
    @url()
    socialProfileUrl?: string;
  
    @minDate({ value: new Date(2000, 0, 1) }) licenseDate?: Date;
  
    @maxDate({ value: new Date(2018, 5, 6) }) licenseExpiration?: Date;
  
    @propObject(UserAddress) userAddress?: UserAddress;
  
    @propArray(Course) courses?: Array<Course>;
  
    constructor(init?: Partial<User>) {
        super()
        Object.assign(this, init);
    }
  }
  