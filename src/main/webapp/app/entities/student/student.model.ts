import dayjs from 'dayjs/esm';
import { IDiscipline } from 'app/entities/discipline/discipline.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { IClassRoom } from 'app/entities/class-room/class-room.model';
import { ICourse } from 'app/entities/course/course.model';

export interface IStudent {
  id?: number;
  cpf?: string | null;
  birthDate?: dayjs.Dayjs | null;
  name?: string | null;
  disciplines?: IDiscipline[] | null;
  teachers?: ITeacher[] | null;
  classRoom?: IClassRoom | null;
  course?: ICourse | null;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public cpf?: string | null,
    public birthDate?: dayjs.Dayjs | null,
    public name?: string | null,
    public disciplines?: IDiscipline[] | null,
    public teachers?: ITeacher[] | null,
    public classRoom?: IClassRoom | null,
    public course?: ICourse | null
  ) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
