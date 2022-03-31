import { IStudent } from 'app/entities/student/student.model';
import { IDiscipline } from 'app/entities/discipline/discipline.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';

export interface ICourse {
  id?: number;
  name?: string | null;
  duration?: string | null;
  students?: IStudent[] | null;
  disciplines?: IDiscipline[] | null;
  teachers?: ITeacher[] | null;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public name?: string | null,
    public duration?: string | null,
    public students?: IStudent[] | null,
    public disciplines?: IDiscipline[] | null,
    public teachers?: ITeacher[] | null
  ) {}
}

export function getCourseIdentifier(course: ICourse): number | undefined {
  return course.id;
}
