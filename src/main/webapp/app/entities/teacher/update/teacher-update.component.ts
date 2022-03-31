import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITeacher, Teacher } from '../teacher.model';
import { TeacherService } from '../service/teacher.service';
import { IClassRoom } from 'app/entities/class-room/class-room.model';
import { ClassRoomService } from 'app/entities/class-room/service/class-room.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

@Component({
  selector: 'jhi-teacher-update',
  templateUrl: './teacher-update.component.html',
})
export class TeacherUpdateComponent implements OnInit {
  isSaving = false;

  classRoomsCollection: IClassRoom[] = [];
  coursesSharedCollection: ICourse[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    address: [],
    phone: [],
    specialization: [],
    classRoom: [],
    courses: [],
  });

  constructor(
    protected teacherService: TeacherService,
    protected classRoomService: ClassRoomService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teacher }) => {
      this.updateForm(teacher);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teacher = this.createFromForm();
    if (teacher.id !== undefined) {
      this.subscribeToSaveResponse(this.teacherService.update(teacher));
    } else {
      this.subscribeToSaveResponse(this.teacherService.create(teacher));
    }
  }

  trackClassRoomById(index: number, item: IClassRoom): number {
    return item.id!;
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }

  getSelectedCourse(option: ICourse, selectedVals?: ICourse[]): ICourse {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeacher>>): void {
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

  protected updateForm(teacher: ITeacher): void {
    this.editForm.patchValue({
      id: teacher.id,
      name: teacher.name,
      address: teacher.address,
      phone: teacher.phone,
      specialization: teacher.specialization,
      classRoom: teacher.classRoom,
      courses: teacher.courses,
    });

    this.classRoomsCollection = this.classRoomService.addClassRoomToCollectionIfMissing(this.classRoomsCollection, teacher.classRoom);
    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(
      this.coursesSharedCollection,
      ...(teacher.courses ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classRoomService
      .query({ filter: 'teacher-is-null' })
      .pipe(map((res: HttpResponse<IClassRoom[]>) => res.body ?? []))
      .pipe(
        map((classRooms: IClassRoom[]) =>
          this.classRoomService.addClassRoomToCollectionIfMissing(classRooms, this.editForm.get('classRoom')!.value)
        )
      )
      .subscribe((classRooms: IClassRoom[]) => (this.classRoomsCollection = classRooms));

    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(
        map((courses: ICourse[]) =>
          this.courseService.addCourseToCollectionIfMissing(courses, ...(this.editForm.get('courses')!.value ?? []))
        )
      )
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));
  }

  protected createFromForm(): ITeacher {
    return {
      ...new Teacher(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      specialization: this.editForm.get(['specialization'])!.value,
      classRoom: this.editForm.get(['classRoom'])!.value,
      courses: this.editForm.get(['courses'])!.value,
    };
  }
}
