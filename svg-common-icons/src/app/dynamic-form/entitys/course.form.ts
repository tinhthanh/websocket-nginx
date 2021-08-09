import { required } from "..";

export class Course {
    @required() courseName?: string;
}
