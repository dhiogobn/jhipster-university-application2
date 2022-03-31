import { IClassRoom } from 'app/entities/class-room/class-room.model';
import { IDiscipline } from 'app/entities/discipline/discipline.model';
import { ICourse } from 'app/entities/course/course.model';
import { IStudent } from 'app/entities/student/student.model';

export interface ITeacher {
  id?: number;
  name?: string | null;
  address?: string | null;
  phone?: string | null;
  specialization?: string | null;
  classRoom?: IClassRoom | null;
  disciplines?: IDiscipline[] | null;
  courses?: ICourse[] | null;
  students?: IStudent[] | null;
}

export class Teacher implements ITeacher {
  constructor(
    public id?: number,
    public name?: string | null,
    public address?: string | null,
    public phone?: string | null,
    public specialization?: string | null,
    public classRoom?: IClassRoom | null,
    public disciplines?: IDiscipline[] | null,
    public courses?: ICourse[] | null,
    public students?: IStudent[] | null
  ) {}
}

export function getTeacherIdentifier(teacher: ITeacher): number | undefined {
  return teacher.id;
}
