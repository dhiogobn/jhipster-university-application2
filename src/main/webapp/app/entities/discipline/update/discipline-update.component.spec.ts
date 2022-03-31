import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DisciplineService } from '../service/discipline.service';
import { IDiscipline, Discipline } from '../discipline.model';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';
import { ITeacher } from 'app/entities/teacher/teacher.model';
import { TeacherService } from 'app/entities/teacher/service/teacher.service';

import { DisciplineUpdateComponent } from './discipline-update.component';

describe('Discipline Management Update Component', () => {
  let comp: DisciplineUpdateComponent;
  let fixture: ComponentFixture<DisciplineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let disciplineService: DisciplineService;
  let courseService: CourseService;
  let teacherService: TeacherService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DisciplineUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DisciplineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisciplineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    disciplineService = TestBed.inject(DisciplineService);
    courseService = TestBed.inject(CourseService);
    teacherService = TestBed.inject(TeacherService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Course query and add missing value', () => {
      const discipline: IDiscipline = { id: 456 };
      const courses: ICourse[] = [{ id: 45987 }];
      discipline.courses = courses;

      const courseCollection: ICourse[] = [{ id: 93816 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [...courses];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ discipline });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(courseCollection, ...additionalCourses);
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Teacher query and add missing value', () => {
      const discipline: IDiscipline = { id: 456 };
      const teacher: ITeacher = { id: 2889 };
      discipline.teacher = teacher;

      const teacherCollection: ITeacher[] = [{ id: 28554 }];
      jest.spyOn(teacherService, 'query').mockReturnValue(of(new HttpResponse({ body: teacherCollection })));
      const additionalTeachers = [teacher];
      const expectedCollection: ITeacher[] = [...additionalTeachers, ...teacherCollection];
      jest.spyOn(teacherService, 'addTeacherToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ discipline });
      comp.ngOnInit();

      expect(teacherService.query).toHaveBeenCalled();
      expect(teacherService.addTeacherToCollectionIfMissing).toHaveBeenCalledWith(teacherCollection, ...additionalTeachers);
      expect(comp.teachersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const discipline: IDiscipline = { id: 456 };
      const courses: ICourse = { id: 74715 };
      discipline.courses = [courses];
      const teacher: ITeacher = { id: 72505 };
      discipline.teacher = teacher;

      activatedRoute.data = of({ discipline });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(discipline));
      expect(comp.coursesSharedCollection).toContain(courses);
      expect(comp.teachersSharedCollection).toContain(teacher);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Discipline>>();
      const discipline = { id: 123 };
      jest.spyOn(disciplineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ discipline });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: discipline }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(disciplineService.update).toHaveBeenCalledWith(discipline);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Discipline>>();
      const discipline = new Discipline();
      jest.spyOn(disciplineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ discipline });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: discipline }));
      saveSubject.complete();

      // THEN
      expect(disciplineService.create).toHaveBeenCalledWith(discipline);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Discipline>>();
      const discipline = { id: 123 };
      jest.spyOn(disciplineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ discipline });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(disciplineService.update).toHaveBeenCalledWith(discipline);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCourseById', () => {
      it('Should return tracked Course primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCourseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTeacherById', () => {
      it('Should return tracked Teacher primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTeacherById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCourse', () => {
      it('Should return option if no Course is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedCourse(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Course for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedCourse(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Course is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedCourse(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
