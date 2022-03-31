import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IStudent, Student } from '../student.model';
import { StudentService } from '../service/student.service';
import { IDiscipline } from 'app/entities/discipline/discipline.model';
import { DisciplineService } from 'app/entities/discipline/service/discipline.service';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { TeacherService } from 'app/entities/teacher/service/teacher.service';
import { IClassRoom } from 'app/entities/class-room/class-room.model';
import { ClassRoomService } from 'app/entities/class-room/service/class-room.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

@Component({
  selector: 'jhi-student-update',
  templateUrl: './student-update.component.html',
})
export class StudentUpdateComponent implements OnInit {
  isSaving = false;

  disciplinesSharedCollection: IDiscipline[] = [];
  teachersSharedCollection: ITeacher[] = [];
  classRoomsSharedCollection: IClassRoom[] = [];
  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [],
    cpf: [],
    birthDate: [],
    name: [],
    disciplines: [],
    teachers: [],
    classRoom: [],
    course: [],
  });

  constructor(
    protected studentService: StudentService,
    protected disciplineService: DisciplineService,
    protected teacherService: TeacherService,
    protected classRoomService: ClassRoomService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ student }) => {
      if (student.id === undefined) {
        const today = dayjs().startOf('day');
        student.birthDate = today;
      }

      this.updateForm(student);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const student = this.createFromForm();
    if (student.id !== undefined) {
      this.subscribeToSaveResponse(this.studentService.update(student));
    } else {
      this.subscribeToSaveResponse(this.studentService.create(student));
    }
  }

  trackDisciplineById(index: number, item: IDiscipline): number {
    return item.id!;
  }

  trackTeacherById(index: number, item: ITeacher): number {
    return item.id!;
  }

  trackClassRoomById(index: number, item: IClassRoom): number {
    return item.id!;
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }

  getSelectedDiscipline(option: IDiscipline, selectedVals?: IDiscipline[]): IDiscipline {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedTeacher(option: ITeacher, selectedVals?: ITeacher[]): ITeacher {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStudent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(student: IStudent): void {
    this.editForm.patchValue({
      id: student.id,
      cpf: student.cpf,
      birthDate: student.birthDate ? student.birthDate.format(DATE_TIME_FORMAT) : null,
      name: student.name,
      disciplines: student.disciplines,
      teachers: student.teachers,
      classRoom: student.classRoom,
      course: student.course,
    });

    this.disciplinesSharedCollection = this.disciplineService.addDisciplineToCollectionIfMissing(
      this.disciplinesSharedCollection,
      ...(student.disciplines ?? [])
    );
    this.teachersSharedCollection = this.teacherService.addTeacherToCollectionIfMissing(
      this.teachersSharedCollection,
      ...(student.teachers ?? [])
    );
    this.classRoomsSharedCollection = this.classRoomService.addClassRoomToCollectionIfMissing(
      this.classRoomsSharedCollection,
      student.classRoom
    );
    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(this.coursesSharedCollection, student.course);
  }

  protected loadRelationshipsOptions(): void {
    this.disciplineService
      .query()
      .pipe(map((res: HttpResponse<IDiscipline[]>) => res.body ?? []))
      .pipe(
        map((disciplines: IDiscipline[]) =>
          this.disciplineService.addDisciplineToCollectionIfMissing(disciplines, ...(this.editForm.get('disciplines')!.value ?? []))
        )
      )
      .subscribe((disciplines: IDiscipline[]) => (this.disciplinesSharedCollection = disciplines));

    this.teacherService
      .query()
      .pipe(map((res: HttpResponse<ITeacher[]>) => res.body ?? []))
      .pipe(
        map((teachers: ITeacher[]) =>
          this.teacherService.addTeacherToCollectionIfMissing(teachers, ...(this.editForm.get('teachers')!.value ?? []))
        )
      )
      .subscribe((teachers: ITeacher[]) => (this.teachersSharedCollection = teachers));

    this.classRoomService
      .query()
      .pipe(map((res: HttpResponse<IClassRoom[]>) => res.body ?? []))
      .pipe(
        map((classRooms: IClassRoom[]) =>
          this.classRoomService.addClassRoomToCollectionIfMissing(classRooms, this.editForm.get('classRoom')!.value)
        )
      )
      .subscribe((classRooms: IClassRoom[]) => (this.classRoomsSharedCollection = classRooms));

    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(map((courses: ICourse[]) => this.courseService.addCourseToCollectionIfMissing(courses, this.editForm.get('course')!.value)))
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): IStudent {
    return {
      ...new Student(),
      id: this.editForm.get(['id'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value ? dayjs(this.editForm.get(['birthDate'])!.value, DATE_TIME_FORMAT) : undefined,
      name: this.editForm.get(['name'])!.value,
      disciplines: this.editForm.get(['disciplines'])!.value,
      teachers: this.editForm.get(['teachers'])!.value,
      classRoom: this.editForm.get(['classRoom'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }
}
