import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDiscipline, Discipline } from '../discipline.model';
import { DisciplineService } from '../service/discipline.service';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { TeacherService } from 'app/entities/teacher/service/teacher.service';

@Component({
  selector: 'jhi-discipline-update',
  templateUrl: './discipline-update.component.html',
})
export class DisciplineUpdateComponent implements OnInit {
  isSaving = false;

  coursesSharedCollection: ICourse[] = [];
  teachersSharedCollection: ITeacher[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    credits: [],
    phone: [],
    courses: [],
    teacher: [],
  });

  constructor(
    protected disciplineService: DisciplineService,
    protected courseService: CourseService,
    protected teacherService: TeacherService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ discipline }) => {
      this.updateForm(discipline);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const discipline = this.createFromForm();
    if (discipline.id !== undefined) {
      this.subscribeToSaveResponse(this.disciplineService.update(discipline));
    } else {
      this.subscribeToSaveResponse(this.disciplineService.create(discipline));
    }
  }

  trackCourseById(index: number, item: ICourse): number {
    return item.id!;
  }

  trackTeacherById(index: number, item: ITeacher): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiscipline>>): void {
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

  protected updateForm(discipline: IDiscipline): void {
    this.editForm.patchValue({
      id: discipline.id,
      name: discipline.name,
      credits: discipline.credits,
      phone: discipline.phone,
      courses: discipline.courses,
      teacher: discipline.teacher,
    });

    this.coursesSharedCollection = this.courseService.addCourseToCollectionIfMissing(
      this.coursesSharedCollection,
      ...(discipline.courses ?? [])
    );
    this.teachersSharedCollection = this.teacherService.addTeacherToCollectionIfMissing(this.teachersSharedCollection, discipline.teacher);
  }

  protected loadRelationshipsOptions(): void {
    this.courseService
      .query()
      .pipe(map((res: HttpResponse<ICourse[]>) => res.body ?? []))
      .pipe(
        map((courses: ICourse[]) =>
          this.courseService.addCourseToCollectionIfMissing(courses, ...(this.editForm.get('courses')!.value ?? []))
        )
      )
      .subscribe((courses: ICourse[]) => (this.coursesSharedCollection = courses));

    this.teacherService
      .query()
      .pipe(map((res: HttpResponse<ITeacher[]>) => res.body ?? []))
      .pipe(
        map((teachers: ITeacher[]) => this.teacherService.addTeacherToCollectionIfMissing(teachers, this.editForm.get('teacher')!.value))
      )
      .subscribe((teachers: ITeacher[]) => (this.teachersSharedCollection = teachers));
  }

  protected createFromForm(): IDiscipline {
    return {
      ...new Discipline(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      credits: this.editForm.get(['credits'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      courses: this.editForm.get(['courses'])!.value,
      teacher: this.editForm.get(['teacher'])!.value,
    };
  }
}
