import { ICourse } from 'app/entities/course/course.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { IStudent } from 'app/entities/student/student.model';

export interface IDiscipline {
  id?: number;
  name?: string | null;
  credits?: string | null;
  phone?: string | null;
  courses?: ICourse[] | null;
  teacher?: ITeacher | null;
  students?: IStudent[] | null;
}

export class Discipline implements IDiscipline {
  constructor(
    public id?: number,
    public name?: string | null,
    public credits?: string | null,
    public phone?: string | null,
    public courses?: ICourse[] | null,
    public teacher?: ITeacher | null,
    public students?: IStudent[] | null
  ) {}
}

export function getDisciplineIdentifier(discipline: IDiscipline): number | undefined {
  return discipline.id;
}
