import { IStudent } from 'app/entities/student/student.model';
import { ITeacher } from 'app/entities/teacher/teacher.model';

export interface IClassRoom {
  id?: number;
  number?: string | null;
  capacity?: string | null;
  students?: IStudent[] | null;
  teacher?: ITeacher | null;
}

export class ClassRoom implements IClassRoom {
  constructor(
    public id?: number,
    public number?: string | null,
    public capacity?: string | null,
    public students?: IStudent[] | null,
    public teacher?: ITeacher | null
  ) {}
}

export function getClassRoomIdentifier(classRoom: IClassRoom): number | undefined {
  return classRoom.id;
}
